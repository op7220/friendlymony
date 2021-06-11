package com.nect.friendlymony.Fragments.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nect.friendlymony.Activity.BlockUserActivity;
import com.nect.friendlymony.Activity.ChatActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Adapter.ConversationAdapter;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Conversation.ConversationListResponse;
import com.nect.friendlymony.Model.Conversation.DataItem;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.nect.friendlymony.Utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Activity.MainActivity.mainActivity;

public class ChatFragment extends BaseFragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    List<DataItem> listData = new ArrayList<>();
    ConversationAdapter conversationAdapter;

    View view;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvNo)
    TextView tvNo;
    @BindView(R.id.rvUser)
    RecyclerView rvUser;
    Runnable updater;

    boolean isFirstTimeUpdate = true;
    Handler timerHandler;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);
        //CalligraphyUtils.applyFontToTextView(tvTitle, fontExtraBoldMuli);

        conversationAdapter = new ConversationAdapter(getActivity(), listData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvUser.setLayoutManager(layoutManager);
        rvUser.setAdapter(conversationAdapter);


        listeners();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Chat==onResume==");
        Pref.setStringValue(getContext(), Constants.IS_CHAT_SCREEN, "0");
        isFirstTimeUpdate = true;
        timerHandler = new Handler();
        getUsers();
        callGetUserOnlineStatus();
    }

    private void listeners() {
        rvUser.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvUser, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LinearLayout llContain = view.findViewById(R.id.llContain);

                llContain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        checkSubscribtion(position);


                    }
                });


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void checkSubscribtion(int position) {

        if (!isConnectedToInternet()) {
            return;
        }


        showProgress();

        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().chatSubscriptionValidate();
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response chatSubscriptionValidate: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {


                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        //String IUrl = AppUtils.IMAGE_BASE_URL + listData.get(position).getPhoto();


                        String rId = listData.get(position).getSender() + "";

                        if (rId.equals(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {
                            rId = listData.get(position).getRecipient() + "";
                        }

                        String IUrl;
                        if (String.valueOf(listData.get(position).getSender()).equalsIgnoreCase(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {
                            IUrl = listData.get(position).getImage_receiver();

                        } else {
                            IUrl = listData.get(position).getImage_sender();
                        }

                        //
                        // String rToken = "fC5pWTabLO4:APA91bEOkR6zK66AbQJFXzqDuyr4WqTKf5jL_9Hr5hY1lv22_cDOQygyDY3TDr5Qy4gpLEpYEfQ8ox93YQm-DXoUN3kLdGpqeU-6MiiM6RSVNmb37RUigm6peRPYr2zYms6SV5xnfooJ";

                        String rToken;
                        if (HawkAppUtils.getInstance().getUSERDATA().getData().getId().equalsIgnoreCase(String.valueOf(listData.get(position).getSender()))) {
                            rToken = listData.get(position).getReceiver_firebase_token();
                        } else {
                            rToken = listData.get(position).getSender_firebase_token();
                        }


                        /*if (rToken.equalsIgnoreCase("")) {
                            rToken = Pref.getStringValue(getContext(), PREF_FCM_TOKEN, "");
                        }*/
                        intent.putExtra("conversationID", listData.get(position).getId() + "");
                        intent.putExtra("rName", listData.get(position).getWholename() + "");
                        intent.putExtra("rPhoto", IUrl + "");
                        intent.putExtra("rToken", rToken + "");
                        intent.putExtra("rId", rId + "");
                        intent.putExtra("from", "ChatList");
                        startActivityForResult(intent, 101);
                        AppUtils.showLog(getContext(), "rName==" + listData.get(position).getWholename());

                    } else {
                        showToast("You can not chat. Please purchase subscription");
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
                AppUtils.showLog(getContext(), "onFailure chatSubscriptionValidate: " + t.getMessage());
            }
        });
    }

    private void initUsersList() {
        //    List<QBUser> currentOpponentsList = dbManager.getAllUsers();
        //  currentOpponentsList.remove(sharedPrefsHelper.getQbUser());

    }

    private void getUsers() {

        if (!isConnectedToInternet()) {
            return;
        }


        if (isFirstTimeUpdate) {
            showProgress();
        }
        final Map<String, Object> request = new HashMap<>();

        //AppUtils.showLog(getContext(), "MatchResult : PARAMETER==" + request.toString());
        Call<ConversationListResponse> call = RetrofitBuilder.getInstance().getRetrofit().getConversation();
        call.enqueue(new Callback<ConversationListResponse>() {
            @Override
            public void onResponse(Call<ConversationListResponse> call, Response<ConversationListResponse> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(getContext(), "Response getConversation: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                if (response.isSuccessful()) {

                    listData.clear();
                    if (response.body().isSuccess()) {

                        if (response.body().getData() != null) {
                            listData.addAll(response.body().getData());
                            conversationAdapter.notifyDataSetChanged();
                            if (isFirstTimeUpdate) {
                                isFirstTimeUpdate = false;
                                updateTime();
                            }
                        }

                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                tvNo.setVisibility((listData.size() <= 0) ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onFailure(Call<ConversationListResponse> call, Throwable t) {
                Log.e("tt", t + "<<");
                AppUtils.showLog(getContext(), "onFailure getConversation: " + t.getMessage());
                hideProgress();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirstTimeUpdate = false;
        timerHandler.removeCallbacks(updater);
        AppUtils.showLog(mainActivity, "Chat : onPause==");
    }


    private void callGetUserOnlineStatus() {
        final Map<String, Object> request = new HashMap<>();
        if (HawkAppUtils.getInstance().getUSERDATA() != null) {
            request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());

            AppUtils.showLog(mainActivity.getApplicationContext(), "getUserOnlineStatus : PARAMETER==" + request.toString());
            Call<ResponseGetOnlineUserStatus> call = RetrofitBuilder.getInstance().getRetrofit().getUserOnlineStatus(request);
            call.enqueue(new Callback<ResponseGetOnlineUserStatus>() {
                @Override
                public void onResponse(Call<ResponseGetOnlineUserStatus> call, Response<ResponseGetOnlineUserStatus> response) {
                    JSONObject jsonObj = null;
                    hideProgress();
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(getActivity(), "Response getUserOnlineStatus: " + jsonObj.toString());

                        if (response.isSuccessful()) {
                            if (response.body().getData() != null) {
                                if (response.body().getData().getIsActive() != null) {
                                    if (response.body().getData().getIsActive() == 0) {
                                        startActivity(new Intent(getActivity(), BlockUserActivity.class));
                                        getActivity().finishAffinity();
                                    }
                                }

                                if (response.body().getData().getPlanId() != null) {
                                    Pref.setStringValue(mainActivity, Constants.PLAN_ID, response.body().getData().getPlanId());
                                } else {
                                    Pref.setStringValue(mainActivity, Constants.PLAN_ID, "");
                                }

                                if (response.body().getData().getIs_free_trial() != null) {
                                    Pref.setStringValue(mainActivity, Constants.IS_FREE_TRAIL, response.body().getData().getIs_free_trial());
                                } else {
                                    Pref.setStringValue(mainActivity, Constants.IS_FREE_TRAIL, "");
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //showToast(getString(R.string.msg_something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetOnlineUserStatus> call, Throwable t) {
                    hideProgress();
                    //showToast(getString(R.string.msg_something_wrong));
                    AppUtils.showLog(mainActivity, "onFailure getUpdateUserOnlineStatus:" + t.getMessage());
                }
            });
        }

    }

    private void updateTime() {
        updater = () -> {
            getUsers();
            timerHandler.postDelayed(updater, 5000);
        };
        timerHandler.post(updater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == 100) {
            String conversation_id = data.getExtras().getString("conversation_id");
            String sender_id = data.getExtras().getString("sender_id");
            String recipient_id = data.getExtras().getString("recipient_id");

            callReadMessageAPI(conversation_id, sender_id, recipient_id);

        }
    }

    private void callReadMessageAPI(String conversation_id, String sender_id, String recipient_id) {
        final Map<String, Object> request = new HashMap<>();
        request.put("conversation_id", conversation_id);
        request.put("sender_id", sender_id);
        request.put("recipient_id", recipient_id);
        Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().readConversation(request);

        AppUtils.showLog(mainActivity, "readConversation : PARAMETER==" + request.toString());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(mainActivity, "Response readConversation: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppUtils.showLog(mainActivity, "readConversation : JSONException==" + e.getMessage());
                    }
                } else {
                    AppUtils.showLog(mainActivity, "readConversation : error message==" + response.message());
                }
            }


            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                AppUtils.showLog(mainActivity, "readConversation : JSONException==" + t.getMessage());
            }
        });
    }
}
