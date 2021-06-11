package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nect.friendlymony.Adapter.LikedUserAdapter;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Crused.CrushesResponse;
import com.nect.friendlymony.Model.Crused.DataItem;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.GridSpacingItemDecoration;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLikesActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNo)
    TextView tvNo;
    @BindView(R.id.rvUser)
    RecyclerView rvUser;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    String from = "Crush";
    List<DataItem> listData = new ArrayList<>();
    LikedUserAdapter likedUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_likes);
        ButterKnife.bind(this);
        setToolbar(toolbar, "", true);

        //CalligraphyUtils.applyFontToTextView(tvTitle,fontExtraBoldMuli);
        from = getIntent().getStringExtra("from");
        if (from.equalsIgnoreCase("Crush")) {
            tvTitle.setText("Crushes sent for you");
        } else if (from.equalsIgnoreCase("Interested")) {
            tvTitle.setText("Who liked my profile");
        }


        AppUtils.showLog(getApplicationContext(), "from Status==" + from);
        likedUserAdapter = new LikedUserAdapter(this, listData, from);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvUser.setLayoutManager(layoutManager);
        rvUser.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(5), true));

        rvUser.setAdapter(likedUserAdapter);

        getUsers();
        listeners();
    }

    private void listeners() {
        rvUser.addOnItemTouchListener(new RecyclerTouchListener(this, rvUser, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LinearLayout llContain = view.findViewById(R.id.llContain);
                ImageView ivLike = view.findViewById(R.id.ivLike);
                ImageView ivClose = view.findViewById(R.id.ivClose);
                llContain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(MyLikesActivity.this, FeedDetailActivity.class);
                        intent.putExtra("fId", listData.get(position).getId() + "");
                        intent.putExtra("Status", from + "");
                        intent.putExtra("From", "MyLikesActivity");
                        startActivity(intent);


                    }
                });
                ivLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        acceptDeclineRequest("1", listData.get(position).getId() + "");

                    }
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        acceptDeclineRequest("0", listData.get(position).getId() + "");

                    }
                });


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getUsers() {

        if (!isConnectedToInternet()) {
            return;
        }


        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("userStatus", from);


        Call<CrushesResponse> call = RetrofitBuilder.getInstance().getRetrofit().userLikeDislikeSuperlike(request);
        call.enqueue(new Callback<CrushesResponse>() {
            @Override
            public void onResponse(Call<CrushesResponse> call, Response<CrushesResponse> response) {

                hideProgress();
                if (response.isSuccessful()) {

                    listData.clear();
                    if (response.body().isSuccess()) {

                        listData.addAll(response.body().getData());
                        likedUserAdapter.notifyDataSetChanged();

                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                tvNo.setVisibility((listData.size() <= 0) ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onFailure(Call<CrushesResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
            }
        });
    }

    private void acceptDeclineRequest(String isStatus, String fid) {

        if (!isConnectedToInternet()) {
            return;
        }


        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("user_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("userFrom", fid);
        request.put("is_status", isStatus);
        request.put("user_status", from);


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().usermatch(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                hideProgress();
                if (response.isSuccessful()) {
                    showToast(response.body().getMessage());
                    getUsers();
                    if (response.body().isSuccess()) {


                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                hideProgress();
            }
        });
    }
}
