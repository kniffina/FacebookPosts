package com.kniffina.android.facebookposts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

public class FacebookAdapter extends ArrayAdapter<FacebookData> {
    public FacebookAdapter(Context context, ArrayList<FacebookData> data) {
        super(context, 0, data);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.posts_list_item, parent, false);
        }

        FacebookData currData = getItem(position);

        TextView date = (TextView) listItemView.findViewById(R.id.date_text_view);
        date.setText(currData.getDate());

        TextView message = (TextView) listItemView.findViewById(R.id.message_text_view);
        message.setText(currData.getMessage());

        TextView id = (TextView) listItemView.findViewById(R.id.id_for_given_post);
        id.setText(currData.getId());

        return listItemView;
    }
}
