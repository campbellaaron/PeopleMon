package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.aaroncampbell.peoplemon.Adapters.NearbyListAdapter;
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
 * Created by aaroncampbell on 11/11/16.
 */

public class NearbyListView extends RelativeLayout {
    private Context context;
    public ArrayList<User> caughtPeeps;
    private RestClient restClient;
    private NearbyListAdapter nearbyListAdapter;
    private Integer usrId;
    private String peepName;
    private String base64mage;

    @Bind(R.id.nearby_recycler)
    RecyclerView recyclerView;


    public NearbyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        nearbyListAdapter = new NearbyListAdapter(new ArrayList<User>(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(nearbyListAdapter);
        getUserInfo();
    }

    public void getUserInfo() {
//        final User user = new User();
        restClient = new RestClient();
        restClient.getApiService().nearby(500).enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response.isSuccessful()) {

                    nearbyListAdapter.peeps = new ArrayList<>(Arrays.asList(response.body()));

                    for (User user : nearbyListAdapter.peeps) {
                        nearbyListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {

            }
        });
    }
}
