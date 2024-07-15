import { Arg, Ctx, Field, InputType, Int, Mutation, Query, Resolver, UseMiddleware } from "type-graphql";
import { Post } from "../entities/Post";
import { MyContext } from "../types";
import { AppDataSource } from "..";
import { isAuth } from "../middleware/isAuth";

@InputType()
class PostInput {
    @Field()
    title!: string;
    @Field()
    text!: string;
}

@Resolver(Post)
export class PostResolver {

    // getPosts()
    @Query(() => [Post])
    async posts(): Promise<Post[]> {
        return Post.find()
    }

    // getPostById()
    @Query(() => Post, { nullable: true })
    post(@Arg("id", () => Int) id: number): Promise<Post | null> {
        return Post.findOneBy({ id });
    }

    // createPost()
    @Mutation(() => Post)
    @UseMiddleware(isAuth)
    async createPost(
        @Arg("input") input: PostInput,
        @Ctx() { req }: MyContext
    ): Promise<Post> {
        return Post.create({
            ...input,
            creatorId: req.session.userId,
        }).save();
    }

    // updatePost
    @Mutation(() => Post, { nullable: true })
    async updatePost(
        @Arg("id", () => Int) id: number,
        @Arg("title") title: string,
        @Arg("text") text: string,
        @Ctx() { req }: MyContext
    ): Promise<Post | null> {
        const result = await AppDataSource.getRepository(Post)
            .createQueryBuilder()
            .update(Post)
            .set({ title, text })
            .where('id = :id and "creatorId" = :creatorId', {
                id,
                creatorId: req.session.userId,
            })
            .returning("*")
            .execute();

        return result.raw[0];
    }

    // deletePost()
    @Mutation(() => Boolean)
    async deletePost(
        @Arg("id", () => Int) id: number,
        @Ctx() { req }: MyContext
    ): Promise<boolean> {
        const post = await Post.findOneBy({ id });
        if (!post) {
            return false;
        }
        if (post.creatorId !== req.session.userId) {
            throw new Error("not authorized");
        }

        await Post.delete({ id });

        await Post.delete({ id, creatorId: req.session.userId });
        return true;
    }
}