package com.nect.friendlymony.Fragments.home;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.nect.friendlymony.Activity.BlockUserActivity;
import com.nect.friendlymony.Activity.FeedDetailActivity;
import com.nect.friendlymony.Activity.FilterActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Adapter.FeedsCardAdapter;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Feeds.DataItem;
import com.nect.friendlymony.Model.Feeds.FeedResponse;
import com.nect.friendlymony.Model.FilterModel;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.nect.friendlymony.Activity.MainActivity.mainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends BaseFragment {

    private static final int RESULT_FILTER = 222;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 11;
    String TAG = FeedFragment.class.getSimpleName();

    @BindView(R.id.cardstack_feed)
    CardStackView cardstack_feed;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.llLocation)
    LinearLayout llLocation;
    @BindView(R.id.llFilter)
    LinearLayout llFilter;
    @BindView(R.id.tvFilter)
    TextView tvFilter;

    @BindView(R.id.tvNo)
    TextView tvNo;
    FeedResponse feedsResponse = null;
    int page = 1;
    FeedsCardAdapter feedsAdapter;
    private List<DataItem> listFeed = new ArrayList<>();
    public FeedsCardAdapter.OnClick onClick = new FeedsCardAdapter.OnClick() {
        @Override
        public void onInfoClicked(int index) {

            Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
            intent.putExtra("fId", listFeed.get(index).getId());
            intent.putExtra("data", listFeed.get(index));
            intent.putExtra("Status", Constants.FEED_HOME);
            intent.putExtra("From", "Feeds");
            startActivity(intent);

        }

        @Override
        public void onDislike(int index) {
            swipeLeft();
        }

        @Override
        public void onLike(int index) {

            swipeRight();
        }

        @Override
        public void onCrush(int index) {
            swipeTop();
        }
    };
    private View view;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        ButterKnife.bind(this, view);
        if (!Places.isInitialized()) {
            Places.initialize(MainActivity.mainActivity.getApplicationContext(), getResources().getString(R.string.google_app_key));
        }


        /*CalligraphyUtils.applyFontToTextView(tvFilter, fontSemiBold);
        CalligraphyUtils.applyFontToTextView(tvLocation, fontSemiBold);*/

        HawkAppUtils.getInstance().resetFilter();


        tvLocation.setText(Pref.getStringValue(getActivity(), Constants.PREF_CITY, ""));

        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        Log.e("TOKEN", lp.getToken() + "");
