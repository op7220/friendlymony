package com.nect.friendlymony.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.nect.friendlymony.CustomView.CardEditText;
import com.nect.friendlymony.Model.payment.ResponseStripePayment;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.CardType;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCardActivity extends BaseAppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher, CardEditText.OnCardTypeChangedListener {

    PaymentCardActivity paymentCardActivity;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.apm_etNameOnCard)
    EditText etNameOnCard;
    @BindView(R.id.apm_etCardNumber)
    EditText etCardNumber;
    @BindView(R.id.apm_etExpiryDate)
    EditText etExpiryDate;
    @BindView(R.id.apm_etCVV)
    EditText etCVV;
    @BindView(R.id.btnApply)
    Button btnApply;
    @BindView(R.id.ap_tvTotalAmount)
    TextView ap_tvTotalAmount;
    @BindView(R.id.bt_card_form_card_number)
    CardEditText bt_card_form_card_number;

    String nameOnCard, cardNumber, expiryDate, cvv = "", amount, paymentFor, planId, fromIntent = "", totalAmount = "";
    int currentYear, currentMonth;
    ArrayList<String> listOfPattern = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_card);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Payment details", true);

        paymentCardActivity = PaymentCardActivity.this;

        amount = getIntent().getStringExtra("amount");
        paymentFor = getIntent().getStringExtra("payment_for");
        planId = getIntent().getStringExtra("plan_id");
        fromIntent = getIntent().getStringExtra("fromIntent");
        totalAmount = getIntent().getStringExtra("totalAmount");

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        if (totalAmount != null && !totalAmount.isEmpty()) {
            totalAmount = new DecimalFormat("##.##").format((Double.parseDouble(totalAmount)));
            totalAmount = "Total amount:-" + "  " + Pref.getStringValue(paymentCardActivity, Constants.CURRENCY_SYMBOL, "₹") + totalAmount;
            ap_tvTotalAmount.setText(totalAmount);
        }
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String formattedDate = df.format(Calendar.getInstance().getTime());
        currentYear = Integer.valueOf(formattedDate);

        Calendar c = Calendar.getInstance();
        currentMonth = c.get(Calendar.MONTH) + 1;

        btnApply.setOnClickListener(this);
        expiryDateValidation(etExpiryDate);

        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*String ccNum = s.toString();
                for (String p : listOfPattern) {
                    if (ccNum.matches(p)) {
                        Toast.makeText(paymentCardActivity, ccNum, Toast.LENGTH_SHORT).show();
                        Toast.makeText(paymentCardActivity, p, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }*/
            }
        });


        bt_card_form_card_number.setOnFocusChangeListener(this);
        bt_card_form_card_number.setOnClickListener(this);
        bt_card_form_card_number.addTextChangedListener(this);
        bt_card_form_card_number.setOnCardTypeChangedListener(this);


        if (isConnectedToInternet()) {
            callStripPaymentApi();
        } else {
            showToast(getString(R.string.msg_no_internet));
        }
    }


    private void expiryDateValidation(EditText editTextRegular) {
        editTextRegular.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if ('/' == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), "/").length <= 2) {
                        editable.insert(editable.length() - 1, "/");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        /*if (AppGlobal.isConnectingToInternet(paymentMethodActivity)) {
            AppGlobal.showProgressBar(avLoadingIndicatorView, lpb_llMain);
            callGetCardDetailsApi();
        } else {
            AppGlobal.commonToast(paymentMethodActivity, getString(R.string.msg_internet_network));
        }*/
    }


    @Override
    public void onClick(View view) {
        if (view == btnApply) {
            if (isValidate()) {
                if (isConnectedToInternet()) {
                    callStripPaymentApi();
                } else {
                    showToast(getString(R.string.msg_no_internet));
                }
            }
        }
    }


    //Call payment API
    private void callStripPaymentApi() {
        showProgress();

        /*String exMonth = "", exYear = "";
        CharSequence foo = expiryDate;
        String bar = foo.toString();
        exMonth = bar.substring(0, 2);
        exYear = "20" + expiryDate.substring(expiryDate.length() - 2);*/

        final Map<String, Object> request = new HashMap<>();
        request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("email", HawkAppUtils.getInstance().getUSERDATA().getData().getEmail());
        request.put("amount", amount);
        request.put("currency_code", Pref.getStringValue(this, Constants.CURRENCY_CODE, "INR"));
        /*request.put("card_number", cardNumber);
        request.put("exp_month", exMonth);
        request.put("exp_year", exYear);
        request.put("cvc", cvv);*/
        request.put("payment_for", paymentFor);
        request.put("plan_id", planId);

        AppUtils.showLog(paymentCardActivity, "StripePaymentProcess : PARAMETER==" + request.toString());
        Call<ResponseStripePayment> call = RetrofitBuilder.getInstance().getRetrofit().getStripePaymentProcess(request);
        call.enqueue(new Callback<ResponseStripePayment>() {
            @Override
            public void onResponse(Call<ResponseStripePayment> call, Response<ResponseStripePayment> response) {
                JSONObject jsonObj = null;
                try {
                    hideProgress();
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(paymentCardActivity, "Response StripePaymentProcess: " + jsonObj.toString());

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //showToast(response.body().getMessage());

                            Intent intent = new Intent(PaymentCardActivity.this, PaymentWebviewActivity.class);
                            intent.putExtra("url", response.body().getData().getPaymentUrl());
                            intent.putExtra("From", fromIntent);
                            startActivityForResult(intent, 100);

                        } else {
                            showToast(response.body().getMessage());
                        }
                    } else {
                        showToast(getString(R.string.msg_something_wrong));
                    }

                } catch (JSONException e) {
                    hideProgress();
                    e.printStackTrace();
                    AppUtils.showLog(paymentCardActivity, "StripePaymentProcess JSONException: " + jsonObj.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseStripePayment> call, Throwable t) {
                hideProgress();
                showToast(getString(R.string.msg_something_wrong));
                AppUtils.showLog(paymentCardActivity, "StripePaymentProcess JSONException: " + t.getMessage());
            }
        });
    }


    private boolean isValidate() {
        nameOnCard = etNameOnCard.getText().toString().trim();
        cardNumber = bt_card_form_card_number.getText().toString().trim();
        expiryDate = etExpiryDate.getText().toString().trim();
        cvv = etCVV.getText().toString().trim();
        //String desiredString = expiryDate.substring(3,4);

        if (nameOnCard.isEmpty()) {
            showToast(getString(R.string.msg_enter_name_on_card));
            return false;
        } else if (cardNumber.isEmpty()) {
            showToast(getString(R.string.msg_enter_card_number));
            return false;
        } else if (cardNumber.length() != 16) {
            showToast(getString(R.string.msg_plz_enter_card_valid));
            return false;
        } else if (expiryDate.isEmpty()) {
            showToast(getString(R.string.msg_card_expiry_date));
            return false;
        } /*else if (currentYear < Integer.valueOf(desiredString)) {
            AppGlobal.commonToast(paymentMethodActivity, getString(R.string.msg_plz_enter_exp_Date_valid));
            return false;
        }*/ else if (!AppUtils.validateCardExpiryDate(expiryDate)) {
            showToast(getString(R.string.msg_plz_enter_exp_Date_valid));
            return false;
        } else if (cvv.isEmpty()) {
            showToast(getString(R.string.msg_plz_enter_cvv));
            return false;
        } else if (etCVV.length() < bt_card_form_card_number.getCardType().getSecurityCodeLength()) {
            showToast(getString(R.string.msg_plz_enter_cvv_valid));
            return false;
        } else {
            String expiryYear = expiryDate.substring(3, 5);
            String month = expiryDate.substring(0, 2);
            if (currentYear >= Integer.valueOf(expiryYear)) {
                if (currentYear > Integer.valueOf(expiryYear)) {
                    showToast(getString(R.string.msg_plz_enter_exp_Date_valid));
                    return false;
                } else {
                    if (currentMonth > Integer.valueOf(month)) {
                        showToast(getString(R.string.msg_plz_enter_exp_Date_valid));
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Intent intent = new Intent(paymentCardActivity, MainActivity.class);
            intent.putExtra("from", 1);
            startActivity(intent);
            finishAffinity();
        } else if (requestCode == 100) {
            if (fromIntent.equalsIgnoreCase("3")) {
                String from = null;
                if (data != null) {
                    from = data.getStringExtra("from");
                }
                if (from != null) {
                    if (from.equalsIgnoreCase("3")) {
                        finish();
                    } else {
                        Intent intent = new Intent(paymentCardActivity, SubcriptionActivity.class);
                        intent.putExtra("from", "SubscriptionPayment");
                        startActivityForResult(intent, 101);
                    }
                } else {
                    Intent intent = new Intent(paymentCardActivity, SubcriptionActivity.class);
                    intent.putExtra("from", "SubscriptionPayment");
                    startActivityForResult(intent, 101);
                }
            } else if (fromIntent.equalsIgnoreCase("1")) {
                //Go to boost
                setResult(102);
                finish();
            } else if (fromIntent.equalsIgnoreCase("2")) {
                //Go to crush
                setResult(103);
                finish();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        etCVV.setFilters(new InputFilter[]{new InputFilter.LengthFilter(bt_card_form_card_number.getCardType().getSecurityCodeLength())});
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {

    }
}
