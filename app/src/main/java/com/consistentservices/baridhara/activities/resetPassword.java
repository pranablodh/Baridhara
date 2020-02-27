package com.consistentservices.baridhara.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class resetPassword extends AppCompatActivity
{
    private Button reset_password;
    private EditText mobile_Number_reset;
    private EditText new_password;
    private EditText new_password_re;
    private TextView back_to_login;

    //Animation Component
    private Animation from_bottom;
    private TextView reset_header;
    private TextView reset_footer;
    private CardView reset_card;

    private Dialog progressDialog;
    private Dialog messageDialog;
    private TextView message;
    private Button okay_red;

    private String RESET_PASSWORD_API = String.format("%s%s", url.BASE_URL, url.SUFFIX_RESET_PASSWORD);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Hiding Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //UI Elements
        reset_password = (Button) findViewById(R.id.reset_password);
        mobile_Number_reset = (EditText) findViewById(R.id.mobile_Number_reset);
        new_password = (EditText) findViewById(R.id.new_password);
        new_password_re = (EditText) findViewById(R.id.new_password_re);
        back_to_login = (TextView) findViewById(R.id.back_to_login);
        reset_header = (TextView) findViewById(R.id.reset_header);
        reset_footer = (TextView) findViewById(R.id.reset_footer);
        reset_card = (CardView) findViewById(R.id.reset_card);

        messageDialog = new Dialog(resetPassword.this);
        progressDialog = new Dialog(resetPassword.this);

        //On Click Listener
        reset_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!validateMobile() || !validatePassword() | !validatePasswordRe() | !matchPassword())
                {
                    return;
                }
                ResetPassword();
            }
        });

        back_to_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        Animation();
    }

    @Override
    public void onBackPressed()
    {
        Intent login = new Intent(resetPassword.this, login.class);
        login.putExtra("Flag",0);
        startActivity(login);
        finish();
    }

    private void getOTP()
    {
        Intent GetOTP = new Intent(resetPassword.this, otpVerification.class);
        GetOTP.putExtra("MobileNumber",mobile_Number_reset.getText().toString().trim());
        GetOTP.putExtra("OTP_FLAG","0");
        startActivity(GetOTP);
        finish();
    }

    //Animation Function
    private void Animation()
    {
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        reset_header.setAnimation(from_bottom);
        reset_card.setAnimation(from_bottom);
        reset_footer.setAnimation(from_bottom);
    }

    //Validating Our Text Inputs
    private Boolean validateMobile()
    {
        if(String.valueOf(mobile_Number_reset.getText()).isEmpty())
        {
            mobile_Number_reset.setError("Field can't be empty");
            return false;
        }

        if(mobile_Number_reset.getText().toString().trim().length() == 0)
        {
            mobile_Number_reset.setError("Field can't be empty");
            return false;
        }

        if(mobile_Number_reset.getText().toString().trim().length() < 10)
        {
            mobile_Number_reset.setError("Enter A Valid 10 Digit Mobile Number");
            return false;
        }

        else
        {
            mobile_Number_reset.setError(null);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        if(String.valueOf(new_password.getText()).trim().isEmpty())
        {
            new_password.setError("Field can't be empty");
            return false;
        }

        if(new_password.getText().toString().trim().length() == 0)
        {
            new_password.setError("Field can't be empty");
            return false;
        }

        else
        {
            new_password.setError(null);
            return true;
        }
    }

    private Boolean validatePasswordRe()
    {
        if(String.valueOf(new_password_re.getText()).trim().isEmpty())
        {
            new_password_re.setError("Field can't be empty");
            return false;
        }

        if(new_password_re.getText().toString().trim().length() == 0)
        {
            new_password_re.setError("Field can't be empty");
            return false;
        }

        else
        {
            new_password_re.setError(null);
            return true;
        }
    }

    private boolean matchPassword()
    {
        if(new_password.getText().toString().trim().length() < 4)
        {
            new_password.setError("PIN Must Contain 4 Digit");
            return false;
        }

        if(!new_password.getText().toString().equals(new_password_re.getText().toString()))
        {
            new_password.setError("PIN Doesn't Match");
            new_password_re.setError("PIN Doesn't Match");
            return false;
        }

        else
        {
            new_password.setError(null);
            new_password_re.setError(null);
            return true;
        }
    }

    private void ResetPassword()
    {
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_layout);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        String mobile = mobile_Number_reset.getText().toString().trim();
        String password = new_password.getText().toString().trim();

        postRequest(mobile,password);
    }

    private void postRequest(final String MOBILE, final String PASSWORD)
    {
        StringRequest resetPasswordRequest = new StringRequest(Request.Method.POST, RESET_PASSWORD_API,
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
                                progressDialog.dismiss();
                                getOTP();
                            }

                            else if (server_response.getString("Response").equals("wrong mobile number"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.user_not_exists));
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
                        Toast.makeText(resetPassword.this,String.valueOf(error),Toast.LENGTH_SHORT).show();
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
        requestQueue.add(resetPasswordRequest);
    }

    private void showErrorDialog(String Message)
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
            }
        });
        Objects.requireNonNull(messageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }
}
