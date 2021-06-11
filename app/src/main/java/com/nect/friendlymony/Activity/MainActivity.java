package com.nect.friendlymony.Activity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.nect.friendlymony.Fragments.home.ChatFragment;
import com.nect.friendlymony.Fragments.home.FeedFragment;
import com.nect.friendlymony.Fragments.home.MatchesFragment;
import com.nect.friendlymony.Fragments.home.SettingsFragment;
import com.nect.friendlymony.Fragments.home.UpgradeFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Conversation.ConversationListResponse;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Quickblox.services.LoginService;
import com.nect.friendlymony.Quickblox.utils.Consts;
import com.nect.friendlymony.Quickblox.utils.QBEntityCallbackImpl;
import com.nect.friendlymony.Quickblox.utils.SharedPrefsHelper;
import com.nect.friendlymony.Quickblox.utils.ToastUtils;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.App;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

public class MainActivity extends BaseAppCompatActivity implements PaymentResultWithDataListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static MainActivity mainActivity;
    @BindViews({R.id.llExplore, R.id.llMathes, R.id.llChat, R.id.llUpgrade, R.id.llSetting})
    List<LinearLayout> llBottmViews;
    @BindViews({R.id.tvExplore, R.id.tvMathes, R.id.tvChat, R.id.tvUpgrade, R.id.tvSetting})
    List<TextView> tvBottmViews;
    @BindViews({R.id.vwExplore, R.id.vwMathes, R.id.vwChat, R.id.vwUpgrade, R.id.vwSetting})
    List<View> vwBottmViews;
    @BindViews({R.id.ivExplore, R.id.ivMathes, R.id.ivChat, R.id.ivUpgrade, R.id.ivSetting})
    List<ImageView> ivBottmViews;
    int from;
    int[][] listIcons = {{R.drawable.ic_explore, R.drawable.ic_explore_active},
            {R.drawable.ic_matches, R.drawable.ic_matches_active},
            {R.drawable.ic_chat, R.drawable.ic_chat_active},
            {R.drawable.ic_upgrade, R.drawable.ic_upgrade_active},
            {R.drawable.ic_setting, R.drawable.ic_setting_active}};
    /*@BindView(R.id.ccPicker)
    CountryCodePicker ccPicker;*/
    @BindView(R.id.amn_ccpCountryCode)
    com.hbb20.CountryCodePicker ccpCountryCode;
    @BindView(R.id.am_tvCount)
    TextView tvCount;
    Runnable updater;
    private QBUser userForSave;
    private Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;
        ButterKnife.bind(this);

        Pref.setStringValue(this, Constants.CURRENT_BASE_URL, "2");
        //AutoStartPermissionHelper.getInstance().getAutoStartPermission(MainActivity.this);
        from = getIntent().getIntExtra("from", 0);
        //  LoginResponse lgs = HawkAppUtils.getInstance().getUSERDATA();

       /* String dataS = new Gson().toJson(lgs, new TypeToken<LoginResponse>() {
        }.getRawType());
        Log.e("datas", dataS + "<");*/
        setbottombar(0);

        getCountryDetail();
        /*ccPicker.setOnCountryChangeListener(selectedCountry -> {
            AppUtils.showLog(MainActivity.this, "getIso==" + selectedCountry.getIso());
            AppUtils.showLog(MainActivity.this, "getName==" + selectedCountry.getName());
            AppUtils.showLog(MainActivity.this, "getPhoneCode==" + selectedCountry.getPhoneCode());
            getCountryDetail();
        });*/

        ccpCountryCode.setOnCountryChangeListener(() -> {

            Log.e("Mobile", "getSelectedCountryNameCode==" + ccpCountryCode.getSelectedCountryNameCode());
            Log.e("Mobile", "getSelectedCountryEnglishName==" + ccpCountryCode.getSelectedCountryEnglishName());
            Log.e("Mobile", "getSelectedCountryCodeWithPlus==" + ccpCountryCode.getSelectedCountryCodeWithPlus());

            getCountryDetail();
        });

        loadFragment(new FeedFragment(), FeedFragment.class.getSimpleName(), false);

        if (!(Pref.getStringValue(this, Constants.PREF_LATITUDE, "0.0").equals("0.0"))) {
            sendLocation();
        } else {
            initGoogle();
        }


        //screen open  firsttime(redirect from login)
        if (from == 1) {

            userForSave = createQBUserWithCurrentData(HawkAppUtils.getInstance().getUSERDATA().getData().getId(), HawkAppUtils.getInstance().getUSERDATA().getData().getName() + " " + HawkAppUtils.getInstance().getUSERDATA().getData().getLastName());
            startSignUpNewUser(userForSave);
        }

        timerHandler = new Handler();
        updateChatCount();
    }


    private void getCountryDetail() {
        /*if (ccPicker.getPhoneNumber() != null) {
            Locale locale = new Locale("", ccPicker.getSelectedCountryNameCode());
            Locale.getDefault();
            if (Currency.getInstance(locale) != null) {
                Currency currency = Currency.getInstance(locale);
                AppUtils.showLog(MainActivity.this, "getCountry==" + locale.getCountry());
                AppUtils.showLog(MainActivity.this, "getCurrencyCode==" + currency.getCurrencyCode());
                AppUtils.showLog(MainActivity.this, "getSymbol==" + currency.getSymbol());
                Pref.setStringValue(MainActivity.this, Constants.COUNTRY_CODE, ccPicker.getSelectedCountryCodeWithPlus());
                Pref.setStringValue(MainActivity.this, Constants.COUNTRY_CODE_INFO, ccPicker.getSelectedCountryNameCode());
                Pref.setStringValue(MainActivity.this, Constants.CURRENCY_CODE, currency.getCurrencyCode());
                Pref.setStringValue(MainActivity.this, Constants.CURRENCY_SYMBOL, currency.getSymbol());

                AppUtils.showLog(MainActivity.this, "COUNTRY_CODE==" + Pref.getStringValue(MainActivity.this, Constants.COUNTRY_CODE, Constants.COUNTRY_CODE));
                AppUtils.showLog(MainActivity.this, "COUNTRY_CODE_INFO==" + Pref.getStringValue(MainActivity.this, Constants.COUNTRY_CODE_INFO, Constants.COUNTRY_CODE_INFO));
                AppUtils.showLog(MainActivity.this, "CURRENCY_CODE==" + Pref.getStringValue(MainActivity.this, Constants.CURRENCY_CODE, Constants.CURRENCY_CODE));
                AppUtils.showLog(MainActivity.this, "CURRENCY_SYMBOL==" + Pref.getStringValue(MainActivity.this, Constants.CURRENCY_SYMBOL, Constants.CURRENCY_SYMBOL));
            }
        }*/

        Locale locale = new Locale("", ccpCountryCode.getSelectedCountryNameCode());
        Locale.getDefault();
        if (Currency.getInstance(locale) != null) {
            Currency currency = Currency.getInstance(locale);
            /*stCurrencySymbol = currency.getSymbol();
            stCurrencyCode = currency.getCurrencyCode();
            stCountryCodeInfo = binding.amnCcpCountryCode.getSelectedCountryNameCode();*/

            AppUtils.showLog(MainActivity.this, "getCountry==" + locale.getCountry());
            AppUtils.showLog(MainActivity.this, "getCurrencyCode==" + currency.getCurrencyCode());
            AppUtils.showLog(MainActivity.this, "getSymbol==" + currency.getSymbol());
            Pref.setStringValue(MainActivity.this, Constants.COUNTRY_CODE, ccpCountryCode.getSelectedCountryCodeWithPlus());
            Pref.setStringValue(MainActivity.this, Constants.COUNTRY_CODE_INFO, ccpCountryCode.getSelectedCountryNameCode());
            Pref.setStringValue(MainActivity.this, Constants.CURRENCY_CODE, currency.getCurrencyCode());
            Pref.setStringValue(MainActivity.this, Constants.CURRENCY_SYMBOL, currency.getSymbol());
        }
    }


    private void sendLocation() {
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
                callGetUserOnlineStatus();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                AppUtils.showLog(MainActivity.this, "userLocation : onFailure==" + t.getMessage());
                callGetUserOnlineStatus();
            }
        });
    }


    public void loadFragment(Fragment fragment, String simpleName, boolean isBack) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_home, fragment, simpleName);
        if (isBack) {
            ft.addToBackStack(null);
        }
        ft.commit();


    }

    @OnClick({R.id.llExplore, R.id.llMathes, R.id.llChat, R.id.llUpgrade, R.id.llSetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llExplore:
                loadFragment(new FeedFragment(), FeedFragment.class.getSimpleName(), false);

                setbottombar(0);

                break;
            case R.id.llMathes:
                loadFragment(new MatchesFragment(), MatchesFragment.class.getSimpleName(), false);

                setbottombar(1);
                break;
            case R.id.llChat:
                setbottombar(2);
                loadFragment(new ChatFragment(), ChatFragment.class.getSimpleName(), false);

                break;
            case R.id.llUpgrade:
                loadFragment(new UpgradeFragment(), UpgradeFragment.class.getSimpleName(), false);
                setbottombar(3);
                break;
            case R.id.llSetting:
                setbottombar(4);
                loadFragment(new SettingsFragment(), SettingsFragment.class.getSimpleName(), false);
                break;

        }
    }

    private void setbottombar(int pos) {

        Typeface fontRegular = Typeface.createFromAsset(mContext.getAssets(), "font/Muli-Regular.ttf");
        Typeface fontSemibold = Typeface.createFromAsset(mContext.getAssets(), "font/Muli-Bold.ttf");
        for (int i = 0; i < llBottmViews.size(); i++) {

            vwBottmViews.get(i).setVisibility((pos == i) ? View.VISIBLE : View.INVISIBLE);
            ivBottmViews.get(i).setImageResource((pos == i) ? listIcons[i][1] : listIcons[i][0]);
            //CalligraphyUtils.applyFontToTextView(tvBottmViews.get(i), (pos == i) ? fontSemibold : fontRegular);
            llBottmViews.get(i).setEnabled((pos == i) ? false : true);

        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    //Quickbloxx
    private QBUser createQBUserWithCurrentData(String userLogin, String userFullName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userLogin) && !TextUtils.isEmpty(userFullName)) {
            qbUser = new QBUser();
            qbUser.setLogin(userLogin);
            qbUser.setFullName(userFullName);
            qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        }
        return qbUser;
    }

    private void startSignUpNewUser(final QBUser newUser) {
        Log.i(TAG, "SignUp New User");
        //showProgress();////////////////////////////////////
        //showProgressDialog(R.string.dlg_creating_new_user);
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.i(TAG, "SignUp Successful");
                        saveUserData(newUser);
                        loginToChat(result);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.i(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser);
                        } else {
                            // hideProgress();/////////////////////////
                            ToastUtils.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }

    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void startLoginService(QBUser qbUser) {
        SubscribeService.subscribeToPushes(MainActivity.this, true);
        Intent tempIntent = new Intent(this, LoginService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        LoginService.start(this, qbUser, pendingIntent);
    }

    private void signInCreatedUser(final QBUser qbUser) {
        Log.i(TAG, "SignIn Started");
        requestExecutor.signInUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Log.i(TAG, "SignIn Successful");
                sharedPrefsHelper.saveQbUser(userForSave);
                updateUserOnServer(qbUser);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.i(TAG, "Error SignIn" + responseException.getMessage());
                // hideProgress();///////////////////////
                ToastUtils.longToast(R.string.sign_in_error);
            }
        });
    }

    private void updateUserOnServer(QBUser user) {
        user.setPassword(null);
        QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                //hideProgress();/////////////////////////////
                // OpponentsActivity.start(MainActivity.this);
                //finish();
            }

            @Override
            public void onError(QBResponseException e) {
                //hideProgress();/////////////////////////////////////////////////
                ToastUtils.longToast(R.string.update_user_error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            // hideProgress();////////////////////////////////////////////////////////
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);
                signInCreatedUser(userForSave);
            } else {
                ToastUtils.longToast(getString(R.string.login_chat_login_error) + errorMessage);
                // userLoginEditText.setText(userForSave.getLogin());
                // userFullNameEditText.setText(userForSave.getFullName());
            }
        }
    }


    private void purchaseCount(String amount, String qty, String transaction_id) {
        showProgress();
        Log.e("Call", "call " + amount);
        final Map<String, Object> request = new HashMap<>();
        request.put("type", "subscription");
        request.put("amount", amount);
        request.put("description", "");
        request.put("transaction_id", transaction_id + "");
        request.put("crush_boost_qty", qty);

        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().addTransaction(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    showToast(response.body().getMessage());
                    if (response.body().isSuccess()) {


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
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {

        Log.e(TAG, "onPaymentPaymentDataSuccess W: " + paymentData.getOrderId() + "\n" + paymentData.getPaymentId() + "\n" +
                paymentData.getData());

        String amt = Pref.getStringValue(MainActivity.this, Constants.PREF_AMOUNT_PLAN, "");
        String mnth = Pref.getStringValue(MainActivity.this, Constants.PREF_MONTH_PLAN, "");

        Log.e("ammm", amt + " " + mnth);


        purchaseCount(amt, mnth, razorpayPaymentID);
    }

    @Override
    public void onPaymentError(int i, String response, PaymentData paymentData) {

        hideProgress();
        String err = response + "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject errorO = jsonObject.optJSONObject("error");
            err = errorO.optString("description");
        } catch (Exception e) {

        }
        showOKAlert(err, new onDialogClickListener() {
            @Override
            public void onPositive() {

            }

            @Override
            public void onNegative() {

            }
        });
    }

    private void callUpdateUserOnlineStatusApi(String status) {
        final Map<String, Object> request = new HashMap<>();
        if (HawkAppUtils.getInstance().getUSERDATA() != null) {
            request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
            request.put("is_online", status);


            AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "getUpdateUserOnlineStatus : PARAMETER==" + request.toString());
            Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().getUpdateUserOnlineStatus(request);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(mainActivity, "Response getUpdateUserOnlineStatus: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //showToast(getString(R.string.msg_something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    hideProgress();
                    //showToast(getString(R.string.msg_something_wrong));
                    AppUtils.showLog(MainActivity.mainActivity, "onFailure getUpdateUserOnlineStatus:" + t.getMessage());
                }
            });
        }

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
                        AppUtils.showLog(mainActivity, "Response getUserOnlineStatus: " + jsonObj.toString());

                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                if (response.body().getData().getIsActive() != null) {
                                    if (response.body().getData().getIsActive() == 0) {
                                        startActivity(new Intent(mainActivity, BlockUserActivity.class));
                                        finishAffinity();
                                    }
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
                            callUpdateUserOnlineStatusApi("1");
                        } else {
                            callUpdateUserOnlineStatusApi("1");
                        }

                    } catch (JSONException e) {
                        callUpdateUserOnlineStatusApi("1");
                        e.printStackTrace();
                        //showToast(getString(R.string.msg_something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetOnlineUserStatus> call, Throwable t) {
                    callUpdateUserOnlineStatusApi("1");
                    hideProgress();
                    //showToast(getString(R.string.msg_something_wrong));
                    AppUtils.showLog(MainActivity.mainActivity, "onFailure getUpdateUserOnlineStatus:" + t.getMessage());
                }
            });
        }

    }


    private void callChatCountApi() {

        AppUtils.showLog(mainActivity, "my token: " + Pref.getStringValue(MainActivity.this, PREF_FCM_TOKEN, ""));
        Call<ConversationListResponse> call = RetrofitBuilder.getInstance().getRetrofit().getConversation();
        call.enqueue(new Callback<ConversationListResponse>() {
            @Override
            public void onResponse(Call<ConversationListResponse> call, Response<ConversationListResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(mainActivity, "Response callChatCountApi: " + jsonObj.toString());
                    if (response.body() != null && response.body().getTotalUnreadCount() != null) {
                        int count = Integer.parseInt(response.body().getTotalUnreadCount());
                        if (count > 0) {
                            tvCount.setVisibility(View.VISIBLE);
                            tvCount.setText(response.body().getTotalUnreadCount());
                        } else {
                            tvCount.setVisibility(View.GONE);
                        }
                    } else {
                        tvCount.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //showToast(getString(R.string.msg_something_wrong));
                }
            }

            @Override
            public void onFailure(Call<ConversationListResponse> call, Throwable t) {
                hideProgress();
                //showToast(getString(R.string.msg_something_wrong));
                AppUtils.showLog(MainActivity.mainActivity, "onFailure callChatCountApi:" + t.getMessage());
                //callChatCountApi();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(updater);
        Pref.setStringValue(MainActivity.this, Constants.IS_ACTIVE_APP, "0");
        AppUtils.showLog(MainActivity.mainActivity, "MainActivity onDestroy==");
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerHandler = new Handler();
        Pref.setStringValue(MainActivity.this, Constants.IS_ACTIVE_APP, "1");
        Pref.setStringValue(this, Constants.IS_CHAT_SCREEN, "0");
        Log.d(TAG, "onResume==");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart==");
        callUpdateUserOnlineStatusApi("1");
        updateChatCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause==");
        timerHandler.removeCallbacks(updater);
        callUpdateUserOnlineStatusApi("0");
    }

    private void updateChatCount() {
        try {
            updater = () -> {
                callChatCountApi();
                timerHandler.postDelayed(updater, 5000);
            };
            timerHandler.post(updater);
        } catch (Exception e) {
            AppUtils.showLog(MainActivity.mainActivity, "updateChatCount==Exception==" + e.getMessage());
        }
    }
}
//L3B5HTN42N key id apple login