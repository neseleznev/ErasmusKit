package com.cooldevs.erasmuskit.ui.posts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cooldevs.erasmuskit.R;
import com.cooldevs.erasmuskit.ui.posts.model.Event;
import com.cooldevs.erasmuskit.ui.posts.model.Place;
import com.cooldevs.erasmuskit.ui.posts.model.Post;
import com.cooldevs.erasmuskit.ui.posts.model.Tip;
import com.cooldevs.erasmuskit.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

import static com.cooldevs.erasmuskit.ui.posts.model.Post.PostType.EVENT;
import static com.cooldevs.erasmuskit.ui.posts.model.Post.PostType.GENERIC;
import static com.cooldevs.erasmuskit.ui.posts.model.Post.PostType.PLACE;
import static com.cooldevs.erasmuskit.ui.posts.model.Post.PostType.TIP;

/**
 * Created by mario on 23/04/2017
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private ArrayList<Post> postsList;
    private Post.PostType postType;

    PostsAdapter(ArrayList<Post> posts, Post.PostType postType) {
        this.postsList = posts;
        this.postType = postType;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == GENERIC.ordinal()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_post, parent, false);
        }
        else if (viewType == EVENT.ordinal()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_event, parent, false);
        }
        else if (viewType == TIP.ordinal()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_tip, parent, false);
        }
        else if (viewType == PLACE.ordinal()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_place, parent, false);
        } else {
            throw new IllegalStateException("View type is unknown " +
                    "(caused by element in ArrayList<Post>)");
        }
        return new PostViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        Post post = postsList.get(position);
        postType = post.getPostType();

        return postType.ordinal();
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

            switch (postType) {
                case EVENT:
                    final Event event = (Event) post;
                    String date = Utils.getDateString(event.getEventTimestamp());

                    postTitle = (TextView) itemView.findViewById(R.id.event_title);
                    postContent = (TextView) itemView.findViewById(R.id.event_content);

                    TextView evLocation = (TextView) itemView.findViewById(R.id.event_location);
                    TextView evDate = (TextView) itemView.findViewById(R.id.event_date);

                    evLocation.setText(event.getPlaceName());
                    evDate.setText(date);

                    Button button = (Button) itemView.findViewById(R.id.event_facebook_button);
                    if (event.getFacebookLink() != null) {
                        button.setVisibility(View.VISIBLE);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setData(Uri.parse(event.getFacebookLink()));
                                itemView.getContext().startActivity(intent);
                            }
                        });
                    } else {
                        button.setVisibility(View.GONE);
                    }
                    break;

                case TIP:
                    Tip tip = (Tip) post;

                    postTitle = (TextView) itemView.findViewById(R.id.tip_title);
                    postContent = (TextView) itemView.findViewById(R.id.tip_content);

                    TextView tipCategory = (TextView) itemView.findViewById(R.id.tip_category);
                    tipCategory.setText(tip.getTipCategory());

                    break;

                case PLACE:
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
