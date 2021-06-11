package com.nect.friendlymony.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.interfaces.OnvalueChangeListener;
import com.nect.friendlymony.Quickblox.util.QBResRequestExecutor;
import com.nect.friendlymony.Quickblox.utils.SharedPrefsHelper;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.App;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.nect.friendlymony.Utils.ProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class BaseAppCompatActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int REQUEST_CHECK_SETTINGS = 4;
    private static final int REQUEST_PERMISSIONS_CODE_LOCATION = 222;
    public ProgressDialog mProgressDialog;
    public Context mContext;
    public onDialogClickListener onDialogClickListener;
    public QBResRequestExecutor requestExecutor;
    public SharedPrefsHelper sharedPrefsHelper;
    boolean ISLOCATIONFETCH = false;
    Typeface fontExtraBoldMuli;
    Typeface fontSemiBoldMuli;
    OnvalueChangeListener listener;
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mContext = this;
        fontExtraBoldMuli = Typeface.createFromAsset(getAssets(), "font/Muli-ExtraBold.ttf");
        fontSemiBoldMuli = Typeface.createFromAsset(getAssets(), "font/Muli-SemiBold.ttf");

        requestExecutor = App.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();


        if (!AppUtils.CheckLocationPermission(this)) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            // listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSIONS_CODE_LOCATION);

        } else {
            //initGoogle();

        }

    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;
    }

    public void initGoogle() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        // Create the LocationRequest object
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(2000);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000);

    }

    protected void showProgress() {

        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /**
     * Hide progress dialog.
     */
    protected void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.cancel();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /**
     * set Toolbar
     *
     * @param toolbar      toolbar object defined in view.
     * @param title        toolbar title
     * @param isbackenable set toolbar ic_back_main button is enable or not.
     */
    public void setToolbar(Toolbar toolbar, String title, boolean isbackenable) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (isbackenable) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * Popup message and after short time this popup will automatically destroy.
     *
     * @param message Message for display in popup.
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Hide keyboard
     */
    protected void hideKeyboard() {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(viewGroup.getWindowToken(), 0);
    }

    /**
     * Open Keyboard.
     */
    public void showKeyboard() {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInputFromWindow(viewGroup.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    public void showOKAlert(String messageString, onDialogClickListener onDialogClickListener1) {
        this.onDialogClickListener = onDialogClickListener1;
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseAppCompatActivity.this);
        builder.setMessage(messageString);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onDialogClickListener.onPositive();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showPositiveNegativeAlert(String messageString, int yes, int no, onDialogClickListener onDialogClickListener1) {
        onDialogClickListener = onDialogClickListener1;
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseAppCompatActivity.this);
        builder.setMessage(messageString);
        builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onDialogClickListener.onPositive();
            }
        });
        builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onDialogClickListener.onNegative();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) BaseAppCompatActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        } else {
            showToast(getResources().getString(R.string.msg_no_internet));
            return false;
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        ISLOCATIONFETCH = false;
        // Log.e("CALLED", "CALLEd");
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this); // This is the changed line.
        } else {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        initGoogle();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        initGoogle();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!ISLOCATIONFETCH) {
            ISLOCATIONFETCH = true;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

            Pref.setStringValue(this, Constants.PREF_LATITUDE, location.getLatitude() + "");
            Pref.setStringValue(this, Constants.PREF_LONGITUDE, location.getLongitude() + "");
            String city = AppUtils.getCity(BaseAppCompatActivity.this, location.getLatitude(), location.getLongitude());
            Pref.setStringValue(this, Constants.PREF_CITY, city + "");

            /*if (HawkAppUtils.getInstance().getIsLogin()) {
                setMyLocation();
            }*/
            if (listener != null) {
                listener.onLocationChange();
                listener = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setMyLocation() {
        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("user_id", lp.getData().getId());
        request.put("lat", Pref.getStringValue(this, Constants.PREF_LATITUDE, "0.0"));
        request.put("lang", Pref.getStringValue(this, Constants.PREF_LONGITUDE, "0.0"));


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLocation(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {


                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
            }
        });
    }

    public void onValueChange(OnvalueChangeListener listener) {
        this.listener = listener;
    }


    interface onDialogClickListener {
        void onPositive();

        void onNegative();
    }

}
