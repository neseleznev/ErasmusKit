package com.cooldevs.erasmuskit;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    private boolean isFavourite = false;

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
        ctLayout.setTitle(getIntent().getStringExtra("city_name"));

        // Initializing an empty recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.city_recView);
        CitiesAdapter adapter = new CitiesAdapter(new ArrayList<City>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Floating Action Button
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.star_city_fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int drawableIcon = isFavourite ? R.drawable.ic_star_border_black_24dp : R.drawable.ic_star_black_24dp;
                int message = isFavourite ? R.string.fav_city_remove_snack : R.string.fav_city_add_snack;

                button.setImageDrawable(ContextCompat.getDrawable(CityActivity.this, drawableIcon));
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
                isFavourite = !isFavourite;
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
