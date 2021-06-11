package com.nect.friendlymony.Activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Login.Data;
import com.nect.friendlymony.Model.Login.UserResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.swInterest)
    Switch swInterest;
    @BindView(R.id.swCrush)
    Switch swCrush;

    int isInterest = 1;
    int isCrush = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        setToolbar(toolbar, "", true);

        //CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);


        swInterest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b && isInterest == 0) {
                    isInterest = 1;
                    updateNotification(0);
                } else if (!b && isInterest == 1) {
                    isInterest = 0;
                    updateNotification(0);
                }

            }
        });

        swCrush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b && isCrush == 0) {
                    isCrush = 1;
                    updateNotification(1);
                } else if (!b && isCrush == 1) {
                    isCrush = 0;
                    updateNotification(1);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getprofile();
    }

    private void updateNotification(int type) {

  /*      if (!isConnectedToInternet()) {

            return;

        }*/

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        if (type == 0) {

            request.put("is_interested_notification", isInterest + "");
            request.put("is_crush_notification", "");
        } else {
            request.put("is_crush_notification", isCrush + "");
            request.put("is_interested_notification", "");
        }


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().notificationCrushInterseted(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {


                    } else {
                        showToast(response.body().getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void getprofile() {
        showProgress();

        Call<UserResponse> call = RetrofitBuilder.getInstance().getRetrofit().userData();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getApplicationContext(), "Response userData:" + jsonObj.toString());

                    if (response.isSuccessful()) {

                        if (response.body().isSuccess()) {

                            Data dataUser = response.body().getData();

                            isInterest = dataUser.getIs_interested_notification() + 0;
                            isCrush = dataUser.getIs_crush_notification() + 0;


                            swCrush.setChecked((isCrush == 1) ? true : false);
                            swInterest.setChecked((isInterest == 1) ? true : false);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

}
