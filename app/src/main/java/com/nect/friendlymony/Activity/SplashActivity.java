package com.nect.friendlymony.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.interfaces.OnvalueChangeListener;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

public class SplashActivity extends BaseAppCompatActivity implements OnvalueChangeListener {

    private static final int REQUEST_PERMISSIONS_CODE_LOCATION = 201;
    private static final String TAG = SplashActivity.class.getName();
    @BindView(R.id.btnStarted)
    Button btnStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "getInstanceId failed", task.getException());
                return;
            }

            // Get new Instance ID token
            String token = Objects.requireNonNull(task.getResult()).getToken();
            Pref.setStringValue(getBaseContext(), PREF_FCM_TOKEN, token);

            Log.e(TAG, "FCM token:" + token);
        });

        /*FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        AutoStartPermissionHelper.getInstance().getAutoStartPermission(SplashActivity.this);*/
        Log.e(TAG, "CheckNotiAutoStart:" + AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(SplashActivity.this));
        Log.e(TAG, "FCM:" + Pref.getStringValue(SplashActivity.this, Constants.PREF_FCM_TOKEN, ""));

        if (!AppUtils.CheckLocationPermission(this)) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            // listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSIONS_CODE_LOCATION);

        }

        //redirectNext();
        if (HawkAppUtils.getInstance().getIsLogin()) {
            try {
                onValueChange(this);
                initGoogle();
                goToNext();
            } catch (Exception e) {
                goToNext();
            }

        } else {

            if (AppUtils.CheckLocationPermission(this)) {
                initGoogle();
            }


            if (Pref.getintValue(SplashActivity.this, Constants.PREF_IS_INTRO, -1) == 1) {
                Intent intentP = new Intent(SplashActivity.this, LoginOptionActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentP);
                finish();
            }

        }

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nect.friendlymony",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

    }

    private void redirectNext() {
        int SPLASH_TIME_OUT = 1300;
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {


                Intent intentP = new Intent(SplashActivity.this, MainActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentP);
                finish();


            }
        }, SPLASH_TIME_OUT);
    }

    @OnClick(R.id.btnStarted)
    public void onViewClicked() {
        initGoogle();
        goToNext();
    }

    private void goToNext() {
        if (HawkAppUtils.getInstance().getIsLogin()) {
            Intent intentP;
            if (getIntent() == null || getIntent().getExtras() == null) {
                intentP = new Intent(SplashActivity.this, MainActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else if (getIntent().getExtras().get("userId") == null) {
                intentP = new Intent(SplashActivity.this, MainActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else if (getIntent().getExtras().getString("pushType") != null && getIntent().getExtras().getString("pushType").equalsIgnoreCase("2")) {
                intentP = new Intent(this, ChatActivity.class);
                intentP.putExtra("conversationID", getIntent().getExtras().get("conversationId").toString() + "");
                intentP.putExtra("rName", getIntent().getExtras().get("senderName").toString() + "");
                intentP.putExtra("rPhoto", getIntent().getExtras().get("senderProfile").toString() + "");
                intentP.putExtra("rToken", getIntent().getExtras().get("fcmToken").toString() + "");
                intentP.putExtra("rId", getIntent().getExtras().get("senderId").toString() + "");
                intentP.putExtra("from", "PushNotification");
                intentP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intentP = new Intent(SplashActivity.this, FeedDetailActivity.class);
                intentP.putExtra("fId", Objects.requireNonNull(getIntent().getExtras().get("userId")).toString() + "");
                intentP.putExtra("Status", getIntent().getExtras().get("Status").toString() + "");
                intentP.putExtra("From", "PushNotification");
                intentP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intentP);
            finish();

        } else {

            if (Pref.getintValue(SplashActivity.this, Constants.PREF_IS_INTRO, -1) == 1) {
                Intent intentP = new Intent(SplashActivity.this, LoginOptionActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentP);
                finish();

            } else {
                Intent intentP = new Intent(SplashActivity.this, IntroActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentP);
                finish();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE_LOCATION: {


                try {

                    if (AppUtils.CheckLocationPermission(this)) {
                        if (HawkAppUtils.getInstance().getIsLogin()) {
                            showProgress();
                            onValueChange(this);
                        }
                        initGoogle();
                    } else {
                        if (HawkAppUtils.getInstance().getIsLogin()) {
                            Intent intentP;
                            if (getIntent() == null || getIntent().getExtras() == null) {
                                intentP = new Intent(SplashActivity.this, MainActivity.class);
                                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else if (getIntent().getExtras().get("userId") == null) {
                                intentP = new Intent(SplashActivity.this, MainActivity.class);
                                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else {
                                intentP = new Intent(SplashActivity.this, FeedDetailActivity.class);
                                intentP.putExtra("fId", Objects.requireNonNull(getIntent().getExtras().get("userId")).toString() + "");
                                intentP.putExtra("Status", getIntent().getExtras().get("Status").toString() + "");
                                intentP.putExtra("From", "PushNotification");
                                intentP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                            startActivity(intentP);
                            finish();
                        }
                    }


                } catch (Exception e) {

                }

            }

        }
    }


    @Override
    public void onLocationChange() {
        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("user_id", lp.getData().getId());
        request.put("lat", Pref.getStringValue(this, Constants.PREF_LATITUDE, "0.0"));
        request.put("lang", Pref.getStringValue(this, Constants.PREF_LONGITUDE, "0.0"));

        AppUtils.showLog(mContext, "userLocation : PARAMETER==" + request.toString());
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLocation(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(mContext, "Response userLocation: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                Intent intentP = new Intent(SplashActivity.this, MainActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intentP);
                finish();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(mContext, "Response onFailure==: " + t.getMessage());
                Intent intentP = new Intent(SplashActivity.this, MainActivity.class);
                intentP.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intentP);
                finish();
            }
        });
    }

}
