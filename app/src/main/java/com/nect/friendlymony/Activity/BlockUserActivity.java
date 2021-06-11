package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Quickblox.services.LoginService;
import com.nect.friendlymony.Quickblox.utils.UsersUtils;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.quickblox.messages.services.SubscribeService;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockUserActivity extends BaseAppCompatActivity implements View.OnClickListener {
    @BindView(R.id.abu_tvEmail)
    TextView abu_tvEmail;
    @BindView(R.id.abu_tvLogout)
    TextView abu_tvLogout;

    BlockUserActivity blockUserActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);

        blockUserActivity = BlockUserActivity.this;
        ButterKnife.bind(this);

        abu_tvEmail.setOnClickListener(this);
        abu_tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == abu_tvEmail) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] recipients = {getString(R.string.lbl_support_email)};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        } else if (v == abu_tvLogout) {
            logoutapi();
        }
    }

    private void logoutapi() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("vPushToken", Pref.getStringValue(blockUserActivity, Constants.PREF_FCM_TOKEN, ""));

        AppUtils.showLog(blockUserActivity, "userLogout : PARAMETER==" + request.toString());
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLogout(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(mContext, "Response userLogout: " + jsonObj.toString());
                    LoginManager.getInstance().logOut();

                } catch (Exception e) {
                    AppUtils.showLog(mContext, "Exception userLogout: " + e.getMessage());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                            FirebaseInstanceId.getInstance().getInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                try {


                    SubscribeService.unSubscribeFromPushes(blockUserActivity);
                    LoginService.logout(blockUserActivity);
                    UsersUtils.removeUserData(blockUserActivity);
                    requestExecutor.signOut();

                } catch (Exception e) {
                    AppUtils.showLog(mContext, "Exception userLogout: " + e.getMessage());
                }

                HawkAppUtils.getInstance().setIsLogin(false);
                HawkAppUtils.getInstance().clear();
                Intent intent = new Intent(blockUserActivity, LoginOptionActivity.class);
                startActivity(intent);

                blockUserActivity.finish();

            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(mContext, "Exception userLogout: " + t.getMessage());
                //showToast("Try again");
                finish();
            }
        });
    }

}
