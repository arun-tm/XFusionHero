package com.teramatrix.xfusionhero;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.teramatrix.xfusionhero.controller.RESTClient;
import com.teramatrix.xfusionhero.utils.DateUtils;
import com.teramatrix.xfusionhero.utils.GeneralUtils;
import com.teramatrix.xfusionhero.utils.SPUtils;

import org.json.JSONObject;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by arun.singh on 10/24/2016.
 */
public class LoginActivity extends AppCompatActivity {


    private AlertDialog dialog;
    private okhttp3.Call call;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTracker = ((XFusionHero) getApplication()).getDefaultTracker();

        String token = new SPUtils(this).getToken();
        if (token != null && token.length() > 0) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        //change status bar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        initView();
    }

    private void initView() {


        SPUtils spUtils = new SPUtils(this)  ;
        boolean last_remember_me_status = spUtils.getRememberMeStatus();
        //Set remember me status
        ((CheckBox) findViewById(R.id.cb_remember_me)).setChecked(last_remember_me_status);

        //set userid if  user has checked remember me optino in previous login
        if (last_remember_me_status)
            ((EditText) findViewById(R.id.ed_login_id)).setText(new SPUtils(this).getString(SPUtils.USER_ID));


        //set Action for DOne button click.
        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GeneralUtils.isConnected(LoginActivity.this, R.layout.pop_up_dialog)) {
                    if (validateForm()) {
                        login();
                    }
                }

            }
        });
        dialog = new SpotsDialog(this, R.style.Custom);


        //Set Terms and Privacy String
        TextView tv_terms = (TextView) findViewById(R.id.txt_terms_and_condition);
        String messag = "By login, you agree to our Terms and Privacy Policy";
        SpannableString spannableString = new SpannableString(messag);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), messag.indexOf("Terms and Privacy Policy"), messag.length(), 0);
        spannableString.setSpan(new URLSpan("http://playstore.teramatrix.in/"), messag.indexOf("Terms and Privacy Policy"), messag.length(), 0);
        tv_terms.setMovementMethod(LinkMovementMethod.getInstance());
        tv_terms.setText(spannableString);

    }

    private boolean validateForm() {

        findViewById(R.id.txt_invalid_login_id_message).setVisibility(View.INVISIBLE);
        findViewById(R.id.txt_invalid_password_message).setVisibility(View.INVISIBLE);

        String ed_login_id = ((EditText) findViewById(R.id.ed_login_id)).getText().toString();
        String ed_password = ((EditText) findViewById(R.id.ed_password)).getText().toString();

        if (!ed_login_id.equals(null) && ed_login_id.isEmpty()) {
            //Show Empty Login Id
            findViewById(R.id.txt_invalid_login_id_message).setVisibility(View.VISIBLE);
            return false;
        } else if (!ed_password.equals(null) && ed_password.isEmpty()) {
            //Show Empty password
            findViewById(R.id.txt_invalid_password_message).setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void login() {
        final String username = ((EditText) findViewById(R.id.ed_login_id)).getText().toString();
        final String password = ((EditText) findViewById(R.id.ed_password)).getText().toString();

        dialog.show();
        String body = "username=" + username +
                "&password=" + password +
                "&applicationid=" + "da9c8f88-588f-11e6-85b9-fe984cc15272";

        call = RESTClient.Login(this, body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
                showToast("Error in Login");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {

                                JSONObject jsonObject1 = jsonObject.getJSONObject("object");

                                String access_token = jsonObject1.getString("access_token");
                                String userKey = jsonObject1.getString("userKey");
                                String access_key = jsonObject1.getString("access_key");
                                String user_id = jsonObject1.getString("user_id");

                                SPUtils spUtils = new SPUtils(LoginActivity.this);
                                spUtils.setValue(SPUtils.ACCESS_TOKEN, access_token);
                                spUtils.setValue(SPUtils.ACCESS_KEY, access_key);
                                spUtils.setValue(SPUtils.USER_KEY, userKey);
                                spUtils.setValue(SPUtils.USER_ID, user_id);


                                if (mTracker != null) {
                                    mTracker.setScreenName("User Login");
                                    mTracker.set("&cd1", user_id);
                                    mTracker.set("&cd2", DateUtils.getCurrentIST_Time());
                                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //save remember me status
                                        CheckBox checkBox = (CheckBox) findViewById(R.id.cb_remember_me);
                                        new SPUtils(LoginActivity.this).setRememberMeStatus(checkBox.isChecked());
                                        //redirect user to main activity/landing page of app
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });


                            } else {

                                if (jsonObject.has("description")) {
                                    showToast(jsonObject.getString("description"));
                                }

                            }
                        } else {
                            showToast("Error in Login");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("Error in Login Response");

                    } finally {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    showToast("Error in Login Response");
                }
            }
        });
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) call.cancel();
    }
}
