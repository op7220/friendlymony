package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.nect.friendlymony.Adapter.IntroSliderAdapter;
import com.nect.friendlymony.Adapter.PagerImageAdapter;
import com.nect.friendlymony.Fragments.pager.CrushYouFragment;
import com.nect.friendlymony.Fragments.pager.LikeYouFragment;
import com.nect.friendlymony.Model.Feeddetail.FeedDetailResponse;
import com.nect.friendlymony.Model.Feeddetail.ImagesItem;
import com.nect.friendlymony.Model.Feeddetail.UserData;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.HawkAppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseAppCompatActivity {
    String fId;
    UserData dataFeed = new UserData();
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.pagerImage)
    ViewPager pagerImage;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvBirth)
    TextView tvBirth;
    @BindView(R.id.tvMiles)
    TextView tvMiles;
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    @BindView(R.id.tvSmoke)
    TextView tvSmoke;
    @BindView(R.id.tvDrink)
    TextView tvDrink;
    @BindView(R.id.tvPolitics)
    TextView tvPolitics;
    @BindView(R.id.tvEmploy)
    TextView tvEmploy;
    @BindView(R.id.tvEarn)
    TextView tvEarn;
    @BindView(R.id.tvHobby)
    TextView tvHobby;
    @BindView(R.id.rlDetail)
    RelativeLayout rlDetail;
    @BindView(R.id.tvMatch)
    TextView tvMatch;
    @BindView(R.id.tab_viewpager)
    ViewPager tabViewpager;
    @BindView(R.id.llDots)
    LinearLayout llDots;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    private List<ImagesItem> listImages = new ArrayList<>();
    private PagerImageAdapter myViewPagerAdapter;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        fId = getIntent().getStringExtra("fId");

        //setDetail();


        IntroSliderAdapter sliderAdapter = new IntroSliderAdapter(getSupportFragmentManager());
        sliderAdapter.addFragment(new LikeYouFragment(), "");
        sliderAdapter.addFragment(new CrushYouFragment(), "");


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


    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
    }

    private void getDetail() {

        if (!isConnectedToInternet()) {
            return;
        }

        showProgress();
        String uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "";
        final Map<String, Object> request = new HashMap<>();
        request.put("id", fId);

        AppUtils.showLog(ProfileActivity.this, "feeddetail : PARAMETER==" + request.toString());
        Call<FeedDetailResponse> call = RetrofitBuilder.getInstance().getRetrofit().feeddetail(request);
        call.enqueue(new Callback<FeedDetailResponse>() {
            @Override
            public void onResponse(Call<FeedDetailResponse> call, Response<FeedDetailResponse> response) {
                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getApplicationContext(), "Response feeddetail:" + jsonObj.toString());

                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            dataFeed = response.body().getUserData();
                            setDetail();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(getApplicationContext(), "JSONException feeddetail:" + e.getMessage());
                }
            }


            @Override
            public void onFailure(Call<FeedDetailResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(getApplicationContext(), "onFailure feeddetail:" + t.getMessage());
            }
        });
    }

    private void setDetail() {
        tvLocation.setText("Age : " + dataFeed.getVAge() + "");
        tvName.setText(dataFeed.getFirst() + " " + dataFeed.getLast());
        tvMiles.setText(dataFeed.getKm() + " Km away");
        tvEducation.setText("Education qualification : " + dataFeed.getIsQualification());
        tvSmoke.setText("Smoking : " + dataFeed.getIsSmoke());
        tvDrink.setText("Drinking : " + dataFeed.getIsDrink());
        tvPolitics.setText("Discuss politics : " + dataFeed.getIsPolitics());
        tvEmploy.setText("Employment : " + dataFeed.getIsEmployee());
        tvEarn.setText(getResources().getString(R.string.q6_me) + " : " + dataFeed.getIsEarn());
        tvHobby.setText("" + dataFeed.getVAbout());
        tvMatch.setText(dataFeed.getMatchPercentage() + "% Match !");

        listImages.clear();
        listImages.addAll(dataFeed.getImages());
        myViewPagerAdapter = new PagerImageAdapter(this, listImages, 1, 1);
        pagerImage.setAdapter(myViewPagerAdapter);

        ivImage.setVisibility((listImages.size() <= 0) ? View.VISIBLE : View.GONE);

        try {

            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");

            Date myDate = fromFormat.parse(dataFeed.getVBirthdate());

            String finalDate = toFormat.format(myDate);

            tvBirth.setText(finalDate);

        } catch (Exception e) {
            tvBirth.setText(dataFeed.getVBirthdate());

        }


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[2];


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

    private void showEditDialog() {
        PopupMenu popupMenu = new PopupMenu(this, ivEdit);
        popupMenu.getMenuInflater().inflate(R.menu.menu_edit, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_edit:
                        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                        startActivity(intent);

                        break;


                }
                return false;
            }
        });
        popupMenu.show();
    }


    @OnClick({R.id.ivLeft, R.id.ivRight, R.id.ivBack, R.id.ivEdit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivLeft:
                if (listImages.isEmpty()) {
                    return;
                }
                int position = pagerImage.getCurrentItem();
                int Currentitem = (listImages.size() - 1);
                if (position != 0) {
                    Currentitem = position - 1;
                }

                pagerImage.setCurrentItem(Currentitem);
                break;
            case R.id.ivRight:
                if (listImages.isEmpty()) {
                    return;
                }
                int positionP = pagerImage.getCurrentItem();
                int CurrentitemP = 0;
                if (positionP < (listImages.size() - 1)) {
                    CurrentitemP = positionP + 1;
                }
                Log.e(">CP>", CurrentitemP + "<");
                pagerImage.setCurrentItem(CurrentitemP);

                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivEdit:
                showEditDialog();
                break;
        }
    }
}
