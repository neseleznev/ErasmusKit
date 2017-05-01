package com.cooldevs.erasmuskit.ui.posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cooldevs.erasmuskit.R;
import com.cooldevs.erasmuskit.ui.BaseInternetActivity;
import com.cooldevs.erasmuskit.ui.posts.model.Event;
import com.cooldevs.erasmuskit.ui.posts.model.Post;
import com.cooldevs.erasmuskit.ui.profile.ProfileActivity;
import com.cooldevs.erasmuskit.ui.profile.User;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.cooldevs.erasmuskit.utils.FacebookParser.getEventsListAsync;
import static com.cooldevs.erasmuskit.utils.Utils.toPossessive;

public class PostsActivity extends BaseInternetActivity {

    private static final String TAG = "PostsActivity";

    private String cityName;
    private String cityKey;
    private String cityFacebookGroupId;
    private int citySection = -1;
    private RecyclerView recyclerView;

    private Query usersRef;
    private ChildEventListener usersEventListener;
    private ArrayList<User> users = new ArrayList<>();

    private Query postsRef;
    private ChildEventListener postsEventListener;
    private final ArrayList<Post> posts = new ArrayList<>();
    private EventsComparator eventsComparator = new EventsComparator();

    private TextView emptyListText;

    private enum FacebookParsingStatus {
        Ready, InProgress, Finished  // Semaphore
    }
    private FacebookParsingStatus facebookParsingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_posts);

        // Initialize views
        recyclerView = (RecyclerView) findViewById(R.id.posts_recView);
        emptyListText = (TextView) findViewById(R.id.empty_list_text);

        getIntentExtras(getIntent());
        setDataListeners();
        setFabFunctionality();
    }

    private void getIntentExtras(Intent intent) {
        cityName = intent.getStringExtra("cityName");
        cityKey = intent.getStringExtra("cityKey");
        if (!intent.hasExtra("cityFacebookGroupId")
                || intent.getStringExtra("cityFacebookGroupId") == null) {
            cityFacebookGroupId = getString(R.string.default_erasmus_facebook_group);
        } else {
            cityFacebookGroupId = intent.getStringExtra("cityFacebookGroupId");
        }
        citySection = intent.getIntExtra("citySection", -1);
    }

    private void setFabFunctionality() {
        /*
        -------POSSIBLE VALUES-------
        citySection = 0 -> PEOPLE SECTION
        citySection = 1 -> EVENTS SECTION
        citySection = 2 -> TIPS SECTION
        citySection = 3 -> PLACES SECTION
        */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_post_fab);
        String toolbarTitle = toPossessive(cityName);
        Class mClass = null;

        switch (citySection) {
            case 0:
                toolbarTitle += " " + getString(R.string.city_section_1);
                fab.setVisibility(View.GONE);
                break;
            case 1:
                toolbarTitle += " " + getString(R.string.city_section_2);
                mClass = NewEventActivity.class;
                break;
            case 2:
                toolbarTitle += " " + getString(R.string.city_section_3);
                mClass = NewTipActivity.class;
                break;
            case 3:
                toolbarTitle += " " + getString(R.string.city_section_4);
                mClass = NewPlaceActivity.class;
                break;
        }

        // Set FAB listener (depending on city section)
        final Class finalMClass = mClass;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostsActivity.this, finalMClass);
                intent.putExtra("cityKey", cityKey);
                startActivity(intent);
            }
        });

        // Finish activity from toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(toolbarTitle);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setDataListeners() {
        switch (citySection) {
            case 0:
                setPeopleListeners();
                break;
            case 1:
                setPostsListeners(Post.PostType.EVENT);
                break;
            case 2:
                setPostsListeners(Post.PostType.TIP);
                break;
            case 3:
                setPostsListeners(Post.PostType.PLACE);
                break;
        }
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        View noInternet = findViewById(R.id.no_internet_view);
        if (facebookParsingStatus == FacebookParsingStatus.Finished) {
            facebookParsingStatus = FacebookParsingStatus.Ready;
        }
        users.clear();
        posts.clear();
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        if (isConnected) {
            Log.d(TAG, "Internet connection established");
            noInternet.setVisibility(View.GONE);
            if (citySection == 1  // EVENTS
                    && facebookParsingStatus == FacebookParsingStatus.Ready) {
                addEventsFromFacebook();
            }
        } else {
            Log.d(TAG, "Internet connection lost");
            noInternet.setVisibility(View.VISIBLE);

            if (usersRef != null && usersEventListener != null) {
                usersRef.removeEventListener(usersEventListener);
                usersRef.addChildEventListener(usersEventListener);
            }
            if (postsRef != null && postsEventListener != null) {
                postsRef.removeEventListener(postsEventListener);
                postsRef.addChildEventListener(postsEventListener);
            }
        }
    }

    /**
     * Setting listener for getting the list of people registered in this city
     * (from Firebase Realtime Database) and listener to handle click on list item.
     */
    private void setPeopleListeners() {
        users = new ArrayList<>();
        final PeopleAdapter adapter = new PeopleAdapter(users);
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
                intent.putExtra("userFacebookLink", users.get(recyclerView.getChildAdapterPosition(view)).getUserFacebookLink());
                intent.putExtra("userPicture", users.get(recyclerView.getChildAdapterPosition(view)).getUserPicture());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get the array of users from Firebase Database (QUERY BY CITY)
        usersRef = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("hostCity").equalTo(cityName);
        usersEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());
                User user = dataSnapshot.getValue(User.class);

                user.setKey(dataSnapshot.getKey());

                users.add(user);
                adapter.notifyDataSetChanged();

                if (emptyListText.getVisibility() != View.GONE)
                    emptyListText.setVisibility(View.GONE);

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

    /**
     * Setting listener for getting the list of posts for this city, of the specified type
     * (from Firebase Realtime Database).
     * @param postType the type of posts. See {@link Post.PostType}
     */
    private void setPostsListeners(final Post.PostType postType) {
        posts.clear();
        final PostsAdapter adapter = new PostsAdapter(posts, postType);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get the array of posts from Firebase Database (QUERY BY CITY)
        postsRef = FirebaseDatabase.getInstance().getReference("posts").child(postType.getDbRef())
                .orderByChild("city").equalTo(cityKey);
        postsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());
                Post post = dataSnapshot.getValue(postType.getmClass());

                post.setKey(dataSnapshot.getKey());

                //-------------------------------------------------------------
                // Way to access children fields...
                // Log.d(TAG, "Event place ID is " + ((Event) post).getPlaceID());
                //-------------------------------------------------------------

                posts.add(post);

                // Sort with timestamps. Well, in decreases speed, but let's take a look
                // sort O(n*log(n)) * [each add] O(n) = O(n*n*log(n))
                // If we have 128 posts -> 114688 operations or 0.1sec on every modern processor
                Collections.sort(posts, eventsComparator);
                adapter.notifyDataSetChanged();

                if (emptyListText.getVisibility() != View.GONE)
                    emptyListText.setVisibility(View.GONE);
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

    private void addEventsFromFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            Log.d(TAG, "User is authorized; parsing facebook for events...");
            facebookParsingStatus = FacebookParsingStatus.InProgress;

            getEventsListAsync(accessToken, cityFacebookGroupId, cityKey, posts,
                    new FacebookParsingFinishedListener() {
                        @Override
                        public void onFinish() {
                            facebookParsingStatus = FacebookParsingStatus.Finished;
                            if (emptyListText.getVisibility() != View.GONE) {
                                emptyListText.setVisibility(View.GONE);
                            }
                            Collections.sort(posts, eventsComparator);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

        } else {
            Toast.makeText(
                    PostsActivity.this,
                    "To get more results, log in\n" +
                            "Facebook in My profile section",
                    Toast.LENGTH_LONG).show();
        }
    }

    class EventsComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            if (o1 instanceof Event && o2 instanceof Event) {
                long t1 = ((Event) o1).getEventTimestamp() / 10000,
                        t2 = ((Event) o2).getEventTimestamp() / 10000;
                if (t1 < t2) {
                    return 1;
                }
                if (t1 == t2) {
                    return 0;
                }
                return -1;
            }
            return 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (usersRef != null) {
            users.clear();
            usersRef.removeEventListener(usersEventListener);
            usersRef.addChildEventListener(usersEventListener);
        }

        if (postsRef != null) {
            posts.clear();
            postsRef.removeEventListener(postsEventListener);
            postsRef.addChildEventListener(postsEventListener);
        }
        if (citySection == 1) {  // EVENTS
            facebookParsingStatus = FacebookParsingStatus.Ready;
            addEventsFromFacebook();
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

    public interface FacebookParsingFinishedListener {
        void onFinish();
    }
}
