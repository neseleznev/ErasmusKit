package com.cooldevs.erasmuskit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

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

    private int viewType;

    PostsAdapter(ArrayList<Post> posts, Post.PostType postType) {
        this.postsList = posts;
        this.postType = postType;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case POST_GENERIC:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
                break;
            case POST_EVENT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
                break;
            case POST_TIP:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tip, parent, false);
                break;
            case POST_PLACE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_place, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
        }

        return new PostViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        viewType = POST_GENERIC;
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

        PostViewHolder(View itemView) {
            super(itemView);
        }

        void bindPost(final Post post) {
            TextView postTitle, postContent;

            switch (viewType) {
                case POST_EVENT:
                    Event event = (Event) post;
                    String date = Utils.getDateString(event.getEventTimestamp());

                    postTitle = (TextView) itemView.findViewById(R.id.event_title);
                    postContent = (TextView) itemView.findViewById(R.id.event_content);

                    TextView evLocation = (TextView) itemView.findViewById(R.id.event_location);
                    TextView evDate = (TextView) itemView.findViewById(R.id.event_date);

                    evLocation.setText(event.getPlaceName());
                    evDate.setText(date);

                    break;

                case POST_TIP:
                    Tip tip = (Tip) post;

                    postTitle = (TextView) itemView.findViewById(R.id.tip_title);
                    postContent = (TextView) itemView.findViewById(R.id.tip_content);

                    TextView tipCategory = (TextView) itemView.findViewById(R.id.tip_category);
                    tipCategory.setText(tip.getTipCategory());

                    break;

                case POST_PLACE:
                    Place place = (Place) post;

                    postTitle = (TextView) itemView.findViewById(R.id.place_title);
                    postContent = (TextView) itemView.findViewById(R.id.place_content);

                    TextView placeRating = (TextView) itemView.findViewById(R.id.place_rating_text);
                    placeRating.setText(String.format(Locale.US, "%1.1f", place.getRating()));
                    break;

                default:
                    postTitle = (TextView) itemView.findViewById(R.id.post_title);
                    postContent = (TextView) itemView.findViewById(R.id.post_content);

                    TextView postSubtitle = (TextView) itemView.findViewById(R.id.post_subtitle);
                    postSubtitle.setText(postType.getPostType());
            }

            postTitle.setText(post.getTitle());
            postContent.setText(post.getContent());
        }
    }
}
