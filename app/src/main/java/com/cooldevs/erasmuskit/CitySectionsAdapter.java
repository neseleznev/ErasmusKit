package com.cooldevs.erasmuskit;

import android.content.Context;
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

public class CitySectionsAdapter extends RecyclerView.Adapter<CitySectionsAdapter.CitySectionViewHolder> implements View.OnClickListener {

    private ArrayList<CitySection> citySections;
    private View.OnClickListener listener;

    public CitySectionsAdapter(ArrayList<CitySection> citySections) {
        this.citySections = citySections;
    }

    @Override
    public CitySectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_section_card, parent, false);
        itemView.setOnClickListener(this);

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

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
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
