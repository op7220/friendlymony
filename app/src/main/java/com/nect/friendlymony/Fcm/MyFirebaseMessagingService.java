package com.nect.friendlymony.Fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.nect.friendlymony.Activity.ChatActivity;
import com.nect.friendlymony.Activity.FeedDetailActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Quickblox.services.LoginService;
import com.nect.friendlymony.Quickblox.utils.SharedPrefsHelper;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.Pref;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;
import com.quickblox.users.model.QBUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;


/*com.google.firebase.messaging.RemoteMessage@e88772a
        2019-08-21 16:02:31.561 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: userFromName Value: Testing
        2019-08-21 16:02:31.561 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: userId Value: 12
        2019-08-21 16:02:31.561 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: userName Value: Jatin
        2019-08-21 16:02:31.561 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: body Value: Jatin  CrushYou
        2019-08-21 16:02:31.562 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: title Value: Crush
        2019-08-21 16:02:31.562 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: userFromId Value: 23
        2019-08-21 16:02:31.562 4454-4649/com.nect.friendlymony E/FCMPlugin: 	Key: pushType Value: 1

        */

/*
https://github.com/QuickBlox/quickblox-android-sdk/issues/374
*/

public class MyFirebaseMessagingService extends QBFcmPushListenerService {
    //APPROVED ORDER
    private static final String TAG = "FCMPlugin";

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Pref.setStringValue(getBaseContext(), PREF_FCM_TOKEN, s);
        Log.e("MessagingService", s + "");


