package com.dmcapps.navigationfragment.example.SingleStackExample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dmcapps.navigationfragment.fragment.pattern.manager.SingleStackNavigationManagerFragment;

import java.util.UUID;

public class SingleStackNavigationExampleActivity extends AppCompatActivity {

    private static final String STATE_NAV_TAG = "NAV_TAG";

    private String mNavigationManagerFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mNavigationManagerFragmentTag = savedInstanceState.getString(STATE_NAV_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNavigationManagerFragmentTag == null) {
            addFragment(SingleStackNavigationManagerFragment.newInstance(SampleFragment.newInstance("Root Fragment in the Stack")));
        }
        else {
            showFragment(mNavigationManagerFragmentTag);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mNavigationManagerFragmentTag);
        ft.detach(fragment);
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_NAV_TAG, mNavigationManagerFragmentTag);
    }

    private void addFragment(SingleStackNavigationManagerFragment fragment)  {
        mNavigationManagerFragmentTag = UUID.randomUUID().toString();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment, mNavigationManagerFragmentTag);
        ft.commit();
    }

    private void showFragment(String tag)  {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment != null && fragment.isDetached()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.attach(fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        SingleStackNavigationManagerFragment fragment = (SingleStackNavigationManagerFragment)getSupportFragmentManager().findFragmentByTag(mNavigationManagerFragmentTag);
        if (!fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}