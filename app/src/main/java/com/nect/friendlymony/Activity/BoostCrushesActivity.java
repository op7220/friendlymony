package com.nect.friendlymony.Activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nect.friendlymony.Adapter.IntroSliderAdapter;
import com.nect.friendlymony.Fragments.BuyPager.BoostBuyFragment;
import com.nect.friendlymony.Fragments.BuyPager.CrushBuyFragment;
import com.nect.friendlymony.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoostCrushesActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_tabs)
    TabLayout tabTabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    String type = "";
    int qty;
    String qtyMsg;
    double payAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_crushes);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Boosts & Crushes", true);

        init();
    }

    private void init() {
        IntroSliderAdapter sliderAdapter = new IntroSliderAdapter(getSupportFragmentManager());
        sliderAdapter.addFragment(new BoostBuyFragment(), "Boosts");
        sliderAdapter.addFragment(new CrushBuyFragment(), "Crushes");


        viewpager.setAdapter(sliderAdapter);
        tabTabs.setupWithViewPager(viewpager);
    }


}
