"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const postgresql_1 = require("@mikro-orm/postgresql");
const constants_1 = require("./constants");
const Post_1 = require("./entities/Post");
const path_1 = __importDefault(require("path"));
const migrations_1 = require("@mikro-orm/migrations");
const User_1 = require("./entities/User");
exports.default = {
    migrations: {
        path: path_1.default.join(__dirname, "./migrations"),
        glob: '!(*.d).{js,ts}',
        generator: migrations_1.TSMigrationGenerator,
    },
    entities: [Post_1.Post, User_1.User],
    dbName: "filodiscuss",
    user: "postgres",
    password: "12345678",
    driver: postgresql_1.PostgreSqlDriver,
    extensions: [migrations_1.Migrator],
    allowGlobalContext: true,
    debug: !constants_1.__prod__,
};
