package com.aaroncampbell.peoplemon.Stages;

import android.app.Application;

import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Riggers.SlideRigger;
import com.davidstemmer.screenplay.stage.Stage;

/**
 * Created by aaroncampbell on 11/11/16.
 */

public class NearbyListStage extends IndexedStage {
    private final SlideRigger rigger;

    public NearbyListStage(Application context) {
        //ScreenPlay and Flow handle the work
        super(NearbyListStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public NearbyListStage() {
        this(PeopleMonApplication.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.nearby_view;
    }


    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}
