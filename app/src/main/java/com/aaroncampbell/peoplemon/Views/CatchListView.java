package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.aaroncampbell.peoplemon.Adapters.CatchListAdapter;
import com.aaroncampbell.peoplemon.Models.User;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class CatchListView extends RelativeLayout {
    private Context context;
    private RestClient restClient;
    private CatchListAdapter catchListAdapter;
    private Integer usrId;
    private String peepName;
    private String base64mage;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    public CatchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        catchListAdapter = new CatchListAdapter(new ArrayList<User>(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(catchListAdapter);
        getUserInfo();
    }

    public void getUserInfo() {
        restClient = new RestClient();
        restClient.getApiService().caughtPeeps().enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response.isSuccessful()) {

                    catchListAdapter.caughtPeeps = new ArrayList<>(Arrays.asList(response.body()));

                    for (User user : catchListAdapter.caughtPeeps) {
                        catchListAdapter.notifyDataSetChanged();
                        user.getUserName();
                        user.setCaughtUserId(user.getUserId());
                        user.getCaughtUserId();
                        Log.d("+++++++++++", user.getUserName().toString());
                    }
//
                }
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {

            }
        });
    }
}
