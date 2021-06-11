package com.nect.friendlymony.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nect.friendlymony.Model.CommonResponse.CommonParam;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.WebService;
import com.nect.friendlymony.Utils.AppUtils;

public class WebviewTest2Activity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        WebView webView = (WebView) findViewById(R.id.webView);
        TextView contentView = (TextView) findViewById(R.id.contentView);

        /* An instance of this class will be registered as a JavaScript interface */
        class MyJavaScriptInterface {
            private TextView contentView;

            public MyJavaScriptInterface(TextView aContentView) {
                contentView = aContentView;
            }

            @SuppressWarnings("unused")

            public void processContent(String aContent) {
                final String content = aContent;
                Toast.makeText(WebviewTest2Activity.this, aContent, Toast.LENGTH_SHORT).show();
                contentView.post(() -> {
                    contentView.setText(content);
                    Toast.makeText(WebviewTest2Activity.this, aContent, Toast.LENGTH_SHORT).show();
                });
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('success')[0].innerText);");
            }
        });

        //webView.loadUrl("https://appleid.apple.com/auth/authorize?response_type=code&v=1.1.6&response_mode=form_post&client_id=com.nect.friendlymony&scope=name%20email&state=7bb9e22b-a5b1-444b-bdc2-4c1eac81e011&redirect_uri=https://sweetshe.in/admin/api/appleCallBack");

        //callUserBlockAPI();
    }


    @SuppressLint("StaticFieldLeak")
    private void callUserBlockAPI() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                String result = null;
                try {
                    //LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
                    Gson gson = new Gson();
                    CommonParam commonParam = new CommonParam();
                    commonParam.setUserId("443");
                    commonParam.setIs_online("1");
                    String json = gson.toJson(commonParam);

                    String registerUrl = AppUtils.UPDATE_USER;
                    result = WebService.callApi(commonParam, json, registerUrl, false);

                    AppUtils.showLog(WebviewTest2Activity.this, "callUserBlockAPI==json==" + json);
                    AppUtils.showLog(WebviewTest2Activity.this, "callUserBlockAPI==result==" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppUtils.showLog(WebviewTest2Activity.this, "callUserBlockAPI==Exception==" + e.toString());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    Gson gson = new Gson();
                    CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);

                    Toast.makeText(WebviewTest2Activity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    e.printStackTrace();
                    AppUtils.showLog(WebviewTest2Activity.this, "callUserBlockAPI==Exception==" + e.toString());
                    Toast.makeText(WebviewTest2Activity.this, getString(R.string.msg_something_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

}
