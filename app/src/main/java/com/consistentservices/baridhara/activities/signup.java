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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class signup extends AppCompatActivity
{
    private Button submit;
    private EditText your_name;
    private EditText your_mobile;
    private EditText your_address;
    private EditText account_password;
    private EditText account_password_re;
    private TextView login_page;
    private TextView toc_textView;
    private CheckBox terms_and_condition;

    private CardView sign_up_card;
    private Animation right_to_left;

    private Dialog progressDialog;
    private Dialog messageDialog;
    private TextView message;
    private Button okay_red;

    private String SIGN_UP_API = String.format("%s%s", url.BASE_URL, url.SUFFIX_SIGN_UP);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Hiding Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //UI Element Function
        submit = (Button) findViewById(R.id.submit);
        your_name = (EditText) findViewById(R.id.your_name);
        your_mobile = (EditText) findViewById(R.id.your_mobile);
        your_address = (EditText) findViewById(R.id.your_address);
        account_password = (EditText) findViewById(R.id.account_password);
        account_password_re = (EditText) findViewById(R.id.account_password_re);
        login_page = (TextView) findViewById(R.id.login_page);
        toc_textView = (TextView) findViewById(R.id.toc_textView);
        sign_up_card = (CardView) findViewById(R.id.sign_up_card);
        terms_and_condition = (CheckBox) findViewById(R.id.terms_and_condition); 

        messageDialog = new Dialog(signup.this);
        progressDialog = new Dialog(signup.this);

        //On Click Listener
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SignUp();
            }
        });

        login_page.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        toc_textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                termsAndConditions();
            }
        });

        terms_and_condition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    terms_and_condition.setError(null);
                }
            }
        });

        Animation();
    }

    @Override
    public void onBackPressed()
    {
        Intent login = new Intent(signup.this, login.class);
        login.putExtra("Flag",0);
        startActivity(login);
        finish();
    }

    private void Animation()
    {
        right_to_left = AnimationUtils.loadAnimation(this, R.anim.move_right_to_left);
        sign_up_card.setAnimation(right_to_left);
    }

    private void termsAndConditions()
    {
        Intent toc = new Intent(signup.this, termsAndConditions.class);
        startActivity(toc);
        finish();
    }

    private void OTPVerification()
    {
        Intent OTPVerify = new Intent(signup.this, otpVerification.class);
        OTPVerify.putExtra("MobileNumber",your_mobile.getText().toString().trim());
        OTPVerify.putExtra("OTP_FLAG","1");
        startActivity(OTPVerify);
    }


    //Validating Our Text Inputs
    private Boolean validateName()
    {
        if(String.valueOf(your_name.getText()).trim().isEmpty())
        {
            your_name.setError("Field can't be empty");
            return false;
        }

        if(your_name.getText().toString().trim().length() == 0)
        {
            your_name.setError("Field can't be empty");
            return false;
        }

        else
        {
            your_name.setError(null);
            return true;
        }
    }

    private Boolean validateAddress()
    {
        if(String.valueOf(your_address.getText()).trim().isEmpty())
        {
            your_address.setError("Field can't be empty");
            return false;
        }

        if(your_address.getText().toString().trim().length() == 0)
        {
            your_address.setError("Field can't be empty");
            return false;
        }

        else
        {
            your_address.setError(null);
            return true;
        }
    }

    private Boolean validateMobile()
    {
        if(String.valueOf(your_mobile.getText()).isEmpty())
        {
            your_mobile.setError("Field can't be empty");
            return false;
        }

        if(your_mobile.getText().toString().trim().length() == 0)
        {
            your_mobile.setError("Field can't be empty");
            return false;
        }

        if(your_mobile.getText().toString().trim().length() < 10)
        {
            your_mobile.setError("Enter A Valid 10 Digit Mobile Number");
            return false;
        }

        else
        {
            your_mobile.setError(null);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        if(String.valueOf(account_password.getText()).trim().isEmpty())
        {
            account_password.setError("Field can't be empty");
            return false;
        }

        if(account_password.getText().toString().trim().length() == 0)
        {
            account_password.setError("Field can't be empty");
            return false;
        }

        else
        {
            account_password.setError(null);
            return true;
        }
    }

    private Boolean validatePasswordRe()
    {
        if(String.valueOf(account_password_re.getText()).trim().isEmpty())
        {
            account_password_re.setError("Field can't be empty");
            return false;
        }

        if(account_password_re.getText().toString().trim().length() == 0)
        {
            account_password_re.setError("Field can't be empty");
            return false;
        }

        else
        {
            account_password_re.setError(null);
            return true;
        }
    }

    private boolean matchPassword()
    {
        if(account_password.getText().toString().trim().length() < 4)
        {
            account_password.setError("PIN Must Contain 4 Digit");
            return false;
        }

        if(!account_password.getText().toString().equals(account_password_re.getText().toString()))
        {
            account_password.setError("PIN Doesn't Match");
            account_password_re.setError("PIN Doesn't Match");
            return false;
        }

        else
        {
            account_password.setError(null);
            account_password_re.setError(null);
            return true;
        }
    }

    private boolean validateTerms()
    {
        if(!terms_and_condition.isChecked())
        {
            terms_and_condition.setError(getResources().getString(R.string.terms_error));
            return false;
        }

        else
        {
            terms_and_condition.setError(null);
            return true;
        }
    }

    private void SignUp()
    {
        if(!validateName() | !validateAddress() | !validateMobile() | !validatePassword()
                | !validatePasswordRe() | !matchPassword() | !validateTerms())
        {
            return;
        }

        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_layout);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        String name = your_name.getText().toString().trim();
        String address = your_address.getText().toString().trim();
        String mobile = your_mobile.getText().toString().trim();
        String password = account_password.getText().toString().trim();

        postRequest(name,address,mobile,password);
    }

    private void postRequest(final String NAME, final String ADDRESS,final String MOBILE, final String PASSWORD)
    {
        StringRequest signUpRequest = new StringRequest(Request.Method.POST, SIGN_UP_API,
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
                                OTPVerification();
                            }

                            else if (server_response.getString("Response").equals("already registered"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.mobile_already_registered));
                            }

                            else if(server_response.getString("Response").equals("internal server error"))
                            {
                                progressDialog.dismiss();
                                showErrorDialog(getResources().getString(R.string.internal_server_error));
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
                        Toast.makeText(signup.this,String.valueOf(error),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String, String> getParams()
            {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", NAME);
                MyData.put("address", ADDRESS);
                MyData.put("mobile", MOBILE);
                MyData.put("pin", PASSWORD);
                return MyData;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(signUpRequest);
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
