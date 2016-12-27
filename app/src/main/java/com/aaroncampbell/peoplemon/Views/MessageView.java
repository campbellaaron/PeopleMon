package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by aaroncampbell on 11/10/16.
 */

public class MessageView extends LinearLayout {
    private Context context;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
}
