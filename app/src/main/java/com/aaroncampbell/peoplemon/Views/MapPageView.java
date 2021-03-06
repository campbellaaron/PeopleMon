package com.aaroncampbell.peoplemon.Views;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aaroncampbell.peoplemon.MainActivity;
import com.aaroncampbell.peoplemon.Models.Account;
import com.aaroncampbell.peoplemon.Models.User;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.PeopleMonApplication;
import com.aaroncampbell.peoplemon.R;
import com.aaroncampbell.peoplemon.Stages.CatchListStage;
import com.aaroncampbell.peoplemon.Stages.MessageStage;
import com.aaroncampbell.peoplemon.Stages.NearbyListStage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class MapPageView extends RelativeLayout implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {
    //    STHPNT 38.451, -82.593

    private Context context;
    public static final String TAG = MapPageView.class.getSimpleName();

    @Bind(R.id.map)
    MapView mapView;

    GoogleMap map;
    Integer BUILDING_LEVEL = 17;
    private double getLat, getLng;
    private double lat, lng;
    Location mLastLocation;
    GoogleApiClient apiClient;
    final Handler handler = new Handler();
    private String userId;
    private String caughtUserId;
    private String userName;
    private String yourName;
    private double userLat;
    private double userLng;
    private String base64ava;
    private String encodedImage;
    private final Integer radiusInMeters = 500;
    public Bitmap userMarker;

    public MapPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        mapView.onCreate(((MainActivity) context).savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);   // Needs to go in onFinishInflate()

        apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        handler.post(locationCheck);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        map.setMinZoomPreference(6.0f);
        map.setMaxZoomPreference(20.0f);
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {}

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);

    }


    Runnable locationCheck = new Runnable() {
        @Override
        public void run() {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                if (mLastLocation != null) {
                    handleNewLocation();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                handler.postDelayed(this, 2000);
            }
        }
    };

    private void handleNewLocation() {
        final BitmapDescriptor zomCon = BitmapDescriptorFactory.fromResource(R.drawable.zombie);
        final LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        RestClient currentMarker = new RestClient();
        currentMarker.getApiService().viewProfile().enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Account account = response.body();
                    base64ava = account.getBase64Avatar();
                    yourName = account.getFullName();
                    byte[] decodedString = Base64.decode(base64ava, Base64.DEFAULT);
                    Bitmap biteMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    biteMap = Bitmap.createScaledBitmap(biteMap, 150, 150, false);
                    userMarker = biteMap;

                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(userMarker))
                            .title(yourName);
                    map.addMarker(options);
                } else {

                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, BUILDING_LEVEL));
        final Circle circle = map.addCircle(new CircleOptions().center(latLng)
                .strokeColor(Color.RED).radius(1000));

        ValueAnimator valAnim = new ValueAnimator();
        valAnim.setRepeatCount(ValueAnimator.INFINITE);
        valAnim.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
        valAnim.setIntValues(0, 100000);
        valAnim.setDuration(10000);
        valAnim.setEvaluator(new IntEvaluator());
        valAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                // Log.e("", "" + animatedFraction);
                circle.setRadius(animatedFraction * 100);
            }
        });
        valAnim.start();

        // Call Nearby method
        findNearby();

        Account account = new Account(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        RestClient restClient = new RestClient();
        restClient.getApiService().checkIn(account).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, latLng.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.checkin_failed, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void findNearby() {
        final BitmapDescriptor spyCon = BitmapDescriptorFactory.fromResource(R.drawable.spy);
        RestClient restClient = new RestClient();
        restClient.getApiService().nearby(500).enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response.isSuccessful()) {
                   for (User user : response.body()) {
                       userLat = user.getLatitude();
                       userLng = user.getLongitude();
                       userName = user.getUserName();
                       LatLng userPos = new LatLng(userLat, userLng);
                       userId = user.getUserId();
                       Log.d("NEARBYPEEPS=========&", userId.toString() + ", " + userName.toString());

                       String userBase64 = user.getAvatarBase64();
                       if (userBase64 == null || userBase64.length() < 100) {
                           MarkerOptions peeps = new MarkerOptions()
                                   .position(userPos)
                                   .snippet(userId)
                                   .title(userName)
                                   .icon(spyCon);
                           map.addMarker(peeps);
                           Log.d("----->", userName + " " + userId);
                       } else {
                           try {
                               byte[] decodedString = Base64.decode(userBase64, Base64.DEFAULT);
                               Log.d(TAG, decodedString.toString());
                               Bitmap decodeAvatar = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                               decodeAvatar = Bitmap.createScaledBitmap(decodeAvatar, 100, 100, false);

                               MarkerOptions peeps = new MarkerOptions()
                                       .position(userPos)
                                       .snippet(userId)
                                       .title(userName)
                                       .icon(BitmapDescriptorFactory.fromBitmap(decodeAvatar));
                               map.addMarker(peeps);
                               Log.d("----->", userName + " " + userId);
                           } catch (Exception e) {}

                       }

                       final Circle userCircle = map.addCircle(new CircleOptions().center(userPos)
                               .strokeColor(Color.BLUE).radius(1000));

                       ValueAnimator valAnim = new ValueAnimator();
                       valAnim.setRepeatCount(ValueAnimator.INFINITE);
                       valAnim.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
                       valAnim.setIntValues(0, 100000);
                       valAnim.setDuration(10000);
                       valAnim.setEvaluator(new IntEvaluator());
                       valAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                       valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                           @Override
                           public void onAnimationUpdate(ValueAnimator valueAnimator) {
                               float animatedFraction = valueAnimator.getAnimatedFraction();
                               // Log.e("", "" + animatedFraction);
                               userCircle.setRadius(animatedFraction * 100);
                           }
                       });
                       valAnim.start();

                   }
                } else {
                    Toast.makeText(context, getContext().getString(R.string.nearby_failed) + ":" + response.code(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                Toast.makeText(context, getContext().getString(R.string.nearby_failed), Toast.LENGTH_SHORT);
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                userName = marker.getTitle();
                userId =  marker.getSnippet();
                new AlertDialog.Builder(context)
                        .setTitle(getContext().getString(R.string.peep_alert_title))
                        .setMessage(getContext().getString(R.string.peep_alert_message))
                        .setPositiveButton("Catch", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                catchPeeps();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .setIcon(R.drawable.pokeball)
                        .show();
                return false;
            }
        });
    }

    public void catchPeeps() {
        final User user = new User(userId, radiusInMeters);
        RestClient restClient = new RestClient();
        restClient.getApiService().catchPeeps(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, R.string.catch_success, Toast.LENGTH_SHORT).show();
                        user.setCaughtUserId(user.getUserId());
                        user.getCaughtUserId();
                        Log.d("&&&&&", user.getUserId() + ", " + user.getCaughtUserId() + ", " + user.getUserName());

                } else {
                    Toast.makeText(context, getContext().getString(R.string.catch_failed) + ":" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, getContext().getString(R.string.catch_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.nearby)
    public void nearbyTapped() {
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new NearbyListStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.caught_button)
    public void caughtTapped() {
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new CatchListStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.messages)
    public void messageTapped() {
        Flow flow = PeopleMonApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new MessageStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }
}
