package com.nect.friendlymony.Quickblox.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;

/**
 * QuickBlox team
 */
public class RingtonePlayer {

    private static final String TAG = RingtonePlayer.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private Context context;

    Ringtone ringtoneSound;


    public RingtonePlayer(Context context, int resource) {
        this.context = context;
        Uri beepUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resource);
        mediaPlayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING);

            mediaPlayer.setAudioAttributes(audioAttributesBuilder.build());
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }

        try {
            mediaPlayer.setDataSource(context, beepUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RingtonePlayer(Context context) {

        Log.e("<>>>>>>>>>>", "RINGGGGGGGGGGGGGGgg");
        this.context = context;
        Uri notification = getNotification();
        Log.e("URI",notification+"<");
        if (notification != null) {
            mediaPlayer = new MediaPlayer();
           // mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE);

                mediaPlayer.setAudioAttributes(audioAttributesBuilder.build());
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            }

            try {
                 mediaPlayer.setDataSource(context, notification);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri getNotification() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Log.e("not nll", "not nll");
        if (notification == null) {
            Log.e("nll", "nll");
            // notification is null, using backup
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if (notification == null) {
                // notification backup is null, using 2nd backup
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }
        return notification;
    }

    public void play(boolean looping) {
        Log.i(TAG, "play:" + looping);
        if (mediaPlayer == null) {
            Log.i(TAG, "mediaPlayer isn't created ");
            return;
        }
        mediaPlayer.setLooping(looping);
        mediaPlayer.start();
    }

    public synchronized void stop() {
        Log.i(TAG, "STOPEDD");
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}