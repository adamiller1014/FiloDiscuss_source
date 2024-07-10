import { Arg, Ctx, Field, InputType, Mutation, ObjectType, Resolver, Query } from "type-graphql";
import { MyContext } from "../types";
import { User } from "../entities/User";
import argon2 from "argon2";
import { Response } from "express";
import { COOKIE_NAME } from "../constants";

@InputType()
class UsernamePasswordInput {
    @Field()
    username!: string;
    @Field()
    password!: string;
}
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
        if (options.username.length <= 2) {
            return {
                errors: [{
                    field: 'username',
                    message: "Length must be greater than 2",
                }]
            }
        }
        if (options.password.length <= 2) {
            return {
                errors: [{
                    field: 'password',
                    message: "Length must be greater than 2",
                }]
            }
        }
        const hashedPassword = await argon2.hash(options.password);
        const user = em.fork().create(User, { username: options.username, password: hashedPassword });
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
        @Arg('options') options: UsernamePasswordInput,
        @Ctx() { em, req }: MyContext
    ): Promise<UserResponse> {
        const user = await em.findOne(User, { username: options.username });
        if (!user) {
            return {
                errors: [{
                    field: 'username',
                    message: "That username doesn't exist",
                }]
            }
        }
        const validPassword = await argon2.verify(user.password, options.password);
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