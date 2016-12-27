package com.aaroncampbell.peoplemon.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aaroncampbell on 12/27/16.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CaughtHolder> {

    @Override
    public MessageAdapter.CaughtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.CaughtHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CaughtHolder extends RecyclerView.ViewHolder {

        public CaughtHolder(View itemView) {
            super(itemView);
        }
    }
}
