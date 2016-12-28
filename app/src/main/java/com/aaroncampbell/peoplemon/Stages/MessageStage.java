package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 12/27/16.
 */

public class MessageStage extends IndexedStage {
    private final SlideRigger rigger;

    public MessageStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(MessageStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public MessageStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.message_view;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }

}
