package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;
import com.nect.friendlymony.Model.Login.Data;
import com.nect.friendlymony.Model.Login.UserResponse;
import com.nect.friendlymony.Model.Subsciption.DataItem;
import com.nect.friendlymony.Model.Subsciption.SubcriptionResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcriptionActivity extends BaseAppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvSdate)
    TextView tvSdate;
    @BindView(R.id.tvMAmount)
    TextView tvMAmount;
    @BindView(R.id.tvEdate)
    TextView tvEdate;
    @BindView(R.id.cvPlan)
    CardView cvPlan;
    @BindView(R.id.tvRenew)
    TextView tvRenew;
    @BindView(R.id.swRenew)
    Switch swRenew;
    @BindView(R.id.cvRenew)
    CardView cvRenew;
    @BindView(R.id.as_tvEndDate)
    TextView as_tvEndDate;
    @BindView(R.id.ivBack)
    ImageView ivBack;


    @BindView(R.id.scroll)
    NestedScrollView scroll;

    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcription);
        ButterKnife.bind(this);
        setToolbar(toolbar, "", true);


        /*CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);
        CalligraphyUtils.applyFontToTextView(tvRenew, fontExtraBoldMuli);*/

        from = getIntent().getStringExtra("Form");

        if (!isConnectedToInternet()) {
            return;
        }

        getSbcription();
        getprofile();

        ivBack.setOnClickListener(view -> {
            if (from != null) {
                if (from.equalsIgnoreCase("Upgrade")) {
                    finish();
                } else {
                    Intent intent = new Intent(SubcriptionActivity.this, MainActivity.class);
                    intent.putExtra("from", 1);
                    startActivity(intent);
                    finishAffinity();
                }
            } else {
                finish();
            }
        });
    }

    private void getSbcription() {

        final Map<String, Object> request = new HashMap<>();
        //request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("currency_code", Pref.getStringValue(SubcriptionActivity.this, Constants.CURRENCY_CODE, "INR"));

        AppUtils.showLog(SubcriptionActivity.this, "GetSubscriptionDetails : PARAMETER==" + request.toString());
        Call<SubcriptionResponse> call = RetrofitBuilder.getInstance().getRetrofit().GetSubscriptionDetails(request);
        call.enqueue(new Callback<SubcriptionResponse>() {
            @Override
            public void onResponse(Call<SubcriptionResponse> call, Response<SubcriptionResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(SubcriptionActivity.this, "Response GetSubscriptionDetails: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        if (response.body().getData() != null) {
                            if (!response.body().getData().isEmpty()) {
                                DataItem di = response.body().getData().get(0);
                                String month = di.getMonths();
                                String endDateSubscription = "Valid Till:" + " " + di.getEndDate();
                                if (month.equalsIgnoreCase("1")) {
                                    month = month + " " + "month";
                                } else if (month.equalsIgnoreCase("0")) {
                                    month = "--";
                                } else {
                                    month = month + " " + "months";
                                }
                                tvMAmount.setText(Constants.CURRENCY + /*new DecimalFormat("#.##").format(di.getConverted_price())*/di.getConverted_price());
                                tvMonth.setText(month);
                                as_tvEndDate.setText(endDateSubscription);

                                try {
                                    String dateFormat = "yyyy-MM-dd";
                                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                                    DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                                    Date dateS = sdf.parse(di.getStartDate());
                                    Date dateE = sdf.parse(di.getEndDate());

                                    String startDate = targetFormat.format(dateS);
                                    String endDate = targetFormat.format(dateE);


                                    tvSdate.setText(startDate);
                                    tvEdate.setText(endDate);

                                } catch (Exception e) {

                                }
                            }
                        }
                    } else {
                        showToast(response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<SubcriptionResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
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
                    AppUtils.showLog(SubcriptionActivity.this, "Response userData: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        Data dataUser = response.body().getData();
                        try {
                            String dateFormat = "yyyy-MM-dd";
                            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                            String exDate = (dataUser.getExpire_date()).split("T")[0];
                            Date dateS = sdf.parse(exDate);

                            String startDate = targetFormat.format(dateS);


                            tvSdate.setText(startDate);

                        } catch (Exception e) {
                            tvSdate.setText(dataUser.getExpire_date());
                        }

                    }
                }
            }


            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }
}
