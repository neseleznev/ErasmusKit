package com.cooldevs.erasmuskit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maite on 12/04/17.
 */

public class ProfileSectionAdapter extends RecyclerView.Adapter<ProfileSectionAdapter.ProfileSectionViewHolder> {

    private ArrayList<ProfileSection> profileSections;

    public ProfileSectionAdapter(ArrayList<ProfileSection> profileSections) {
        this.profileSections = profileSections;
    }

    @Override
    public ProfileSectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_section_card, parent,false);

        return new ProfileSectionViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(ProfileSectionViewHolder holder, int position) {

        ProfileSection profileSection = profileSections.get(position);
        holder.bindSection(profileSection);
    }



    @Override
    public int getItemCount() { return profileSections.size();}

    class ProfileSectionViewHolder extends RecyclerView.ViewHolder{

        private TextView sectionTitle;
        private ImageView sectionIcon;

        public ProfileSectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.profile_section_value);
            sectionIcon = (ImageView) itemView.findViewById(R.id.profile_section_icon);


        }

        void bindSection (final ProfileSection profileSection) {
            sectionTitle.setText(profileSection.getValue());
            sectionIcon.setImageResource(profileSection.getIcon());
        }
    }
}
