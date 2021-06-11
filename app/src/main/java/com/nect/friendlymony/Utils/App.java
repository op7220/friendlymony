package com.nect.friendlymony.Utils;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.messaging.FirebaseMessaging;
import com.nect.friendlymony.Quickblox.util.QBResRequestExecutor;
import com.nect.friendlymony.R;
import com.orhanobut.hawk.Hawk;
import com.quickblox.auth.session.QBSettings;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class App extends MultiDexApplication {

    /*One signal App ID: a810a9aa-0141-468e-9bba-9f45de5f804e*/
    private static final String APPLICATION_ID = "78327";
    private static final String AUTH_KEY = "zBcPpLdjKA5GFjC";
    private static final String AUTH_SECRET = "4PWNExeePNstGUm";
    private static final String ACCOUNT_KEY = "Gg_N7HPyP_6oSTz_dyhs";

    public static final String USER_DEFAULT_PASSWORD = "quickblox";

    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().setDeliveryMetricsExportToBigQuery(true);

        /*OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/
        try {
            //MultiDex.install(this);
            Hawk.init(this).build();
            /*Crashlytics Tools*/
            /*Fabric.with(this, new Crashlytics());*/

            /*Set Fonts for app*/
            /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    //.setDefaultFontPath("fonts/Archivo-SemiBold.ttf")
                    .setDefaultFontPath("font/Muli-Regular.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );*/

            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath("font/Muli-Regular.ttf")
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());

        } catch (Exception e) {

        }


        // Fabric.with(this, new Crashlytics());
        //realm
        checkAppCredentials();
        initCredentials();


    }

    private void checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw new AssertionError(getString(R.string.error_credentials_empty));
        }
    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        // Uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    public static App getInstance() {
        return instance;
    }

}
