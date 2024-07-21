"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AppDataSource = void 0;
require("reflect-metadata");
const constants_1 = require("./constants");
const Post_1 = require("./entities/Post");
const apollo_server_express_1 = require("apollo-server-express");
const express_1 = __importDefault(require("express"));
const type_graphql_1 = require("type-graphql");
const hello_1 = require("./resolvers/hello");
const post_1 = require("./resolvers/post");
const user_1 = require("./resolvers/user");
const connect_redis_1 = __importDefault(require("connect-redis"));
const express_session_1 = __importDefault(require("express-session"));
const User_1 = require("./entities/User");
const ioredis_1 = __importDefault(require("ioredis"));
const typeorm_1 = require("typeorm");
const path_1 = __importDefault(require("path"));
const Updoot_1 = require("./entities/Updoot");
const createUserLoader_1 = require("./utils/createUserLoader");
const createUpdootLoader_1 = require("./utils/createUpdootLoader");
exports.AppDataSource = new typeorm_1.DataSource({
    type: "postgres",
    username: "postgres",
    password: "12345678",
    database: "filodiscuss2",
    synchronize: true,
    logging: true,
    entities: [User_1.User, Post_1.Post, Updoot_1.Updoot],
    subscribers: [],
    migrations: [path_1.default.join(__dirname, "./migrations/*")],
});
const main = () => __awaiter(void 0, void 0, void 0, function* () {
    yield exports.AppDataSource.initialize();
    yield exports.AppDataSource.runMigrations();
    //await Post.delete({});
    //sendEmail("azza@azza.com", "Hello there !");
    //await orm.em.nativeDelete(User, {});
    // const post = orm.em.fork().create(Post, {
    //     title: 'Foo is Bar',
    // });
    // await orm.em.persistAndFlush(post);
    // const posts = await orm.em.find(Post,{});
    // console.log(posts);
    const app = (0, express_1.default)();
    app.set("trust proxy", process.env.NODE_ENV !== "production"); //a little fix here from another users codes--- actually think this made it works
    app.set("Access-Control-Allow-Origin", "https://studio.apollographql.com");
    app.set("Access-Control-Allow-Credentials", true);
    // ___ Redis - set-up __
    const redisClient = new ioredis_1.default();
    redisClient.on("connect", () => {
        console.log("Redis connected");
    });
    redisClient.on("error", (err) => {
        console.error("Redis connection error:", err);
    });
    let redisStore = new connect_redis_1.default({
        client: redisClient,
        prefix: "myapp:",
        disableTouch: true
    });
    const cors = {
        credentials: true,
        origin: "https://studio.apollographql.com",
    };
    app.use((0, express_session_1.default)({
        name: constants_1.COOKIE_NAME,
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
    }));
    const apolloServer = new apollo_server_express_1.ApolloServer({
        schema: yield (0, type_graphql_1.buildSchema)({
            resolvers: [hello_1.HelloResolver, post_1.PostResolver, user_1.UserResolver],
            validate: false
        }),
        context: ({ req, res }) => ({
            req,
            res,
            redis: redisClient,
            userLoader: (0, createUserLoader_1.createUserLoader)(),
            updootLoader: (0, createUpdootLoader_1.createUpdootLoader)(),
        })
    });
    yield apolloServer.start();
    apolloServer.applyMiddleware({ app, cors });
    // app.get('/', (_, res) => {
    //     res.send(" Hello ");
    // });
    app.listen(4000, () => {
        console.log('Server started on localhost:4000');
    });
});
main().catch(err => {
    console.error(err);
});
