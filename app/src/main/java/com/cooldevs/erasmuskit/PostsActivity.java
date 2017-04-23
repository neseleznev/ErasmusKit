package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    private static final String TAG = "PostsActivity";

    private String cityName;
    private String cityKey;
    private RecyclerView recyclerView;

    private Query usersRef;
    private ChildEventListener usersEventListener;
    private ArrayList<User> users;

    private DatabaseReference postsRef;
    private ChildEventListener postsEventListener;
    private ArrayList<Post> posts;

    private TextView emptyListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        // Initialize views
        recyclerView = (RecyclerView) findViewById(R.id.posts_recView);
        emptyListText = (TextView) findViewById(R.id.empty_list_text);

        // Get intent extras
        cityName = getIntent().getStringExtra("cityName");
        cityKey = getIntent().getStringExtra("cityKey");
        int id = getIntent().getIntExtra("id", 0);

        String toolbarTitle = cityName;

        // FAB functionality
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_post_fab);
        View.OnClickListener listener = null;

        switch (id) {
            case 0:
                toolbarTitle = cityName + "'s " + getString(R.string.city_section_1);
                getPeopleList();
                break;

            case 1:
                toolbarTitle = cityName + "'s " + getString(R.string.city_section_2);
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PostsActivity.this, NewEventActivity.class);
                        intent.putExtra("cityKey", cityKey);
                        startActivity(intent);
                    }
                };
                getPostsList(Post.PostType.EVENT);
                break;

            case 2:
                toolbarTitle = cityName + "'s " + getString(R.string.city_section_3);

                getPostsList(Post.PostType.TIP);
                break;

            case 3:
                toolbarTitle = cityName + "'s " + getString(R.string.city_section_4);

                getPostsList(Post.PostType.PLACE);
                break;
        }

        fab.setOnClickListener(listener);

        // Finish activity from toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(toolbarTitle);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getPeopleList() {
        users = new ArrayList<>();
        final UsersAdapter adapter = new UsersAdapter(users);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click on the element " + recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(PostsActivity.this, ProfileActivity.class);
                intent.putExtra("userName", users.get(recyclerView.getChildAdapterPosition(view)).getUserName());
                intent.putExtra("userNationality", users.get(recyclerView.getChildAdapterPosition(view)).getNationality());
                intent.putExtra("userStudyField", users.get(recyclerView.getChildAdapterPosition(view)).getStudyField());
                intent.putExtra("userHostCity", users.get(recyclerView.getChildAdapterPosition(view)).getHostCity());
                intent.putExtra("userType", users.get(recyclerView.getChildAdapterPosition(view)).getUserType());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get the array of users from Firebase Database (and sort them by name)
        usersRef = FirebaseDatabase.getInstance().getReference("users").orderByChild("userName");
        usersEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());
                User user = dataSnapshot.getValue(User.class);

                user.setKey(dataSnapshot.getKey());

                if (cityName.equalsIgnoreCase(user.getHostCity())) {
                    users.add(user);
                    adapter.notifyDataSetChanged();

                    if (emptyListText.getVisibility() != View.GONE)
                        emptyListText.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "childEventListener:onChildRemoved");
                String key = dataSnapshot.getKey();

                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getKey().equals(key)) {
                        users.remove(user);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, users.size());

                        return;
                    }

                }
                throw new IllegalStateException("Removed child not found in local array.");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

    }

    private void getPostsList(final Post.PostType postType) {
        posts = new ArrayList<>();
        final PostsAdapter adapter = new PostsAdapter(posts, postType);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get the array of posts from Firebase Database
        postsRef = FirebaseDatabase.getInstance().getReference("posts").child(postType.getDbRef());
        postsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());
                Post post = dataSnapshot.getValue(postType.getmClass());

                post.setKey(dataSnapshot.getKey());

                //-------------------------------------------------------------
                // Way to access children fields...
                Log.d(TAG, "Event place ID is " + ((Event) post).getPlaceID());
                //-------------------------------------------------------------

                if (cityKey.equals(post.getCity())) {
                    posts.add(post);
                    adapter.notifyDataSetChanged();

                    if (emptyListText.getVisibility() != View.GONE)
                        emptyListText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "childEventListener:onChildRemoved");
                String key = dataSnapshot.getKey();

                for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    if (post.getKey().equals(key)) {
                        posts.remove(post);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, posts.size());

                        return;
                    }

                }
                throw new IllegalStateException("Removed child not found in local array.");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        if (usersRef != null) {
            users.clear();
            usersRef.addChildEventListener(usersEventListener);
        }

        if (postsRef != null) {
            posts.clear();
            postsRef.addChildEventListener(postsEventListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (usersRef != null && usersEventListener != null)
            usersRef.removeEventListener(usersEventListener);

        if (postsRef != null && postsEventListener != null)
            postsRef.removeEventListener(postsEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
