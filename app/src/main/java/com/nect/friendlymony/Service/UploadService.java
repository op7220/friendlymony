package com.nect.friendlymony.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Chat.ChatModel;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadService extends Service {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private static final String TAG_FOREGROUND_SERVICE = UploadService.class.getSimpleName();
    DatabaseReference dbRef;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            String action = intent.getAction();
            if (action.equals(ACTION_START_FOREGROUND_SERVICE)) {

                int notification_id = intent.getIntExtra("notification_id", 2);
                String current_id = intent.getStringExtra("conversationID");
                String description = intent.getStringExtra("description");
                String fileUrl = intent.getStringExtra("fileUrl");
                String pushKey = intent.getStringExtra("pushKey");
                String rToken = intent.getStringExtra("rToken");
                String rId = intent.getStringExtra("rId");

                Log.e("Cid", rToken + "");
                dbRef = FirebaseDatabase.getInstance().getReference();

                //dbRef.child("Conversation").child(current_id);
                dbRef.child("Conversation").push();

                uploadImages(pushKey, notification_id, 0, fileUrl, current_id, description, rToken, rId);

            }

        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopForegroundService(boolean removeNotification) {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");
        // Stop foreground service and remove the notification.
        stopForeground(removeNotification);
        // Stop the foreground service.
        stopSelf();
    }

    private void notifyProgress(int id, int icon, String title, String message, Context context, int max_progress, int progress, boolean indeterminate) {


        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

       /* builder.setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTicker(message)
                .setChannelId("other_channel")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(max_progress, progress, indeterminate)
                .setVibrate(new long[0]);

        startForeground(id, builder.build());
        */


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-05";
        String channelName = "fm";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationCompat.Builder notificationBuilder;
        Uri soundU;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setTicker(message)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setProgress(max_progress, progress, indeterminate)
                    .setVibrate(new long[0]);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setTicker(message)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setProgress(max_progress, progress, indeterminate)
                    .setVibrate(new long[0]);
        }
        startForeground(id, notificationBuilder.build());

        // notificationManager.notify(id /* ID of notification */, notificationBuilder.build());

    }

    private void uploadImages(String pushKey, int notification_id, int i, String fileUrl, String conversationID, String description, String rToken, String rId) {
        String extension = "jpg";
        try {
            extension = fileUrl.substring(fileUrl.lastIndexOf("."));

        } catch (Exception e) {

        }

        Uri imageUri = null;
        try {
            File compressedFile = new Compressor(this)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(new File(fileUrl));


            imageUri = Uri.fromFile(compressedFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Image not found", Toast.LENGTH_SHORT).show();
            return;

        } catch (Exception e) {
            e.printStackTrace();
            imageUri = Uri.fromFile(new File(fileUrl));
        }

        Toast.makeText(getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();


        final StorageReference fileToUpload = FirebaseStorage.getInstance().getReference().child("Images").child("FM_" + System.currentTimeMillis() + "_" + HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "." + extension);
        fileToUpload.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileToUpload.getDownloadUrl()
                        .addOnSuccessListener(uri -> {

                           /* uploadedImagesUrl.add(uri.toString());
                            int next_index = index + 1;*/
                            try {
                                AppUtils.showLog(getApplicationContext(), "image url uri.toString()==" + uri.toString());
                                uploadPost(pushKey, conversationID, notification_id, description, uri.toString(), rToken, rId);

                            } catch (Exception e) {
                                e.printStackTrace();
                                AppUtils.showLog(getApplicationContext(), "image url Exception==" + e.getMessage());
                                //uploadPost(notification_id, currentUser_id, description, uploadedImagesUrl);
                            }

                        })
                        .addOnFailureListener(Throwable::printStackTrace))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_LONG).show();
                        AppUtils.showLog(getApplicationContext(), "image url onFailure Exception==" + e.getMessage());
                    }
                })
                .addOnProgressListener(taskSnapshot -> {

                    //if (count == 1) {
                    String title = "Uploading...";
                    int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    notifyProgress(notification_id
                            , android.R.drawable.stat_sys_upload
                            , title
                            , progress + "%"
                            , getApplicationContext()
                            , 100
                            , progress
                            , false);
                    //}
                    /*else if (count > 1) {

                        notifyProgress(notification_id
                                , android.R.drawable.stat_sys_upload
                                , "Hify"
                                , "Uploading " + count + " posts"
                                , getApplicationContext()
                                , 100
                                , 0
                                , true);

                    }*/

                });
    }


    private void uploadPost(String pushKey, String conversationID, int notification_id, String description, String uploadedImagesUrl2, String rToken, String rId) {

        if (!uploadedImagesUrl2.isEmpty()) {

            notifyProgress(notification_id
                    , android.R.drawable.stat_sys_upload
                    , "FriendlyMony"
                    , "Sending post.."
                    , getApplicationContext()
                    , 100
                    , 0
                    , true);


            ChatModel cm = new ChatModel();
            cm.setSenderId(HawkAppUtils.getInstance().getUSERDATA().getData().getId());
            cm.setSenderName(HawkAppUtils.getInstance().getUSERDATA().getData().getName());
            cm.setImageAvailable(true);
            cm.setImageUrl(uploadedImagesUrl2);
            cm.setMessage(".");
            cm.setTimeStamp(System.currentTimeMillis() + "");

            dbRef.child("Conversation").child(conversationID).child("messages").push()
                    .setValue(cm).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //AppUtils.showLog(getApplicationContext(), "uploadPost==onSuccess==" + aVoid.toString());
                    sendNotification(conversationID, rToken, uploadedImagesUrl2, rId);
                    stopForegroundService(true);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    stopForegroundService(true);
                    e.printStackTrace();
                    AppUtils.showLog(getApplicationContext(), "uploadPost==onFailure==" + e.getMessage());
                }
            });


        } else {
            Toast.makeText(this, "No image has been uploaded, Please wait or try again", Toast.LENGTH_SHORT).show();
            stopForegroundService(true);
        }
    }

    private void sendNotification(String conversationID, String rToken, String uploadedImagesUrl, String rId) {


        Log.e("called", "caerllsd21");
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
            notifRequest.put("body", "Photo");
            notifRequest.put("title", myNae);
            notifRequest.put("userId", lg.getData().getId() + "");
            notifRequest.put("content_available", true);
            notifRequest.put("sound", ".sound");
            notifRequest.put("pushType", "2");
            notifRequest.put("senderId", "0");
            notifRequest.put("senderName", myNae);
            notifRequest.put("conversationId", conversationID + "");
            notifRequest.put("senderProfile", imageUrl + "");
            notifRequest.put("fcmToken", Pref.getStringValue(getBaseContext(), Constants.PREF_FCM_TOKEN, "") + "");
            dataRequest.put("click_action", "chat");
            dataRequest.put("image", uploadedImagesUrl);

            dataRequest.put("priority", "high");
            dataRequest.put("body", "Photo");
            dataRequest.put("title", myNae);
            dataRequest.put("userId", lg.getData().getId() + "");
            dataRequest.put("content_available", true);
            dataRequest.put("pushType", "2");
            dataRequest.put("senderId", "0");
            dataRequest.put("senderName", myNae);
            dataRequest.put("conversationId", conversationID + "");
            dataRequest.put("senderProfile", imageUrl + "");
            dataRequest.put("fcmToken", Pref.getStringValue(this, Constants.PREF_FCM_TOKEN, "") + "");
            dataRequest.put("click_action", "chat");
            dataRequest.put("image", uploadedImagesUrl);

            request.put("to", rToken + "");
            request.put("notification", notifRequest);
            request.put("data", dataRequest);

            //Log.e("req", request + "");
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

        lastMessage(conversationID, rId);
    }


    private void lastMessage(String conversationID, String rId) {

        final Map<String, Object> request = new HashMap<>();
        request.put("conversation_id", conversationID);
        request.put("sender_id", HawkAppUtils.getInstance().getUSERDATA().getData().getId());
        request.put("recipient_id", rId);
        request.put("lastMsg", "Photo");

        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().lastConversation(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
            }
        });
    }

}
