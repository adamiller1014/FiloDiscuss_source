import { MikroORM } from "@mikro-orm/core";
import { PostgreSqlDriver } from "@mikro-orm/postgresql";
import { __prod__ } from "./constants";
import { Post } from "./entities/Post";
import path from 'path';
import { Migrator, TSMigrationGenerator } from '@mikro-orm/migrations';
import { User } from "./entities/User";

export default {
    migrations: {
        path: path.join(__dirname, "./migrations"),
        glob: '!(*.d).{js,ts}',
        generator: TSMigrationGenerator,
    },
    entities: [Post, User],
    dbName: "filodiscuss",
    user: "postgres",
    password: "12345678",
    driver: PostgreSqlDriver,
    extensions: [Migrator],
    allowGlobalContext: true,
    debug: !__prod__,
} as Parameters<typeof MikroORM.init>[0];