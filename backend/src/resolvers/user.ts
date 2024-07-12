import { Arg, Ctx, Field, Mutation, ObjectType, Resolver, Query } from "type-graphql";
import { MyContext } from "../types";
import { User } from "../entities/User";
import argon2 from "argon2";
import e, { Response } from "express";
import { COOKIE_NAME, FORGET_PASSWORD_PREFIX } from "../constants";
import { UsernamePasswordInput } from "../utils/UsernamePasswordInput";
import { validRegister } from "../utils/validateRegister";
import { sendEmail } from "../utils/sendEmail";
import { v4 } from "uuid";

@ObjectType()
class FieldError {
    @Field()
    field!: string;

    @Field()
    message!: string;
}

@ObjectType()
class UserResponse {
    @Field(() => [FieldError], { nullable: true })
    errors?: FieldError[];

    @Field(() => User, { nullable: true })
    user?: User;
}

@Resolver()
export class UserResolver {

    // changePassword()
    @Mutation(() => UserResponse)
    async changePassword(
        @Arg('token') token: string,
        @Arg('newPossword') newPassword: string,
        @Ctx() { em, redis, req }: MyContext
    ): Promise<UserResponse> {
        if (newPassword.length <= 2) {
            return {
                errors: [
                    {
                        field: 'newPassword',
                        message: "Length must be greater than 2",
                    }]
            }

        }
        const key = FORGET_PASSWORD_PREFIX + token;
        const userId = await redis.get(key)
        if (!userId) {
            return {
                errors: [
                    {
                        field: 'token',
                        message: "Token expired",
                    }]
            }
        }

        const user = await em.findOne(User, { id: parseInt(userId) });

        if (!user) {
            return {
                errors: [
                    {
                        field: 'token',
                        message: "User no longer exists",
                    }]
            }
        }

        user.password = await argon2.hash(newPassword);
        await em.persistAndFlush(user);

        // Remove the token for a single use case
        await redis.del(key);

        // log in user after chnage password
        req.session.userId = user.id;

        return {user}
    }

    // forgotPassword()
    @Mutation(() => Boolean)
    async forgotPassword(
        @Arg('email') email: string,
        @Ctx() { em, redis }: MyContext
    ): Promise<Boolean> {
        const user = await em.findOne(User, { email });
        if (!user) {
            return true;
        }
        const token = v4();
        await redis.set(FORGET_PASSWORD_PREFIX + token, user.id, 'EX', 1000 * 60 * 60 * 24 * 3);
        sendEmail(
            email,
            `<a href="http://localhost:3000/change-password/${token}">reset password</a>`);
        return true;
    }


    // checkSession()
    @Query(() => User, { nullable: true })
    me(
        @Ctx() { req, em }: MyContext
    ) {
        console.log(req.session);
        if (!req.session.userId) {
            return null;
        }
        const user = em.findOne(User, { id: req.session.userId });
        return user;
    }

    // registerUser()
    @Mutation(() => UserResponse)
    async register(
        @Arg('options') options: UsernamePasswordInput,
        @Ctx() { em, req }: MyContext
    ): Promise<UserResponse> {
        const errors = validRegister(options);
        if (errors) {
            return { errors };
        }
        const hashedPassword = await argon2.hash(options.password);
        const user = em.fork().create(User, {
            username: options.username, password: hashedPassword,
            email: options.email,
        });
        try {
            await em.persistAndFlush(user)

        } catch (error) {
            // duplicate username error 
            if (error.code === '23505') {
                return {
                    errors: [{
                        field: 'username',
                        message: "Username already taken",
                    }]
                }
            }
        }
        // store user id session
        // set a cookie to the user 
        req.session.userId = user.id;
        return { user };
    }

    // loginUser()
    @Mutation(() => UserResponse)
    async login(
        @Arg('usernameOrEmail') usernameOrEmail: string,
        @Arg('password') password: string,
        @Ctx() { em, req }: MyContext
    ): Promise<UserResponse> {
        const user = await em.findOne(User, usernameOrEmail.includes('@') ?
            { email: usernameOrEmail } :
            { username: usernameOrEmail });
        if (!user) {
            return {
                errors: [{
                    field: 'usernameOrEmail',
                    message: "That username doesn't exist",
                }]
            }
        }
        const validPassword = await argon2.verify(user.password, password);
        if (!validPassword) {
            return {
                errors: [{
                    field: 'password',
                    message: "Incorrect password",
                }]
            }
        }
        req.session!.userId = user.id;
        return { user };
    }


    // logoutUser()
    @Mutation(() => Boolean)
    async logout(
        @Ctx() { req, res }: MyContext
    ): Promise<Boolean> {
        return new Promise((resolve) =>
            req.session!.destroy((err) => {
                (res as Response).clearCookie(COOKIE_NAME);
                if (err) {
                    console.log(err);
                    resolve(false);
                    return;
                }
                resolve(true);
            })
        );
    }

}