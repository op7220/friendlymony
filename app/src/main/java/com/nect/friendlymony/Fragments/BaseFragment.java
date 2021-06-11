package com.nect.friendlymony.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.ProgressDialog;

public class BaseFragment extends Fragment {
    public ProgressDialog mProgressDialog;

    public Typeface fontSemiBold,fontExtraBoldMuli;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());
        fontSemiBold = Typeface.createFromAsset(getActivity().getAssets(), "font/Muli-SemiBold.ttf");
        fontExtraBoldMuli = Typeface.createFromAsset(getActivity().getAssets(), "font/Muli-ExtraBold.ttf");

    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
