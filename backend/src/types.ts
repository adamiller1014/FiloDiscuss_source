import { Session } from 'express-session';
import { Redis } from "ioredis";
import { createUserLoader } from './utils/createUserLoader';
import { createUpdootLoader } from './utils/createUpdootLoader';

export type MyContext = {
    req: Express.Request & { session?: Session & { userId?: number } };
    res: Express.Response;
    redis: Redis
    userLoader :  ReturnType<typeof createUserLoader>
    updootLoader : ReturnType<typeof createUpdootLoader>
}