import { MikroORM } from "@mikro-orm/core";
import { __prod__ } from "./constants";
import { Post } from "./entities/Post";
import microConfig from "./mikro-orm.config";
import { ApolloServer } from "apollo-server-express";
import express from "express";
import { buildSchema } from "type-graphql";
import { HelloResolver } from "./resolvers/hello";

const main = async () => {
    const orm = await MikroORM.init(microConfig);
    // const post = orm.em.fork().create(Post, {
    //     title: 'Foo is Bar',
    // });

    // await orm.em.persistAndFlush(post);

    // const posts = await orm.em.find(Post,{});
    // console.log(posts);

    const app = express() as any;

    const apolloServer = new ApolloServer({
        schema: await buildSchema({
            resolvers: [HelloResolver],
            validate: false
        })
    });

    await apolloServer.start();
    apolloServer.applyMiddleware({app});
    
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