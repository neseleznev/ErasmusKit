package com.cooldevs.erasmuskit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mario on 19/03/2017
 */

public class CitySectionsAdapter extends RecyclerView.Adapter<CitySectionsAdapter.CitySectionViewHolder> {

    private ArrayList<CitySection> citySections;

    public CitySectionsAdapter(ArrayList<CitySection> citySections) {
        this.citySections = citySections;
    }

    @Override
    public CitySectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_section_card, parent, false);

        return new CitySectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CitySectionViewHolder holder, int position) {
        CitySection citySection = citySections.get(position);
        holder.bindSection(citySection);
    }

    @Override
    public int getItemCount() {
        return citySections.size();
    }

    class CitySectionViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionTitle;
        private ImageView sectionIcon;

        CitySectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.city_section_title);
            sectionIcon = (ImageView) itemView.findViewById(R.id.city_section_icon);
        }

        void bindSection(final CitySection citySection) {
            sectionTitle.setText(citySection.getTitle());
            sectionIcon.setImageResource(citySection.getIcon());
        }
    }
}
