package com.consistentservices.baridhara.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.adapter.pumpAdapterManage;
import com.consistentservices.baridhara.adapterDataBinder.pumpDataManage;
import com.consistentservices.baridhara.networkFunction.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class managePump extends Fragment
{

    private List<pumpDataManage> pumpListManage;
    RecyclerView recyclerView;
    private Dialog progressDialog;

    private String REQUEST_URL = String.format("%s%s", url.BASE_URL, url.SUFFIX_GET_PUMP_LIST);
    private String TOKEN = "";


    public managePump()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_manage_pump, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        addData();

        // Inflate the layout for this fragment
        return view;
    }

    public void addData()
    {
        progressDialog = new Dialog(Objects.requireNonNull(getActivity()));
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_layout);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        pumpListManage = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, REQUEST_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONArray pumpData = new JSONArray(response);

                            for(int i = 0; i < pumpData.length(); i++)
                            {
                                JSONObject pump = pumpData.getJSONObject(i);

                                String name = pump.getString("name");
                                String device_id =  pump.getString("device_id");

                                pumpListManage.add(new pumpDataManage(name,device_id));
                            }

                            pumpAdapterManage adapter =   new pumpAdapterManage(getContext(),pumpListManage);
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
