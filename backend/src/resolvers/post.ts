import { Arg, Ctx, Int, Mutation, Query, Resolver } from "type-graphql";
import { Post } from "../entities/Post";
import { MyContext } from "../types";
import { sleep } from "../utils/sleep";

@Resolver()
export class PostResolver {

    // getPost()
    @Query(() => [Post])
    async posts(
        @Ctx() { em }: MyContext
    ): Promise<Post[]> {
        //await sleep(3000)
        return em.find(Post, {});
    }

    // getPostById()
    @Query(() => Post, { nullable: true })
    post(
        @Arg('id', () => Int) id: number,
        @Ctx() { em }: MyContext
    ): Promise<Post | null> {
        return em.findOne(Post, { id });
    }

    // createPost()
    @Mutation(() => Post)
    async CreatePost(
        @Arg('title') title: string,
        @Ctx() { em }: MyContext
    ): Promise<Post> {
        const post = em.fork().create(Post, { title });
        await em.persistAndFlush(post)
        return post;
    }

    // updatePost()
    @Mutation(() => Post, { nullable: true })
    async updatePost(
        @Arg('id') id: number,
        @Arg('title', () => String, { nullable: true }) title: string,
        @Ctx() { em }: MyContext
    ): Promise<Post | null> {

        const post = await em.findOne(Post, { id });
        if (!post) {
            return null;
        }
        if (typeof title !== 'undefined') {
            post.title = title;
            await em.persistAndFlush(post)
        }
        return post;
    }

    // deletePost()
    @Mutation(() => Boolean)
    async deletePost(
        @Arg('id') id: number,

        @Ctx() { em }: MyContext
    ): Promise<boolean> {
        await em.nativeDelete(Post, {});
        return true;
    }
}