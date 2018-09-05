package com.arnaud.heh.be.androidproject.Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.arnaud.heh.be.androidproject.R;

/**
 * Model Adapter for a automaton list view
 *
 * @author Arnaud Urbain
 */
public class AutomatonAdapter extends ArrayAdapter<Automaton> {

    /**
     * References.
     */
    private Context context;
    private int layoutResourceId;
    private ArrayList<Automaton> data = null;

    /**
     * Constructor.
     *
     * @param context context of the activity
     * @param layoutResourceId id of the layout resource
     * @param data list of users
     */
    public AutomatonAdapter(Context context, int layoutResourceId, ArrayList<Automaton> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    /**
     * UI references.
     */
    static class AutomatonHolder {
        TextView tv_gv_automaton_row_name;
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
        AutomatonAdapter.AutomatonHolder holder;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AutomatonHolder();
            holder.tv_gv_automaton_row_name = row.findViewById(R.id.tv_gv_automaton_row_name);

            row.setTag(holder);
        } else {
            holder = (AutomatonAdapter.AutomatonHolder) row.getTag();
        }

        Automaton automaton = data.get(position);
        holder.tv_gv_automaton_row_name.setText("" + automaton.getName());

        return row;
    }



    }
