package com.cooldevs.erasmuskit;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.cooldevs.erasmuskit.Utils.getTimestamp;

/**
 * Parser class with abstract methods for different purposes. Connection require AccessToken
 */

public class FacebookParser {

    private static final String TAG = "FacebookParser";
    /**
     * Asyncronoysly get events from facebook's group and put them into posts
     * @param accessToken Valid fresh access token
     * @param facebookGroupId String [0-9a-zA-Z] facebook's group identifier
     * @param posts reference to ArrayList<Post> to put there results
     * @param adapter reference to PostsAdapter to invoke .notifyDataSetChanged()
     */
    static void getEventsListAsync(AccessToken accessToken, final String facebookGroupId,
                                   final String cityKey,
                                   final ArrayList<Post> posts, final PostsAdapter adapter) {

        GraphRequest request = new GraphRequest(accessToken,
                facebookGroupId + "/events",  // TODO v2.0 parse multiple groups (bundle query)
                null,
                HttpMethod.GET,
                new GraphRequest.Callback()
                {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, facebookGroupId + "/events\t" + response.toString());
                        JSONArray data = null;
                        try {
                            data = response.getJSONObject().getJSONArray("data");
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                            return;
                        }

                        String name, descr, placeName, facebookLink;
                        long startTime;
                        for (int i = 0; i < data.length(); ++i) {
                            try
                            {
                                JSONObject object = data.getJSONObject(i);
                                name = object.getString("name");
                                descr = object.getString("description");
                                if (descr.length() > 500) {
                                    descr = descr.substring(0, 500) + "...";
                                }
                                startTime = getTimestamp(object.getString("start_time"));
                                if (object.has("place")) {
                                    placeName = object.getJSONObject("place").getString("name");
                                } else {
                                    placeName = "Unknown";
                                }
                                facebookLink = "https://www.facebook.com/" + object.getString("id");

                                posts.add(new Event(
                                        name,facebookLink+ descr, cityKey, System.currentTimeMillis(),
                                        null, placeName, startTime, facebookLink));
                                adapter.notifyDataSetChanged();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                "v2.9");
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,start_time,id,description,place{name}");
        parameters.putString("limit", "100");
        request.setParameters(parameters);
        request.executeAsync();
    }

    static FacebookCallback<LoginResult> getUpdateUserAfterLoginCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String link = object.getString("link");
                                    String cover = object.getJSONObject("cover").getString("source");
                                    Log.d(TAG, "Got fb fields: " + link + ", " + cover);

                                    // Get current user
                                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance()
                                            .getCurrentUser();
                                    if (firebaseUser == null) {
                                        throw new IllegalStateException("User must not be null " +
                                                "in this activity!");
                                    }
                                    final String userKey = firebaseUser.getEmail().replace(".", "");
                                    DatabaseReference userRef = FirebaseDatabase.getInstance()
                                            .getReference("users").child(userKey);
                                    // Save obtained fields
                                    userRef.child("userFacebookLink").setValue(link);
                                    userRef.child("userPicture").setValue(cover);
                                    Log.d(TAG, "Added fields to user's profile");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "link,cover"); // id,name,email,gender,
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError");
                Log.v(TAG, exception.getCause().toString());
            }
        };
    }
}
