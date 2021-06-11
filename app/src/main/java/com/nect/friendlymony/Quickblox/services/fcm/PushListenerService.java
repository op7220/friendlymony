package com.nect.friendlymony.Quickblox.services.fcm;

public class PushListenerService {
/* extends QBFcmPushListenerService {
    private static final String TAG = PushListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            Log.e(TAG, "2222222222App has logged user" + qbUser.getId());
            LoginService.start(this, qbUser);
        }
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        //Log.e(TAG, "22222222From: " + from);
        Log.e(TAG, "2222222Message: " + message);
    }
}*/
}