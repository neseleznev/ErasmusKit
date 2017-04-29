package com.cooldevs.erasmuskit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maite on 11/04/17
 */

class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> implements View.OnClickListener {

    private ArrayList<User> usersList;
    private View.OnClickListener listener;

    UsersAdapter(ArrayList<User> users) {
        this.usersList = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        itemView.setOnClickListener(this);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.bindUser(user);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
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

    class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView userIcon;
        private TextView userName;
        private TextView userNationality;
        private ImageView userFacebookButton;

        UserViewHolder(View itemView) {
            super(itemView);

            userIcon = (ImageView) itemView.findViewById(R.id.list_item_icon);
            userName = (TextView) itemView.findViewById(R.id.list_item_title);
            userNationality = (TextView) itemView.findViewById(R.id.list_item_subtitle);
            userFacebookButton = (ImageView) itemView.findViewById(R.id.list_item_secondary_icon);
        }

        void bindUser(final User user) {
            userIcon.setImageResource(R.drawable.ic_person_black_24dp);
            userName.setText(user.getUserName());
            userNationality.setText(user.getNationality());

            final String link = user.getUserFacebookLink();
            if (link != null && !link.equals("")) {
                userFacebookButton.setImageResource(R.drawable.com_facebook_button_icon_blue);
                userFacebookButton.setVisibility(View.VISIBLE);
            }
        }
    }

}
