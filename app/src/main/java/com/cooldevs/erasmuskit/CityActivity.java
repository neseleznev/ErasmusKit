package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    private boolean isFavorite = false;

    private static final String TAG = "CityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.city_activity_toolbar);
        setSupportActionBar(toolbar);

        // Finish activity from status bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setting the title in the toolbar (actually in the "collapsing toolbar layout")
        CollapsingToolbarLayout ctLayout = (CollapsingToolbarLayout) findViewById(R.id.city_ctlayout);
        final String cityName = getIntent().getStringExtra("cityName");
        final String cityKey = getIntent().getStringExtra("cityKey");
        final String cityFacebookGroupId = getIntent().getStringExtra("cityFacebookGroupId");
        ctLayout.setTitle(cityName);

        // Setting city picture if exists
        ImageView imageView = (ImageView) findViewById(R.id.imgToolbar);
        final String cityPictureUrl = getIntent().getStringExtra("cityPicture");
        if (cityPictureUrl != null && !cityPictureUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(cityPictureUrl).into(imageView);
        }

        // Initializing recyclerView
        ArrayList<Section> sections = new ArrayList<>();
        sections.add(new Section(R.drawable.ic_people_black_24dp, getString(R.string.city_section_1)));
        sections.add(new Section(R.drawable.ic_event_black_24dp, getString(R.string.city_section_2)));
        sections.add(new Section(R.drawable.ic_lightbulb_outline_black_24dp, getString(R.string.city_section_3)));
        sections.add(new Section(R.drawable.ic_place_black_24dp, getString(R.string.city_section_4)));

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_recView);
        recyclerView.setHasFixedSize(true);
        SectionsAdapter adapter = new SectionsAdapter(sections);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Click on the element " + recyclerView.getChildAdapterPosition(view));

                Intent intent = new Intent(CityActivity.this, PostsActivity.class);
                intent.putExtra("cityName", cityName);
                intent.putExtra("cityKey", cityKey);
                intent.putExtra("cityFacebookGroupId", cityFacebookGroupId);
                intent.putExtra("citySection", recyclerView.getChildAdapterPosition(view));
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(CityActivity.this, 2));


        // Floating Action Button
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.star_city_fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int drawableIcon = isFavorite ? R.drawable.ic_star_border_black_24dp : R.drawable.ic_star_black_24dp;
                int message = isFavorite ? R.string.fav_city_remove_snack : R.string.fav_city_add_snack;

                button.setImageDrawable(ContextCompat.getDrawable(CityActivity.this, drawableIcon));
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
                isFavorite = !isFavorite;
            }
        });
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
