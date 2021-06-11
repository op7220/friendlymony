package com.nect.friendlymony.Activity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Block.BlockResponse;
import com.nect.friendlymony.Model.Chat.ChatModel;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Login.Data;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.Login.UserResponse;
import com.nect.friendlymony.Quickblox.activities.CallActivity;
import com.nect.friendlymony.Quickblox.activities.PermissionsActivity;
import com.nect.friendlymony.Quickblox.services.CallService;
import com.nect.friendlymony.Quickblox.services.LoginService;
import com.nect.friendlymony.Quickblox.utils.Consts;
import com.nect.friendlymony.Quickblox.utils.PermissionsChecker;
import com.nect.friendlymony.Quickblox.utils.PushNotificationSender;
import com.nect.friendlymony.Quickblox.utils.SharedPrefsHelper;
import com.nect.friendlymony.Quickblox.utils.ToastUtils;
import com.nect.friendlymony.Quickblox.utils.WebRtcSessionManager;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Service.UploadService;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;
import com.nect.friendlymony.Utils.RecyclerTouchListener;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nect.friendlymony.Activity.MainActivity.mainActivity;

public class ChatActivity extends BaseAppCompatActivity {

    private static final int CAMERA_REQUEST = 101;
    private static final int GALLRY_REQUEST = 201;
    private static final int SHARE_LOCATION = 301;
    private static final int MY_PERMISSION_ALL_STORE = 22;
    private static final String TAG = ChatActivity.class.getSimpleName();


    String rName = "";
    String rPhoto = "";
    String rId = "", fromUser = "";
    String rToken = "";

    String blockBy = "";
    int isBlock = 0, isPaid;

    String imgString = "";
    String conversationID = "";