        try {
            SubscribeService.subscribeToPushes(getBaseContext(), true);
            // QBRequest.createSubscription

        } catch (Exception e) {

        }
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        //Log.e(TAG, "22222222From: " + from);
        Log.e(TAG, "2222222Message: " + message);
    }
    // [END receive_message]

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //  Log.e(TAG, "11111111 has logged user");


        // Log.e(TAG, "\tNotification Data: " + data.toString());
        //  FCMPlugin.sendPushPayload( data );

        //pushtype ==2 --> for FCM chat

        try {

            Log.e(TAG, "==> MyFirebaseMessagingService onMessageReceived");
            Log.e(TAG, "==> " + remoteMessage.getData().size());
            Log.e(TAG, "==> " + remoteMessage.getData().get("pushType") + "");
            Log.e(TAG, "==> " + remoteMessage.getData().get("Status") + "");
            Log.e(TAG, "==> " + remoteMessage.getData().get("senderId") + "");
            Log.e(TAG, "pushType ==> " + remoteMessage.getNotification() + "");
            Log.e(TAG, "==> " + new Gson().toJson(remoteMessage.getNotification()) + "");

            if (remoteMessage.getNotification() != null) {
                //Log.e(TAG, "\tNotification Title: " + remoteMessage.getNotification().getTitle());
                //Log.e(TAG, "\tNotification Message: " + remoteMessage.getNotification().getBody());
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("wasTapped", false);
            for (String key : remoteMessage.getData().keySet()) {
                Object value = remoteMessage.getData().get(key);
                Log.e(TAG, "\tKey: " + key + " Value: " + value);
                data.put(key, value);
            }

            if ((remoteMessage.getData().get("pushType") + "").equals("2")) {
                if (Pref.getStringValue(getApplicationContext(), Constants.IS_CHAT_SCREEN, "").equalsIgnoreCase("0")) {
                    sendChatNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), data);
                }
            } else if ((remoteMessage.getData().get("pushType") + "").equals("3")) {

                try {
                    SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
                    if (sharedPrefsHelper.hasQbUser()) {
                        QBUser qbUser = sharedPrefsHelper.getQbUser();
                        Log.e(TAG, "App has logged user" + qbUser.getId());
                        LoginService.start(this, qbUser);
                    }
                } catch (Exception e) {

                }

            } else {
                //data.get("userId").toString();
                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), data);
            }

        } catch (Exception e) {

            Log.e(TAG, "Exception data==" + e.getMessage());
            try {
                SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
                if (sharedPrefsHelper.hasQbUser()) {
                    QBUser qbUser = sharedPrefsHelper.getQbUser();
                    Log.e(TAG, "App has logged user" + qbUser.getId());
                    LoginService.start(this, qbUser);
                }
            } catch (Exception ex) {
                Log.e(TAG, "Exception data ex ==" + e.getMessage());
            }

        }

        //sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBodys FCM message body received.
     */
    private void sendNotification(String title, String messageBodys, Map<String, Object> data) {

        int random = new Random().nextInt(61) + 20;
        String uid = "";
        String image = "";
        try {

            if (data.get("image") != null) {
                image = data.get("image").toString();
            }

            if (data.get("data") != null) {
                Log.e(TAG, "data feed detils==" + data.get("data").toString());
            }

            Intent intent = new Intent(this, FeedDetailActivity.class);
            intent.putExtra("fId", data.get("userId").toString() + "");
            intent.putExtra("Status", data.get("Status").toString() + "");
            intent.putExtra("From", "PushNotification");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*for (String key : data.keySet()) {
            intent.putExtra(key, data.get(key).toString());
        }*/
            PendingIntent pendingIntent = PendingIntent.getActivity(this, random /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


            if ((image + "").equalsIgnoreCase("") || (image + "").equalsIgnoreCase("null")) {
                showSmallNotification(pendingIntent, messageBodys, title, random);
            } else {

                Bitmap bitmap = getBitmapFromURL(image);
                showBigNotification(bitmap, pendingIntent, messageBodys, title, image, random);
            }
        } catch (Exception e) {
            AppUtils.showLog(getApplicationContext(), "Exception Intent FeedDetailActivity==" + e.getMessage());
            PendingIntent pendingIntent = PendingIntent.getActivity(this, random /* Request code */, new Intent(this, MainActivity.class),
                    PendingIntent.FLAG_ONE_SHOT);
            showSmallNotification(pendingIntent, messageBodys, title, random);
        }

    }

    private void sendChatNotification(String title, String messageBodys, Map<String, Object> data) {

        int random = new Random().nextInt(61) + 20;
        String uid = "";
        String image = "";
        try {
            //uid = data.get("userFromId");
            // JSONObject jsonObject = new JSONObject(messageBodys);

            if (data.get("image") != null) {
                image = data.get("image").toString();
                Log.e(TAG, "image==" + image);
            }

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("conversationID", data.get("conversationId").toString() + "");
            intent.putExtra("rName", data.get("senderName").toString() + "");
            intent.putExtra("rPhoto", data.get("senderProfile").toString() + "");
            intent.putExtra("rToken", data.get("fcmToken").toString() + "");
            intent.putExtra("rId", data.get("senderId").toString() + "");
            intent.putExtra("from", "PushNotification");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, random /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


            if ((image + "").equalsIgnoreCase("")) {
                showSmallNotification(pendingIntent, messageBodys, title, random);
            } else {

                Bitmap bitmap = getBitmapFromURL(image);
                showBigNotification(bitmap, pendingIntent, messageBodys, title, image, random);
            }

        } catch (Exception e) {
            AppUtils.showLog(getApplicationContext(), "sendChatNotification Exception Intent ChatActivity==" + e.getMessage());
        }

        try {

            int nId = Integer.parseInt(data.get("conversationId").toString());
            random = nId + 0;
        } catch (Exception e) {
            AppUtils.showLog(getApplicationContext(), "Exception Intent ChatActivity==" + e.getMessage());
        }

    }

    private void showSmallNotification(PendingIntent pendingIntent, String myreciveMsg, String title, int notifId) {

        Log.e("n?ID", notifId + "");
//        int random = new Random().nextInt(61) + 20;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Cable";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationCompat.Builder notificationBuilder;
        Uri soundU;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    //.setContentText(myreciveMsg)
                    .setAutoCancel(true)
                    // .setPriority(Notification.PRIORITY_MAX)
                    //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    //   .setSound(soundU)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(myreciveMsg))
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    //.setContentText(myreciveMsg)
                    .setAutoCancel(true)
                    // .setSound(soundU)
                    //.setPriority(Notification.PRIORITY_MAX)
                    // .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(myreciveMsg))
                    .setContentIntent(pendingIntent);
        }


        notificationManager.notify(notifId /* ID of notification */, notificationBuilder.build());
    }

    private void showBigNotification(Bitmap bitmap, PendingIntent pendingIntent, String myreciveMsg, String title, String image, int notifId) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        String channelId = "channel-01";
        String channelName = "Cable";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationCompat.Builder notificationBuilder;
        Uri soundU;


        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(myreciveMsg).toString());
        bigPictureStyle.bigPicture(bitmap);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(myreciveMsg)
                    // .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setStyle(bigPictureStyle)
                    // .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    //   .setSound(soundU)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(myreciveMsg)
                    .setAutoCancel(true)
                    // .setPriority(Notification.PRIORITY_MAX)
                    // .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(bigPictureStyle)
                    // .setSound(soundU)
                    .setContentIntent(pendingIntent);
        }

        notificationManager.notify(notifId /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
