package com.consistentservices.baridhara.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.consistentservices.baridhara.R;
import com.consistentservices.baridhara.fragments.addPump;
import com.consistentservices.baridhara.fragments.managePump;
import com.consistentservices.baridhara.fragments.myPumps;
import com.consistentservices.baridhara.fragments.userInfo;
import com.consistentservices.baridhara.miscellaneousFunctions.sharedPreferenceData;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private FrameLayout main_frame;
    private myPumps MyPumps;
    private addPump AddPump;
    private managePump ManagePump;
    private userInfo UserInfo;

    //Shared Preference
    private static final String PreferenceName = sharedPreferenceData.MyPREFERENCES;
    private static final String key_mobile = sharedPreferenceData.Mobile_key;
    private static final String key_password = sharedPreferenceData.Password_key;
    public String User_Mobile = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.my_pumps:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("My Pumps");
                    setFragments(MyPumps);
                    return true;
                case R.id.add_pump:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Add Pump");
                    setFragments(AddPump);
                    return true;
                case R.id.manage_pump:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Manage Pump");
                    setFragments(ManagePump);
                    return true;
                case R.id.profile:
                    Objects.requireNonNull(getSupportActionBar()).setTitle("User Info");
                    setFragments(UserInfo);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MyPumps = new myPumps();
        AddPump = new addPump();
        ManagePump = new managePump();
        UserInfo = new userInfo();
        main_frame = (FrameLayout) findViewById(R.id.main_frame);

        navView.getMenu().getItem(0).setChecked(true);
        setFragments(MyPumps);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Pumps");
        getSharedPreferenceData();
    }

    private void getSharedPreferenceData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceName,MODE_PRIVATE);
        User_Mobile = sharedPreferences.getString(key_mobile,"");
        String PASS = sharedPreferences.getString(key_password,"");

        if(!Objects.requireNonNull(User_Mobile).isEmpty() && !Objects.requireNonNull(PASS).isEmpty())
        {
            Toast.makeText(this,User_Mobile+" : "+PASS,Toast.LENGTH_SHORT).show();
        }
    }

    private void setFragments(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public String getUserMobile()
    {
        return User_Mobile;
    }
}
