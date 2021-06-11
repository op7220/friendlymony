package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.nect.friendlymony.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowActivity extends BaseAppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llFb)
    LinearLayout llFb;
    @BindView(R.id.llTwitter)
    LinearLayout llTwitter;
    @BindView(R.id.llInsta)
    LinearLayout llInsta;
    @BindView(R.id.llLinked)
    LinearLayout llLinked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);

        setToolbar(toolbar, "", true);

        //CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.llFb, R.id.llTwitter, R.id.llInsta, R.id.llLinked})
    public void onViewClicked(View view) {
        String msgShare;
        Intent sendIntent;
        switch (view.getId()) {
            case R.id.llFb:

                msgShare = "https://www.facebook.com/friendlymony1/";
                sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(msgShare));
                startActivity(sendIntent);

                break;
            case R.id.llTwitter:
                msgShare = "https://twitter.com/FriendlyMony";
                sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(msgShare));
                startActivity(sendIntent);
                break;
            case R.id.llInsta:
                msgShare = "https://www.instagram.com/friendlymony1/";
                sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(msgShare));
                startActivity(sendIntent);

                break;
            case R.id.llLinked:
                msgShare = "https://www.linkedin.com/company/friendlymony/";
                sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(msgShare));
                startActivity(sendIntent);
                break;
        }
    }
}
