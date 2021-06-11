package com.nect.friendlymony.Activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.graphics.Rect
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.nect.friendlymony.Model.CommonResponse.CommonParam
import com.nect.friendlymony.Retrofit.RetrofitBuilder
import com.nect.friendlymony.Retrofit.WebService
import com.nect.friendlymony.Utils.AppUtils
import com.nect.friendlymony.Utils.AppleConstants
import com.nect.friendlymony.Utils.Constants
import com.nect.friendlymony.Utils.Pref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*

class AppleSignInActivity : AppCompatActivity() {

    lateinit var appleAuthURLFull: String
    lateinit var appledialog: Dialog
    lateinit var appleAuthCode: String
    lateinit var appleClientSecret: String
    lateinit var appleState: String
    var isHandle = false

    var appleId = ""
    var appleFirstName = ""
    var appleMiddleName = ""
    var appleLastName = ""
    var appleEmail = ""
    var appleAccesToken = ""
    var TAG = "AppleSignInActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_apple_sign_in)

        val state = UUID.randomUUID().toString()

        appleAuthURLFull = AppleConstants.AUTHURL + "?response_type=code&v=1.1.6&response_mode=form_post&client_id=" + AppleConstants.CLIENT_ID + "&scope=" +
                AppleConstants.SCOPE + "&state=" + state + "&redirect_uri=" + AppleConstants.REDIRECT_URI

