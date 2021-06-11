package com.nect.friendlymony.Retrofit;


import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.nect.friendlymony.Model.CommonResponse.CommonParam;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebService {

    public static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static String upload(String url, File file) throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(50000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Profileimage", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                .addFormDataPart("Userid", "")
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }


    //Update Profile API
    /*public static String updateProfile(String url, String profilePath, CommonParam paramLoginRegister, boolean isProfilePicked) throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(50000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();


        RequestBody formBody;
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                // .addFormDataPart("profile_image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                .addFormDataPart("loginuser_id", paramLoginRegister.getLoginuser_id())
                .addFormDataPart("session_token", paramLoginRegister.getSession_token())
                .addFormDataPart("user_type", paramLoginRegister.getUser_type())
                .addFormDataPart("mobile_no", paramLoginRegister.getMobile_no())
                .addFormDataPart("user_name", paramLoginRegister.getUser_name())
                .addFormDataPart("email_id", paramLoginRegister.getEmail_id())
                .addFormDataPart("country_code", paramLoginRegister.getCountry_code())
                .addFormDataPart("country_code_info", paramLoginRegister.getCountry_code_info())
                .addFormDataPart("currency_code", paramLoginRegister.getCurrency_code())
                .addFormDataPart("currency_symbol", paramLoginRegister.getCurrency_symbol());
        //  .build();

        File file = null;
        MediaType MEDIA_TYPE = profilePath.endsWith("png") ?
                MediaType.parse("image/png") : MediaType.parse("image/jpeg");
        if (!profilePath.equalsIgnoreCase("") && isProfilePicked) {
            file = new File(profilePath);
            buildernew.addFormDataPart("profile_image", file.getName(), RequestBody.create(MEDIA_TYPE, file));
        } else {
            if (!isProfilePicked) {
                file = new File(profilePath);
                buildernew.addFormDataPart("profile_image", file.getName(), RequestBody.create(MEDIA_TYPE, file));
            } else {
                buildernew.addFormDataPart("profile_image", "");
            }
            buildernew.addFormDataPart("profile_image", "");
        }
        formBody = buildernew.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }*/


    //Common API
    public static String callApi(CommonParam paramLoginRegister,String params, String fullUrl, boolean isToken) {
        String responseString = "", accessToken = "";
        try {

            //String newParam = params.replaceAll("\"\"","");
            params = params.replaceAll("\"", "");
            params = params.replaceAll(":", "=");
            params = params.replaceAll(",", "&");
            params = params.replaceAll("[\\\\{+\\[\\]']", "");
            params = params.replaceAll("[\\\\}+\\[\\]']", "");
            if (HawkAppUtils.getInstance().getUSERDATA() != null) {
                accessToken = HawkAppUtils.getInstance().getUSERDATA().getToken();
            }

            Log.e("RetrofitBuilder", "Authorization==" + accessToken);
            Log.e("RetrofitBuilder", "params==" + params);

            final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(50000, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            final Request request;
            if (params != null && !params.isEmpty()) {
                //POST METHOD
                RequestBody body = RequestBody.create(mediaType, params);
                if (isToken) {
                    request = new Request.Builder()
                            .url(fullUrl)
                            .method("POST", body)
                            .addHeader("Authorization", TextUtils.isEmpty(accessToken) ? "" : accessToken)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(fullUrl)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Accept", "application/json")
                            .build();
                }

            } else {
                //GET METHOD
                /*request = new Request.Builder()
                        .url(fullUrl)
                        .addHeader("Content-Type", JSON.toString());*/

                request = new Request.Builder()
                        .url(fullUrl)
                        .method("GET", null)
                        .build();
            }

            /*OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "userId=540&toUserId=523&mainUserId=");
            Request request = new Request.Builder()
                    .url("http://18.223.187.132:3000/v1/blockUser")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();*/

            //final Response response = client.newCall(request.build()).execute();
            Response response = okHttpClient.newCall(request).execute();
            /*final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(50000, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            RequestBody formBody;
            MultipartBody.Builder buildernew = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    // .addFormDataPart("profile_image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file))
                    .addFormDataPart("userId", paramLoginRegister.getUserId())
                    .addFormDataPart("toUserId", paramLoginRegister.getToUserId())
                    .addFormDataPart("mainUserId", paramLoginRegister.getMainUserId());


            formBody = buildernew.build();
            Request request = new Request.Builder().url(fullUrl).post(formBody).build();
            Response response = okHttpClient.newCall(request).execute();*/
            //Log.d(WebService.class.getSimpleName(), String.format("Response String : %s", responseString));

            responseString = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;

    }

    public static String callgetLaudaryListApi(String url, String login_user_id, String session_token, String userType, String page, String lang_type, String latitude, String longitude, String service_type) throws IOException {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .build();

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("loginuser_id", login_user_id)
                .addFormDataPart("session_token", session_token)
                .addFormDataPart("user_type", userType)
                .addFormDataPart("page", page)
                .addFormDataPart("latitude", latitude)
                .addFormDataPart("longitude", longitude)
                .addFormDataPart("lang_type", lang_type)
                .addFormDataPart("service_type", service_type)
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private static RequestBody generateRequest(ArrayList<ArrayMap<String, String>> params) {

        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null) {
            ArrayMap<String, String> map;
            String parameters = "";
            for (int i = 0; i < params.size(); i++) {
                map = params.get(i);
                if (map != null) {
                    parameters += map.get("key") + ":";
                    parameters += map.get("value");
                    parameters += "\n";
                    if (map.get("key") != null && map.get("value") != null) {
                        formBody.add(map.get("key"), map.get("value"));
                    }
                }
            }
            Log.d("parameters", parameters);
        }
        return formBody.build();
    }


}
