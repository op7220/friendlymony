package com.nect.friendlymony.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.nect.friendlymony.R;


/**
 * Created by nectabits on 1/16/2018.
 * Display loading indicator.
 */

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        setCancelable(false);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

}

