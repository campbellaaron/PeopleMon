package com.aaroncampbell.peoplemon.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaroncampbell.peoplemon.Models.User;
import com.aaroncampbell.peoplemon.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aaroncampbell on 11/11/16.
 */

public class NearbyListAdapter extends RecyclerView.Adapter<NearbyListAdapter.NearbyHolder> {
    public ArrayList<User> peeps;
    private Context context;

    public NearbyListAdapter(ArrayList<User> peeps, Context context) {
        this.peeps = peeps;
        this.context = context;
    } //Call immediately in onCreate to start process

    @Override // Getting our layout inflated into this memory space to deal with layout in code, applies to every row
    public NearbyListAdapter.NearbyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.nearby_list, parent, false);
        // Life cycle of cells - created, then binded
        return new NearbyListAdapter.NearbyHolder(inflatedView);
    }

    @Override // Where we actually start the work of getting position of each row
    public void onBindViewHolder(NearbyListAdapter.NearbyHolder holder, int position) {
        User user = peeps.get(position);
        holder.bindNearby(user);
    }

    @Override // First thing called by Recycler View
    public int getItemCount() {
        return peeps.size();
    }

    class NearbyHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.peep_id)
        TextView peepId;
        @Bind(R.id.peep_name)
        TextView nameView;

        public NearbyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // Puts data into UI
        public void bindNearby(User user) {
            peepId.setText(user.getUserId());
            nameView.setText(user.getUserName());
        }
    }
}
