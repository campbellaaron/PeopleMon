package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.aaroncampbell.peoplemon.Adapters.MessageAdapter;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aaroncampbell on 11/10/16.
 */

public class MessageView extends RelativeLayout {
    private Context context;
    private RestClient restClient;
    private MessageAdapter messageAdapter;
    private String recipientId;
    private String message;

    @Bind(R.id.message_view)
    RecyclerView recyclerView;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
