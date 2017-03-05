package com.cooldevs.erasmuskit;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by mario on 04/03/2017
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private ArrayList<City> citiesList;

    private static final String TAG = "CitiesAdapter";

    public CitiesAdapter(ArrayList<City> cities) {
        this.citiesList = cities;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);

        return new CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = citiesList.get(position);
        holder.bindCity(city);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;
        private TextView cityCountry;
        private ImageView deleteIcon;

        CityViewHolder(View itemView) {
            super(itemView);

            cityName = (TextView) itemView.findViewById(R.id.city_name);
            cityCountry = (TextView) itemView.findViewById(R.id.city_country);
            deleteIcon = (ImageView) itemView.findViewById(R.id.city_delete_icon);
        }

        void bindCity(final City c) {
            cityName.setText(c.getName());
            cityCountry.setText("Country: " + c.getCountry());

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(getAdapterPosition());

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cities").child(c.getKey());
                    ref.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null)
                                Log.d(TAG, "removeValue:onComplete, " + databaseError.getDetails());
                        }
                    });
                }
            });
        }
    }

    private void removeAt(int position) {
        citiesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, citiesList.size());
    }
}
