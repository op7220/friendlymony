package com.nect.friendlymony.Fragments.BuyPager;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Activity.PaymentActivity;
import com.nect.friendlymony.Activity.PaymentCardActivity;
import com.nect.friendlymony.Adapter.AdvantagesAdapter;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.GetSubscriptions.ResponseGetSubscriptions;
import com.nect.friendlymony.Model.Razorpay.OrderIdResponse;
import com.nect.friendlymony.Model.buy.CountCrushResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.ApiEndpointInterface;
import com.nect.friendlymony.Retrofit.BasicAuthInterceptor;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoostBuyFragment extends BaseFragment {


    private static final int REQEST_PAYMENT = 27;

    View view;
    @BindView(R.id.tvBought)
    TextView tvBought;
    @BindView(R.id.tvSpent)
    TextView tvSpent;
    @BindView(R.id.tvLeft)
    TextView tvLeft;
    @BindView(R.id.btnBoost)
    Button btnBoost;
    @BindView(R.id.btnBuy)
    Button btnBuy;
    @BindView(R.id.tvAdvantage)
    TextView tvAdvantage;
    @BindView(R.id.rvAds)
    RecyclerView rvAds;
    @BindView(R.id.nesttedSccroll)
    NestedScrollView nesttedSccroll;


    AdvantagesAdapter advantagesAdapter;


    double payAmount = 0;
    String qty = "";

    public BoostBuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_boost_buy, container, false);
        ButterKnife.bind(this, view);


        //CalligraphyUtils.applyFontToTextView(tvAdvantage, fontExtraBoldMuli);

        advantagesAdapter = new AdvantagesAdapter(getActivity(), Constants.LIST_BENFIT_BOOST);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAds.setLayoutManager(layoutManager);

        rvAds.setAdapter(advantagesAdapter);
        rvAds.setNestedScrollingEnabled(false);

        nesttedSccroll.post(new Runnable() {
            @Override
            public void run() {
                nesttedSccroll.scrollTo(0, 0);
                nesttedSccroll.pageScroll(View.FOCUS_UP);
                nesttedSccroll.smoothScrollTo(0, 0);
            }
        });


        getCount();

        return view;
    }

    private void getCount() {
        showProgress();

        Call<CountCrushResponse> call = RetrofitBuilder.getInstance().getRetrofit().getCrucshCount();
        call.enqueue(new Callback<CountCrushResponse>() {
            @Override
            public void onResponse(Call<CountCrushResponse> call, Response<CountCrushResponse> response) {
                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response getCrucshCount: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(getContext(), "getCrucshCount : JSONException==" + e.getMessage());
                    Toast.makeText(getContext(), getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                }
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        tvBought.setText(response.body().getData().getTotal_boost() + "");
                        tvLeft.setText(response.body().getData().getLeft_boost() + "");
                        tvSpent.setText(response.body().getData().getSpent_boost() + "");
                    }
                }
            }


            @Override
            public void onFailure(Call<CountCrushResponse> call, Throwable t) {
                AppUtils.showLog(getContext(), "getCrucshCount : onFailure==" + t.getMessage());
                hideProgress();
            }
        });
    }


    private void purchaseBoost() {

        if (!isConnectedToInternet()) {
            return;
        }

        showProgress();


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().UsedBoost();
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response UsedBoost: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(getContext(), "UsedBoost : JSONException==" + e.getMessage());
                    Toast.makeText(getContext(), getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                }
                if (response.isSuccessful()) {

                    showToast(response.body().getMessage());
                    if (response.body().isSuccess()) {

                        getCount();
                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                AppUtils.showLog(getContext(), "UsedBoost : onFailure==" + t.getMessage());
                hideProgress();
            }
        });
    }


    private void callPurchaseBoostAPI() {

    }


    @OnClick({R.id.btnBoost, R.id.btnBuy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBoost:
                //callPurchaseBoostAPI();
                purchaseBoost();
                break;
            case R.id.btnBuy:
                callGetBoostRecordsApi();
                break;
        }
    }


    private void callGetBoostRecordsApi() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("currency_code", Pref.getStringValue(getContext(), Constants.CURRENCY_CODE, "INR"));

        AppUtils.showLog(MainActivity.mainActivity.getApplicationContext(), "getBoostRecords : PARAMETER==" + request.toString());
        Call<ResponseGetSubscriptions> call = RetrofitBuilder.getInstance().getRetrofit().getBoostRecords(request);
        call.enqueue(new Callback<ResponseGetSubscriptions>() {
            @Override
            public void onResponse(Call<ResponseGetSubscriptions> call, Response<ResponseGetSubscriptions> response) {
                JSONObject jsonObj = null;
                try {
                    hideProgress();
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response getBoostRecords: " + jsonObj.toString());

                    if (response.isSuccessful()) {
                        if (response.body().getData() != null && response.body().getData().size() > 0) {

                            Dialog dialog = new Dialog(getActivity());
                            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_buy_boosts);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);

                            LinearLayout llOne = dialog.findViewById(R.id.llOne);
                            LinearLayout llTwo = dialog.findViewById(R.id.llTwo);
                            LinearLayout llThree = dialog.findViewById(R.id.llThree);

                            TextView tvPaymentOne = dialog.findViewById(R.id.tvPaymentOne);
                            TextView tvPaymentTwo = dialog.findViewById(R.id.tvPaymentTwo);
                            TextView tvPaymentThree = dialog.findViewById(R.id.tvPaymentThree);

                            TextView tvBoostOne = dialog.findViewById(R.id.tvBoostOne);
                            TextView tvBoostTwo = dialog.findViewById(R.id.tvBoostTwo);
                            TextView tvBoostThree = dialog.findViewById(R.id.tvBoostThree);

                            Pref.setStringValue(getContext(), Constants.CURRENCY_SYMBOL, response.body().getCurrency_symbol());
                            Pref.setStringValue(getContext(), Constants.CURRENCY_CODE, response.body().getCurrency_code());
                            String oneMonthPrice = response.body().getCurrency_symbol() + response.body().getData().get(0).getConvertedPrice();
                            String threeMonthPrice = response.body().getCurrency_symbol() + response.body().getData().get(1).getConvertedPrice();
                            String sixMonthPrice = response.body().getCurrency_symbol() + response.body().getData().get(2).getConvertedPrice();

                            tvPaymentOne.setText(oneMonthPrice);
                            tvPaymentTwo.setText(threeMonthPrice);
                            tvPaymentThree.setText(sixMonthPrice);

                            tvBoostOne.setText(response.body().getData().get(0).getName());
                            tvBoostTwo.setText(response.body().getData().get(1).getName());
                            tvBoostThree.setText(response.body().getData().get(2).getName());

                            ImageView ivClose = dialog.findViewById(R.id.ivClose);

                            llOne.setOnClickListener(view -> {
                                dialog.dismiss();
                                payAmount = 4.99;
                                qty = "5";

                                if (response.body().getData().size() > 0) {
                                    int totalAmount;
                                    //totalAmount = Integer.parseInt(response.body().getData().get(0).getConvertedPrice()) * response.body().getData().get(0).getMonth();
                                    Intent intent = new Intent(getActivity(), PaymentCardActivity.class);
                                    intent.putExtra("amount", response.body().getData().get(0).getPrice());
                                    intent.putExtra("totalAmount", response.body().getData().get(0).getConvertedPrice());
                                    intent.putExtra("payment_for", "1");
                                    intent.putExtra("fromIntent", "1");
                                    intent.putExtra("plan_id", String.valueOf(response.body().getData().get(0).getId()));
                                    startActivityForResult(intent, 101);
                                }
                            });
                            llTwo.setOnClickListener(view -> {
                                dialog.dismiss();
                                payAmount = 9.99;
                                qty = "20";
                                if (response.body().getData().size() > 0) {
                                    Intent intent = new Intent(getActivity(), PaymentCardActivity.class);
                                    intent.putExtra("amount", response.body().getData().get(1).getPrice());
                                    intent.putExtra("totalAmount", response.body().getData().get(1).getConvertedPrice());
                                    intent.putExtra("payment_for", "1");
                                    intent.putExtra("fromIntent", "1");
                                    intent.putExtra("plan_id", String.valueOf(response.body().getData().get(1).getId()));
                                    startActivityForResult(intent, 101);
                                }
                            });
                            llThree.setOnClickListener(view -> {
                                dialog.dismiss();
                                payAmount = 19.99;
                                qty = "50";

                                if (response.body().getData().size() > 0) {
                                    Intent intent = new Intent(getActivity(), PaymentCardActivity.class);
                                    intent.putExtra("amount", response.body().getData().get(2).getPrice());
                                    intent.putExtra("totalAmount", response.body().getData().get(2).getConvertedPrice());
                                    intent.putExtra("payment_for", "1");
                                    intent.putExtra("fromIntent", "1");
                                    intent.putExtra("plan_id", String.valueOf(response.body().getData().get(2).getId()));
                                    startActivityForResult(intent, 101);
                                }

                            });
                            ivClose.setOnClickListener(view -> dialog.dismiss());
                            dialog.show();

                        } else {
                            showToast(getString(R.string.msg_data_not_found));
                        }
                    } else {
                        showToast(getString(R.string.msg_something_wrong));
                    }

                } catch (JSONException e) {
                    hideProgress();
                    e.printStackTrace();
                    AppUtils.showLog(getContext(), "getBoostRecords JSONException: " + jsonObj.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseGetSubscriptions> call, Throwable t) {
                hideProgress();
                showToast(getString(R.string.msg_something_wrong));
                AppUtils.showLog(getActivity(), "getBoostRecords JSONException: " + t.getMessage());
            }
        });
    }


    private void startPaymentOrder() {

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


        if (!isConnectedToInternet()) {
            return;
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

                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("oid", oid);
                    intent.putExtra("qtyMsg", qty + " Boosts");
                    intent.putExtra("payAmount", payAmount * 70);
                    startActivityForResult(intent, REQEST_PAYMENT);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgress();
        if (requestCode == REQEST_PAYMENT && resultCode == RESULT_OK) {
            String rId = data.getStringExtra("rId");
            purchaseCount(rId);
        } else if (requestCode == 101 && resultCode == 102) {
            getCount();
        }
    }

    private void purchaseCount(String rId) {


        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("type", "boost");
        request.put("amount", payAmount + "");
        request.put("description", "");
        request.put("transaction_id", rId + "");
        request.put("crush_boost_qty", qty);
        AppUtils.showLog(getContext(), "Response addTransaction: " + request.toString());
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().addTransaction(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response addTransaction: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(getContext(), "addTransaction : JSONException==" + e.getMessage());
                    Toast.makeText(getContext(), getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                }
                if (response.isSuccessful()) {

                    showToast(response.body().getMessage());
                    if (response.body().isSuccess()) {

                        getCount();
                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

}
