package com.nect.friendlymony.Fragments.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.nect.friendlymony.Activity.SignupQuestionsActivity;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.SignupModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupFragment extends BaseFragment {
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendarCurrent = Calendar.getInstance();
    Calendar myCalendar_min = Calendar.getInstance();
    Calendar myCalendar_max = Calendar.getInstance();

    String dateFormat = "dd/MM/yyyy";
    DatePickerDialog.OnDateSetListener datePickerListener;

    View view;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etFname)
    EditText etFname;
    @BindView(R.id.etLname)
    EditText etLname;
    @BindView(R.id.etSex)
    EditText etSex;
    @BindView(R.id.etDob)
    EditText etDob;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.ivFemale)
    CircleImageView ivFemale;
    @BindView(R.id.ivMale)
    CircleImageView ivMale;
    @BindView(R.id.ivTrueM)
    ImageView ivTrueM;
    @BindView(R.id.ivTrueF)
    ImageView ivTrueF;
    @BindView(R.id.ivOther)
    CircleImageView ivOther;
    @BindView(R.id.ivTrueO)
    ImageView ivTrueO;

    String intrested = "";
    int age;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    SignupModel sm = new SignupModel();
    /*@BindView(R.id.ccp)
    CountryCodePicker ccp;*/
    @BindView(R.id.amn_ccpCountryCode)
    com.hbb20.CountryCodePicker ccpCountryCode;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.llMobile)
    LinearLayout llMobile;
    @BindView(R.id.tvRange)
    TextView tvRange;
    @BindView(R.id.rangeAge)
    CrystalRangeSeekbar rangeAge;


    public SignupFragment() {
        // Required empty public constructor
    }


    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         /*   mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_signup, container, false);
            ButterKnife.bind(this, view);

            myCalendar_max.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 18);

            sm = HawkAppUtils.getInstance().getSIGNUP();

            if (sm != null) {
                if (sm.getType().equals("2") || sm.getType().equals("3") || sm.getType().equals("4")) {
                    etFname.setText(sm.getFname());
                    etLname.setText(sm.getLname());
                    etEmail.setText(sm.getEmail());
                    llMobile.setVisibility(View.VISIBLE);
                } else {
                    llMobile.setVisibility(View.VISIBLE);
                }
            } else {
                llMobile.setVisibility(View.VISIBLE);
            }
            datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    etDob.setText(sdf.format(myCalendar.getTime()));
                    age = myCalendarCurrent.get(Calendar.YEAR) - year;

                }
            };

            getCountryDetail();
            /*ccp.setOnCountryChangeListener(selectedCountry -> {
                AppUtils.showLog(getContext(), "getIso==" + selectedCountry.getIso());
                AppUtils.showLog(getContext(), "getName==" + selectedCountry.getName());
                AppUtils.showLog(getContext(), "getPhoneCode==" + selectedCountry.getPhoneCode());
                getCountryDetail();
            });*/

            ccpCountryCode.setOnCountryChangeListener(() -> {

                Log.e("Mobile", "getSelectedCountryNameCode==" + ccpCountryCode.getSelectedCountryNameCode());
                Log.e("Mobile", "getSelectedCountryEnglishName==" + ccpCountryCode.getSelectedCountryEnglishName());
                Log.e("Mobile", "getSelectedCountryCodeWithPlus==" + ccpCountryCode.getSelectedCountryCodeWithPlus());

                getCountryDetail();
            });
        }


        rangeAge.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvRange.setText(minValue + " - " + maxValue);

            }
        });
        rangeAge.setMaxStartValue(70);
        return view;
    }

    private void getCountryDetail() {
        /*if (ccp.getPhoneNumber() != null) {
            Locale locale = new Locale("en", ccp.getSelectedCountryNameCode());
            Locale.getDefault();
            if (Currency.getInstance(locale) != null) {
                Currency currency = Currency.getInstance(locale);
                AppUtils.showLog(getContext(), "getCountry==" + locale.getCountry());
                AppUtils.showLog(getContext(), "getCurrencyCode==" + currency.getCurrencyCode());
                AppUtils.showLog(getContext(), "getSymbol==" + currency.getSymbol());
                Pref.setStringValue(getContext(), Constants.COUNTRY_CODE, ccp.getSelectedCountryCodeWithPlus());
                Pref.setStringValue(getContext(), Constants.COUNTRY_CODE_INFO, ccp.getSelectedCountryNameCode());
                Pref.setStringValue(getContext(), Constants.CURRENCY_CODE, currency.getCurrencyCode());
                Pref.setStringValue(getContext(), Constants.CURRENCY_SYMBOL, currency.getSymbol());

                AppUtils.showLog(getContext(), "COUNTRY_CODE==" + Pref.getStringValue(getContext(), Constants.COUNTRY_CODE, Constants.COUNTRY_CODE));
                AppUtils.showLog(getContext(), "COUNTRY_CODE_INFO==" + Pref.getStringValue(getContext(), Constants.COUNTRY_CODE_INFO, Constants.COUNTRY_CODE_INFO));
                AppUtils.showLog(getContext(), "CURRENCY_CODE==" + Pref.getStringValue(getContext(), Constants.CURRENCY_CODE, Constants.CURRENCY_CODE));
                AppUtils.showLog(getContext(), "CURRENCY_SYMBOL==" + Pref.getStringValue(getContext(), Constants.CURRENCY_SYMBOL, Constants.CURRENCY_SYMBOL));
            }
        }*/

        Locale locale = new Locale("", ccpCountryCode.getSelectedCountryNameCode());
        Locale.getDefault();
        if (Currency.getInstance(locale) != null) {
            Currency currency = Currency.getInstance(locale);
            /*stCurrencySymbol = currency.getSymbol();
            stCurrencyCode = currency.getCurrencyCode();
            stCountryCodeInfo = binding.amnCcpCountryCode.getSelectedCountryNameCode();*/

            AppUtils.showLog(getContext(), "getCountry==" + locale.getCountry());
            AppUtils.showLog(getContext(), "getCurrencyCode==" + currency.getCurrencyCode());
            AppUtils.showLog(getContext(), "getSymbol==" + currency.getSymbol());
            Pref.setStringValue(getContext(), Constants.COUNTRY_CODE, ccpCountryCode.getSelectedCountryCodeWithPlus());
            Pref.setStringValue(getContext(), Constants.COUNTRY_CODE_INFO, ccpCountryCode.getSelectedCountryNameCode());
            Pref.setStringValue(getContext(), Constants.CURRENCY_CODE, currency.getCurrencyCode());
            Pref.setStringValue(getContext(), Constants.CURRENCY_SYMBOL, currency.getSymbol());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.etSex, R.id.etDob, R.id.btnSignUp, R.id.ivMale, R.id.ivFemale, R.id.ivOther})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.etSex:
                showMenuGender(etSex);
                break;
            case R.id.etDob:

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                // datePickerDialog.getDatePicker().setMinDate(myCalendar_min.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(myCalendar_max.getTimeInMillis());

                datePickerDialog.show();
                break;
            case R.id.btnSignUp:
                if (isConnectedToInternet()) {
                    signNext();
                }

                break;
            case R.id.ivMale:
                changeGender(0);
                break;
            case R.id.ivFemale:
                changeGender(1);
                break;
            case R.id.ivOther:
                changeGender(2);
                break;
        }
    }

    private void signNext() {
        etFname.setError(null);
        etLname.setError(null);
        etSex.setError(null);
        etDob.setError(null);
        etMobile.setError(null);
        etEmail.setError(null);

        if (etFname.getText().toString().isEmpty()) {

            showToast("Enter your first name");
            etFname.setError("Required");
            return;
        }
        if (etLname.getText().toString().isEmpty()) {
            showToast("Enter your last name");
            etLname.setError("Required");
            return;
        }
        if (etEmail.getText().toString().isEmpty()) {
            showToast("Enter your email address");
            etEmail.setError("Required");
            return;
        }
        if (!AppUtils.checkEmail(etEmail.getText().toString())) {
            showToast("Invalid email address");
            etEmail.setError("Invalid");
            return;
        }
        if (etSex.getText().toString().isEmpty()) {
            showToast("Enter your sex");
            etSex.setError("Required");
            return;
        }
        if (etDob.getText().toString().isEmpty()) {
            showToast("Enter your date of birth");
            etDob.setError("Required");
            return;
        }
        if (intrested.equals("")) {

            showToast("Please select interested option");
            return;
        }

        String contryCode = sm.getCountrycode();
        String phone = sm.getPhonenumber();

        if (sm.getType().equals("2") || sm.getType().equals("3") || sm.getType().equals("4")) {
            if (etMobile.getText().toString().isEmpty()) {
                etMobile.setError("Required");
                showToast("Enter your mobile number");

                return;
            }
            if (etMobile.getText().toString().length() != 10) {
                etMobile.setError("Invalid");
                showToast("Invalid mobile number");
                return;
            } else if (!((etMobile.getText().toString()).matches("[0-9.? ]*"))) {
                etMobile.setError("Invalid");
                showToast("Invalid mobile number");
                return;

            }
            contryCode = ccpCountryCode.getSelectedCountryCode();
            phone = etMobile.getText().toString();

            /*ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                @Override
                public void onCountrySelected(Country selectedCountry) {

                }
            });*/
        }


        if (HawkAppUtils.getInstance().getSIGNUP() != null) {
            sm = HawkAppUtils.getInstance().getSIGNUP();
        }

        String formattedDate = etDob.getText().toString();
        try {
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(etDob.getText().toString());
            formattedDate = targetFormat.format(date);  // 20120821


        } catch (Exception e) {

        }


        sm.setFname(etFname.getText().toString());
        sm.setLname(etLname.getText().toString());
        sm.setGender(etSex.getText().toString());
        sm.setDob(formattedDate);
        sm.setIntrested(intrested);
        sm.setAgeMin(rangeAge.getSelectedMinValue() + "");
        sm.setAgeMax(rangeAge.getSelectedMaxValue() + "");
        sm.setAge(age);
        sm.setEmail(etEmail.getText().toString());
        sm.setPhonenumber(phone);
        sm.setCountrycode(contryCode);
        sm.setCurrency_code(Pref.getStringValue(getActivity(), Constants.CURRENCY_CODE, "INR"));
        sm.setCurrency_symbol(Pref.getStringValue(getActivity(), Constants.CURRENCY_SYMBOL, "₹"));

        HawkAppUtils.getInstance().setSIGNUP(sm);

        // if (sm.getType().equals("1")) {

        ChaeckMail();
        /*} else {
            ((SignupQuestionsActivity) getActivity()).loadFragment(OneQesFragment.newInstance(), OneQesFragment.class.getSimpleName(), true);

        }*/
    }

    private void ChaeckMail() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("email", etEmail.getText().toString() + "");
        request.put("mobile_no", sm.getPhonenumber());
        request.put("country_code", sm.getCountrycode());
        request.put("is_sign_up", sm.getType());


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().checkUserMail(request);
        AppUtils.showLog(getContext(), "checkUserMail : PARAMETER==" + request.toString());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response checkUserMail: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        showToast(response.body().getMessage());
                    } else {
                        ((SignupQuestionsActivity) getActivity()).loadFragment(OneQesFragment.newInstance(), OneQesFragment.class.getSimpleName(), true);

                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();

            }
        });
    }

    private void changeGender(int i) {
        if (i == 0) {
            intrested = "Male";
            ivMale.setBorderWidth(5);
            ivFemale.setBorderWidth(0);
            ivOther.setBorderWidth(0);

            ivTrueM.setVisibility(View.VISIBLE);
            ivTrueF.setVisibility(View.INVISIBLE);
            ivTrueO.setVisibility(View.INVISIBLE);
        } else if (i == 1) {
            intrested = "Female";
            ivMale.setBorderWidth(0);
            ivFemale.setBorderWidth(5);
            ivOther.setBorderWidth(0);

            ivTrueM.setVisibility(View.INVISIBLE);
            ivTrueF.setVisibility(View.VISIBLE);
            ivTrueO.setVisibility(View.INVISIBLE);
        } else if (i == 2) {
            intrested = "Other";
            ivMale.setBorderWidth(0);
            ivFemale.setBorderWidth(0);
            ivOther.setBorderWidth(5);

            ivTrueM.setVisibility(View.INVISIBLE);
            ivTrueF.setVisibility(View.INVISIBLE);
            ivTrueO.setVisibility(View.VISIBLE);
        }
    }

    public void showMenuGender(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                etSex.setText(menuItem.getTitle());

               /* switch (menuItem.getItemId()) {
                    case R.id.action_male:
                        etSex.setText(menuItem.getTitle());
                        return true;
                    case R.id.action_female:
                         return true;
                    case R.id.action_other:
                         return true;

                }*/
                return false;
            }
        });
        popup.inflate(R.menu.menu_gender);
        popup.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        sm = HawkAppUtils.getInstance().getSIGNUP();

    }
}
