package com.nect.friendlymony.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.nect.friendlymony.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvWallet)
    TextView tvWallet;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvTrn)
    TextView tvTrn;
    @BindView(R.id.rvWallet)
    RecyclerView rvWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        setToolbar(toolbar, "", true);

        //CalligraphyUtils.applyFontToTextView(tvWallet, fontExtraBoldMuli);
    }
}
