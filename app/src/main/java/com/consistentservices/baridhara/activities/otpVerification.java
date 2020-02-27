package com.consistentservices.baridhara.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.consistentservices.baridhara.networkFunction.url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class otpVerification extends AppCompatActivity
{

    private String MOBILE_NUMBER = "";
    private String OTP = "";
    private String OTP_RESEND_FLAG = "RESEND";
    private String OTP_VERIFY_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_VERIFY_OTP);
    private int OTP_TYPE_FLAG_COUNTER = 0;
    private String OTP_TYPE_FLAG = "0";
    private String OTP_VERIFICATION_TYPE = "";

    private EditText otp;
    private TextView resend_otp;
    private Button verify_otp;

    private Dialog messageDialog;
    private Dialog progressDialog;
    private TextView message;
    private Button okay_green;
    private Button okay_red;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        //Hiding Action Bar
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();

        Intent i = getIntent();
        MOBILE_NUMBER = i.getStringExtra("MobileNumber");

        //Check Whether it is new registration or reset password
        OTP_VERIFICATION_TYPE = i.getStringExtra("OTP_FLAG");

        //UI Elements
        otp = (EditText) findViewById(R.id.otp);
        resend_otp = (TextView) findViewById(R.id.resend_otp);
        verify_otp = (Button) findViewById(R.id.verify_otp);

        messageDialog = new Dialog(otpVerification.this);
        progressDialog = new Dialog(otpVerification.this);

        verify_otp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!validateOTP())
                {
                    return;
                }
                verifyOTP();
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OTP_TYPE_FLAG_COUNTER++;
                if(OTP_TYPE_FLAG_COUNTER >= 2)
                {
                    OTP_TYPE_FLAG = "1";
                    resend_otp.setText(getResources().getString(R.string.otp_on_call));
                }
                postRequest(MOBILE_NUMBER,OTP_RESEND_FLAG,OTP_VERIFICATION_TYPE,OTP_TYPE_FLAG);
                countDownTimer();
                resend_otp.setEnabled(false);
            }
        });

        countDownTimer();
    }

    @Override
    public void onBackPressed()
    {
        Intent login = new Intent(otpVerification.this, login.class);
        login.putExtra("Flag",0);
        startActivity(login);
        finish();
    }

    //Countdown Timer
    private void countDownTimer()
    {
        resend_otp.setEnabled(false);
        new CountDownTimer(30000,1000)
        {
            public void onTick(final long millisUntilFinished)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        resend_otp.setTextColor(ContextCompat.getColor(otpVerification.this, R.color.colorGrey));
                        resend_otp.setText(String.format("%s(%s)", getResources().getString(R.string.resend_otp),
                                String.valueOf(millisUntilFinished / 1000)));

                        if(OTP_TYPE_FLAG_COUNTER >= 2)
                        {
                            resend_otp.setText(String.format("%s(%s)", getResources().getString(R.string.otp_on_call),
                                    String.valueOf(millisUntilFinished / 1000)));
                        }
                    }
                });
            }

            public void onFinish()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(OTP_TYPE_FLAG_COUNTER >= 2)
                        {
                            resend_otp.setText(getResources().getString(R.string.otp_on_call));
                        }
                        else
                        {
                            resend_otp.setText(getResources().getString(R.string.resend_otp));
                        }
                        resend_otp.setTextColor(ContextCompat.getColor(otpVerification.this, R.color.colorPrimary));
                        resend_otp.setEnabled(true);
                    }
                });
            }

        }.start();
    }

    //Validate OTP
    private Boolean validateOTP()
    {
        if(String.valueOf(otp.getText()).trim().isEmpty())
        {
            otp.setError("Field can't be empty");
            return false;
        }

        if(otp.getText().toString().trim().length() == 0)
        {
            otp.setError("Field can't be empty");
            return false;
        }

        if(otp.getText().toString().trim().length() < 6)
        {
            otp.setError("Enter A Six Digit OTP");
            return false;
        }

        else
        {
            otp.setError(null);
            return true;
        }
    }

    private void  verifyOTP()
    {
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_layout);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        OTP = otp.getText().toString().trim();
        postRequest(MOBILE_NUMBER,OTP,OTP_VERIFICATION_TYPE,OTP_TYPE_FLAG);
    }

    private void postRequest(final String MOBILE, final String PASSWORD, final String OTP_TYPE,final String OTP_TYPE_FLAG)
    {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, OTP_VERIFY_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject server_response = new JSONObject(response);
                            if(server_response.getString("Response").equals("Verified"))
                            {
                                progressDialog.dismiss();
                                showDialogSuccess(getResources().getString(R.string.otp_verified_new_account));
                            }

                            else if(server_response.getString("Response").equals("password changed"))
                            {
                                progressDialog.dismiss();
                                showDialogSuccess(getResources().getString(R.string.password_changed));
                            }

                            else if(server_response.getString("Response").equals("otp mismatched"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.otp_verify_failed));
                                otp.setError("OTP Mismatched");
                            }

                            else if(server_response.getString("Response").equals("otp resend"))
                            {
                                Toast.makeText(otpVerification.this,"OTP Resend To Your Mobile",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(otpVerification.this,String.valueOf(error),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String, String> getParams()
            {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("mobile", MOBILE);
                MyData.put("otp", PASSWORD);
                //Check Whether it is new registration or reset password. For new registration value is 1 or for reset password value is 0.
                MyData.put("otp_type",OTP_TYPE);
                //Check Whether SMS OTP or OTP Over Call. For OTP over SMS value is 0 or for OTP Over Call value is 1.
                MyData.put("otp_type_flag",OTP_TYPE_FLAG);
                return MyData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginRequest);
    }

    //Dialog Function
    private void showDialogSuccess(String displayMessage)
    {
        progressDialog.dismiss();
        messageDialog.setContentView(R.layout.dialog_success);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_green = (Button) messageDialog.findViewById(R.id.okayGreen);
        message.setText(displayMessage);
        okay_green.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
        Objects.requireNonNull(messageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }

    private void showErrorDialog(String displayMessage)
    {
        progressDialog.dismiss();
        messageDialog.setContentView(R.layout.dailog_failed);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_red = (Button) messageDialog.findViewById(R.id.okayred);
        message.setText(displayMessage);
        okay_red.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                messageDialog.dismiss();
            }
        });
        Objects.requireNonNull(messageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }
}
