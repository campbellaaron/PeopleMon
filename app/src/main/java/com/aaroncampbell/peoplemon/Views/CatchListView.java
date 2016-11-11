package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    public ArrayList<User> caughtPeeps;
    private RestClient restClient;
    private CatchListAdapter catchListAdapter;
    private Integer usrId;
    private String peepName;
    private String base64mage;

    @Bind(R.id.imageView)
    ImageView usrAvatar;
    @Bind(R.id.id_textview)
    TextView userId;
    @Bind(R.id.name_textview)
    TextView usrName;
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

        getUserInfo();
    }

    public void getUserInfo() {
        final User user = new User();
        restClient = new RestClient();
        restClient.getApiService().caughtPeeps().enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response.isSuccessful()) {
                    catchListAdapter.peeps = new ArrayList<>(Arrays.asList(response.body()));
                    catchListAdapter.notifyDataSetChanged();
                    base64mage = user.getBase64();
                    byte[] decodedString = Base64.decode(base64mage, Base64.DEFAULT);
                    Bitmap biteMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    usrAvatar.setImageBitmap(biteMap);
                    userId.setText(user.getUserId());
                    usrName.setText(user.getUserName());
                }
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {

            }
        });
    }
}