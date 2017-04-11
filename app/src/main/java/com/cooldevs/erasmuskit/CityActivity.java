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
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    private boolean isFavorite = false;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.city_activity_toolbar);
        setSupportActionBar(toolbar);

        // Finish activity from status bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setting the title in the toolbar (actually in the "collapsing toolbar layout")
        CollapsingToolbarLayout ctLayout = (CollapsingToolbarLayout) findViewById(R.id.city_ctlayout);
        city=getIntent().getStringExtra("city_name");
        ctLayout.setTitle(city);

        // Initializing recyclerView
        ArrayList<CitySection> sections = new ArrayList<>();
        sections.add(new CitySection(R.drawable.ic_people_black_24dp, getString(R.string.city_section_1)));
        sections.add(new CitySection(R.drawable.ic_event_black_24dp, getString(R.string.city_section_2)));
        sections.add(new CitySection(R.drawable.ic_lightbulb_outline_black_24dp, getString(R.string.city_section_3)));
        sections.add(new CitySection(R.drawable.ic_place_black_24dp, getString(R.string.city_section_4)));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_recView);
        recyclerView.setHasFixedSize(true);
        CitySectionsAdapter adapter = new CitySectionsAdapter(sections);
        adapter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Click on the element " + recyclerView.getChildAdapterPosition(view));

                Intent intent = new Intent(CityActivity.this, PeopleActivity.class);
                intent.putExtra("city",city);
                //Sintent.putExtra("city", cities.get(recyclerView.getChildAdapterPosition(view)).getName());
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
