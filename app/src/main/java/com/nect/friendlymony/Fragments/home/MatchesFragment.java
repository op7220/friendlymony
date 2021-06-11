package com.nect.friendlymony.Fragments.home;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nect.friendlymony.Activity.BlockUserActivity;
import com.nect.friendlymony.Activity.FeedDetailActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Adapter.LikedUserAdapter;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.Crused.CrushesResponse;
import com.nect.friendlymony.Model.Crused.DataItem;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.GridSpacingItemDecoration;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.nect.friendlymony.Utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Activity.MainActivity.mainActivity;

public class MatchesFragment extends BaseFragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    View view;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvNo)
    TextView tvNo;
    @BindView(R.id.rvUser)
    RecyclerView rvUser;

    List<DataItem> listData = new ArrayList<>();
    LikedUserAdapter likedUserAdapter;

    Runnable updater;
    boolean isFirstTimeUpdate = true;
    Handler timerHandler;

    public MatchesFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matches, container, false);
        ButterKnife.bind(this, view);


        //CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);


        likedUserAdapter = new LikedUserAdapter(getActivity(), listData, "match");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvUser.setLayoutManager(layoutManager);
        rvUser.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(5), true));

        rvUser.setAdapter(likedUserAdapter);

        getUsers();
        listeners();
        return view;
    }

    private void listeners() {
        rvUser.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvUser, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LinearLayout llContain = view.findViewById(R.id.llContain);

                llContain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
                        intent.putExtra("fId", listData.get(position).getId() + "");
                        intent.putExtra("Status", Constants.FEED_ACCEPT);
                        intent.putExtra("From", "Matches");
                        startActivity(intent);


                    }
                });


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getUsers() {

        if (!isConnectedToInternet()) {
            return;
        }


        if (isFirstTimeUpdate) {
            showProgress();
        }
        final Map<String, Object> request = new HashMap<>();

        request.put("user_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());

        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "MatchResult : PARAMETER==" + request.toString());
        Call<CrushesResponse> call = RetrofitBuilder.getInstance().getRetrofit().MatchResult(request);
        call.enqueue(new Callback<CrushesResponse>() {
            @Override
            public void onResponse(Call<CrushesResponse> call, Response<CrushesResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "Response MatchResult: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                if (response.isSuccessful()) {

                    listData.clear();
                    if (response.body().isSuccess()) {

                        listData.addAll(response.body().getData());
                        likedUserAdapter.notifyDataSetChanged();

                        if (isFirstTimeUpdate) {
                            if (timerHandler != null) {
                                isFirstTimeUpdate = false;
                                updateTime();
                            }
                        }
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                tvNo.setVisibility((listData.size() <= 0) ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onFailure(Call<CrushesResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                AppUtils.showLog(getContext(), "onFailure MatchResult: " + t.getMessage());
                hideProgress();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Match==onPause==");
        isFirstTimeUpdate = false;
        timerHandler.removeCallbacks(updater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Match==onResume==");
        Pref.setStringValue(getContext(), Constants.IS_CHAT_SCREEN, "0");
        isFirstTimeUpdate = true;
        timerHandler = new Handler();
        callGetUserOnlineStatus();
    }

    private void updateTime() {
        updater = () -> {
            getUsers();
            timerHandler.postDelayed(updater, 5000);
        };
        timerHandler.post(updater);
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
