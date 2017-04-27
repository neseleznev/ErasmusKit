package com.cooldevs.erasmuskit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mario on 23/04/2017
 */

class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private static final int POST_GENERIC = 0;
    private static final int POST_EVENT = 1;
    private static final int POST_TIP = 2;
    private static final int POST_PLACE = 3;

    private ArrayList<Post> postsList;
    private Post.PostType postType;

    PostsAdapter(ArrayList<Post> posts, Post.PostType postType) {
        this.postsList = posts;
        this.postType = postType;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // TODO: Create new card layouts and use them!!!
        switch (viewType) {
            case POST_GENERIC:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
                break;
            case POST_EVENT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false); //TODO: Change
                break;
            case POST_TIP:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false); //TODO: Change
                break;
            case POST_PLACE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false); //TODO: Change
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
        }

        return new PostViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = POST_GENERIC;
        Post post = postsList.get(position);

        if (post instanceof Event)
            viewType = POST_EVENT;
        else if (post instanceof Tip)
            viewType = POST_TIP;
        else if (post instanceof Place)
            viewType = POST_PLACE;

        return viewType;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.bindPost(post);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView postTitle;
        private TextView postSubtitle;
        private TextView postContent;

        PostViewHolder(View itemView) {
            super(itemView);

            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postSubtitle = (TextView) itemView.findViewById(R.id.post_subtitle);
            postContent = (TextView) itemView.findViewById(R.id.post_content);
        }

        void bindPost(final Post post) {
            postTitle.setText(post.getTitle());
            postSubtitle.setText(postType.getPostType());
            postContent.setText(post.getContent());
        }
    }
}
