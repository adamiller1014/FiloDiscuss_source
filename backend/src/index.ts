import 'reflect-metadata';
import { MikroORM } from "@mikro-orm/core";
import { __prod__, COOKIE_NAME } from "./constants";
import { Post } from "./entities/Post";
import microConfig from "./mikro-orm.config";
import { ApolloServer } from "apollo-server-express";
import express from "express";
import { buildSchema } from "type-graphql";
import { HelloResolver } from "./resolvers/hello";
import { PostResolver } from "./resolvers/post";
import { UserResolver } from './resolvers/user';
import RedisStore from "connect-redis"
import session from "express-session"
import { createClient } from "redis"
import { MyContext } from './types';

const main = async () => {
    const orm = await MikroORM.init(microConfig);
    await orm.getMigrator().up();
    // const post = orm.em.fork().create(Post, {
    //     title: 'Foo is Bar',
    // });

    // await orm.em.persistAndFlush(post);

    // const posts = await orm.em.find(Post,{});
    // console.log(posts);

    const app = express() as any;
    app.set("trust proxy", process.env.NODE_ENV !== "production"); //a little fix here from another users codes--- actually think this made it works
    app.set("Access-Control-Allow-Origin", "https://studio.apollographql.com");
    app.set("Access-Control-Allow-Credentials", true);
    // ___ Redis - set-up __

    let redisClient = createClient()
    redisClient.connect().catch(console.error)

    let redisStore = new RedisStore({
        client: redisClient,
        prefix: "myapp:",
        disableTouch: true
    })

    const cors = {
        credentials: true,
        origin: "https://studio.apollographql.com",
      };

    app.use(
        session({
            name: COOKIE_NAME,
            store: redisStore,
            cookie: {
                maxAge: 1000 * 60 * 60 * 24 * 365 * 10, // Alive for 10 years
                httpOnly: true,
                sameSite: "lax", //csrf
                secure: process.env.NODE_ENV === 'production' // cookie only works in https
            },
            secret: "qpwdomwqeoxqiewpoqjh",
            resave: false,
            saveUninitialized: false,
        }),
    );

    const apolloServer = new ApolloServer({
        schema: await buildSchema({
            resolvers: [HelloResolver, PostResolver, UserResolver],
            validate: false
        }),
        context: ({ req, res }) : MyContext => ({
            em: orm.em,
            req,
            res
        })
    });

    await apolloServer.start();
    apolloServer.applyMiddleware({ app , cors});

    // app.get('/', (_, res) => {
    //     res.send(" Hello ");
    // });

    app.listen(4000, () => {
        console.log('Server started on localhost:4000');
    });
}

main().catch(err => {
    console.error(err);
});