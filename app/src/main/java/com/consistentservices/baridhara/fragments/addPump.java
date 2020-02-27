package com.consistentservices.baridhara.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.networkFunction.url;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class addPump extends Fragment
{

    private EditText pump_id;
    private EditText pump_name;
    private Button add_pump;
    private Button okay_green;
    private Button okay_red;
    private Dialog messageDialog;
    private Dialog progressDialog;
    private TextView message;

    private String REQUEST_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_ADD_PUMP);
    private String TOKEN = "";

    public addPump()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pump, container, false);

        pump_id = (EditText) view.findViewById(R.id.pump_id);
        pump_name = (EditText) view.findViewById(R.id.pump_name);
        add_pump = (Button) view.findViewById(R.id.add_pump);

        messageDialog = new Dialog(getActivity());
        progressDialog = new Dialog(getActivity());

        add_pump.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addPumpAPI();
            }
        });
        return view;
    }

    //Adding Pump API Call

    private void addPumpAPI()
    {
        if(!validateID() | !validateName())
        {
            return;
        }

        progressDialog.setContentView(R.layout.progress_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(response.trim().equals("success"))
                        {
                            showDialogSuccess();
                        }

                        else if(response.trim().contains("already"))
                        {
                            showDialogAlready();
                        }

                        else
                        {
                            showDialogFailed();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getActivity(),String.valueOf(error),Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("id", pump_id.getText().toString());
                        MyData.put("name", pump_name.getText().toString());
                        MyData.put("admin","1");
                        return MyData;
                    }
                };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    //Validating Our Text Inputs
    private Boolean validateID()
    {
        if(String.valueOf(pump_id.getText()).isEmpty())
        {
            pump_id.setError("Field can't be empty");
            return false;
        }

        if(pump_id.getText().toString().trim().length() == 0)
        {
            pump_id.setError("Field can't be empty");
            return false;
        }

        if(pump_id.getText().toString().trim().length() < 4)
        {
            pump_id.setError("Enter A Valid Four Digit Pump Id");
            return false;
        }

        else
        {
            pump_id.setError(null);
            return true;
        }
    }

    private Boolean validateName()
    {
        if(String.valueOf(pump_name.getText()).trim().isEmpty())
        {
            pump_name.setError("Field can't be empty");
            return false;
        }

        if(pump_name.getText().toString().trim().length() == 0)
        {
            pump_name.setError("Field can't be empty");
            return false;
        }

        else
        {
            pump_name.setError(null);
            return true;
        }
    }

    private void showDialogSuccess()
    {
        progressDialog.dismiss();
        messageDialog.setContentView(R.layout.dialog_success);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_green = (Button) messageDialog.findViewById(R.id.okayGreen);
        message.setText(getString(R.string.add_pump_success));
        okay_green.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                messageDialog.dismiss();
            }
        });
        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }

    private void showDialogFailed()
    {
        progressDialog.dismiss();
        messageDialog.setContentView(R.layout.dailog_failed);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_red = (Button) messageDialog.findViewById(R.id.okayred);
        message.setText(getString(R.string.add_pump_failed));
        okay_red.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                messageDialog.dismiss();
            }
        });
        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }

    private void showDialogAlready()
    {
        progressDialog.dismiss();
        messageDialog.setContentView(R.layout.dialog_success);
        messageDialog.setCancelable(false);
        message = (TextView) messageDialog.findViewById(R.id.message);
        okay_red = (Button) messageDialog.findViewById(R.id.okayred);
        message.setText(getString(R.string.add_pump_already));
        okay_red.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                messageDialog.dismiss();
            }
        });
        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }
}