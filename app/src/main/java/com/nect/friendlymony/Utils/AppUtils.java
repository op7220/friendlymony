package com.nect.friendlymony.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nectarbits on 1/16/2018.
 * Reusable methods and contains constant values.
 */

public class AppUtils {
    /*API base url*/
    /*Live*/
    public static String OLD_BASE_URL = "http://13.234.164.40:3000/v1/";
    public static String NEW_BASE_URL = "http://208.109.10.11:8000/v1/";
    public static String IMAGE_BASE_URL = "http://13.234.164.40/friendlymony/uploads/";
    public static String BLOCK_USER = NEW_BASE_URL + "blockUser";
    public static String UPDATE_USER = NEW_BASE_URL + "updateUserOnlineStatus";
    public static String APPLE_LOGIN_BASE_URL = "https://appleid.apple.com/auth/";

    /*Local*/
    // public static String BASE_URL = "http://192.168.1.162:3010/v1/";
    //public static String IMAGE_BASE_URL = "http://192.168.1.162/friendlymony/uploads/";

    public static String BASE_FCM_URL = "https://fcm.googleapis.com/fcm/";
    public static String SERVER_KEY = "AAAAEHPgz5o:APA91bFtPXicXhXN8QMDs5EtTHntsxC9C5ca9JiKYOV-eeZzdq3cyBtD8AIW9VaOebMNTFhekaGCkRFSJQkN9ixtBRdmRW41sj99LLOFSl5tKhaK98nTTcXy8si64CxrRDxd439cBo6R";

    /*public static String URL_ABOUT = "http://13.234.164.40/friendlymony-website/aboutus.html";
    public static String URL_TERMS = "http://13.234.164.40/friendlymony-website/terms.html";

    {to=dA9IPDSSRP2wcv3egiTwmd:APA91bHHjMyrnmy1selvgjHXhRwbQ3vOs32Gyyr61Synd19OQttIOeB9zRuDB4JXErYKd4ocCb5zdlFbYdTy1pZ6ToEk5v89GeCwrZK959AoqV1o-qVwunIT4vxz6l35wigcO5EICbWW, data={pushType=2, click_action=chat, userId=48, image=, content_available=true, body=Hello, senderId=48, senderName=Dhaval Patel, fcmToken=dA9IPDSSRP2wcv3egiTwmd:APA91bHHjMyrnmy1selvgjHXhRwbQ3vOs32Gyyr61Synd19OQttIOeB9zRuDB4JXErYKd4ocCb5zdlFbYdTy1pZ6ToEk5v89GeCwrZK959AoqV1o-qVwunIT4vxz6l35wigcO5EICbWW, Title=Dhaval Patel, title=Dhaval Patel, conversationId=40, priority=high, senderProfile=http://3.15.38.50/api/uploads/48/ih1yod2i-1-48.jpg}, notification={pushType=2, click_action=chat, userId=48, image=, content_available=true, body=Hello, sound=.sound, senderId=48, senderName=Dhaval Patel, fcmToken=dA9IPDSSRP2wcv3egiTwmd:APA91bHHjMyrnmy1selvgjHXhRwbQ3vOs32Gyyr61Synd19OQttIOeB9zRuDB4JXErYKd4ocCb5zdlFbYdTy1pZ6ToEk5v89GeCwrZK959AoqV1o-qVwunIT4vxz6l35wigcO5EICbWW, Title=Dhaval Patel, title=Dhaval Patel, conversationId=40, priority=high, senderProfile=http://3.15.38.50/api/uploads/48/ih1yod2i-1-48.jpg}}

    public static String URL_PRIVACY = "http://13.234.164.40/friendlymony-website/privacypolicy.html";*/

    public static String URL_ABOUT = "https://friendlymony.com/aboutus.html";
    public static String URL_TERMS = "https://friendlymony.com/terms.html";
    public static String URL_PRIVACY = "https://friendlymony.com/privacypolicy.html";
    public static String APPLE_LOGIN_TOKEN = APPLE_LOGIN_BASE_URL + "token";

    public static String RAZOR_KEY = "rzp_test_KLeoyWgjuFMuo8";
    public static String RAZOR_SECRETE = "dt0FU2YmZr7IPgWVAKwvRHha";
    public static String[] PERMISSIONS_PIC = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
    };

    public static boolean checkEmail(String eml) {

        return (!TextUtils.isEmpty(eml) && Patterns.EMAIL_ADDRESS.matcher(eml).matches());
    }

    public static boolean hasStorageCameraPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean CheckLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        //  mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public static String getCity(Context context, double latitude, double longitude) {

        String city = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            //  String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            //String cAddress = address;// + ", " + city + ", " + postalCode + ", " + state + ", " + country;
            String Mycity = addresses.get(0).getLocality();
            //String postal = addresses.get(0).getPostalCode();
            if (!(city + "").equals("null")) {
                city = Mycity + "";
            }
        } catch (Exception e) {
        }

        return city;
    }


    public static boolean isGPSOn(Context context) {
        boolean isGPSOn;
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // check if GPS enabled
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            if (mLocationManager != null) {
                gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception ex) {
            Log.e("LOG_CAT", "isGPSOn=Exception==" + ex.getMessage());
        }

        try {
            if (mLocationManager != null) {
                network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception ex) {
            Log.e("LOG_CAT", "isGPSOn=Exception==" + ex.getMessage());
        }

        isGPSOn = gps_enabled && network_enabled;
        return isGPSOn;
    }

    // Method to check internet connection
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //GET ADDRESS FROM LAT LONG
    public static Address getAddressFromLatLong(Context context, double MyLat, double MyLong) {

        //PASS USER SELECTED LANGUAGE
        Locale locale = new Locale("en");

        Geocoder geocoder = new Geocoder(context, locale);
        List<Address> addresses = null;
        Address currentAddress = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
            if (addresses != null) {
                Log.e("LOG_CAT", "Location=" + addresses.size() + "");
                if (addresses.size() > 0) {
                    Log.e("LOG_CAT", "Location=" + addresses.get(0).toString() + "");
                } else {
                    Log.e("LOG_CAT", "Location=Size=0");
                }
            } else {
                Log.e("LOG_CAT", "Location=00000");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LOG_CAT", "" + e.toString());
        }
        if (addresses != null && addresses.size() > 0) {
            currentAddress = addresses.get(0);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);

            Log.e("LOG_CAT", cityName + "\n" + stateName + "\n" + countryName);
            //return cityName;
        }

        return currentAddress;
    }

    // GET LAT LONG FROM ADDRESS
    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        LatLng latLong = null;
        Geocoder geoCoder = new Geocoder(context);
        List<Address> addressList = null;
        try {
            addressList = geoCoder.getFromLocationName(strAddress, 1);
            Address address = addressList.get(0);
            if (address.hasLatitude() && address.hasLongitude()) {
                double selectedLat = address.getLatitude();
                double selectedLng = address.getLongitude();
                latLong = new LatLng(selectedLat, selectedLng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return latLong;
    }

    public static void showLog(Context mContext, String msg) {
        try {
            Log.e("FRIENDLYMONY APP LOG", mContext.getClass().getSimpleName() + " :: " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateCardExpiryDate(String expiryDate) {
        return expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    public static String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today";
        } /*else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday";
        }*/ /*else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        }*/ else {
            return DateFormat.format("dd/MM/yyyy", smsTime).toString();
        }

    }

    public static long getMilliFromDate(String date) {
        //String date = "Tue Apr 23 16:08:28 GMT+05:30 2013";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date mDate = null;
        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeInMilliseconds = mDate.getTime();
        return timeInMilliseconds;
    }
}