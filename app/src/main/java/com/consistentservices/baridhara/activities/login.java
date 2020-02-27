package com.consistentservices.baridhara.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.miscellaneousFunctions.sharedPreferenceData;
import com.consistentservices.baridhara.networkFunction.url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class login extends AppCompatActivity
{
    private CardView login_card;
    private Button logIn;
    private TextView forgotPassword;
    private TextView signUp;
    private CheckBox remember;
    private EditText mobileNumber;
    private EditText password;
    private Dialog messageDialog;
    private Dialog progressDialog;
    private TextView message;
    private Button okay_red;

    private Animation left_to_right;

    private String LOGIN_API = String.format("%s%s", url.BASE_URL, url.SUFFIX_LOGIN);
    private String Mobile = "";
    private String Password = "";

    //Shared Preference
    private static final String PreferenceName = sharedPreferenceData.MyPREFERENCES;
    private static final String key_mobile = sharedPreferenceData.Mobile_key;
    private static final String key_password = sharedPreferenceData.Password_key;
    private int ClearFlag = 0;

    //Flag for Auto Login
    private int AutoLoginFlag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hiding Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Fetching Auto Login Flag
        Intent i = getIntent();
        AutoLoginFlag = i.getIntExtra("Flag",1);
        ClearFlag = i.getIntExtra("ClearFlag",0);

        //UI Elements Functions
        logIn = (Button)findViewById(R.id.login);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        signUp = (TextView) findViewById(R.id.signUp);
        remember = (CheckBox) findViewById(R.id.remember);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        password = (EditText) findViewById(R.id.password);
        login_card = (CardView) findViewById(R.id.login_card);

        messageDialog = new Dialog(login.this);
        progressDialog = new Dialog(login.this);


        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn();
            }
        });

        //On Click Listener
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signUp();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                forgotPassword();
            }
        });

        //Animation Function
        Animation();
        checkAutoLogin();
        clearCredential();
        Toast.makeText(this,"cLEAN fLAG: "+ClearFlag,Toast.LENGTH_SHORT).show();
    }

    private void Animation()
    {
        left_to_right = AnimationUtils.loadAnimation(this, R.anim.move_left_to_right);
        login_card.setAnimation(left_to_right);
    }

    //Validating Our Text Inputs
    private Boolean validateMobile()
    {
        if(String.valueOf(mobileNumber.getText()).isEmpty())
        {
            mobileNumber.setError("Field can't be empty");
            return false;
        }

        if(mobileNumber.getText().toString().trim().length() == 0)
        {
            mobileNumber.setError("Field can't be empty");
            return false;
        }

        if(mobileNumber.getText().toString().trim().length() < 10)
        {
            mobileNumber.setError("Enter A Valid 10 Digit Mobile Number");
            return false;
        }

        else
        {
            mobileNumber.setError(null);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        if(String.valueOf(password.getText()).trim().isEmpty())
        {
            password.setError("Field can't be empty");
            return false;
        }

        if(password.getText().toString().trim().length() == 0)
        {
            password.setError("Field can't be empty");
            return false;
        }

        else
        {
            password.setError(null);
            return true;
        }
    }

    //UI Navigation Functions
    private void goToMainActivity()
    {
        Intent mainScreen = new Intent(login.this, MainActivity.class);
        startActivity(mainScreen);
        finish();
    }

    private void signUp()
    {
        Intent Signup = new Intent(login.this, signup.class);
        startActivity(Signup);
    }

    private void forgotPassword()
    {
        Intent forgot = new Intent(login.this, resetPassword.class);
        startActivity(forgot);
    }

    private void OTPVerification()
    {
        Intent OTPVerify = new Intent(login.this, otpVerification.class);
        OTPVerify.putExtra("MobileNumber",mobileNumber.getText().toString().trim());
        OTPVerify.putExtra("OTP_FLAG","1");
        startActivity(OTPVerify);
    }

    private void signIn()
    {
        if(!validateMobile() | !validatePassword())
        {
            return;
        }

        String Mobile = mobileNumber.getText().toString().trim();
        String Password = password.getText().toString().trim();

        //progressDialog.setCancelable(false);
        //progressDialog.setContentView(R.layout.progress_layout);
        //Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //progressDialog.show();

        goToMainActivity();
        saveCredentials(Mobile,Password);


        //postRequest(Mobile,Password);
    }

    private void checkAutoLogin()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceName,MODE_PRIVATE);
        String UID = sharedPreferences.getString(key_mobile,"");
        String PASS = sharedPreferences.getString(key_password,"");

        if(!Objects.requireNonNull(UID).isEmpty() && !Objects.requireNonNull(PASS).isEmpty() && AutoLoginFlag == 1)
        {
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.progress_layout);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            postRequest(UID,PASS);
        }
    }

    private void postRequest(final String MOBILE, final String PASSWORD)
    {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, LOGIN_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject server_response = new JSONObject(response);
                            if(server_response.getString("Response").equals("Success"))
                            {
                                if(remember.isChecked())
                                {
                                    saveCredentials(MOBILE,PASSWORD);
                                }

                                else
                                {
                                    saveCredentials(MOBILE,"");
                                }
                                progressDialog.dismiss();
                                goToMainActivity();
                            }

                            else if(server_response.getString("Response").equals("OTP not verified"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.otp_not_verified),1);
                            }

                            else if(server_response.getString("Response").equals("mobile number not found"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.user_not_exists),0);
                            }

                            else if(server_response.getString("Response").equals("password mismatched"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.wrong_password),0);
                                password.setError("Password Mismatched");
                            }

                            else if (server_response.getString("Response").equals("already login elsewhere"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.user_already_logged_in),0);
                            }
                        }
                        catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(login.this,String.valueOf(error),Toast.LENGTH_SHORT).show();
                    }
                })
                {
                protected Map<String, String> getParams()
                {
                    Map<String, String> MyData = new HashMap<String, String>();
                    MyData.put("mobile", MOBILE);
                    MyData.put("pin", PASSWORD);
                    return MyData;
                }
            };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);
    }

    public void saveCredentials(String MOBILE, String PASSWORD)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceName,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_mobile,MOBILE);
        editor.putString(key_password,PASSWORD);
        editor.apply();
    }

    public void clearCredential()
    {
        if(ClearFlag == 1)
        {
            SharedPreferences sharedPreferences = getSharedPreferences(PreferenceName,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    private void showErrorDialog(String Message, final int flag)
    {
        messageDialog.setContentView(R.layout.dailog_failed);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_red = (Button) messageDialog.findViewById(R.id.okayred);
        message.setText(Message);
        okay_red.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                messageDialog.dismiss();

                if(flag == 1)
                {
                    OTPVerification();
                }
            }
        });
        Objects.requireNonNull(messageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }
}
