package com.nect.friendlymony.Fragments.register;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nect.friendlymony.Activity.AddImagesActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.SignupModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalQesFragment extends BaseFragment {

    View view;
    @BindView(R.id.etqes)
    EditText etqes;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.btnSkip)
    Button btnSkip;
    @BindView(R.id.llFooter)
    LinearLayout llFooter;

    private SignupModel sm;


    public FinalQesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        FinalQesFragment fragment = new FinalQesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {

            view = inflater.inflate(R.layout.fragment_final_qes, container, false);
            ButterKnife.bind(this, view);
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.d("IntroActivity", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Pref.setStringValue(getContext(), PREF_FCM_TOKEN, token);
                        // Log and toast
                        Log.d("IntroActivity", token);
                    });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btnNext, R.id.btnSkip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (etqes.getText().toString().isEmpty()) {
                    etqes.setError("Required");
                    showToast("Please write something about yourself");
                    return;
                }


                sm = HawkAppUtils.getInstance().getSIGNUP();
                sm.setYourself(etqes.getText().toString());
                HawkAppUtils.getInstance().setSIGNUP(sm);
                //((SignupQuestionsActivity) getActivity()).loadFragment(UploadPhotoFragment.newInstance(), UploadPhotoFragment.class.getSimpleName(), true);

                if (isConnectedToInternet()) {
                    SignupUser();
                }
                break;
            case R.id.btnSkip:

                sm = HawkAppUtils.getInstance().getSIGNUP();
                sm.setYourself(etqes.getText().toString());
                HawkAppUtils.getInstance().setSIGNUP(sm);


                if (isConnectedToInternet()) {
                    SignupUser();
                }

                // ((SignupQuestionsActivity) getActivity()).loadFragment(UploadPhotoFragment.newInstance(), UploadPhotoFragment.class.getSimpleName(), true);

                break;
        }
    }

    private void SignupUser() {

        sm = HawkAppUtils.getInstance().getSIGNUP();

        registerUser();
    }

    private void registerUser() {

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        if (sm.getType().equals("3")) {
            request.put("facebook_id", sm.getSocialtoken() + "");
        } else if (sm.getType().equals("2")) {
            request.put("google_id", sm.getSocialtoken() + "");
        } else if (sm.getType().equals("4")) {
            request.put("apple_id", sm.getSocialtoken() + "");
        }
        request.put("is_sign_up", sm.getType() + "");
        request.put("country_code", sm.getCountrycode() + "");
        request.put("currency_symbol", sm.getCurrency_symbol() + "");
        request.put("currency_code", sm.getCurrency_code() + "");
        request.put("mobile_no", sm.getPhonenumber() + "");
        request.put("name", sm.getFname() + "");
        request.put("email", sm.getEmail() + "");
        request.put("device_token", Pref.getStringValue(getActivity(), PREF_FCM_TOKEN, "") + "");
        request.put("firebase_token", Pref.getStringValue(getActivity(), PREF_FCM_TOKEN, "") + "");
        request.put("device_type", "Android");
        request.put("vAge", sm.getAge() + "");
        request.put("vGender", sm.getGender() + "");
        request.put("vRadius", "25");
        request.put("vAbout", sm.getYourself() + "");
        request.put("is_qualification", sm.getQes1() + "");
        request.put("is_smoke", sm.getIsSmoke() + "");
        request.put("is_drink", sm.getIsDrink() + "");
        request.put("is_politics", sm.getIsPolitics() + "");
        request.put("is_employee", sm.getIsEmployed() + "");
        request.put("is_earn", sm.getQes6() + "");
        request.put("last_name", sm.getLname() + "");
        request.put("vBirthdate", sm.getDob() + "");
        request.put("vShow_me", sm.getIntrested() + "");
        request.put("vAge_min", sm.getAgeMin() + "");
        request.put("vAge_max", sm.getAgeMax() + "");
        request.put("distance_type", "Km");
        request.put("referral_code", Pref.getStringValue(getActivity(), Constants.PREF_REFERRAL, ""));
        Call<LoginResponse> call = null;
        AppUtils.showLog(getContext(), "signup : PARAMETER==" + request.toString());
        call = RetrofitBuilder.getInstance().getRetrofit().signup(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response signup: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(getContext(), "JSONException signup: " + e.getMessage());
                }

                if (response.isSuccessful()) {


                    //showToast(response.body().getMessage());
                    if (response.body().isSuccess()) {

                        //if (response.body().isRegistered()) {

                        HawkAppUtils.getInstance().setIsLogin(true);
                        HawkAppUtils.getInstance().setUSERDATA(response.body());


                        sendLocation(response.body());


                        //}
                    } else {
                        hideProgress();
                        showToast(response.body().getMessage());
                    }
                } else {
                    hideProgress();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgress();
                Log.e("errr: ", ">>err" + t + "\n");

            }
        });
    }

    private void sendLocation(LoginResponse body) {

        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("user_id", lp.getData().getId());
        request.put("lat", Pref.getStringValue(getActivity(), Constants.PREF_LATITUDE, "0.0"));
        request.put("lang", Pref.getStringValue(getActivity(), Constants.PREF_LONGITUDE, "0.0"));


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLocation(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                callUpdateUserOnlineStatusApi();
                /*hideProgress();
                Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                startActivity(intent);
                getActivity().finish();*/
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callUpdateUserOnlineStatusApi();
                /*hideProgress();
                Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                startActivity(intent);
                getActivity().finish();*/
            }
        });
    }


    private void callUpdateUserOnlineStatusApi() {
        final Map<String, Object> request = new HashMap<>();
        if (HawkAppUtils.getInstance().getUSERDATA() != null) {
            request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());


            AppUtils.showLog(MainActivity.mainActivity, "getSignupValidationFreeTrial : PARAMETER==" + request.toString());
            Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().getSignupValidationFreeTrial(request);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(getActivity(), "Response getSignupValidationFreeTrial: " + jsonObj.toString());

                        hideProgress();
                        Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideProgress();
                        Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        //showToast(getString(R.string.msg_something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    hideProgress();
                    //showToast(getString(R.string.msg_something_wrong));
                    AppUtils.showLog(MainActivity.mainActivity, "onFailure getSignupValidationFreeTrial:" + t.getMessage());
                    Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

    }

}
