package com.nect.friendlymony.Fragments.home;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nect.friendlymony.Activity.BlockUserActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Activity.PaymentCardActivity;
import com.nect.friendlymony.Adapter.AdvantagesAdapter;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.Model.GetSubscriptions.DataGetSubscriptions;
import com.nect.friendlymony.Model.GetSubscriptions.ResponseGetSubscriptions;
import com.nect.friendlymony.Model.Razorpay.OrderIdResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.ApiEndpointInterface;
import com.nect.friendlymony.Retrofit.BasicAuthInterceptor;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nect.friendlymony.Activity.MainActivity.mainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpgradeFragment extends BaseFragment {
    AdvantagesAdapter advantagesAdapter;
    TextView[] dots;

    View view;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.llOne)
    LinearLayout llOne;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.llTwo)
    LinearLayout llTwo;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.llThree)
    LinearLayout llThree;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.llFour)
    LinearLayout llFour;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.pagerUpgrade)
    ViewPager pagerUpgrade;
    @BindView(R.id.llDots)
    LinearLayout llDots;
    @BindView(R.id.tvAdvantage)
    TextView tvAdvantage;
    @BindView(R.id.rvAds)
    RecyclerView rvAds;

    double payAmount = 0;
    String month = "";

    List<DataGetSubscriptions> dataGetSubscriptions;

    public UpgradeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upgrade, container, false);

        ButterKnife.bind(this, view);

        dataGetSubscriptions = new ArrayList<>();
        /*CalligraphyUtils.applyFontToTextView(tvTitle, fontSemiBold);

        CalligraphyUtils.applyFontToTextView(tv1, fontSemiBold);
        CalligraphyUtils.applyFontToTextView(tv2, fontSemiBold);

        CalligraphyUtils.applyFontToTextView(tv3, fontSemiBold);
        CalligraphyUtils.applyFontToTextView(tv4, fontSemiBold);*/


        addBottomDots(2);

        //CalligraphyUtils.applyFontToTextView(tvAdvantage, fontExtraBoldMuli);

        advantagesAdapter = new AdvantagesAdapter(getActivity(), Constants.LIST_ADVANTAGE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAds.setLayoutManager(layoutManager);

        rvAds.setAdapter(advantagesAdapter);

        rvAds.setNestedScrollingEnabled(false);

        if (isConnectedToInternet()) {
            callGetSubscriptionsApi();
        } else {
            showToast(getString(R.string.msg_no_internet));
        }

        return view;
    }

    private void callGetSubscriptionsApi() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("currency_code", Pref.getStringValue(getContext(), Constants.CURRENCY_CODE, "INR"));

        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "getSubscriptions : PARAMETER==" + request.toString());
        Call<ResponseGetSubscriptions> call = RetrofitBuilder.getInstance().getRetrofit().getSubscriptions(request);
        call.enqueue(new Callback<ResponseGetSubscriptions>() {
            @Override
            public void onResponse(Call<ResponseGetSubscriptions> call, Response<ResponseGetSubscriptions> response) {
                JSONObject jsonObj = null;
                try {
                    hideProgress();
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response getSubscriptions: " + jsonObj.toString());

                    if (response.isSuccessful()) {
                        if (response.body().getData() != null && response.body().getData().size() > 0) {
                            dataGetSubscriptions.addAll(response.body().getData());
                            Pref.setStringValue(getContext(), Constants.CURRENCY_SYMBOL, response.body().getCurrency_symbol());
                            Pref.setStringValue(getContext(), Constants.CURRENCY_CODE, response.body().getCurrency_code());
                            String oneMonthPrice = response.body().getCurrency_symbol() + String.valueOf(dataGetSubscriptions.get(0).getConvertedPrice() + "/month");
                            String threeMonthPrice = response.body().getCurrency_symbol() + String.valueOf(dataGetSubscriptions.get(1).getConvertedPrice() + "/month");
                            String sixMonthPrice = response.body().getCurrency_symbol() + dataGetSubscriptions.get(2).getConvertedPrice() + "/month";
                            String twelveMonthPrice = response.body().getCurrency_symbol() + dataGetSubscriptions.get(3).getConvertedPrice() + "/month";
                            tv1.setText(oneMonthPrice);
                            tv2.setText(threeMonthPrice);
                            tv3.setText(sixMonthPrice);
                            tv4.setText(twelveMonthPrice);
                        } else {
                            showToast(getString(R.string.msg_data_not_found));
                        }
                    } else {
                        showToast(getString(R.string.msg_something_wrong));
                    }

                } catch (JSONException e) {
                    hideProgress();
                    e.printStackTrace();
                    AppUtils.showLog(getContext(), "getSubscriptions JSONException: " + jsonObj.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseGetSubscriptions> call, Throwable t) {
                hideProgress();
                showToast(getString(R.string.msg_something_wrong));
                AppUtils.showLog(getActivity(), "getSubscriptions onFailure: " + t.getMessage());
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[5];


        llDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.grey_300));
            llDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @OnClick({R.id.llOne, R.id.llTwo, R.id.llThree, R.id.llFour})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.llOne:
                payAmount = 5.99;
                month = "30";

                if (dataGetSubscriptions.size() > 0) {
                    double totalAmount = Double.parseDouble(dataGetSubscriptions.get(0).getConvertedPrice()) * Double.parseDouble(String.valueOf(dataGetSubscriptions.get(0).getMonth()));
                    intent = new Intent(getActivity(), PaymentCardActivity.class);
                    intent.putExtra("amount", dataGetSubscriptions.get(0).getPrice());
                    intent.putExtra("totalAmount", String.valueOf(totalAmount));
                    intent.putExtra("payment_for", "3");
                    intent.putExtra("fromIntent", "3");
                    intent.putExtra("plan_id", String.valueOf(dataGetSubscriptions.get(0).getId()));
                    startActivity(intent);
                }
                break;
            case R.id.llTwo:
                payAmount = 4.99 * 3;
                month = "90";

                if (dataGetSubscriptions.size() > 0) {
                    double totalAmount3 = Double.parseDouble(dataGetSubscriptions.get(1).getConvertedPrice()) * Double.parseDouble(String.valueOf(dataGetSubscriptions.get(1).getMonth()));
                    intent = new Intent(getActivity(), PaymentCardActivity.class);
                    intent.putExtra("amount", dataGetSubscriptions.get(1).getPrice());
                    intent.putExtra("totalAmount", String.valueOf(totalAmount3));
                    intent.putExtra("payment_for", "3");
                    intent.putExtra("fromIntent", "3");
                    intent.putExtra("plan_id", String.valueOf(dataGetSubscriptions.get(1).getId()));
                    startActivity(intent);
                }
                break;
            case R.id.llThree:
                payAmount = 3.50 * 6;
                month = "180";

                if (dataGetSubscriptions.size() > 0) {
                    double totalAmount6 = Double.parseDouble(dataGetSubscriptions.get(2).getConvertedPrice()) * Double.parseDouble(String.valueOf(dataGetSubscriptions.get(2).getMonth()));
                    intent = new Intent(getActivity(), PaymentCardActivity.class);
                    intent.putExtra("amount", dataGetSubscriptions.get(2).getPrice());
                    intent.putExtra("totalAmount", String.valueOf(totalAmount6));
                    intent.putExtra("payment_for", "3");
                    intent.putExtra("fromIntent", "3");
                    intent.putExtra("plan_id", String.valueOf(dataGetSubscriptions.get(2).getId()));
                    startActivity(intent);
                }
                break;
            case R.id.llFour:
                payAmount = 3 * 12;
                month = "365";

                if (dataGetSubscriptions.size() > 0) {
                    double totalAmount12 = Double.parseDouble(dataGetSubscriptions.get(3).getConvertedPrice()) * Double.parseDouble(String.valueOf(dataGetSubscriptions.get(3).getMonth()));
                    intent = new Intent(getActivity(), PaymentCardActivity.class);
                    intent.putExtra("amount", dataGetSubscriptions.get(3).getPrice());
                    intent.putExtra("totalAmount", String.valueOf(totalAmount12));
                    intent.putExtra("payment_for", "3");
                    intent.putExtra("fromIntent", "3");
                    intent.putExtra("plan_id", String.valueOf(dataGetSubscriptions.get(3).getId()));
                    startActivity(intent);
                }
                break;
        }
    }

    private void checkPayment() {

        if (!isConnectedToInternet()) {
            return;
        }

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("amount", payAmount + "");


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().subscriptionLowerToUper(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {


                    if (response.body().isSuccess()) {

                        Log.e(">>", String.format("%.2f", payAmount) + "");

                        Pref.setStringValue(getActivity(), Constants.PREF_AMOUNT_PLAN, String.format("%.2f", payAmount) + "");
                        Pref.setStringValue(getActivity(), Constants.PREF_MONTH_PLAN, month + "");
                        getOrderId();
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


    private void getOrderId() {

        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        showProgress();
        String ORDER_URL = "https://api.razorpay.com/v1/"; //orders";
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.MINUTES);
        httpClient.readTimeout(30, TimeUnit.MINUTES);
        httpClient.writeTimeout(30, TimeUnit.MINUTES);
        httpClient.addInterceptor(new BasicAuthInterceptor(AppUtils.RAZOR_KEY, AppUtils.RAZOR_SECRETE));
        httpClient.interceptors().add(logging);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ORDER_URL).client(httpClient.build())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        ApiEndpointInterface apiInterface = retrofit.create(ApiEndpointInterface.class);


        double Payvalue = (Double.parseDouble(new DecimalFormat("##.##").format(payAmount))) * 70 * 100;

        Call<OrderIdResponse> response = apiInterface.getOrderId((int) Payvalue, "INR");
        response.enqueue(new Callback<OrderIdResponse>() {
            @Override
            public void onResponse(Call<OrderIdResponse> call, Response<OrderIdResponse> rawResponse) {


                if (rawResponse.isSuccessful()) {


                    String oid = rawResponse.body().getId();
                    startPayment(oid);
                } else {
                    hideProgress();
                    showToast("Bad Request");
                }

            }

            @Override
            public void onFailure(Call<OrderIdResponse> call, Throwable throwable) {
                // other stuff...
                Log.e("err", throwable + "");
                showToast("Bad Request");
                hideProgress();
            }
        });

    }

    public void startPayment(String oid) {


        Checkout checkout = new Checkout();

        checkout.setImage(R.mipmap.ic_launcher);

        //Pass your payment options to the Razorpay Checkout as a JSONObject

        try {
            JSONObject options = new JSONObject();


            options.put("name", getResources().getString(R.string.app_name));


            options.put("description", month + " days Plan");
            options.put("order_id", oid + "");
            options.put("currency", "INR");
            JSONObject preFill = new JSONObject();
            preFill.put("email", HawkAppUtils.getInstance().getUSERDATA().getData().getEmail());
            preFill.put("contact", HawkAppUtils.getInstance().getUSERDATA().getData().getMobileNo());
            options.put("prefill", preFill);

         /* Amount is always passed in currency subunits
          Eg: "500" = INR 5.00*/

            options.put("amount", ((int) (payAmount * 70 * 100)));

            hideProgress();
            checkout.open(getActivity(), options);


        } catch (Exception e) {
            hideProgress();
            Log.e("pay", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        AppUtils.showLog(MainActivity.mainActivity, "Upgrade : onPause==");
    }

    @Override
    public void onResume() {
        super.onResume();
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
