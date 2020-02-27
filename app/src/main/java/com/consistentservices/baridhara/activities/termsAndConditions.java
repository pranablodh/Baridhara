package com.consistentservices.baridhara.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.consistentservices.baridhara.R;

public class termsAndConditions extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condtions);

        getSupportActionBar().setTitle(getResources().getString(R.string.TOC));
    }

    @Override
    public void onBackPressed()
    {
        Intent signUp = new Intent(termsAndConditions.this, signup.class);
        startActivity(signUp);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
