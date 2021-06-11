package com.nect.friendlymony.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.AppUtils;

import im.delight.android.webview.AdvancedWebView;

public class PaymentWebviewActivity extends BaseAppCompatActivity implements AdvancedWebView.Listener {

    String loadUrl = "", fromIntent = "";
    AdvancedWebView webView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);

        webView = findViewById(R.id.webView);
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, "Payment details", false);
        loadUrl = getIntent().getStringExtra("url");
        fromIntent = getIntent().getStringExtra("From");

        showProgress();
        webView.setListener(this, this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setGeolocationEnabled(false);
        webView.setMixedContentAllowed(true);
        webView.setCookiesEnabled(true);
        webView.clearCache(true);
        webView.setThirdPartyCookiesEnabled(true);
        webView.setDesktopMode(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equalsIgnoreCase("https://friendlymony.com/stripepayment/success_payment.php")) {
                    Toast.makeText(PaymentWebviewActivity.this, "Payment successfully done", Toast.LENGTH_LONG).show();
                    finish();
                } else if (url.equalsIgnoreCase("https://friendlymony.com/stripepayment/failure_payment.php")) {
                    Toast.makeText(PaymentWebviewActivity.this, "Payment fail", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //Toast.makeText(otpWebviewActivity, title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                AppUtils.showLog(PaymentWebviewActivity.this, "newProgress==" + newProgress);

                if (newProgress > 99) {
                    hideProgress();
                }
            }
        });
        webView.addHttpHeader("X-Requested-With", "");

        webView.loadUrl(loadUrl);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        // ...
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("from", fromIntent);
        setResult(RESULT_OK, intent);
        if (!webView.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        AppUtils.showLog(PaymentWebviewActivity.this, "onPageStarted==" + url);

        if (url.equalsIgnoreCase("https://friendlymony.com/stripepayment/success_payment.php")) {
            Toast.makeText(PaymentWebviewActivity.this, "Payment successfully done", Toast.LENGTH_LONG).show();
            finish();
        } else if (url.equalsIgnoreCase("https://friendlymony.com/stripepayment/failure_payment.php")) {
            Toast.makeText(PaymentWebviewActivity.this, "Payment fail", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onPageFinished(String url) {
        AppUtils.showLog(PaymentWebviewActivity.this, "onPageFinished==" + url);

        if (url.equalsIgnoreCase("https://friendlymony.com/stripepayment/success_payment.php")) {
            Toast.makeText(PaymentWebviewActivity.this, "Payment successfully done", Toast.LENGTH_LONG).show();
            finish();
        } else if (url.equalsIgnoreCase("https://friendlymony.com/stripepayment/failure_payment.php")) {
            Toast.makeText(PaymentWebviewActivity.this, "Payment fail", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        AppUtils.showLog(PaymentWebviewActivity.this, "onPageError==" + failingUrl);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }


    protected void showProgress() {

        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /**
     * Hide progress dialog.
     */
    protected void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.cancel();
    }


}