package com.nect.friendlymony.Fragments.home;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nect.friendlymony.Activity.BlockUserActivity;
import com.nect.friendlymony.Activity.BoostCrushesActivity;
import com.nect.friendlymony.Activity.FollowActivity;
import com.nect.friendlymony.Activity.LoginOptionActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Activity.MyLikesActivity;
import com.nect.friendlymony.Activity.NotificationsActivity;
import com.nect.friendlymony.Activity.ProfileActivity;
import com.nect.friendlymony.Activity.ReferralUserActivity;
import com.nect.friendlymony.Activity.ReferralsActivity;
import com.nect.friendlymony.Activity.ReportProblemActivity;
import com.nect.friendlymony.Activity.SubcriptionActivity;
import com.nect.friendlymony.BuildConfig;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.Model.Login.Data;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.Login.UserResponse;
import com.nect.friendlymony.Quickblox.services.LoginService;
import com.nect.friendlymony.Quickblox.utils.UsersUtils;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.quickblox.messages.services.SubscribeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Activity.MainActivity.mainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {


    View view;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.llReferral)
    LinearLayout llReferral;
    @BindView(R.id.llLogout)
    LinearLayout llLogout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.swHide)
    Switch swHide;
    @BindView(R.id.swRenew)
    Switch swRenew;
    @BindView(R.id.tvVersion)
    TextView tvVersion;

    String isPublic = "Public", isSubscribe = "0";
    boolean isFirstTime = true;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        //CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);

        tvVersion.setText("Version " + BuildConfig.VERSION_NAME);
        swHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                try {
                    if (b && (isPublic.equalsIgnoreCase("Public"))) {
                        isPublic = "Private" + "";
                        HideShowProfile();
                    } else if (!b && (isPublic.equalsIgnoreCase("Private"))) {
                        isPublic = "Public" + "";
                        HideShowProfile();
                    }
                } catch (Exception e) {

                }


            }
        });

        swRenew.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (!isFirstTime) {
                if (AppUtils.isNetworkAvailable(getContext())) {
                    callCancelSubscription(isChecked);
                } else {
                    showToast(getString(R.string.msg_no_internet));
                }
            }
        });

        getprofile();
        return view;
    }

    private void callCancelSubscription(boolean isChecked) {

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        if (isChecked) {
            request.put("is_auto_subscribe", "1");
        } else {
            request.put("is_auto_subscribe", "0");
        }

        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "getUserCancelSubscription : PARAMETER==" + request.toString());
        Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().getUserCancelSubscription(request);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response getUserCancelSubscription: " + jsonObj.toString());

                    if (response.isSuccessful()) {
                        showToast(response.body().getMessage());
                        /*if (isChecked) {
                            isSubscribe = "1";
                        } else {
                            isSubscribe = "0";
                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(getString(R.string.msg_something_wrong));
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                hideProgress();
                showToast(getString(R.string.msg_something_wrong));
                AppUtils.showLog(MainActivity.mainActivity, "onFailure getUpdateUserOnlineStatus:" + t.getMessage());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to Logout?");
        builder.setMessage(getResources().getString(R.string.logout));
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isConnectedToInternet()) {
                    logoutapi();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void disableAccount() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("user_status", "0");


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().cancelMyAccount(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                logoutapi();

            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                showToast("Try again");
            }
        });
    }

    private void logoutapi() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("vPushToken", Pref.getStringValue(getActivity(), Constants.PREF_FCM_TOKEN, ""));


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLogout(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                try {
                    LoginManager.getInstance().logOut();

                } catch (Exception e) {

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


                    SubscribeService.unSubscribeFromPushes(getActivity());
                    LoginService.logout(getActivity());
                    UsersUtils.removeUserData(getActivity());
                    ((MainActivity) getActivity()).requestExecutor.signOut();

                } catch (Exception e) {

                }

                HawkAppUtils.getInstance().setIsLogin(false);
                HawkAppUtils.getInstance().clear();
                Intent intent = new Intent(getActivity(), LoginOptionActivity.class);
                startActivity(intent);

                getActivity().finish();

            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                showToast("Try again");
            }
        });
    }


    @OnClick({R.id.llProfile, R.id.llReferral, R.id.llWhoCrush, R.id.llWhoLiked,
            R.id.llBoost, R.id.llLogout, R.id.llReferralUser, R.id.llSubcription, R.id.llContactus,
            R.id.llNotification, R.id.llCancel, R.id.llAboutus, R.id.llPrivacy,
            R.id.llTerms, R.id.llFollow, R.id.llShare})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.llProfile:

                intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("fId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
                startActivity(intent);
                break;
            case R.id.llReferral:

                intent = new Intent(getActivity(), ReferralsActivity.class);
                startActivity(intent);
                break;
            case R.id.llSubcription:

                intent = new Intent(getActivity(), SubcriptionActivity.class);
                intent.putExtra("Form","Upgrade");
                startActivity(intent);
                break;
            case R.id.llReferralUser:

                intent = new Intent(getActivity(), ReferralUserActivity.class);
                startActivity(intent);
                break;
            case R.id.llWhoLiked:

                intent = new Intent(getActivity(), MyLikesActivity.class);
                intent.putExtra("from", "Interested");
                startActivity(intent);
                break;
            case R.id.llWhoCrush:
                intent = new Intent(getActivity(), MyLikesActivity.class);
                intent.putExtra("from", "Crush");
                startActivity(intent);
                break;
            case R.id.llBoost:

                intent = new Intent(getActivity(), BoostCrushesActivity.class);
                startActivity(intent);
                break;

            case R.id.llNotification:

                intent = new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
                break;
            case R.id.llAboutus:

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(AppUtils.URL_ABOUT));
                startActivity(intent);

                break;
            case R.id.llPrivacy:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(AppUtils.URL_PRIVACY));
                startActivity(intent);
                break;
            case R.id.llTerms:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(AppUtils.URL_TERMS));
                startActivity(intent);
                break;
            case R.id.llLogout:
                logout();
                break;
            case R.id.llCancel:
                cancelMyacount();
                break;
            case R.id.llShare:
                shareApp();
                break;
            case R.id.llContactus:
                intent = new Intent(getActivity(), ReportProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.llFollow:
                intent = new Intent(getActivity(), FollowActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void shareApp() {
        String msgShare = "Check out this amazing Dating App with FREE chat and video call. Just Swipe. Match. Connect " +"\n\n"+ "For Android: https://bit.ly/3f1g54w"+"\n\n" + "For iOS: ";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msgShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void cancelMyacount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to cancel your account?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            if (isConnectedToInternet()) {
                disableAccount();
            }

        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void getprofile() {
        showProgress();

        Call<UserResponse> call = RetrofitBuilder.getInstance().getRetrofit().userData();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Data dataUser = response.body().getData();
                        try {
                            AppUtils.showLog(getContext(), "Response userData: " + new JSONObject(new Gson().toJson(response.body())).toString());
                            if (response.body().getData().getIs_auto_subscribe().equalsIgnoreCase("1")) {
                                swRenew.setChecked(true);
                            } else {
                                swRenew.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        isPublic = dataUser.getvIsPublic();

                        LoginResponse lg = HawkAppUtils.getInstance().getUSERDATA();
                        lg.getData().setvIsPublic(isPublic);
                        HawkAppUtils.getInstance().setUSERDATA(lg);

                        swHide.setChecked((isPublic.equalsIgnoreCase("Private")) ? true : false);

                        isFirstTime = false;
                    }
                }
            }


            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(MainActivity.mainActivity, "onFailure userData:" + t.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        LoginResponse lg = HawkAppUtils.getInstance().getUSERDATA();
        isPublic = lg.getData().getvIsPublic();

        try {
            Pref.setStringValue(getContext(), Constants.IS_CHAT_SCREEN, "0");
            swHide.setChecked(((isPublic + "").equalsIgnoreCase("Private")) ? true : false);
            callGetUserOnlineStatus();
        } catch (Exception e) {

        }
    }

    private void HideShowProfile() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("vIsPublic", isPublic + "");


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().hideShowProfile(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {
                        LoginResponse lg = HawkAppUtils.getInstance().getUSERDATA();
                        lg.getData().setvIsPublic(isPublic);
                        HawkAppUtils.getInstance().setUSERDATA(lg);


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

    @Override
    public void onPause() {
        super.onPause();
        AppUtils.showLog(MainActivity.mainActivity, "Setting : onPause==");
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
