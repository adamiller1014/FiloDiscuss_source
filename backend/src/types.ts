import { Connection, EntityManager, IDatabaseDriver } from "@mikro-orm/core"
import { Session } from 'express-session';

export type MyContext = {

    em: EntityManager<IDatabaseDriver<Connection>>;
    req: Express.Request & { session?: Session & { userId?: number } };
    res: Express.Response;
}