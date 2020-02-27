package com.consistentservices.baridhara.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.activities.MainActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class userInfo extends Fragment
{
    private CardView profile_info;
    private CardView monthly_calculator;
    private CardView analytics;
    private CardView weather_report;
    private CardView kisanSamachar;
    private CardView promotions;
    private CardView get_in_touch;
    private CardView about_us;
    private CardView logout_button;
    private ImageView profile_pic;
    private TextView profile_name;
    private TextView profile_mobile;
    private MainActivity mainActivity;

    public userInfo()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        //CardViews Initialization
        profile_info = (CardView) view.findViewById(R.id.profile_info);
        monthly_calculator = (CardView) view.findViewById(R.id.monthly_calculator);
        analytics = (CardView) view.findViewById(R.id.analytics);
        weather_report = (CardView) view.findViewById(R.id.weather_report);
        kisanSamachar = (CardView) view.findViewById(R.id.kisanSamachar);
        promotions = (CardView) view.findViewById(R.id.promotions);
        get_in_touch = (CardView) view.findViewById(R.id.get_in_touch);
        about_us = (CardView) view.findViewById(R.id.about_us);
        logout_button = (CardView) view.findViewById(R.id.logout_button);

        //Profile Display Section Elements
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_mobile = (TextView) view.findViewById(R.id.profile_mobile);


        //OnClick Events of CardViews
        profile_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                profile_mobile.setText(((MainActivity) Objects.requireNonNull(getActivity())).getUserMobile());
            }
        });

        monthly_calculator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        analytics.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        weather_report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        kisanSamachar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        promotions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        get_in_touch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        about_us.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
                Logout();
            }
        });


        return view;
    }

    private void Logout()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.logout_button))
                .setMessage(getResources().getString(R.string.logout_alert))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        goToLogin();
                    }
                })
                .setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goToLogin()
    {
        Intent login = new Intent(getActivity(), com.consistentservices.baridhara.activities.login.class);
        login.putExtra("ClearFlag",1);
        startActivity(login);
        Objects.requireNonNull(getActivity()).finish();
    }

}
