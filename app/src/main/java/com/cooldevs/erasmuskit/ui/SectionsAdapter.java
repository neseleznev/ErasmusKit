package com.cooldevs.erasmuskit.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooldevs.erasmuskit.R;

import java.util.ArrayList;

/**
 * Created by maite on 12/04/17
 */

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.SectionViewHolder> implements View.OnClickListener {

    private ArrayList<Section> sections;
    private View.OnClickListener listener;

    public SectionsAdapter(ArrayList<Section> sections) {
        this.sections = sections;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_section, parent, false);
        itemView.setOnClickListener(this);

        return new SectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        Section section = sections.get(position);
        holder.bindSection(section);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionTitle;
        private ImageView sectionIcon;

        SectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.section_title);
            sectionIcon = (ImageView) itemView.findViewById(R.id.section_icon);
        }

        void bindSection(final Section section) {
            sectionTitle.setText(section.getTitle());
            sectionIcon.setImageResource(section.getIcon());
        }
    }
}