        //setupAppleWebviewDialog(appleAuthURLFull)

        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = AppleWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(appleAuthURLFull)
        setContentView(webView)
    }


    // Show 'Sign in with Apple' login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupAppleWebviewDialog(url: String) {
        appledialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = AppleWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        appledialog.setContentView(webView)
        appledialog.show()
    }


    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class AppleWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
        ): Boolean {
            if (request != null) {
                Log.e(TAG + "UrlLoading startsWith==", request.url.toString())
            }
            if (request!!.url.toString().startsWith(AppleConstants.REDIRECT_URI)) {
                Log.e(TAG + "UrlLoading==", request.url.toString())
                handleUrl(request.url.toString())

                // Close the dialog after getting the authorization code
                /*if (request.url.toString().contains("success=")) {
                    appledialog.dismiss()
                }*/
                return true
            }
            return true
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(AppleConstants.REDIRECT_URI)) {
                Log.e(TAG + "UrlLoading==", url)
                handleUrl(url)
                // Close the dialog after getting the authorization code
                /*if (url.contains("success=")) {
                    appledialog.dismiss()
                }*/
                return true
            }
            Log.e(TAG + "UrlLoading==", url)
            return false
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // retrieve display dimensions
            val displayRectangle = Rect()
            val window = this@AppleSignInActivity.window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

            //WebView(this@AppleSignInActivity).loadUrl("javascript:window.HtmlViewer.showHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

            // Set height of the Dialog to 90% of the screen
            val layoutparms = view?.layoutParams
            layoutparms?.height = (displayRectangle.height() * 0.9f).toInt()
            view?.layoutParams = layoutparms
            Log.e(TAG + "onPageFinished==", url.toString())
            if (url.equals(AppleConstants.REDIRECT_URI)) {
                if (!isHandle) {
                    isHandle = true
                    handleUrl(url.toString())
                }
                //appledialog.dismiss() //https://sweetshe.in/admin?success=false
            } /*else if (url.toString().contains("https://sweetshe.in/admin?success")) {
                handleUrl(url.toString())
                appledialog.dismiss()
            }*/

        }

        // Check webview url for access token code or error
        @SuppressLint("LongLogTag")
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)

            Log.e(TAG + "handleUrl==", url)
            val success = uri.getQueryParameter("success")
            if (success == "true") {

                // Get the Authorization Code from the URL
                appleAuthCode = uri.getQueryParameter("code") ?: ""
                Log.e(TAG, appleAuthCode)

                // Get the State from the URL
                appleState = uri.getQueryParameter("state") ?: ""
                Log.e(TAG, appleState)

                // Get the Client Secret from the URL
                appleClientSecret = uri.getQueryParameter("client_secret") ?: ""
                Log.e(TAG, appleClientSecret ?: "")

                //Check if user gave access to the app for the first time by checking if the url contains their email
                if (url.contains("email")) {
                    //Get user's First Name
                    val firstName = uri.getQueryParameter("first_name")
                    Log.e(TAG, firstName ?: "")
                    appleFirstName = firstName ?: ""

                    //Get user's Middle Name
                    val middleName = uri.getQueryParameter("middle_name")
                    Log.e(TAG, middleName ?: "")
                    appleMiddleName = middleName ?: ""

                    //Get user's Last Name
                    val lastName = uri.getQueryParameter("last_name")
                    Log.e(TAG, lastName ?: "")
                    appleLastName = lastName ?: ""

                    //Get user's email
                    val email = uri.getQueryParameter("email")
                    Log.e(TAG, email ?: "")
                    appleEmail = email ?: ""
                }

                Pref.setStringValue(this@AppleSignInActivity, Constants.CURRENT_BASE_URL, "1")
                //getAppleInfo(this@AppleSignInActivity, appleAuthCode, appleClientSecret)

                // Exchange the Auth Code for Access Token
                AppleRequestForAccessToken(
                        this@AppleSignInActivity,
                        appleAuthCode,
                        appleState,
                        url,
                        appleClientSecret
                ).execute()

                //finish()
                /*Pref.setStringValue(this@AppleSignInActivity, Constants.APPLE_ID, appleAuthCode.toString())
                Pref.setStringValue(this@AppleSignInActivity, Constants.APPLE_FIRST_NAME, appleFirstName.toString())
                Pref.setStringValue(this@AppleSignInActivity, Constants.APPLE_LAST_NAME, appleLastName.toString())
                Pref.setStringValue(this@AppleSignInActivity, Constants.APPLE_EMAIL, appleEmail.toString())
                Pref.setStringValue(this@AppleSignInActivity, Constants.APPLE_ACCESS_TOKEN, appleClientSecret.toString())

                Log.e(TAG, Pref.getStringValue(this@AppleSignInActivity, Constants.APPLE_ID, ""))
                Log.e(TAG, Pref.getStringValue(this@AppleSignInActivity, Constants.APPLE_FIRST_NAME, ""))
                Log.e(TAG, Pref.getStringValue(this@AppleSignInActivity, Constants.APPLE_LAST_NAME, ""))
                Log.e(TAG, Pref.getStringValue(this@AppleSignInActivity, Constants.APPLE_EMAIL, ""))
                Log.e(TAG, Pref.getStringValue(this@AppleSignInActivity, Constants.APPLE_ACCESS_TOKEN, ""))
                this@AppleSignInActivity.setResult(1002)
                finish()*/
            } else if (success == "false") {
                Pref.setStringValue(this@AppleSignInActivity, Constants.CURRENT_BASE_URL, "2")
                Log.e(TAG, "We couldn't get the Auth Code")
                Toast.makeText(
                        this@AppleSignInActivity,
                        "We couldn't get the Auth Code",
                        Toast.LENGTH_LONG
                ).show()
                finish()
            } else {
                Pref.setStringValue(this@AppleSignInActivity, Constants.CURRENT_BASE_URL, "2")
                Toast.makeText(
                        this@AppleSignInActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private class AppleRequestForAccessToken
    internal constructor(context: AppleSignInActivity, authCode: String, authState: String, url: String, clientSecret: String) :
            AsyncTask<Void, Void, Void>() {

        private val activityReference: WeakReference<AppleSignInActivity> = WeakReference(context)

        var code = ""
        var state = ""
        var clientsecret = ""
        var urlCallback = ""
        val grantType = "authorization_code"

        init {
            this.code = authCode
            this.state = authState
            this.clientsecret = clientSecret
            this.urlCallback = url
        }

        val postParamsForAuth =
                "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" + AppleConstants.REDIRECT_URI + "&client_id=" + AppleConstants.CLIENT_ID + "&client_secret=" + clientsecret

        //val postParamsForRefreshToken = "grant_type=" + grantType + "&client_id=" + AppleConstants.CLIENT_ID + "&client_secret=" + clientsecret + "&refresh_token" + "REFRESH_TOKEN_FROM_THE_AUTH"

        override fun doInBackground(vararg params: Void): Void? {
            var result: String? = null
            try {
                var TAG = "AppleSignInActivity";
                val url = URL(AppleConstants.TOKENURL)

                /*val httpsURLConnection = url.openConnection() as HttpsURLConnection
                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.setRequestProperty(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                )
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true
                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                outputStreamWriter.write(postParamsForAuth)
                outputStreamWriter.flush()
                val response = httpsURLConnection.inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
                val jsonObject = JSONTokener(response).nextValue() as JSONObject

                val activity = activityReference.get()

                val accessToken = jsonObject.getString("access_token") //Here is the access token
                Log.e(TAG, accessToken)
                activity?.appleAccesToken = accessToken

                val expiresIn = jsonObject.getInt("expires_in") //When the access token expires
                Log.e(TAG, expiresIn.toString())

                val refreshToken = jsonObject.getString("refresh_token") // The refresh token used to regenerate new access tokens. Store this token securely on your server.
                Log.e(TAG, refreshToken)

                val idToken = jsonObject.getString("id_token") // A JSON Web Token that contains the user’s identity information.
                Log.e(TAG, idToken)

                // Get encoded user id by spliting idToken and taking the 2nd piece
                val encodedUserID = idToken.split(".")[1]

                //Decode encodedUserID to JSON
                val decodedUserData = String(Base64.decode(encodedUserID, Base64.DEFAULT))
                val userDataJsonObject = JSONObject(decodedUserData)
                // Get User's ID
                val userId = userDataJsonObject.getString("iat")
                Log.e(TAG, userId)
                activity?.appleId = userId*/

                val gson = Gson()
                val commonParam = CommonParam()
                commonParam.grant_type = "authorization_code"
                commonParam.code = code
                commonParam.redirect_uri = AppleConstants.REDIRECT_URI
                commonParam.client_id = AppleConstants.CLIENT_ID
                commonParam.client_secret = clientsecret
                //commonParam.refresh_token = "refresh_token"
                val json = gson.toJson(commonParam)
                Pref.setStringValue(activityReference.get(), Constants.CURRENT_BASE_URL, "1")
                val registerUrl = AppUtils.APPLE_LOGIN_TOKEN
                result = WebService.callApi(commonParam, json, registerUrl, false)

                AppUtils.showLog(activityReference.get(), "callUserBlockAPI==json==$json")
                AppUtils.showLog(activityReference.get(), "callUserBlockAPI==result==$result")
                Pref.setStringValue(activityReference.get(), Constants.APPLE_RESPONSE, result)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("AppleSignInActivity", e.message)
                //Toast.makeText(activityReference.get(), e.message, Toast.LENGTH_LONG).show()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            // get a reference to the activity if it is still there
            val activity = activityReference.get()
            val gson = Gson()
            //val commonResponse: AppleLoginResponse = gson.fromJson(result, AppleLoginResponse::class.java)


            //val commonResponse: AppleLoginResponse = gson.fromJson<AppleLoginResponse>(result, AppleLoginResponse::class.java)
            /*if (activity != null) {
                Toast.makeText(activityReference.get(), activity.appleFirstName, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activityReference.get(), "activity null", Toast.LENGTH_LONG).show()
            }*/
            if (activity == null || activity.isFinishing) return
            /*val myIntent = Intent(activity, DetailsActivity::class.java)
            myIntent.putExtra("apple_id", activity.appleId)
            myIntent.putExtra("apple_first_name", activity.appleFirstName)
            myIntent.putExtra("apple_middle_name", activity.appleMiddleName)
            myIntent.putExtra("apple_last_name", activity.appleLastName)
            myIntent.putExtra("apple_email", activity.appleEmail)
            myIntent.putExtra("apple_access_token", activity.appleAccesToken)
            activity.startActivity(myIntent)*/

            /*if (commonResponse.error != null) {
                if (commonResponse.error.equals("invalid_client")) {
                    activity.setResult(1002)
                    activity.finish()
                } else {
                    //Toast.makeText(activityReference.get(), activity.appleLastName, Toast.LENGTH_LONG).show()
                }
            }*/

            //Pref.setStringValue(activityReference.get(), Constants.APPLE_RESPONSE, result.toString())
            Pref.setStringValue(activityReference.get(), Constants.APPLE_ID, activity.appleId)
            Pref.setStringValue(activityReference.get(), Constants.APPLE_FIRST_NAME, activity.appleFirstName)
            Pref.setStringValue(activityReference.get(), Constants.APPLE_LAST_NAME, activity.appleLastName)
            Pref.setStringValue(activityReference.get(), Constants.APPLE_EMAIL, activity.appleEmail)
            Pref.setStringValue(activityReference.get(), Constants.APPLE_ACCESS_TOKEN, activity.appleAccesToken)
            activity.setResult(1002)
            activity.finish()

        }
    }


    private fun getAppleInfo(context: AppleSignInActivity, appleAuthCode: String, appleClientSecret: String) {
        //val lp = HawkAppUtils.getInstance().userdata
        // "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" + AppleConstants.REDIRECT_URI + "&client_id=" + AppleConstants.CLIENT_ID + "&client_secret=" + clientsecret

        val activityReference: WeakReference<AppleSignInActivity> = WeakReference(context)
        val activity = activityReference.get()
        val request: MutableMap<String, Any> = HashMap()
        request["grant_type"] = "authorization_code"
        request["code"] = appleAuthCode
        request["redirect_uri"] = AppleConstants.REDIRECT_URI
        request["client_id"] = AppleConstants.CLIENT_ID
        request["client_secret"] = appleClientSecret
        val call = RetrofitBuilder.getInstance().retrofit.getAppleLoginInfo(request)
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    Log.e(TAG, "Response==$response")
                    if (activity == null || activity.isFinishing) return
                    Pref.setStringValue(activityReference.get(), Constants.APPLE_ID, activity.appleId)
                    Pref.setStringValue(activityReference.get(), Constants.APPLE_FIRST_NAME, activity.appleFirstName)
                    Pref.setStringValue(activityReference.get(), Constants.APPLE_LAST_NAME, activity.appleLastName)
                    Pref.setStringValue(activityReference.get(), Constants.APPLE_EMAIL, activity.appleEmail)
                    Pref.setStringValue(activityReference.get(), Constants.APPLE_ACCESS_TOKEN, activity.appleAccesToken)
                    activity.setResult(1002)
                    activity.finish()

                } else {
                    if (activity == null || activity.isFinishing) {
                        activity?.setResult(1003)
                        activity?.finish()
                    } else {
                        activity.setResult(1003)
                        activity.finish()
                        return
                    }

                }
                Pref.setStringValue(this@AppleSignInActivity, Constants.CURRENT_BASE_URL, "2")
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e(TAG, "Response==${t.message}")
                Pref.setStringValue(this@AppleSignInActivity, Constants.CURRENT_BASE_URL, "2")
            }
        })
    }


}
