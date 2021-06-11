package com.nect.friendlymony.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.nect.friendlymony.Adapter.ReportListAdapter;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.ReportUser.DataReportUserReasons;
import com.nect.friendlymony.Model.ReportUser.ResponseReportUserReasons;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportUserActivity extends BaseAppCompatActivity implements View.OnClickListener {

    ImageView ivClose;
    CircleImageView civUserPic;
    TextView tvUserName;
    Spinner spReason;
    EditText etWriteSomething;
    Button btSubmit;
    RecyclerView rvReasonList;
    String reportReasonId = "", reportReasonName = "", userImage = "", userName = "", reportUserId = "";
    //String[] spinnerItem = {"A1", "A2", "A3", "A4", "A5"};

    List<DataReportUserReasons> reasonsList;
    ReportListAdapter reportListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        reasonsList = new ArrayList<>();
        ivClose = findViewById(R.id.aru_ivClose);
        civUserPic = findViewById(R.id.aru_civUserPic);
        tvUserName = findViewById(R.id.aru_tvUserName);
        spReason = findViewById(R.id.aru_spReason);
        etWriteSomething = findViewById(R.id.aru_etWriteSomething);
        btSubmit = findViewById(R.id.aru_btSubmit);
        rvReasonList = findViewById(R.id.aru_rvReasonList);

        btSubmit.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        userName = getIntent().getStringExtra("userName");
        userImage = getIntent().getStringExtra("userImage");
        reportUserId = getIntent().getStringExtra("opponentUserId");

        callGetReportReasonsApi();

        if (userImage != null && !userImage.isEmpty()) {
            Glide.with(mContext).load(userImage)
                    .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .centerCrop())
                    .into(civUserPic);
        } else {
            Glide.with(mContext).load(R.drawable.com_facebook_profile_picture_blank_square)
                    .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .centerCrop())
                    .into(civUserPic);
        }
        tvUserName.setText(userName);
    }

    private void callGetReportReasonsApi() {
        Pref.setStringValue(this, Constants.CURRENT_BASE_URL, "2");
        Call<ResponseReportUserReasons> call = RetrofitBuilder.getInstance().getRetrofit().GetReportReasons();
        call.enqueue(new Callback<ResponseReportUserReasons>() {
            @Override
            public void onResponse(Call<ResponseReportUserReasons> call, Response<ResponseReportUserReasons> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(getApplicationContext(), "Response GetReportReasons:" + jsonObj.toString());

                        reasonsList = new ArrayList<>();
                        /*DataReportUserReasons dataReportUserReasons = new DataReportUserReasons();
                        dataReportUserReasons.setName(getString(R.string.lbl_select_report_reasons));
                        dataReportUserReasons.setId("-1");
                        reasonsList.add(0, dataReportUserReasons);*/
                        reasonsList.addAll(response.body().getData());

                        reportListAdapter = new ReportListAdapter(ReportUserActivity.this, reasonsList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReportUserActivity.this);
                        rvReasonList.setLayoutManager(layoutManager);
                        rvReasonList.setAdapter(reportListAdapter);

                        reportListAdapter.setOnItemClickListener(new ReportListAdapter.MyClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                for (int i = 0; i < reasonsList.size(); i++) {
                                    if (i == position) {
                                        reasonsList.get(i).setChecked(true);
                                        reportReasonId = reasonsList.get(position).getId();
                                        reportReasonName = reasonsList.get(position).getName();
                                    } else {
                                        reasonsList.get(i).setChecked(false);
                                    }

                                    if (i == reasonsList.size() - 1) {
                                        reportListAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                        });
                        /*List<String> list = new ArrayList<>();

                        for (int i = 0; i < reasonsList.size(); i++) {
                            list.add(reasonsList.get(i).getName());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ReportUserActivity.this, android.R.layout.simple_spinner_item, list);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spReason.setAdapter(arrayAdapter);
                        spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                reportReasonId = reasonsList.get(position).getId();
                                AppUtils.showLog(ReportUserActivity.this, "onItemSelected==reportReasonId==" + reportReasonId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ReportUserActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                }

                Pref.setStringValue(ReportUserActivity.this, Constants.CURRENT_BASE_URL, "1");
            }

            @Override
            public void onFailure(Call<ResponseReportUserReasons> call, Throwable t) {
                Log.e("tt", t + "<<");
                Pref.setStringValue(ReportUserActivity.this, Constants.CURRENT_BASE_URL, "1");
                AppUtils.showLog(getApplicationContext(), "onFailure GetReportReasons:" + t.getMessage());
                Toast.makeText(ReportUserActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == ivClose) {
            finish();
        } else if (view == btSubmit) {

            /*if (reportReasonId.isEmpty()) {
                reportReasonId = reasonsList.get(0).getId();
            }*/

            if (reportReasonId.isEmpty()) {
                Toast.makeText(ReportUserActivity.this, getString(R.string.lbl_select_report_reasons), Toast.LENGTH_SHORT).show();
            } else if (reportReasonName.equalsIgnoreCase("Other") || reportReasonName.equalsIgnoreCase("Others") || reportReasonName.equalsIgnoreCase("other")) {
                etWriteSomething.requestFocus();
                if (etWriteSomething.getText().toString().isEmpty()) {
                    Toast.makeText(ReportUserActivity.this, getString(R.string.msg_select_report_reasons), Toast.LENGTH_SHORT).show();
                } else {
                    callReportUserAPI();
                }
            } else {
                callReportUserAPI();
            }
        }
    }

    private void callReportUserAPI() {

        Pref.setStringValue(this, Constants.CURRENT_BASE_URL, "2");
        showProgress();
        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
        final Map<String, Object> request = new HashMap<>();
        request.put("userId", lp.getData().getId());
        request.put("reportUserId", reportUserId);
        request.put("reportTypeId", reportReasonId);
        request.put("message", etWriteSomething.getText().toString().trim());

        AppUtils.showLog(ReportUserActivity.this, "getReportUser : PARAMETER==" + request.toString());
        Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().getReportUser(request);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(getApplicationContext(), "Response getReportUser:" + jsonObj.toString());

                        /*if (response.body().getSuccess()) {
                            Toast.makeText(ReportUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ReportUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }*/

                        showAlertDialog(response.body().getMessage());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ReportUserActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                }

                hideProgress();
                Pref.setStringValue(ReportUserActivity.this, Constants.CURRENT_BASE_URL, "1");
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                hideProgress();
                Log.e("tt", t + "<<");
                Pref.setStringValue(ReportUserActivity.this, Constants.CURRENT_BASE_URL, "1");
                AppUtils.showLog(getApplicationContext(), "onFailure GetReportReasons:" + t.getMessage());
                Toast.makeText(ReportUserActivity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ReportUserActivity.this, MainActivity.class);
                        intent.putExtra("from", 1);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .show();
    }
}
