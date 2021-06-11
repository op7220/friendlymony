package com.nect.friendlymony.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.nect.friendlymony.Adapter.PagerImageAdapter;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Chat.ConversationResponse;
import com.nect.friendlymony.Model.CommonResponse.CommonParam;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Feeddetail.FeedDetailResponse;
import com.nect.friendlymony.Model.Feeddetail.ImagesItem;
import com.nect.friendlymony.Model.Feeddetail.UserData;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Retrofit.WebService;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {


    String fId, from = "";
    UserData dataFeed = new UserData();

    String Status = "";
    @BindView(R.id.pagerImage)
    ViewPager pagerImage;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.tvBirth)
    TextView tvBirth;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    @BindView(R.id.tvSmoke)
    TextView tvSmoke;
    @BindView(R.id.tvDrink)
    TextView tvDrink;
    @BindView(R.id.tvPolitics)
    TextView tvPolitics;
    @BindView(R.id.tvEmploy)
    TextView tvEmploy;
    @BindView(R.id.tvEarn)
    TextView tvEarn;
    @BindView(R.id.tvHobby)
    TextView tvHobby;
    @BindView(R.id.rlDetail)
    RelativeLayout rlDetail;
    @BindView(R.id.tvReport)
    TextView tvReport;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvMiles)
    TextView tvMiles;
    @BindView(R.id.tvMatch)
    TextView tvMatch;
    @BindView(R.id.btnChat)
    Button btnChat;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivLike)
    ImageView ivLike;
    @BindView(R.id.ivCrush)
    ImageView ivCrush;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.ivOption)
    ImageView ivOption;
    private List<ImagesItem> listImages = new ArrayList<>();
    private PagerImageAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        ButterKnife.bind(this);

        //dataFeed = (DataItem) getIntent().getSerializableExtra("data");
        fId = getIntent().getStringExtra("fId");
        Status = getIntent().getStringExtra("Status");
        from = getIntent().getStringExtra("From");

        if (from == null) {
            from = "";
        }

        AppUtils.showLog(getApplicationContext(), "Status==" + Status);
        //Status = Constants.FEED_ACCEPT;
        if ((Status + "").equalsIgnoreCase(Constants.FEED_CRUSH)) {
            llBottom.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.GONE);
            ivOption.setVisibility(View.VISIBLE);
        } else if ((Status + "").equalsIgnoreCase(Constants.FEED_HOME)) {
            llBottom.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.GONE);
            ivOption.setVisibility(View.VISIBLE);
        } else if ((Status + "").equalsIgnoreCase(Constants.FEED_INTERESTED)) {
            llBottom.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.GONE);
            ivOption.setVisibility(View.GONE);
        } else if ((Status + "").equalsIgnoreCase(Constants.FEED_ACCEPT)) {
            llBottom.setVisibility(View.GONE);
            btnChat.setVisibility(View.VISIBLE);
            ivOption.setVisibility(View.VISIBLE);
        } else if ((Status + "").equalsIgnoreCase(Constants.FEED_REJECT)) {
            llBottom.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            ivOption.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            ivOption.setVisibility(View.GONE);
        }


        //CalligraphyUtils.applyFontToTextView(tvName, fontExtraBoldMuli);
        //setDetail();

        if (!isConnectedToInternet()) {
            return;
        }
        getDetail();

        ivOption.setOnClickListener(this);
    }

    private void getDetail() {
        showProgress();
        String uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "";
        final Map<String, Object> request = new HashMap<>();
        request.put("id", fId);

        AppUtils.showLog(getApplicationContext(), "feeddetail : PARAMETER==" + request.toString());
        Call<FeedDetailResponse> call = RetrofitBuilder.getInstance().getRetrofit().feeddetail(request);
        call.enqueue(new Callback<FeedDetailResponse>() {
            @Override
            public void onResponse(Call<FeedDetailResponse> call, Response<FeedDetailResponse> response) {
                hideProgress();

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getApplicationContext(), "Response feeddetail:" + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        dataFeed = response.body().getUserData();
                        setDetail();
                    }
                }
            }


            @Override
            public void onFailure(Call<FeedDetailResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void setDetail() {
        tvLocation.setText("Age : " + dataFeed.getVAge() + "");
        tvName.setText(dataFeed.getFirst() + " " + dataFeed.getLast());
        tvMiles.setText(dataFeed.getKm() + " Km away");
        tvEducation.setText("Education qualification : " + dataFeed.getIsQualification());
        tvSmoke.setText("Smoking : " + dataFeed.getIsSmoke());
        tvDrink.setText("Drinking : " + dataFeed.getIsDrink());
        tvPolitics.setText("Discuss politics : " + dataFeed.getIsPolitics());
        tvEmploy.setText("Employment : " + dataFeed.getIsEmployee());
        tvEarn.setText(getResources().getString(R.string.q6_me) + " : " + dataFeed.getIsEarn());
        tvHobby.setText("" + dataFeed.getVAbout());
        listImages.clear();
        listImages.addAll(dataFeed.getImages());
        myViewPagerAdapter = new PagerImageAdapter(this, listImages, 1, 1);
        pagerImage.setAdapter(myViewPagerAdapter);
        tvMatch.setText(dataFeed.getMatchPercentage() + "% Match !");

        tvReport.setText("Report " + dataFeed.getFirst() + " " + dataFeed.getLast());

        ivImage.setVisibility((listImages.size() <= 0) ? View.VISIBLE : View.GONE);

        try {

            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");

            Date myDate = fromFormat.parse(dataFeed.getVBirthdate());

            String finalDate = toFormat.format(myDate);

            tvBirth.setText(finalDate);

        } catch (Exception e) {
            tvBirth.setText(dataFeed.getVBirthdate());

        }


    }

    private void checkSubscribtion() {

        if (!isConnectedToInternet()) {
            return;
        }


        showProgress();
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().chatSubscriptionValidate();
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                hideProgress();
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {
                        startConversation();
                    } else {
                        showToast("You can not chat. Please purchase subscription");
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void startConversation() {


        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("sender", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("recipient", dataFeed.getId() + "");

        AppUtils.showLog(getApplicationContext(), "StartConversation : PARAMETER==" + request.toString());
        Call<ConversationResponse> call = RetrofitBuilder.getInstance().getRetrofit().StartConversation(request);
        call.enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getApplicationContext(), "Response StartConversation:" + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        String wName = dataFeed.getFirst() + " " + dataFeed.getLast();

                        String rImage = "";
                        try {

                            if (listImages.size() > 0) {
                                rImage = listImages.get(0).getVPhoto();
                            }

                        } catch (Exception e) {

                        }

                        //String rToken = response.body().getData().get(0).getToken();
                        String rToken = dataFeed.getFirebase_token();
                        /*if (rToken.equalsIgnoreCase("")) {
                            rToken = Pref.getStringValue(FeedDetailActivity.this, PREF_FCM_TOKEN, "");
                        }*/

                        Intent intent = new Intent(FeedDetailActivity.this, ChatActivity.class);
                        intent.putExtra("conversationID", response.body().getData().get(0).getId() + "");
                        intent.putExtra("rName", wName + "");
                        intent.putExtra("rPhoto", rImage + "");
                        intent.putExtra("rToken", rToken + "");
                        intent.putExtra("rId", fId + "");
                        intent.putExtra("from", "userProfile");
                        //  intent.putExtra("rToken", response.body().getData().get(0).getToken() + "");
                        startActivity(intent);

                    } else {
                        showToast(response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
            }
        });
    }


    private void acceptDeclineRequest(String isStatus, String fid) {

        if (!isConnectedToInternet()) {
            return;
        }


        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("user_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("userFrom", fid);
        request.put("is_status", isStatus);
        request.put("user_status", Status);

        AppUtils.showLog(getApplicationContext(), "usermatch : PARAMETER==" + request.toString());
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().usermatch(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getApplicationContext(), "Response usermatch:" + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                if (response.isSuccessful()) {
                    showToast(response.body().getMessage());

                    if (response.body().isSuccess()) {
                        llBottom.setVisibility(View.GONE);
                        btnChat.setVisibility((isStatus.equals("1")) ? View.VISIBLE : View.GONE);


                        setResult(102);
                        if (from.equalsIgnoreCase("PushNotification")) {
                            goToHome();
                        } else {
                            finish();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
            }
        });
    }


    @OnClick({R.id.ivLeft, R.id.ivRight, R.id.tvReport, R.id.ivBack, R.id.btnChat,
            R.id.ivClose, R.id.ivLike, R.id.ivCrush})
    public void onViewClicked(View view) {
        Log.e(">>", pagerImage.getCurrentItem() + "<");
        switch (view.getId()) {


            case R.id.ivLeft:
                if (listImages.isEmpty()) {
                    return;
                }
                int position = pagerImage.getCurrentItem();
                int Currentitem = (listImages.size() - 1);
                if (position != 0) {
                    Currentitem = position - 1;
                }

                pagerImage.setCurrentItem(Currentitem);
                break;
            case R.id.ivRight:
                if (listImages.isEmpty()) {
                    return;
                }
                int positionP = pagerImage.getCurrentItem();
                int CurrentitemP = 0;
                if (positionP < (listImages.size() - 1)) {
                    CurrentitemP = positionP + 1;
                }
                Log.e(">CP>", CurrentitemP + "<");
                pagerImage.setCurrentItem(CurrentitemP);

                break;
            case R.id.tvReport:
                break;
            case R.id.ivBack:
                if (from.equalsIgnoreCase("PushNotification")) {
                    goToHome();
                } else {
                    finish();
                }
                break;
            case R.id.btnChat:

                if (!isConnectedToInternet()) {
                    return;
                }
                checkSubscribtion();

                break;
            case R.id.ivClose:
                if (Status.equalsIgnoreCase(Constants.FEED_INTERESTED) || Status.equalsIgnoreCase(Constants.FEED_CRUSH)) {
                    acceptDeclineRequest("0", fId);
                } else {
                    setLikes(fId, Constants.FEED_NOT_INTERESTED);
                }
                break;
            case R.id.ivLike:
                if (Status.equalsIgnoreCase(Constants.FEED_INTERESTED) || Status.equalsIgnoreCase(Constants.FEED_CRUSH)) {
                    acceptDeclineRequest("1", fId);
                } else {
                    setLikes(fId, Constants.FEED_INTERESTED);
                }
                break;
            case R.id.ivCrush:
                if (Status.equalsIgnoreCase(Constants.FEED_INTERESTED) || Status.equalsIgnoreCase(Constants.FEED_CRUSH)) {
                    acceptDeclineRequest("1", fId);
                } else {
                    setLikes(fId, Constants.FEED_CRUSH);
                }
                break;
        }
    }

    private void goToHome() {
        Intent intent = new Intent(FeedDetailActivity.this, MainActivity.class);
        intent.putExtra("from", 1);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v == ivOption) {
            //FeedDetailActivity.this.openOptionsMenu();

            PopupMenu popup = new PopupMenu(FeedDetailActivity.this, ivOption);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_block_user, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.lbl_block_user))) {
                        callBlockUserAPI();
                        //callUserBlockAPI();
                    } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.lbl_report_user))) {
                        Intent intent = new Intent(FeedDetailActivity.this, ReportUserActivity.class);
                        intent.putExtra("opponentUserId", String.valueOf(dataFeed.getId()));
                        if (dataFeed.getImages() != null && dataFeed.getImages().size() > 0) {
                            intent.putExtra("userImage", dataFeed.getImages().get(0).getVPhoto());
                        } else {
                            intent.putExtra("userImage", "");
                        }
                        intent.putExtra("userName", dataFeed.getFirst() + " " + dataFeed.getLast());
                        startActivityForResult(intent, 100);
                    }
                    return true;
                }
            });

            popup.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void callUserBlockAPI() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                try {
                    LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
                    Gson gson = new Gson();
                    CommonParam commonParam = new CommonParam();
                    commonParam.setUserId(lp.getData().getId());
                    commonParam.setToUserId(String.valueOf(dataFeed.getId()));
                    commonParam.setMainUserId("");
                    String json = gson.toJson(commonParam);
                    Pref.setStringValue(FeedDetailActivity.this, Constants.CURRENT_BASE_URL, "2");
                    String baseUrl = "";

                    /*if (Pref.getStringValue(App.getInstance(), Constants.CURRENT_BASE_URL, "1").equalsIgnoreCase("1")) {
                        baseUrl = AppUtils.OLD_BASE_URL;
                    } else {
                        baseUrl = AppUtils.NEW_BASE_URL;
                    }*/
                    String registerUrl = AppUtils.BLOCK_USER;
                    result = WebService.callApi(commonParam, json, registerUrl, false);

                    AppUtils.showLog(FeedDetailActivity.this, "callUserBlockAPI==json==" + json);
                    AppUtils.showLog(FeedDetailActivity.this, "callUserBlockAPI==result==" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppUtils.showLog(FeedDetailActivity.this, "callUserBlockAPI==Exception==" + e.toString());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    Gson gson = new Gson();
                    CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);

                    hideProgress();
                    if (commonResponse.getSuccess()) {
                        showToast(commonResponse.getMessage());
                        Intent intent = new Intent(FeedDetailActivity.this, MainActivity.class);
                        intent.putExtra("from", 1);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        showToast(commonResponse.getMessage());
                    }
                    Pref.setStringValue(FeedDetailActivity.this, Constants.CURRENT_BASE_URL, "1");
                } catch (Exception e) {
                    hideProgress();
                    e.printStackTrace();
                    AppUtils.showLog(FeedDetailActivity.this, "callUserBlockAPI==Exception==" + e.toString());
                    showToast(getString(R.string.msg_something_wrong));
                    Pref.setStringValue(FeedDetailActivity.this, Constants.CURRENT_BASE_URL, "1");
                }
            }
        }.execute();
    }

    private void callBlockUserAPI() {

        showProgress();

        Pref.setStringValue(FeedDetailActivity.this, Constants.CURRENT_BASE_URL, "2");
        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("userId", lp.getData().getId());
        request.put("toUserId", dataFeed.getId());
        request.put("mainUserId", "");

        AppUtils.showLog(FeedDetailActivity.this, "getBlockUser : PARAMETER==" + request.toString());
        Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().getBlockUser(request);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(getApplicationContext(), "Response getBlockUser:" + jsonObj.toString());

                        if (response.body().getSuccess()) {
                            Toast.makeText(FeedDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FeedDetailActivity.this, MainActivity.class);
                            intent.putExtra("from", 1);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(FeedDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(FeedDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FeedDetailActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                }

                Pref.setStringValue(FeedDetailActivity.this, Constants.CURRENT_BASE_URL, "1");
                hideProgress();
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                hideProgress();
                Log.e("tt", t + "<<");
                Pref.setStringValue(FeedDetailActivity.this, Constants.CURRENT_BASE_URL, "1");
                AppUtils.showLog(getApplicationContext(), "onFailure GetReportReasons:" + t.getMessage());
                Toast.makeText(FeedDetailActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            setResult(1001);
        }
    }


    private void setLikes(String fid, String userStatus) {

        String uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "";
        //Log.e(">>", "DATa" + feedtype + " " + fid + " " + fname);
        final Map<String, Object> request = new HashMap<>();
        request.put("userFrom", fid);
        request.put("userStatus", userStatus);
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
                    showToast(response.body().getMessage());

                    if (response.body().isSuccess()) {
                        setResult(102);
                        if (from.equalsIgnoreCase("PushNotification")) {
                            goToHome();
                        } else {
                            finish();
                        }
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

}
//ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC+awzQxmzD3LmmqxEIhAEqWf8OZkNp5/5svLlBslg0FFc/DcfkHM9RRt2XmYqt9c6BpbPb/hlQRxNwhzJOvSZbsMn7WtW5Jj7s/A7SgdvphPDzMM77EehIzMYtAnHoTBq6zYPeSKBOZFfoKSG7AHXUSps2mPcB372a8lFvrWc9FEfButGo9FMVrzkK/kCQA/Si88SVEV0sH3zCKtcEaRpIk3Wo2HXq71fLajErX7au+gP4nSziEAyOXA8CbfI4h6IZSb7plJTWITi9LdjoN/+oNanVPVlUWTTd9irItze8ifDBPo5xydCy2TuYOReE9dJ2XH847JaOaOrBT2QazdkV master@DESKTOP-KOCDUKB
//MD5: 72:76:32:52:e4:3b:21:ad:aa:4a:28:f1:5c:07:00:a8
//SHA256: Dx9rjfzhnqGX/+A18/4QdK2BOXv6ykrILvIC7hu5QKg
/*
ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIIgE8hmGOKsW71WyMRTgUoPoExY3BiwpnIwgTTp1zZi6 youngbrainz.dhaval@gmail.com
MD5: 20:a5:73:b1:22:80:f4:91:d2:34:e4:6b:62:16:dc:b9
SHA256: +Zgnv0UWpqxuUfqDvx59N2rFEkUItMjgzac4Rs21S/o*/

/*
rules_version = '2';
        service cloud.firestore {
        match /databases/{database}/documents {
        match /{document=**} {
        allow read, write: if request.auth.uid != null;
        }
        }
        }*/
