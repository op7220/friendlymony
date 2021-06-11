package com.nect.friendlymony.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nect.friendlymony.Model.AppleLoginResponse;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.CheckLogin.LoginCheckResponse;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

public class LoginOptionActivity extends BaseAppCompatActivity {

    private static final String EMAIL = "email";
    private static final int OTP_REQUEST_CODE = 301;
    private static final int RC_SIGN_IN = 101;
    private static final int REQUEST_PERMISSIONS_CODE_LOCATION = 111;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.btnReferrl)
    Button btnReferrl;
    OAuthProvider.Builder provider;
    FirebaseUser firebaseUser;
    String appleAuthURLFull;
    String CLIENT_ID = "com.nect.friendlymony";
    String REDIRECT_URI = "https://friendlymony-1565701062273.firebaseapp.com/__/auth/handler";
    String SCOPE = "name%20email";
    String AUTHURL = "https://appleid.apple.com/auth/authorize";
    String TOKENURL = "https://appleid.apple.com/auth/token";
    private String TAG = LoginOptionActivity.class.getName();
    private FirebaseAuth firebaseAuth;
    private Dialog signInDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        Pref.setStringValue(this, Constants.PREF_REFERRAL, "");

        Pref.setStringValue(this, Constants.CURRENT_BASE_URL, "2");
        //AutoStartPermissionHelper.getInstance().getAutoStartPermission(LoginOptionActivity.this);
        //CalligraphyUtils.applyFontToTextView(btnReferrl, fontSemiBoldMuli);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        showHashKey();
        if (!AppUtils.CheckLocationPermission(this)) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            // listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSIONS_CODE_LOCATION);

        } else {
            initGoogle();
        }

        initAppleLogin();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    Pref.setStringValue(getBaseContext(), PREF_FCM_TOKEN, token);
                    // Log and toast
                    Log.d(TAG, token);
                });
        //enableAutoStart();
        /*appleAuthURLFull = AppleConstants.AUTHURL + "?response_type=code&v=1.1.6&response_mode=form_post&client_id=" + AppleConstants.CLIENT_ID + "&scope=" +
                        AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + AppleConstants.REDIRECT_URI;*/

        TextView tvVersion = findViewById(R.id.tvVersion);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showHashKey() {
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("LoginActivity", "KeyHash:=" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("LoginActivity", "NameNotFoundException==" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("LoginActivity", "NoSuchAlgorithmException==" + e.getMessage());
        }

    }

    @OnClick({R.id.btnFb, R.id.btnGoogle, R.id.btnPhone,
            R.id.btnTerms, R.id.btnReferrl, R.id.btnPrivacy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnFb:
                if (isConnectedToInternet()) {
                    loginFacebook();
                }
                break;
            case R.id.btnGoogle:
                if (isConnectedToInternet()) {
                    showProgress();
                    mGoogleSignInClient.signOut();

                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
                break;
            case R.id.btnPhone:
               /* Intent intent = new Intent(this, SignupQuestionsActivity.class);
                startActivity(intent);*/
                if (isConnectedToInternet()) {
                    /*final Intent intent = new Intent(this, AccountKitActivity.class);
                    AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                            new AccountKitConfiguration.AccountKitConfigurationBuilder(
                                    LoginType.PHONE,
                                    AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
                    // ... perform additional configuration ...
                    intent.putExtra(
                            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                            configurationBuilder.build());
                    startActivityForResult(intent, OTP_REQUEST_CODE);*/

                    signInAppleClick();
                    /*Intent intent = new Intent(LoginOptionActivity.this, AppleSignInActivity.class);
                    startActivityForResult(intent, 1001);*/

                }
                break;
            case R.id.btnTerms:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(AppUtils.URL_TERMS));
                startActivity(intent);
                break;
            case R.id.btnPrivacy:

                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(AppUtils.URL_PRIVACY));
                startActivity(intent2);
                break;
            case R.id.btnReferrl:
                showRefferalDialog();
                break;
        }
    }

    private void showRefferalDialog() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_referral);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        EditText etCode = dialog.findViewById(R.id.etCode);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etCode.getText().toString().isEmpty()) {
                    showToast("Please enter referral code");
                } else {
                    if (!isConnectedToInternet())
                        return;

                    showProgress();
                    final Map<String, Object> request = new HashMap<>();
                    request.put("inviteCode", etCode.getText().toString());

                    AppUtils.showLog(mContext, "checkReferCode : PARAMETER==" + request.toString());
                    Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().checkReferCode(request);
                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                            hideProgress();
                            if (response.isSuccessful()) {


                                if (response.body().isSuccess()) {
                                    showToast("Referral code is valid. Continue to sign up");
                                    Pref.setStringValue(LoginOptionActivity.this, Constants.PREF_REFERRAL, etCode.getText().toString());
                                    dialog.dismiss();
                                } else {
                                    showToast(response.body().getMessage());
                                    Pref.setStringValue(LoginOptionActivity.this, Constants.PREF_REFERRAL, "");
                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            //Log.e("tt", t + "<<");
                            hideProgress();
                        }
                    });


                }
            }
        });
        dialog.show();
    }

    private void loginFacebook() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //Log.e(TAG, "onSuccess: " + loginResult.getAccessToken());
                AccessToken accessToken = loginResult.getAccessToken();
                getFacebookData(accessToken);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
            }
        });
    }

    private void getFacebookData(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        String id = "";
                        try {

                            String email = response.getJSONObject().optString("email");
                            String firstName = response.getJSONObject().optString("first_name");
                            String lastName = response.getJSONObject().optString("last_name");
                            id = response.getJSONObject().optString("id");

                            Log.e("email", firstName + " " + lastName);
                            checkSosicalLogin(id, "3", firstName, lastName, email, "");

                        } catch (Exception e) {
                            e.printStackTrace();
                            //socialLogin(Profile.getCurrentProfile().getId(), "Facebook", "", "", "", "");
                        }
                        //registeruser(object);

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkSosicalLogin(final String id, final String type, final String firstName, final String lastName, final String email, String s) {

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        if (type.equalsIgnoreCase("3")) {
            request.put("facebook_id", id);
        } else if (type.equalsIgnoreCase("2")) {
            request.put("google_id", id);
        } else if (type.equalsIgnoreCase("4")) {
            request.put("apple_id", id);
        }

        String fcmToken = Pref.getStringValue(LoginOptionActivity.this, PREF_FCM_TOKEN, "");
        request.put("firebase_token", fcmToken);
        Call<LoginCheckResponse> call = RetrofitBuilder.getInstance().getRetrofit().UserRegisteredV1(request);

        AppUtils.showLog(mContext, "checkSosicalLogin : PARAMETER==" + request.toString());
        call.enqueue(new Callback<LoginCheckResponse>() {
            @Override
            public void onResponse(Call<LoginCheckResponse> call, Response<LoginCheckResponse> response) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(mContext, "Response checkSosicalLogin: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        if (response.body().isRegistered()) {
                            // showToast(response.body().getMessage());

                           /* HawkAppUtils.getInstance().setIsLogin(true);
                            HawkAppUtils.getInstance().setUSERDATA(response.body());
                            Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/


                            getUserData(type, id, "", "");
                        } else {
                            Log.e("call", "call " + id);
                            Intent intent = new Intent(LoginOptionActivity.this, SignupQuestionsActivity.class);
                            intent.putExtra("from", 1);
                            intent.putExtra("fname", firstName);
                            intent.putExtra("lname", lastName);
                            intent.putExtra("token", id);
                            intent.putExtra("type", type);
                            intent.putExtra("email", email);

                            SignupModel sm = new SignupModel();
                            sm.setFname(firstName);
                            sm.setLname(lastName);
                            sm.setEmail(email);
                            sm.setType(type);
                            sm.setSocialtoken(id);

                            HawkAppUtils.getInstance().setSIGNUP(sm);


                            startActivity(intent);
                        }
                    } else {
                        showToast(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginCheckResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
                showToast(t.getMessage());
            }
        });
    }


    private void checkPhoneLogin(final String countrycoe, final String phone, String type) {

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("country_code", countrycoe);
        request.put("mobile_no", phone);
        String fcmToken = Pref.getStringValue(LoginOptionActivity.this, PREF_FCM_TOKEN, "");
        request.put("firebase_token", fcmToken);

        AppUtils.showLog(mContext, "Phone UserRegisteredV1 : PARAMETER==" + request.toString());
        Call<LoginCheckResponse> call = RetrofitBuilder.getInstance().getRetrofit().UserRegisteredV1(request);
        call.enqueue(new Callback<LoginCheckResponse>() {
            @Override
            public void onResponse(Call<LoginCheckResponse> call, Response<LoginCheckResponse> response) {

                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(mContext, "Response UserRegisteredV1: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        if (response.body().isRegistered()) {
                            //showToast(response.body().getMessage());

                         /*   HawkAppUtils.getInstance().setIsLogin(true);
                            HawkAppUtils.getInstance().setUSERDATA(response.body());
                            Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/

                            getUserData("1", "", countrycoe, phone);
                        } else {


                            SignupModel sm = new SignupModel();

                            sm.setCountrycode(countrycoe);
                            sm.setPhonenumber(phone);
                            sm.setType("1");
                            HawkAppUtils.getInstance().setSIGNUP(sm);

                            HawkAppUtils.getInstance().setSIGNUP(sm);
                            Intent intent = new Intent(LoginOptionActivity.this, SignupQuestionsActivity.class);
                            startActivity(intent);

                        }
                    } else {
                        showToast(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginCheckResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
            }
        });
    }


    private void getUserData(String type, String sId, String cCode, String Mobile) {

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        if (type.equals("3")) {
            request.put("facebook_id", sId + "");
        } else if (type.equals("2")) {
            request.put("google_id", sId + "");
        } else if (type.equals("4")) {
            request.put("apple_id", sId + "");
        } else {
            request.put("country_code", cCode + "");
            request.put("mobile_no", Mobile + "");
        }
        request.put("is_sign_up", type + "");
        request.put("device_token", Pref.getStringValue(this, PREF_FCM_TOKEN, "") + "");
        request.put("device_type", "Android");

        String fcmToken = Pref.getStringValue(LoginOptionActivity.this, PREF_FCM_TOKEN, "");
        request.put("firebase_token", fcmToken);

        Call<LoginResponse> call = null;

        call = RetrofitBuilder.getInstance().getRetrofit().signup(request);

        AppUtils.showLog(mContext, "signup : PARAMETER==" + request.toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(mContext, "Response signup: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (response.body().isSuccess()) {

                        if (response.body().getData().getUserStatus() == 0) {
                            //showToast("Your account is deactivated. Please contact to admin");
                            showOKAlert(" Your account is inactive. Please contact to admin", new onDialogClickListener() {
                                @Override
                                public void onPositive() {

                                }

                                @Override
                                public void onNegative() {

                                }
                            });
                            return;
                        }
                        HawkAppUtils.getInstance().setIsLogin(true);
                        HawkAppUtils.getInstance().setUSERDATA(response.body());


                        sendLocation();

                        //}
                    } else {

                        showToast(response.body().getMessage());
                    }
                } else {
                    hideProgress();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgress();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgress();
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        } catch (Exception e) {

        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OTP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = "Login Cancelled";
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.wasCancelled()) {

                // RegisterTypeActivity.StartActivity(LoginActivity.this, etPhone.getText().toString(), "91");

            } else {
                //Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
                try {

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            String accountKitId = account.getId();

                            // Get phone number
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            String phoneNumberString = phoneNumber.getPhoneNumber();
                            String conuntrycode = phoneNumber.getCountryCode();

                            Log.e("ccode", conuntrycode + " " + phoneNumberString);

                            checkPhoneLogin(conuntrycode, phoneNumberString, "1");
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                        }
                    });
                } catch (Exception e) {
                }

            }


            // Surface the result to your user in an appropriate way.

        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Log.e("gm", "gmail" + requestCode + " " + result.isSuccess() + " " + resultCode);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                String sToken = acct.getId();

                // Log.e("Login", "display name: " + acct.getDisplayName());
                String sFname = "";
                String sLname = "";
                String personName = acct.getDisplayName();
                String personPhotoUrl = "";
                String email = acct.getEmail();
                Log.e(">>", acct.getDisplayName() + " " + acct.getGivenName() + " " + acct.getFamilyName());
                try {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                } catch (Exception e) {
                    personPhotoUrl = "";
                }

                sFname = personName + "";
                try {

                    sFname = acct.getGivenName();
                    sLname = acct.getFamilyName();

                } catch (Exception e) {
                    sFname = personName + "";
                    sLname = personName + "";
                }

                if ((sFname + "").equals("null")) {
                    sFname = "";
                }
                if ((sLname + "").equals("null")) {
                    sLname = "";
                }
                checkSosicalLogin(sToken, "2", sFname, sLname, email, personPhotoUrl);
                // socialLogin(sToken, "Google", personName, email, personPhotoUrl);

            }
        } else if (requestCode == 1001 && resultCode == 1002) {
            String appleResult = Pref.getStringValue(LoginOptionActivity.this, Constants.APPLE_RESPONSE, "");
            if (appleResult != null) {
                if (!appleResult.equalsIgnoreCase("")) {

                    try {
                        Gson gson = new Gson();
                        AppleLoginResponse appleLoginResponse = gson.fromJson(appleResult, AppleLoginResponse.class);

                        if (appleLoginResponse != null) {
                            if (appleLoginResponse.getError() != null && appleLoginResponse.getError().equalsIgnoreCase("invalid_client")) {
                                showToast(getString(R.string.msg_something_wrong));
                            } else {
                                String idToken = appleLoginResponse.getId_token(); // A JSON Web Token that contains the user’s identity information.
                                // Get encoded user id by spliting idToken and taking the 2nd piece
                                String encodedUserID = idToken.split(".")[1];
                                //Decode encodedUserID to JSON
                                String decodedUserData = new String(Base64.decode(encodedUserID, Base64.DEFAULT));
                                JSONObject userDataJsonObject = null;
                                String appleId = "";
                                userDataJsonObject = new JSONObject(decodedUserData);
                                // Get User's ID
                                appleId = userDataJsonObject.getString("iat");

                                //String appleId = appleLoginResponse.getIat();
                                String sFname = Pref.getStringValue(LoginOptionActivity.this, Constants.APPLE_FIRST_NAME, "");
                                String sLname = Pref.getStringValue(LoginOptionActivity.this, Constants.APPLE_LAST_NAME, "");
                                String email = Pref.getStringValue(LoginOptionActivity.this, Constants.APPLE_EMAIL, "");
                                String sToken = Pref.getStringValue(LoginOptionActivity.this, Constants.APPLE_ACCESS_TOKEN, "");
                                if (appleId != null && !appleId.equalsIgnoreCase("")) {
                                    checkSosicalLogin(appleId, "4", sFname, sLname, email, "");
                                } else {
                                    showToast(getString(R.string.msg_something_wrong));
                                }
                            }

                        } else {
                            showToast(getString(R.string.msg_something_wrong));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast(getString(R.string.msg_something_wrong));
                    }

                } else {
                    showToast(getString(R.string.msg_something_wrong));
                }
            } else {
                showToast(getString(R.string.msg_something_wrong));
            }
        }
    }

    private void sendLocation() {

        showProgress();
        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("user_id", lp.getData().getId());
        request.put("lat", Pref.getStringValue(this, Constants.PREF_LATITUDE, "0.0"));
        request.put("lang", Pref.getStringValue(this, Constants.PREF_LONGITUDE, "0.0"));


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLocation(request);
        AppUtils.showLog(mContext, "userLocation : PARAMETER==" + request.toString());
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

                hideProgress();
                Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
                intent.putExtra("from", 1);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
                intent.putExtra("from", 1);
                startActivity(intent);
                finish();
            }
        });
    }


    private void initAppleLogin() {
        provider = OAuthProvider.newBuilder("apple.com");
        firebaseAuth = FirebaseAuth.getInstance();

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("email");
                        add("name");
                    }
                };
        provider.setScopes(scopes);
        firebaseUser = firebaseAuth.getCurrentUser();
        Task<AuthResult> pending = firebaseAuth.getPendingAuthResult();
        if (pending != null) {
            pending.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Log.d(TAG, "checkPending:onSuccess:" + authResult.getUser());
                    Log.d(TAG, "checkPending:onSuccess:" + authResult.getUser().getDisplayName());
                    Log.d(TAG, "checkPending:onSuccess:" + authResult.getUser().getEmail());
                    Log.d(TAG, "checkPending:onSuccess:" + authResult.getUser().getPhoneNumber());
                    Log.d(TAG, "checkPending:onSuccess:" + authResult.getUser().getProviderId());
                    Log.d(TAG, "checkPending:onSuccess:" + authResult.getUser().getPhotoUrl());
                    // Get the user profile with authResult.getUser() and
                    // authResult.getAdditionalUserInfo(), and the ID
                    // token from Apple with authResult.getCredential().
                    showToast(authResult.getAdditionalUserInfo().getUsername());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "checkPending:onFailure", e);
                    showToast(e.getMessage());
                }
            });
        } else {
            Log.d(TAG, "pending: null");
            //showToast("Something went wrong");
        }
    }


    private void signInAppleClick() {
        firebaseAuth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener(
                        authResult -> {
                            // Sign-in successful!
                            Log.d(TAG, "activitySignIn:onSuccess:" + authResult.getUser());
                            firebaseUser = authResult.getUser();
                            /*showToast(firebaseUser.getEmail());
                            showToast(firebaseUser.getDisplayName());
                            showToast(firebaseUser.getUid());*/

                            Log.d(TAG, "activitySignIn:getEmail:" + firebaseUser.getEmail());
                            Log.d(TAG, "activitySignIn:getUid:" + firebaseUser.getUid());
                            Log.d(TAG, "activitySignIn:getPhoneNumber:" + firebaseUser.getPhoneNumber());
                            Log.d(TAG, "activitySignIn:getCredential:" + new Gson().toJson(authResult.getCredential()));
                            Log.d(TAG, "activitySignIn:getCredential:" + new Gson().toJson(authResult.getAdditionalUserInfo()));
                            Log.d(TAG, "activitySignIn:getCredential:" + new Gson().toJson(authResult.getUser()));


                            checkSosicalLogin(firebaseUser.getUid(), "4", firebaseUser.getDisplayName(), "", firebaseUser.getEmail(), "");
                        })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "activitySignIn:onFailure", e);
                    showToast(e.getMessage());
                });
    }

    /*private void openDialog(String url){
        appledialog =new Dialog(this);
        WebViwe webView =new WebView(this);
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = AppleWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url);
        appledialog.setContentView(webView);
        appledialog.show();
    }*/

    private void enableAutoStart() {
        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable AutoStart")
                    .setMessage("Please allow AppName to run app in the background for get notification, else our services can't be accessed.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.miui.securitycenter",
                                    "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            startActivity(intent);
                        }
                    })
                    .show();
        } else if (Build.BRAND.equalsIgnoreCase("Letv")) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable AutoStart")
                    .setMessage("Please allow AppName to always run in the background,else our services can't be accessed.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.letv.android.letvsafe",
                                    "com.letv.android.letvsafe.AutobootManageActivity"));
                            startActivity(intent);
                        }
                    })
                    .show();
        } else if (Build.BRAND.equalsIgnoreCase("Honor")) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable AutoStart")
                    .setMessage("Please allow AppName to always run in the background,else our services can't be accessed.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.huawei.systemmanager",
                                    "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                            startActivity(intent);
                        }
                    })
                    .show();
        } else if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable AutoStart")
                    .setMessage("Please allow AppName to always run in the background,else our services can't be accessed.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                Intent intent = new Intent();
                                intent.setClassName("com.coloros.safecenter",
                                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                                startActivity(intent);
                            } catch (Exception e) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setClassName("com.oppo.safe",
                                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    try {
                                        Intent intent = new Intent();
                                        intent.setClassName("com.coloros.safecenter",
                                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                                        startActivity(intent);
                                    } catch (Exception exx) {

                                    }
                                }
                            }
                        }
                    })
                    .show();
        } else if (Build.MANUFACTURER.contains("vivo")) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable AutoStart")
                    .setMessage("Please allow AppName to always run in the background,else our services can't be accessed.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.iqoo.secure",
                                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                                startActivity(intent);
                            } catch (Exception e) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    try {
                                        Intent intent = new Intent();
                                        intent.setClassName("com.iqoo.secure",
                                                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                                        startActivity(intent);
                                    } catch (Exception exx) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    })
                    .show();
        }
    }

}