    List<ChatModel> listChat = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference dbRef;
    //ChatAdapter chatAdapter;
    ChatNewAdapter chatNewAdapter;
    //qb
    PermissionsChecker checker;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivProfile)
    CircleImageView ivProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivVideo)
    ImageView ivVideo;
    @BindView(R.id.ivBlock)
    ImageView ivBlock;
    @BindView(R.id.tvBlock)
    TextView tvBlock;
    @BindView(R.id.llBlock)
    LinearLayout llBlock;
    String chooseDeliveryAddress = "";
    String latitude = "", longitude = "";
    boolean isFirstTime = true;
    boolean isSentMessage = false, isShareLocation = false, isNewMessage = false, isScreenActive = true;
    private QBUser currentUser;
    private RecyclerView.RecyclerListener mRecycleListener = holder -> {
        ChatNewAdapter.ViewHolder mapHolder = (ChatNewAdapter.ViewHolder) holder;
        if (mapHolder != null && mapHolder.map != null) {
            // Clear the map and free up resources by changing the map type to none.
            // Also reset the map when it gets reattached to layout, so the previous map would
            // not be displayed.
            mapHolder.map.clear();
            mapHolder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {

        }

        checker = new PermissionsChecker(getApplicationContext());

        conversationID = getIntent().getStringExtra("conversationID");
        rName = getIntent().getStringExtra("rName");
        rPhoto = getIntent().getStringExtra("rPhoto");
        rToken = getIntent().getStringExtra("rToken");
        rId = getIntent().getStringExtra("rId");
        fromUser = getIntent().getStringExtra("from");

        if (conversationID == null) {
            conversationID = getIntent().getStringExtra("conversationId");
        }
        if (rName == null) {
            rName = getIntent().getStringExtra("Title");
        }
        if (rPhoto == null) {
            rPhoto = getIntent().getStringExtra("senderProfile");
        }
        if (rId == null) {
            rId = getIntent().getStringExtra("senderId");
        }
        if (rToken == null) {
            rToken = getIntent().getStringExtra("fcmToken");
        }
        if (fromUser.equalsIgnoreCase("ChatList") || fromUser.equalsIgnoreCase("userProfile")) {
            initChatData();
        } else {
            checkSubscription();
        }

        AppUtils.showLog(ChatActivity.this, "rId==" + rId);
        AppUtils.showLog(this, "rPhoto==" + rPhoto);
    }

    private void checkSubscription() {

        if (!isConnectedToInternet()) {
            return;
        }

        showProgress();
        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().chatSubscriptionValidate();
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                            startPermissionsActivity(false);
                        }
                        initChatData();
                    } else {
                        showToast("You can not chat. Please purchase subscription");
                        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                        intent.putExtra("from", 1);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void initChatData() {
        currentUser = SharedPrefsHelper.getInstance().getQbUser();

        if ((conversationID + "").equalsIgnoreCase("") || (conversationID + "").equalsIgnoreCase("null")) {
            if (getIntent().getExtras() != null) {
                conversationID = getIntent().getStringExtra("conversationId");
                rPhoto = getIntent().getStringExtra("senderProfile");
                rName = getIntent().getStringExtra("senderName");
                rToken = getIntent().getStringExtra("fcmToken");
                AppUtils.showLog(this, "rName==" + rName);
            }
        }


        //CalligraphyUtils.applyFontToTextView(tvName, fontSemiBoldMuli);
        //AppUtils.showLog(this, "rName==" + rName);
        //AppUtils.showLog(ChatActivity.this, "conversationID==" + conversationID);
        //AppUtils.showLog(ChatActivity.this, "PLAN_ID==" + Pref.getStringValue(mainActivity, Constants.PLAN_ID, ""));
        //AppUtils.showLog(ChatActivity.this, "rToken==" + rToken);
        tvName.setText(rName);
        /*Glide.with(mContext).load(rPhoto).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square).into(ivProfile);*/

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        rvChat.setLayoutManager(layoutManager);
        chatNewAdapter = new ChatNewAdapter(listChat);
        rvChat.setAdapter(chatNewAdapter);
        rvChat.setRecyclerListener(mRecycleListener);


        database = FirebaseDatabase.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        //dbRef.child("Conversation").child("123");
        dbRef.child("Conversation").push();
        getData();

        checkBlock();

        listener();


        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            LoginService.start(this, qbUser);
        }

        callReadMessageAPI();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        Pref.setStringValue(this, Constants.IS_CHAT_SCREEN, "1");
    }

    private void callReadMessageAPI() {
        final Map<String, Object> request = new HashMap<>();
        request.put("conversation_id", conversationID);
        request.put("sender_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("recipient_id", rId);
        request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().readConversation(request);

        AppUtils.showLog(ChatActivity.this, "readConversation : PARAMETER==" + request.toString());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (isScreenActive){
                    if (response.isSuccessful()) {


                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(new Gson().toJson(response.body()));
                            AppUtils.showLog(ChatActivity.this, "Response readConversation: " + jsonObj.toString());

                        /*if (rPhoto == null || rPhoto.equalsIgnoreCase("")) {
rPhoto
                        }*/
                            if (response.body() != null && response.body().getReceiverImage() != null) {
                                Glide.with(ChatActivity.this).load(response.body().getReceiverImage()).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                        .error(R.drawable.com_facebook_profile_picture_blank_square).into(ivProfile);
                            } else if (rPhoto != null && !rPhoto.isEmpty()) {
                                Glide.with(ChatActivity.this).load(rPhoto).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                        .error(R.drawable.com_facebook_profile_picture_blank_square).into(ivProfile);
                            } else {
                                Glide.with(ChatActivity.this).load(R.drawable.com_facebook_profile_picture_blank_square)
                                        .error(R.drawable.com_facebook_profile_picture_blank_square).into(ivProfile);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppUtils.showLog(ChatActivity.this, "readConversation : JSONException==" + e.getMessage());
                        }
                    } else {
                        AppUtils.showLog(ChatActivity.this, "readConversation : error message==" + response.message());
                    }
                }

            }


            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                AppUtils.showLog(ChatActivity.this, "readConversation : JSONException==" + t.getMessage());
            }
        });

        if (isFirstTime) {
            checkPaid();
        }
    }

    private void listener() {
        rvChat.addOnItemTouchListener(new RecyclerTouchListener(this, rvChat, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView ivImageR = view.findViewById(R.id.ivImage);

                ivImageR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showFullImageDialog(listChat.get(position).getImageUrl());
                    }
                });
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        callUpdateUserOnlineStatusApi("1");
    }

    private void getData() {
        listChat.clear();
        /*dbRef.child("Conversation").child(conversationID).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ChatModel commandObject = ds.getValue(ChatModel.class);
                        Log.e("data", commandObject.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "er " + e.getMessage());
                }
                Log.e(TAG, "onDataChange Main");

                try {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                    Log.e("data2", ""+chatModel.getMessage());
                } catch (Exception e) {

                    Log.e(TAG, "er2 " + e.getMessage());
                 }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
        dbRef.child("Conversation").child(conversationID).child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.e("onChildAdded", "onChildAdded" + s);
              /*
               for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatModel commandObject = ds.getValue(ChatModel.class);
                    listChat.add(commandObject);
                }

                */

                try {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    listChat.add(chatModel);
                    chatNewAdapter.notifyItemInserted(listChat.size() - 1);

                    if (listChat.size() > 0) {
                        rvChat.scrollToPosition(listChat.size() - 1);
                        /*if (isScreenActive) {
                            callReadMessageAPI();
                        }*/
                        AppUtils.showLog(ChatActivity.this, "onChildAdded==true==");
                    } else {
                        AppUtils.showLog(ChatActivity.this, "onChildAdded==false==");
                    }

                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(Integer.parseInt(conversationID));

                    /*if (!isFirstTime) {
                    }*/
                } catch (Exception e) {
                    chatNewAdapter.notifyDataSetChanged();
                    AppUtils.showLog(ChatActivity.this, "onChildAdded==Exception=="+e.getMessage());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Log.e("onChildChanged", "onChildChanged");

                try {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    //Log.e("data", chatModel.getMessage());
                } catch (Exception e) {
                    AppUtils.showLog(ChatActivity.this, "onChildChanged==Exception=="+e.getMessage());
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                AppUtils.showLog(ChatActivity.this, "onChildRemoved==Exception=="+dataSnapshot.toString());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AppUtils.showLog(ChatActivity.this, "onChildMoved==Exception=="+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppUtils.showLog(ChatActivity.this, "onCancelled==Exception=="+databaseError.getDetails());
            }
        });
    }

    @OnClick({R.id.ivCamera, R.id.ivSend, R.id.ivBack, R.id.ivVideo, R.id.llBlock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivCamera:

                if (!AppUtils.hasStorageCameraPermissions(this, AppUtils.PERMISSIONS_PIC)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(AppUtils.PERMISSIONS_PIC, MY_PERMISSION_ALL_STORE);
                    }
                } else {


                    if (isBlock == 1) {

                        if (blockBy.equals(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {

                            showOKAlert("Please Unblock " + rName + " to continue chat.", new onDialogClickListener() {
                                @Override
                                public void onPositive() {

                                }

                                @Override
                                public void onNegative() {

                                }
                            });
                        } else {

                            showOKAlert("You are blocked by " + rName + ". So you can not send message", new onDialogClickListener() {
                                @Override
                                public void onPositive() {

                                }

                                @Override
                                public void onNegative() {

                                }
                            });
                        }

                    } else {
                        showImageDialog();
                    }
                }
                break;
            case R.id.ivSend:
                isSentMessage = true;
                if (etMessage.getText().toString().isEmpty()) {
                    etMessage.setError("Required");
                } else if (isBlock == 1) {
                    checkBlockUser();
                } else {
                    sendMessage(etMessage.getText().toString().trim(), false);
                }
                break;
            case R.id.ivBack:
                //finish();
                onBackPressed();
                break;
            case R.id.ivVideo:
                if (isBlock == 1) {

                    if (blockBy.equals(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {

                        showOKAlert("Please Unblock " + rName + " to continue chat.", new onDialogClickListener() {
                            @Override
                            public void onPositive() {

                            }

                            @Override
                            public void onNegative() {

                            }
                        });
                    } else {

                        showOKAlert("You are blocked by " + rName + ". So you can not send message", new onDialogClickListener() {
                            @Override
                            public void onPositive() {

                            }

                            @Override
                            public void onNegative() {

                            }
                        });
                    }

                } else {
                    if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                        startPermissionsActivity(false);
                        return;
                    }


                    if (Pref.getStringValue(mainActivity, Constants.PLAN_ID, "").equalsIgnoreCase("0") ||
                            Pref.getStringValue(mainActivity, Constants.PLAN_ID, "").equalsIgnoreCase("")) {
                        showToast("Please subscribe a plan to make a video call.");
                    } else {
                        if (isPaid == 0) {
                            showToast("Please subscribe a plan to make a video call.");
                        } else {
                            /*QBUsers.getUserByLogin(rId).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                    Log.e(TAG, "data " + qbUser.getId() + " " + qbUser.getFullName());

                                    if (checkIsLoggedInChat()) {
                                        HawkAppUtils.getInstance().setQbuserData(qbUser);
                                        startCall(true, qbUser.getId());
                                    }
                                    if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                                        startPermissionsActivity(true);
                                    }
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    Log.e(TAG, "err " + e.getErrors());
                                }
                            });*/

                            QBUsers.getUser(Integer.parseInt(rId)).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                    Log.e(TAG, "data " + qbUser.getId() + " " + qbUser.getFullName());

                                    if (checkIsLoggedInChat()) {
                                        HawkAppUtils.getInstance().setQbuserData(qbUser);
                                        startCall(true, qbUser.getId());
                                    }
                                    if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                                        startPermissionsActivity(true);
                                    }
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    Log.e(TAG, "err array==" + e.getErrors());
                                    Log.e(TAG, "err msg==" + e.getMessage());
                                    Log.e(TAG, "err code==" + e.getHttpStatusCode());
                                }
                            });
                        }
                    }
                    //checkPaid();
                }

                break;
            case R.id.llBlock:

                if (isBlock == 1) {
                    blockUpdate("0");
                } else {
                    blockUpdate("1");
                }

                break;
        }
    }

    private void sendMessage(String message, boolean isAddressLocation) {
        if (isConnectedToInternet()) {
            ChatModel cm = new ChatModel();
            cm.setSenderId(HawkAppUtils.getInstance().getUSERDATA().getData().getId());
            cm.setSenderName(HawkAppUtils.getInstance().getUSERDATA().getData().getName());
            cm.setImageAvailable(false);
            cm.setImageUrl("");

            cm.setTimeStamp(System.currentTimeMillis() + "");
            if (isAddressLocation) {
                cm.setLocationAvailable(true);
                cm.setLatitude(latitude);
                cm.setLongitude(longitude);
                cm.setMessage("Location");
            } else {
                cm.setLocationAvailable(false);
                cm.setLatitude("");
                cm.setLongitude("");
                cm.setMessage(message);
            }

            etMessage.setText("");
            String myKey = dbRef.child("Conversation").child(conversationID).child("messages").push().getKey();
            Log.e("key", myKey);
            dbRef.child("Conversation").child(conversationID).child("messages").child(myKey).setValue(cm).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            sendNotification(message);
                            //String id = eferenceLcl.child("GroupChats").push().getKey()
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            showToast(getString(R.string.msg_no_internet));
        }
    }

    private void checkBlockUser() {
        if (blockBy.equals(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {

            showOKAlert("Please unblock " + rName + " to continue chat.", new onDialogClickListener() {
                @Override
                public void onPositive() {
                }

                @Override
                public void onNegative() {
                }
            });
        } else {
            showOKAlert("You are blocked by " + rName + ". So you can not send message", new onDialogClickListener() {
                @Override
                public void onPositive() {
                }

                @Override
                public void onNegative() {
                }
            });
        }
    }

    private void startCall(boolean isVideoCall, Integer q_rid) {
        Log.d(TAG, "Starting Call");
    /*    if (usersAdapter.getSelectedUsers().size() > Consts.MAX_OPPONENTS_COUNT) {
            ToastUtils.longToast(String.format(getString(R.string.error_max_opponents_count),
                    Consts.MAX_OPPONENTS_COUNT));
            return;
        }
*/
        ArrayList<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(q_rid);

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
        Log.e(TAG, "conferenceType = " + conferenceType);

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);
        PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());
        CallActivity.start(this, false);
        //sendVideoPush();
    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);
    }

    private void sendNotification(String sendMsg) {


        try {


            LoginResponse lg = HawkAppUtils.getInstance().getUSERDATA();
            String myNae = lg.getData().getName() + " " + lg.getData().getLastName();
            String imageUrl = "";
            try {

                if (lg.getData().getImages() != null && !lg.getData().getImages().isEmpty())

                    imageUrl = lg.getData().getImages().get(0).getVPhoto() + "";


            } catch (Exception e) {

            }

            final Map<String, Object> request = new HashMap<>();
            final Map<String, Object> dataRequest = new HashMap<>();
            final Map<String, Object> notifRequest = new HashMap<>();


            notifRequest.put("priority", "high");
            notifRequest.put("body", sendMsg + "");
            notifRequest.put("title", myNae);
            notifRequest.put("Title", myNae);
            notifRequest.put("userId", lg.getData().getId() + "");
            notifRequest.put("content_available", true);
            notifRequest.put("sound", ".sound");
            notifRequest.put("pushType", "2");
            notifRequest.put("senderId", lg.getData().getId() + "");
            notifRequest.put("senderName", myNae);
            notifRequest.put("conversationId", conversationID + "");
            notifRequest.put("senderProfile", imageUrl + "");
            notifRequest.put("click_action", "chat");
            notifRequest.put("image", "");
            notifRequest.put("fcmToken", Pref.getStringValue(ChatActivity.this, Constants.PREF_FCM_TOKEN, "") + "");


            dataRequest.put("priority", "high");
            dataRequest.put("body", sendMsg + "");
            dataRequest.put("title", myNae);
            dataRequest.put("Title", myNae);
            dataRequest.put("userId", lg.getData().getId() + "");
            dataRequest.put("content_available", true);
            dataRequest.put("pushType", "2");
            dataRequest.put("senderId", lg.getData().getId() + "");
            dataRequest.put("senderName", myNae);
            dataRequest.put("conversationId", conversationID + "");
            dataRequest.put("senderProfile", imageUrl + "");
            dataRequest.put("click_action", "chat");
            dataRequest.put("image", "");
            dataRequest.put("fcmToken", Pref.getStringValue(ChatActivity.this, Constants.PREF_FCM_TOKEN, "") + "");


            request.put("to", rToken + "");
            request.put("notification", notifRequest);
            request.put("data", dataRequest);

            //Log.e("req", (new JSONObject(request)).toString() + "");
            String serverKey = "key=" + AppUtils.SERVER_KEY;
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(request)).toString());

            AppUtils.showLog(ChatActivity.this, "send : PARAMETER==" + request.toString());
            Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetroFCM().sendFCM("application/json", serverKey, body);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(ChatActivity.this, "Response send: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppUtils.showLog(ChatActivity.this, "send : JSONException==" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    AppUtils.showLog(ChatActivity.this, "send : onFailure==" + t.getMessage());
                }
            });
        } catch (Exception e) {

        }

        lastMessage(sendMsg);
    }

    private void sendVideoPush() {


        try {


            LoginResponse lg = HawkAppUtils.getInstance().getUSERDATA();
            String myNae = lg.getData().getName() + " " + lg.getData().getLastName();
            String imageUrl = "";
            try {

                if (lg.getData().getImages() != null && !lg.getData().getImages().isEmpty())

                    imageUrl = lg.getData().getImages().get(0).getVPhoto() + "";


            } catch (Exception e) {

            }

            final Map<String, Object> request = new HashMap<>();
            final Map<String, Object> dataRequest = new HashMap<>();
            final Map<String, Object> notifRequest = new HashMap<>();


            notifRequest.put("priority", "high");
            notifRequest.put("pushType", "3");


            dataRequest.put("priority", "high");
            dataRequest.put("pushType", "3");

            request.put("to", rToken + "");
            request.put("notification", notifRequest);
            request.put("data", dataRequest);

            //Log.e("req", (new JSONObject(request)).toString() + "");
            String serverKey = "key=" + AppUtils.SERVER_KEY;
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(request)).toString());

            Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetroFCM().sendFCM("application/json", serverKey, body);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {


                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(conversationID));
        } catch (Exception e) {

        }
        boolean isIncomingCall = SharedPrefsHelper.getInstance().get(Consts.EXTRA_IS_INCOMING_CALL, false);
        if (isCallServiceRunning(CallService.class)) {
            Log.d(TAG, "CallService is running now");
            CallActivity.start(this, isIncomingCall);
        }
        //  clearAppNotifications();

    }

    private boolean checkIsLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            startLoginService();
            ToastUtils.shortToast(R.string.dlg_relogin_wait);
            return false;
        }
        return true;
    }

  /*  private void clearAppNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }
*/

    private void startLoginService() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            LoginService.start(this, qbUser);
        }
    }

    private boolean isCallServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void showImageDialog() {
        PopupMenu popupMenuShareMedia = new PopupMenu(this, ivCamera);
        popupMenuShareMedia.getMenuInflater().inflate(R.menu.menu_share_media_location, popupMenuShareMedia.getMenu());

        popupMenuShareMedia.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.ac_share_media:
                    PopupMenu popupMenu = new PopupMenu(this, ivCamera);
                    popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(item1 -> {
                        switch (item1.getItemId()) {
                            case R.id.ac_camera:
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File photoFile = null;
                                try {
                                    photoFile = AppUtils.createImageFile(ChatActivity.this);
                                    imgString = photoFile.getAbsolutePath();

                                    Log.e("ST", imgString);

                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                    ex.printStackTrace();
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(ChatActivity.this, getPackageName() + ".fileprovider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                                }
                                break;
                            case R.id.ac_gallery:

                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                i.setType("image/*");
                                startActivityForResult(i, GALLRY_REQUEST);
                                break;
                        }
                        return false;
                    });
                    popupMenu.show();
                    break;
                case R.id.ac_share_location:
                    Intent intent = new Intent(ChatActivity.this, GetUserLocationActivity.class);
                    startActivityForResult(intent, SHARE_LOCATION);
                    break;
            }
            return false;
        });

        popupMenuShareMedia.show();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            File output = new File(imgString);
            Log.e("im c", imgString);
            uploadImage();
         /*   Glide.with(tH).load(Uri.fromFile(output)).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .into(ivProfile);*/
               /* if (output.exists()) {

                    iv_camera.setImageURI(Uri.fromFile(output));
                }*/
            /*output = new File(outputPath);

            if (output.exists()) {
                ivProfile.setImageURI(Uri.fromFile(output));
                String picturePath = output.getAbsolutePath();
                uploadImage(picturePath);
            }*/


        } else if (resultCode == RESULT_OK && requestCode == GALLRY_REQUEST) {

            if (data != null) {


                Uri selectedImageUri = data.getData();
                //iv_camera.setImageURI(selectedImageUri);
                imgString = AppUtils.getPathFromUri(ChatActivity.this, selectedImageUri);
                // output = new File(imgString);
                Log.e("im", imgString);

               /* Glide.with(getActivity()).load(selectedImageUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                        .into(ivProfile);*/

                uploadImage();
            }

        } else if (requestCode == SHARE_LOCATION && resultCode == Constants.RESULT_CHOOSE_LOCATION) {
            chooseDeliveryAddress = data.getStringExtra(Constants.CALLBACK_DELIVERY_ADDRESS);
            //GET LAT LONG FROM ADDRESS
            Log.e("ChatActivity", "chooseDeliveryAddress==" + chooseDeliveryAddress);
            LatLng latLng = AppUtils.getLocationFromAddress(ChatActivity.this, chooseDeliveryAddress);
            if (latLng != null) {
                latitude = String.valueOf(latLng.latitude);
                longitude = String.valueOf(latLng.longitude);

                Log.e("ChatActivity", "latitude==" + latitude);
                Log.e("ChatActivity", "longitude==" + longitude);

                //Check user block or not
                if (isBlock == 1) {
                    checkBlockUser();
                } else {
                    //Send firebase message
                    isShareLocation = true;
                    sendMessage(chooseDeliveryAddress, true);
                }
            } else {
                showToast(getString(R.string.msg_something_wrong));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage() {
        Intent intent = new Intent(ChatActivity.this, UploadService.class);
        intent.putExtra("notification_id", (int) System.currentTimeMillis());
        intent.putExtra("conversationID", conversationID);
        intent.putExtra("description", "");
        intent.putExtra("fileUrl", imgString);
        intent.putExtra("pushKey", "");
        intent.putExtra("rToken", rToken);
        intent.putExtra("rId", rId);
        intent.setAction(UploadService.ACTION_START_FOREGROUND_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

       /* ChatModel cm = new ChatModel();
        cm.setSenderId(HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        cm.setSenderName(HawkAppUtils.getInstance().getUSERDATA().getData().getName());
        cm.setImageAvailable(true);
        cm.setImageUrl("");
        cm.setMessage("");
        cm.setTimeStamp(System.currentTimeMillis() + "");

        String uploadKey = dbRef.child("Conversation").child(conversationID).child("messages").push().getKey();
        Log.e("key", uploadKey);
        dbRef.child("Conversation").child(conversationID).child("messages").child(uploadKey)
                .setValue(cm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                Intent intent = new Intent(ChatActivity.this, UploadService.class);
                intent.putExtra("notification_id", (int) System.currentTimeMillis());
                intent.putExtra("conversationID", conversationID);
                intent.putExtra("description", "sdfs");
                intent.putExtra("fileUrl", imgString);
                intent.putExtra("pushKey", uploadKey);
                intent.setAction(UploadService.ACTION_START_FOREGROUND_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });*/


    }

    public void showFullImageDialog(String imgUrl) {
        Dialog dialog = new Dialog(mContext, R.style.TransparentProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_single);
        dialog.setCancelable(true);

        ImageView ivImageD = dialog.findViewById(R.id.ivImageD);
        ImageView ivClosed = dialog.findViewById(R.id.ivClose);


        ivClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Glide.with(ChatActivity.this).load(imgUrl).
                apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.appicon).dontAnimate())
                .into(ivImageD);


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


        dialog.show();


    }

    private void checkPaid() {
        //showProgress();

        Call<UserResponse> call = RetrofitBuilder.getInstance().getRetrofit().userData();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(new Gson().toJson(response.body()));
                    AppUtils.showLog(ChatActivity.this, "Response userData: " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    AppUtils.showLog(ChatActivity.this, "userData : JSONException==" + e.getMessage());
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Data dataUser = response.body().getData();
                        isPaid = dataUser.getIs_paid();
                        /*if (isPaid == 0) {
                            showToast("Please subscribe a plan to make a video call.");
                        } else {
                            QBUsers.getUserByLogin(rId).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                    Log.e(TAG, "data " + qbUser.getId() + " " + qbUser.getFullName());

                                    if (checkIsLoggedInChat()) {
                                        HawkAppUtils.getInstance().setQbuserData(qbUser);
                                        startCall(true, qbUser.getId());
                                    }
                                    if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                                        startPermissionsActivity(true);
                                    }
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    Log.e(TAG, "err " + e.getErrors());
                                }
                            });
                        }*/
                    }
                }

                isFirstTime = false;
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
                isFirstTime = false;
            }
        });
    }

    private void checkBlock() {
        showProgress();

        Call<BlockResponse> call = RetrofitBuilder.getInstance().getRetrofit().isblockChat(conversationID);
        call.enqueue(new Callback<BlockResponse>() {
            @Override
            public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        blockBy = response.body().getData().getBlockedBy() + "";
                        isBlock = response.body().getData().getIsBlock();

                        if (isBlock == 1) {

                            if (blockBy.equals(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {
                                llBlock.setVisibility(View.VISIBLE);
                                tvBlock.setText("Unblock");
                                ivBlock.setImageResource(R.drawable.ic_unblock_wc);
                            } else {

                                llBlock.setVisibility(View.GONE);
                            }

                        } else {
                            llBlock.setVisibility(View.VISIBLE);
                            tvBlock.setText("Block");
                            ivBlock.setImageResource(R.drawable.ic_block_wc);
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<BlockResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void lastMessage(String msg) {

        final Map<String, Object> request = new HashMap<>();
        request.put("conversation_id", conversationID);
        request.put("sender_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("recipient_id", rId);
        if (isShareLocation) {
            request.put("lastMsg", "Location");
            isShareLocation = false;
        } else {
            request.put("lastMsg", msg);
        }

        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().lastConversation(request);

        AppUtils.showLog(ChatActivity.this, "lastConversation : PARAMETER==" + request.toString());
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(ChatActivity.this, "Response lastMessage: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        AppUtils.showLog(ChatActivity.this, "lastMessage : JSONException==" + e.getMessage());
                    }
                    if (response.body().isSuccess()) {

                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
            }
        });
    }

    private void blockUpdate(String type) {

        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().blockChat(HawkAppUtils.getInstance().getUSERDATA().getData().getId(), rId, type);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        checkBlock();
                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
            }
        });
    }

    private void callUpdateUserOnlineStatusApi(String status) {
        final Map<String, Object> request = new HashMap<>();
        if (HawkAppUtils.getInstance().getUSERDATA() != null) {
            request.put("userId", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
            request.put("is_online", status);


            AppUtils.showLog(ChatActivity.this.getApplicationContext(), "getUpdateUserOnlineStatus : PARAMETER==" + request.toString());
            Call<CommonResponse> call = RetrofitBuilder.getInstance().getRetrofit().getUpdateUserOnlineStatus(request);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(new Gson().toJson(response.body()));
                        AppUtils.showLog(ChatActivity.this, "Response getUpdateUserOnlineStatus: " + jsonObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //showToast(getString(R.string.msg_something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    hideProgress();
                    //showToast(getString(R.string.msg_something_wrong));
                    AppUtils.showLog(ChatActivity.this, "onFailure getUpdateUserOnlineStatus:" + t.getMessage());
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        isScreenActive = false;
        callReadMessageAPI();
        intent.putExtra("conversation_id", conversationID);
        intent.putExtra("sender_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        intent.putExtra("recipient_id", rId);
        setResult(100, intent);
    }

    private static class ChatUserData {

        public final String getMessage, getTimeStamp, getImageUrl, getLatitude, getLongitude;
        public final boolean isImageAvailable, isLocationAvailable;

        ChatUserData(String getMessage, String getTimeStamp, String getImageUrl, String getLatitude, String getLongitude, boolean isImageAvailable, boolean isLocationAvailable) {
            this.getMessage = getMessage;
            this.getTimeStamp = getTimeStamp;
            this.getImageUrl = getImageUrl;
            this.getLatitude = getLatitude;
            this.getLongitude = getLongitude;
            this.isImageAvailable = isImageAvailable;
            this.isLocationAvailable = isLocationAvailable;
        }
    }

    private class ChatNewAdapter extends RecyclerView.Adapter<ChatNewAdapter.ViewHolder> {
        List<ChatModel> listChat;
        String uid;

        ChatNewAdapter(List<ChatModel> listChat) {
            this.listChat = listChat;
            uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_my, null);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_oppo, null);
            }

            return new ChatNewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindView(position);
        }

        @Override
        public int getItemCount() {
            return listChat == null ? 0 : listChat.size();
        }

        @Override
        public int getItemViewType(int position) {
            int viewType = 1; //Default is 1

            if ((listChat.get(position).getSenderId()).equals(uid)) {
                viewType = 0;
            }//if zero, it will be a header view

            return viewType;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
            public GoogleMap map;
            TextView tvName, tvMessage, tvTime, tvAddress, tvLatlong, tvDate;
            ImageView ivImage;
            LinearLayout lyrMessage;
            RelativeLayout llAddress;
            MapView mapView;
            View layout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView;
                mapView = itemView.findViewById(R.id.lite_listrow_map);
                tvName = itemView.findViewById(R.id.tvName);
                tvMessage = itemView.findViewById(R.id.tvMessage);
                tvTime = itemView.findViewById(R.id.tvTime);
                ivImage = itemView.findViewById(R.id.ivImage);
                lyrMessage = itemView.findViewById(R.id.lyrMessage);
                llAddress = itemView.findViewById(R.id.llAddress);
                tvAddress = itemView.findViewById(R.id.tvAddress);
                tvLatlong = itemView.findViewById(R.id.tvLatlong);
                tvDate = itemView.findViewById(R.id.tvDate);

                if (mapView != null) {
                    // Initialise the MapView
                    mapView.onCreate(null);
                    // Set the map ready callback to receive the GoogleMap object
                    mapView.getMapAsync(this);
                }
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getApplicationContext());
                map = googleMap;
                setMapLocation();
            }

            private void setMapLocation() {
                if (map == null) return;

                ChatModel data = (ChatModel) mapView.getTag();
                if (data == null) return;

                if (data.getLatitude() != null && data.getLongitude() != null && !data.getLatitude().isEmpty() && !data.getLongitude().isEmpty()) {
                    // Add a marker for this item and set the camera
                    LatLng location = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
                    map.addMarker(new MarkerOptions().position(location));

                    // Set the map type back to normal.
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }

            private void setTimeTextVisibility(long ts1, long ts2, TextView timeText, String date, int position) {
                AppUtils.showLog(getApplicationContext(), "milliSecond==" + ts1);
                AppUtils.showLog(getApplicationContext(), "previousTs==" + ts2);
                AppUtils.showLog(getApplicationContext(), "DateTime==" + date);
                AppUtils.showLog(getApplicationContext(), "position==" + position);

                Calendar smsTime = Calendar.getInstance();
                smsTime.setTimeInMillis(ts1);
                String dateTime = android.text.format.DateFormat.format("dd/MM/yyyy", smsTime).toString();
                AppUtils.showLog(getApplicationContext(), "LastDateTime==" + dateTime);

                /*if (isSentMessage && date.equalsIgnoreCase("Today")) {
                    timeText.setVisibility(View.GONE);
                } else {

                }*/
                if (position == listChat.size() - 1) {
                    /*timeText.setVisibility(View.VISIBLE);
                    timeText.setText(date);*/
                    listChat.get(position).setLastDate(date);
                } else {
                    AppUtils.showLog(getApplicationContext(), "getLastDate==" + listChat.get(position + 1).getLastDate());
                    if (listChat.get(position + 1).getLastDate().equalsIgnoreCase(date)) {
                        timeText.setVisibility(View.GONE);
                        timeText.setText("");
                    } else {
                        timeText.setVisibility(View.VISIBLE);
                        timeText.setText(date);
                    }
                    listChat.get(position).setLastDate(date);
                }

            }

            private void bindView(int position) {
                ChatModel item = listChat.get(position);
                // Store a reference of the ViewHolder object in the layout.
                layout.setTag(this);
                mapView.setTag(item);
                // Store a reference to the item in the mapView's tag. We use it to get the
                // coordinate of a location, when setting the map location.
                tvMessage.setText(listChat.get(position).getMessage());
                long milliSecond = Long.parseLong(listChat.get(position).getTimeStamp());
                DateFormat formatter = new SimpleDateFormat("hh:mma");
                String text = formatter.format(new Date(milliSecond));
                String dateTime = AppUtils.getFormattedDate(getApplicationContext(), milliSecond) + ", " + " " + text;
                tvTime.setText(dateTime);
                //tvDate.setText(dateTime);
                tvDate.setVisibility(View.GONE);
                //tvDate.setText(AppUtils.getFormattedDate(getApplicationContext(), milliSecond));

                /*long previousTs = 0;
                if (position > 1) {
                    previousTs = milliSecond;
                }
                try {
                    setTimeTextVisibility(milliSecond, previousTs, tvDate, AppUtils.getFormattedDate(getApplicationContext(), milliSecond), position);
                } catch (Exception e) {
                    AppUtils.showLog(getApplicationContext(), "DateText==Exception==" + e.toString());
                }*/


                if (listChat.get(position).isImageAvailable()) {
                    lyrMessage.setVisibility(View.GONE);
                    ivImage.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.GONE);
                    mapView.setVisibility(View.GONE);
                    //if (listChat.get(position).isImageSuccess()) {
                    Glide.with(ChatActivity.this).load(listChat.get(position).getImageUrl())
                            .thumbnail(0.5f).apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.appicon).dontAnimate())
                            .into(ivImage);
                    //}
                } else if (listChat.get(position).isLocationAvailable()) {
                    lyrMessage.setVisibility(View.GONE);
                    ivImage.setVisibility(View.GONE);
                    llAddress.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.VISIBLE);


                    setMapLocation();
                } else {
                    lyrMessage.setVisibility(View.VISIBLE);
                    ivImage.setVisibility(View.GONE);
                    llAddress.setVisibility(View.GONE);
                    mapView.setVisibility(View.GONE);
                }
            }

        }
    }
}
