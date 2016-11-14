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
 * Created by aaroncampbell on 11/8/16.
 */

public class CatchListAdapter extends RecyclerView.Adapter<CatchListAdapter.CaughtHolder> {
    public ArrayList<User> caughtPeeps;
    private Context context;

    public CatchListAdapter(ArrayList<User> caughtPeeps, Context context) {
        this.caughtPeeps = caughtPeeps;
        this.context = context;
    } //Call immediately in onCreate to start process

    @Override // Getting our layout inflated into this memory space to deal with layout in code, applies to every row
    public CaughtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.caught_list_item, parent, false);
        // Life cycle of cells - created, then binded
        return new CaughtHolder(inflatedView);
    }

    @Override // Where we actually start the work of getting position of each row
    public void onBindViewHolder(CaughtHolder holder, int position) {
            User user = caughtPeeps.get(position);
            holder.bindPeepsCaught(user);
    }

    @Override // First thing called by Recycler View
    public int getItemCount() {
        return caughtPeeps.size();
    }

    class CaughtHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.name_textview)
        TextView nameView;
        @Bind(R.id.id_textview)
        TextView peepId;

        private String userName;
        private String userId;


        public CaughtHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // Puts data into UI
        public void bindPeepsCaught(User user) {
            userId = user.getUserId().toString();
            userName = user.getUserName().toString();

            nameView.setText(userName);
            peepId.setText(userId);
        }
    }
}