/*
        for (int i = 0; i <= 10; i++) {
            DataItem di = new DataItem();
            di.setFirst("POSITION = " + i);
            listFeed.add(di);

        }*/
        feedsAdapter = new FeedsCardAdapter(getActivity(), onClick);
        cardstack_feed.setAdapter(feedsAdapter);

        feedsAdapter.addAll(listFeed);
        feedsAdapter.notifyDataSetChanged();
        setupCardStack();

        getFeeds();

        /*FirebaseDatabase.getInstance().getReference().child("Conversation").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "getChildrenCount==" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        return view;
    }

    private void getFeeds() {

        if (!isConnectedToInternet()) {

            return;
        }
        FilterModel fm = HawkAppUtils.getInstance().getFiltr();
        if (!isConnectedToInternet())
            return;
        if (page == 1) {
            //showProgress();
        }
        final Map<String, Object> request = new HashMap<>();
        request.put("page", page);


        if (fm != null) {
            if (!(fm.getShow_me() + "").equals("") && !(fm.getShow_me() + "").equals("null")) {
                request.put("vShow_me", fm.getShow_me());
            }

            request.put("vRadius", fm.getRadius());
            request.put("vAge_min", fm.getAge_min());
            request.put("vAge_max", fm.getAge_max());
        } /*else {
            String gender = HawkAppUtils.getInstance().getUSERDATA().getData().getvGender();
            String vShow_me = "Male";
            if ((gender+"").equalsIgnoreCase("Male"))
            {
                vShow_me = "Female";
            }

            request.put("vShow_me", vShow_me);

        }
*/

        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "feeds : PARAMETER==" + request.toString());
        Call<FeedResponse> call = RetrofitBuilder.getInstance().getRetrofit().feeds(request);
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                hideProgress();

                JSONObject jsonObj = null;
                if (response.isSuccessful()) {

                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "Response feeds: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "feeds : JSONException==" + e.getMessage());
                        Toast.makeText(MainActivity.mainActivity.getApplicationContext(), getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                    }

                    /*Pref.setStringValue(getContext(), Constants.IS_PAID, response.body().getData().get(0).getIs_paid());
                    Pref.setStringValue(getContext(), Constants.IS_AUTO_SUBSCRIBED, response.body().getData().get(0).getIs_auto_subscribe());*/
                    feedsResponse = response.body();
                    if (!isVisible()) {
                        return;
                    }
                    if (response.body().isSuccess()) {
                        if (feedsResponse.getData() != null && !feedsResponse.getData().isEmpty()) {
                            //viewfliper.setDisplayedChild(0);
                            if (page == 1) {
                                listFeed.clear();
                            }

                            Log.e("sizw", feedsResponse.getData().size() + "<<");
                            listFeed.addAll(feedsResponse.getData());
                            cardstack_feed.setPaginationReserved();
                            feedsAdapter.addAll(feedsResponse.getData());
                            feedsAdapter.notifyDataSetChanged();

                        }
                       /* if (feedsResponse.getAccoutStatus().equalsIgnoreCase(Constants.Inactive)) {
                            viewfliper.setDisplayedChild(3);
                        }*/
                    } else {

                        showToast(response.body().getMessage());
                        //viewfliper.setDisplayedChild(1);
                    }
                    /*if (feedsResponse.getAccoutStatus().equalsIgnoreCase(Constants.Inactive)) {
                        viewfliper.setDisplayedChild(3);
                    }*/

                    tvNo.setVisibility((listFeed.size() <= 0) ? View.VISIBLE : View.GONE);

                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(MainActivity.mainActivity, "feeds : onFailure==" + t.getMessage());
                if (isAdded()) {
                    Toast.makeText(getActivity(), getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setLikes(String feedtype, String fid, String fname) {

        if (!isConnectedToInternet()) {
            cardstack_feed.reverse();
            return;
        }
        String uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "";
        //Log.e(">>", "DATa" + feedtype + " " + fid + " " + fname);
        final Map<String, Object> request = new HashMap<>();
        request.put("userFrom", fid);
        request.put("userStatus", feedtype);
        request.put("user_id", uid);

        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "userLike : PARAMETER==" + request.toString());
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLike(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                // hideProgress();

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "Response userLike: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "userLike : JSONException==" + e.getMessage());
                    //Toast.makeText(MainActivity.mainActivity.getApplicationContext(), getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                }

                if (response.isSuccessful()) {
                    if (!isVisible()) {
                        return;
                    }
                    if (response.body().isSuccess()) {

                        if (feedtype.equalsIgnoreCase(Constants.FEED_CRUSH)) {
                            showToast("Crush sent successfully");
                        } else if (feedtype.equalsIgnoreCase(Constants.FEED_INTERESTED)) {
                            showToast("You have Expressed your Interest successfully");
                        }
                    } else {
                        cardstack_feed.reverse();
                        showToast(response.body().getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "setLikes : onFailure==" + t.getMessage());
            }
        });

    }

    private void setupCardStack() {


        cardstack_feed.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
                Log.d("CardStackView", "topIndex: " + cardstack_feed.getTopIndex());
                if (listFeed.size() < cardstack_feed.getTopIndex()) {
                    return;
                }
                if (direction == SwipeDirection.Left) {
                    Log.e(TAG, "onCardSwiped: LEFT");

                    setLikes(Constants.FEED_NOT_INTERESTED, "" + listFeed.get(cardstack_feed.getTopIndex() - 1).getId(), "" + listFeed.get(cardstack_feed.getTopIndex() - 1).getFirst());
                   /* if (feedsDataList.get(cardStackView.getTopIndex() - 1).getIsLiked().equalsIgnoreCase("Yes")) {
                        likedislike(Constants.LIKEDISLIKE.Dislike, "" + feedsDataList.get(cardStackView.getTopIndex() - 1).getId(), feedsDataList.get(cardStackView.getTopIndex() - 1).getFirst(), true);
                    } else {
                        likedislike(Constants.LIKEDISLIKE.Dislike, "" + feedsDataList.get(cardStackView.getTopIndex() - 1).getId(), feedsDataList.get(cardStackView.getTopIndex() - 1).getFirst(), false);
                    }
                    if (feedsDataList.get(cardStackView.getTopIndex() - 1).getIsLiked().equalsIgnoreCase("Yes")) {
                        ItsMatchActivity.startActivity(getActivity(), "" + feedsDataList.get(cardStackView.getTopIndex() - 1).getId(), feedsDataList.get(cardStackView.getTopIndex() - 1).getFirst());
                    }*/

                } else if (direction == SwipeDirection.Right) {
                    Log.e(TAG, "onCardSwiped: RIGHT");
                    setLikes(Constants.FEED_INTERESTED, "" + listFeed.get(cardstack_feed.getTopIndex() - 1).getId(), "" + listFeed.get(cardstack_feed.getTopIndex() - 1).getFirst());

                    // likedislike(Constants.LIKEDISLIKE.Like, "" + feedsDataList.get(cardStackView.getTopIndex() - 1).getId(), feedsDataList.get(cardStackView.getTopIndex() - 1).getFirst(), false);
                } else if (direction == SwipeDirection.Top) {
                    Log.e(TAG, "onCardSwiped: TOP");
                    setLikes(Constants.FEED_CRUSH, "" + listFeed.get(cardstack_feed.getTopIndex() - 1).getId(), "" + listFeed.get(cardstack_feed.getTopIndex() - 1).getFirst());

                    // likedislike(Constants.LIKEDISLIKE.Like, "" + feedsDataList.get(cardStackView.getTopIndex() - 1).getId(), feedsDataList.get(cardStackView.getTopIndex() - 1).getFirst(), false);
                }
                if (cardstack_feed.getTopIndex() % 5 == 0) {
                    Log.e(TAG, "onCardSwiped: %5");

                    /*if (mInterstitialAd!=null && mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }*/
                }
                if (cardstack_feed.getTopIndex() == feedsAdapter.getCount() - 5) {
                    Log.e("CardStackView", "Paginate: " + cardstack_feed.getTopIndex());
                    page++;
                    if (isVisible())
                        getFeeds();
                }
                if (feedsAdapter.getCount() == cardstack_feed.getTopIndex()) {
                    //Log.e(TAG, "onCardSwiped: cards empty");
                    tvNo.setVisibility(View.VISIBLE);
                } else {
                    tvNo.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCardReversed() {
                //  Log.d("CardStackView", "onCardReversed");
                if (feedsAdapter.getCount() == cardstack_feed.getTopIndex()) {
                    //Log.e(TAG, "onCardSwiped: cards empty");
                    tvNo.setVisibility(View.VISIBLE);
                } else {
                    tvNo.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCardMovedToOrigin() {
                // Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                //Log.d("CardStackView", "onCardClicked: " + index);
                // cardstack_feed.reverse();

                Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
                intent.putExtra("fId", listFeed.get(index).getId());
                intent.putExtra("Status", Constants.FEED_HOME);
                intent.putExtra("data", listFeed.get(index));
                intent.putExtra("From", "Feeds");
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void swipeLeft() {
        List<DataItem> spots = listFeed;
        if (spots.isEmpty()) {
            return;
        }

        View target = cardstack_feed.getTopView();
        View targetOverlay = cardstack_feed.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardstack_feed.swipe(SwipeDirection.Left, cardAnimationSet, overlayAnimationSet);
    }

    public void swipeRight() {
        List<DataItem> spots = listFeed;
        if (spots.isEmpty()) {
            return;
        }

        View target = cardstack_feed.getTopView();
        View targetOverlay = cardstack_feed.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardstack_feed.swipe(SwipeDirection.Right, cardAnimationSet, overlayAnimationSet);
    }

    public void swipeTop() {
        List<DataItem> spots = listFeed;
        if (spots.isEmpty()) {
            return;
        }


        View target = cardstack_feed.getTopView();
        View targetOverlay = cardstack_feed.getTopView().getOverlayContainer();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 0f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, -2000f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet cardAnimationSet = new AnimatorSet();
        cardAnimationSet.playTogether(rotation, translateX, translateY);

        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f);
        overlayAnimator.setDuration(200);
        AnimatorSet overlayAnimationSet = new AnimatorSet();
        overlayAnimationSet.playTogether(overlayAnimator);

        cardstack_feed.swipe(SwipeDirection.Top, cardAnimationSet, overlayAnimationSet);
    }

    @OnClick({R.id.llLocation, R.id.llFilter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llLocation:
              /*  Intent intentr = new Intent(getActivity(), ReferralsActivity.class);
                startActivity(intentr);*/
                fetchLocation();
                break;
            case R.id.llFilter:
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intent, RESULT_FILTER);
                break;
        }
    }

    private void fetchLocation() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(MainActivity.mainActivity.getApplicationContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_FILTER && resultCode == RESULT_OK) {
            page = 1;
            listFeed.clear();
            feedsAdapter.clear();
            getFeeds();
        } else if (requestCode == 101 && resultCode == 102) {
            page = 1;
            listFeed.clear();
            feedsAdapter.clear();
            getFeeds();
        } else if (requestCode == 101 && resultCode == 1001) {
            /*Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("from", 1);
            startActivity(intent);
            getActivity().finishAffinity();*/
            MainActivity.mainActivity.recreate();
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());


                Pref.setStringValue(getActivity(), Constants.PREF_LATITUDE, place.getLatLng().latitude + "");
                Pref.setStringValue(getActivity(), Constants.PREF_LONGITUDE, place.getLatLng().longitude + "");
                //String city = AppUtils.getCity(getActivity(), location.getLatitude(), location.getLongitude());
                Pref.setStringValue(getActivity(), Constants.PREF_CITY, place.getName() + "");

                tvLocation.setText(place.getName() + "");


                sendLocation();


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    private void sendLocation() {

        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("user_id", lp.getData().getId());
        request.put("lat", Pref.getStringValue(MainActivity.mainActivity.getApplicationContext(), Constants.PREF_LATITUDE, "0.0"));
        request.put("lang", Pref.getStringValue(MainActivity.mainActivity.getApplicationContext(), Constants.PREF_LONGITUDE, "0.0"));


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLocation(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {


                }
                page = 1;
                getFeeds();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                page = 1;
                getFeeds();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Feed==onPause==");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Feed==onResume==");
        Pref.setStringValue(getContext(), Constants.IS_CHAT_SCREEN, "0");
        callGetUserOnlineStatus();
    }


    private void callGetUserOnlineStatus() {
        final Map<String, Object> request = new HashMap<>();
        if (HawkAppUtils.getInstance().getUSERDATA() != null) {
            request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());

            AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "getUserOnlineStatus : PARAMETER==" + request.toString());
            Call<ResponseGetOnlineUserStatus> call = RetrofitBuilder.getInstance().getRetrofit().getUserOnlineStatus(request);
            call.enqueue(new Callback<ResponseGetOnlineUserStatus>() {
                @Override
                public void onResponse(Call<ResponseGetOnlineUserStatus> call, Response<ResponseGetOnlineUserStatus> response) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(getActivity(), "Response getUserOnlineStatus: " + jsonObj.toString());

                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                if (response.body().getData().getIsActive() != null) {
                                    if (response.body().getData().getIsActive() == 0) {
                                        startActivity(new Intent(getActivity(), BlockUserActivity.class));
                                        getActivity().finishAffinity();
                                    }
                                    if (response.body().getData().getPlanId() != null) {
                                        Pref.setStringValue(mainActivity, Constants.PLAN_ID, response.body().getData().getPlanId());
                                    } else {
                                        Pref.setStringValue(mainActivity, Constants.PLAN_ID, "");
                                    }

                                    if (response.body().getData().getIs_free_trial() != null) {
                                        Pref.setStringValue(mainActivity, Constants.IS_FREE_TRAIL, response.body().getData().getIs_free_trial());
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //showToast(getString(R.string.msg_something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetOnlineUserStatus> call, Throwable t) {
                    hideProgress();
                    //showToast(getString(R.string.msg_something_wrong));
                    AppUtils.showLog(MainActivity.mainActivity, "onFailure getUpdateUserOnlineStatus:" + t.getMessage());
                }
            });
        }
    }

}
