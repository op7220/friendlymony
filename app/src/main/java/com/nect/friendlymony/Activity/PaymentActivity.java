package com.nect.friendlymony.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nect.friendlymony.Model.BaseResponse;
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
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends BaseAppCompatActivity implements PaymentResultWithDataListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String oid = "";
    String qtyMsg = "";
    double payAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        setToolbar(toolbar, "Payment", true);

        oid = getIntent().getStringExtra("oid");
        qtyMsg = getIntent().getStringExtra("qtyMsg");
        payAmount = getIntent().getDoubleExtra("payAmount", 0);


        showProgress();
        startPayment(oid);
    }


    public void startPayment(String oid) {


        Checkout checkout = new Checkout();

        checkout.setImage(R.mipmap.ic_launcher);

        //Pass your payment options to the Razorpay Checkout as a JSONObject

        try {
            JSONObject options = new JSONObject();


            options.put("name", getResources().getString(R.string.app_name));


            options.put("description", qtyMsg + "");
            options.put("order_id", oid + "");
            options.put("currency", "INR");
            JSONObject preFill = new JSONObject();
            preFill.put("email", HawkAppUtils.getInstance().getUSERDATA().getData().getEmail());
            preFill.put("contact", HawkAppUtils.getInstance().getUSERDATA().getData().getMobileNo());
            options.put("prefill", preFill);

         /* Amount is always passed in currency subunits
          Eg: "500" = INR 5.00*/

            options.put("amount", ((int) (payAmount * 100)));

            hideProgress();
            checkout.open(this, options);


        } catch (Exception e) {
            hideProgress();
            finish();
            Log.e("pay", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {

        Log.e("data", "onPaymentPaymentDataSuccess W: " + paymentData.getOrderId() + "\n" + paymentData.getPaymentId() + "\n" +
                paymentData.getData());

        String amt = Pref.getStringValue(this, Constants.PREF_AMOUNT_PLAN, "");
        String mnth = Pref.getStringValue(this, Constants.PREF_MONTH_PLAN, "");

        Log.e("ammm", amt + " " + mnth);


        Intent intent = new Intent();
        intent.putExtra("rId", razorpayPaymentID);
        setResult(RESULT_OK, intent);
        finish();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(err);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
