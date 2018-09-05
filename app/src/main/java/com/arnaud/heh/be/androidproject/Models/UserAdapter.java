package com.arnaud.heh.be.androidproject.Models;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.arnaud.heh.be.androidproject.R;
import java.util.ArrayList;


/**
 * Model Adapter for a user list view
 *
 * @author Arnaud Urbain
 */

public class UserAdapter extends ArrayAdapter<User> {

    /**
     * References.
     */
    private Context context;
    private int layoutResourceId;
    private ArrayList<User> data = null;

    /**
     * Constructor.
     *
     * @param context context of the activity
     * @param layoutResourceId id of the layout resource
     * @param data list of users
     */
    public UserAdapter(Context context, int layoutResourceId, ArrayList<User> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    /**
     * Returns a view corresponding to the data at the specified position.
     *
     * @param position position of the item within the adapter's data set of the item whose view we want
     * @param convertView old view to reuse
     * @param parent parent view that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        UserHolder holder;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UserHolder();
            holder.tv_lv_user_row_firstname = row.findViewById(R.id.tv_lv_user_row_firstname);
            holder.tv_lv_user_row_name = row.findViewById(R.id.tv_lv_user_row_name);
            holder.tv_lv_user_row_mail = row.findViewById(R.id.tv_lv_user_row_mail);
            holder.ll_lv_user_row_layout = row.findViewById(R.id.ll_lv_user_row_layout);

            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }

        User user = data.get(position);
        holder.tv_lv_user_row_firstname.setText("First Name : " + user.getFirstName());
        holder.tv_lv_user_row_name.setText("Name : " + user.getName());
        holder.tv_lv_user_row_mail.setText("Mail Address : " + user.getMail());

        if(user.getRights().toString().equals("admin")) {
            holder.ll_lv_user_row_layout.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorBackgroundAdmin));
        } else {
            holder.ll_lv_user_row_layout.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorBackground));
        }

        return row;
    }

    /**
     * UI references.
     */
    static class UserHolder {
        TextView tv_lv_user_row_firstname;
        TextView tv_lv_user_row_name;
        TextView tv_lv_user_row_mail;
        LinearLayout ll_lv_user_row_layout;
    }

}
