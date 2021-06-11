package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nect.friendlymony.Adapter.IntroSliderAdapter;
import com.nect.friendlymony.Fragments.intro.Intro_fragment_1;
import com.nect.friendlymony.Fragments.intro.Intro_fragment_2;
import com.nect.friendlymony.Fragments.intro.Intro_fragment_3;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.Pref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tab_viewpager)
    ViewPager tabViewpager;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnTerms)
    Button btnTerms;
    @BindView(R.id.llDots)
    LinearLayout llDots;

    TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);


        IntroSliderAdapter sliderAdapter = new IntroSliderAdapter(getSupportFragmentManager());
        sliderAdapter.addFragment(new Intro_fragment_1(), "");
        sliderAdapter.addFragment(new Intro_fragment_2(), "");
        sliderAdapter.addFragment(new Intro_fragment_3(), "");


        tabViewpager.setAdapter(sliderAdapter);
        addBottomDots(0);

        tabViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addBottomDots(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d("IntroActivity", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    Pref.setStringValue(getBaseContext(), PREF_FCM_TOKEN, token);
                    // Log and toast
                    Log.d("IntroActivity", token);
                });
    }

    @OnClick({R.id.btnLogin, R.id.btnTerms, R.id.btnPrivacy})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnLogin:
                Pref.setIntValue(this, Constants.PREF_IS_INTRO, 1);
                intent = new Intent(this, LoginOptionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnTerms:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(AppUtils.URL_TERMS));
                startActivity(intent);
                break;
            case R.id.btnPrivacy:

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(AppUtils.URL_PRIVACY));
                startActivity(intent);
                break;
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[3];


        llDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.grey_300));
            llDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorPrimary));
    }

}
