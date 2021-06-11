package com.nect.friendlymony.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.nect.friendlymony.BuildConfig;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReferralsActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnCode)
    Button btnCode;
    @BindView(R.id.btnShare)
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);
        ButterKnife.bind(this);
        setToolbar(toolbar, "", true);

        //CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);

        btnCode.setText(HawkAppUtils.getInstance().getUSERDATA().getData().getUserUniqueId() + "");
    }

    @OnClick({R.id.btnCode, R.id.btnShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCode:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", btnCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                showToast("Copied");
                break;
            case R.id.btnShare:

                String shareD = "Join me on FriendlyMony,   social networking app for free chat and video call. Install the App and enter my code " +
                        HawkAppUtils.getInstance().getUSERDATA().getData().getUserUniqueId() + " and earn additional  7 days free extented usage! \n" +
                        "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

                //String shareBody = HawkAppUtils.getInstance().getUSERDATA().getData().getUser_unique_id() + "";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FriendlyMony");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareD);
                startActivity(Intent.createChooser(sharingIntent, "Share"));
                break;
        }
    }
}
