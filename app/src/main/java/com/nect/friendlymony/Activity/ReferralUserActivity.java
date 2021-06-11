package com.nect.friendlymony.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nect.friendlymony.Adapter.ReferralUserAdapter;
import com.nect.friendlymony.Model.Referral.DataItem;
import com.nect.friendlymony.Model.Referral.ReferralUserResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralUserActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNo)
    TextView tvNo;
    @BindView(R.id.rvUser)
    RecyclerView rvUser;

    List<DataItem> listData = new ArrayList<>();
    private ReferralUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_user);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Referrals users", true);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUser.setLayoutManager(layoutManager);
        adapter = new ReferralUserAdapter(this, listData);
        rvUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvUser.setAdapter(adapter);

        if (!isConnectedToInternet()) {
            return;
        }

        getUsers();
    }

    private void getUsers() {
        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("user_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());


        Call<ReferralUserResponse> call = RetrofitBuilder.getInstance().getRetrofit().getReferalUserData(request);
        call.enqueue(new Callback<ReferralUserResponse>() {
            @Override
            public void onResponse(Call<ReferralUserResponse> call, Response<ReferralUserResponse> response) {

                hideProgress();
                if (response.isSuccessful()) {

                    listData.clear();
                    if (response.body().isSuccess()) {

                        listData.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();

                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                tvNo.setVisibility((listData.size() <= 0) ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onFailure(Call<ReferralUserResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
            }
        });
    }
}
