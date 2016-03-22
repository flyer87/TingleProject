package com.bignerdranch.android.tinlge;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TingleActivity extends AppCompatActivity implements TingleFragment.ToActivityOnDataStateChanged {

    private Fragment list_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment_container = fm.findFragmentById(R.id.fragment_container);
        if (fragment_container == null) {
            fragment_container = new TingleFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment_container)
                    .commit();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            list_container = fm.findFragmentById(R.id.list_container);
            if (list_container == null) {
                list_container = new ListFragment();
                fm.beginTransaction()
                        .add(R.id.list_container, list_container)
                        .commit();
            }
        }
    }

    @Override
    public void stateChange() {
        FragmentManager fm = getSupportFragmentManager();

        list_container = fm.findFragmentById(R.id.list_container);
        if (list_container != null) {
            fm.beginTransaction().remove(list_container).commit();
            list_container = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.list_container, list_container)
                    .commit();
        }
    }
}
