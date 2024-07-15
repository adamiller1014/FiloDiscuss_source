import { Session } from 'express-session';
import { Redis } from "ioredis";

export type MyContext = {
    req: Express.Request & { session?: Session & { userId?: number } };
    res: Express.Response;
    redis: Redis
}