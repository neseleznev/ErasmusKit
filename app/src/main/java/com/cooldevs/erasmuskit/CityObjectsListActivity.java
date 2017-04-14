package com.cooldevs.erasmuskit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CityObjectsListActivity extends AppCompatActivity {

    String city;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    private static final String TAG = "CityObjectsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_objects_list);

        mAuth = FirebaseAuth.getInstance();


        city=getIntent().getStringExtra("city");
        int id =getIntent().getIntExtra("id",0);

        switch (id){
            case 0:
                peopleList();
                break;

            case 1:
                Toast.makeText(getApplicationContext(),"Events List",Toast.LENGTH_SHORT).show();
                break;

            case 2:
                Toast.makeText(getApplicationContext(),"Tips List",Toast.LENGTH_SHORT).show();
                break;

            case 3:
                Toast.makeText(getApplicationContext(),"Places List",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    void peopleList(){

        recyclerView = (RecyclerView) findViewById(R.id.objects_recView);
        final ArrayList<User> users = new ArrayList<>();
        final UsersAdapter adapter = new UsersAdapter(users);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click on the element " + recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(CityObjectsListActivity.this,ProfileActivity.class);
                intent.putExtra("userName",users.get(recyclerView.getChildAdapterPosition(view)).getUserName());
                intent.putExtra("userNationality",users.get(recyclerView.getChildAdapterPosition(view)).getNationality());
                intent.putExtra("userStudyField",users.get(recyclerView.getChildAdapterPosition(view)).getStudyField());
                intent.putExtra("userHostCity",users.get(recyclerView.getChildAdapterPosition(view)).getHostCity());
                intent.putExtra("userType",users.get(recyclerView.getChildAdapterPosition(view)).getUserType());
                startActivity(intent);


            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get the array of users from Firebase Database (and sort them by name)
        Query usersRef = FirebaseDatabase.getInstance().getReference("users").orderByChild("userName");
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());
                User user = dataSnapshot.getValue(User.class);

                user.setKey(dataSnapshot.getKey());


                if(city.equalsIgnoreCase(user.getHostCity())){

                    users.add(user);
                    adapter.notifyDataSetChanged();
                }

                if(users.isEmpty()){
                    Toast.makeText(getApplicationContext(),"There's no one in "+city ,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {  }

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
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {  }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        };
        usersRef.addChildEventListener(childEventListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                return true;

            case R.id.my_profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
