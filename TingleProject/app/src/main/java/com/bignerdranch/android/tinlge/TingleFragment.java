package com.bignerdranch.android.tinlge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;

import java.util.UUID;

public class TingleFragment extends Fragment {
    private Button addThing, mListActivities, mScanner;
    private TextView lastAdded;
    private TextView newWhat, newWhere;
    private static ThingLab sThingLab;

    private final int REQUEST_SCAN_BARCODE = 0;

    public interface ToActivityOnDataStateChanged {public  void  stateChange();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tingle, container, false);

        sThingLab = ThingLab.get(getActivity());
        lastAdded = (TextView) v.findViewById(R.id.last_thing);
        updateUI();

        newWhat = (EditText) v.findViewById(R.id.what_text);
        newWhat.setImeOptions(EditorInfo.IME_ACTION_DONE);
        newWhat.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            for (Thing thing : sThingLab.getThings()){
                                if (thing.getmWhat().toString().equals( newWhat.getText().toString())) {
                                    Toast.makeText(getActivity(), thing.getmWhere(), Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            }
                        }

                        return  false;
                    }
                }
        );

        newWhere = (EditText)v.findViewById(R.id.where_text);

        addThing = (Button) v.findViewById(R.id.add_button);
        addThing.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newWhat.getText().length() > 0 && newWhere.getText().length() > 0) {
                            sThingLab.addThing(
                                    new Thing(newWhat.getText().toString().trim(), newWhere.getText().toString().trim())
                            );


                            int orientation = getResources().getConfiguration().orientation;
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                ((ToActivityOnDataStateChanged) getActivity()).stateChange();

                            /*    FragmentManager fm = getActivity().getSupportFragmentManager();
                                Fragment fragment_list = fm.findFragmentById(R.id.list_container);
                                if (fragment_list != null) {
                                    fm.beginTransaction().remove(fragment_list).commit();
                                    fragment_list = new ListFragment();
                                    fm.beginTransaction()
                                            .add(R.id.list_container, fragment_list)
                                            .commit();
                                } */
                            }

                            newWhat.setText("");
                            newWhere.setText("");

                            updateUI();
                        }
                    }
                }
        );

        int orientation = getResources().getConfiguration().orientation;
        if (orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mListActivities = (Button) v.findViewById(R.id.list_button);
            mListActivities.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TingleFragment.this.getContext(), ListActivity.class);
                            startActivity(intent);
                        }
                    }
            );
        }
        else {
            mListActivities = (Button) v.findViewById(R.id.list_button);
            mListActivities.setVisibility(View.GONE);
        }

        mScanner = (Button) v.findViewById(R.id.scan_button);
        try {
            mScanner.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                            startActivityForResult(intent, REQUEST_SCAN_BARCODE);
                        }
                    }
            );
        } catch (Exception e) {
            Log.i("onCreate", "Scanner Not Found", e);
        }

        return  v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN_BARCODE){
            if (resultCode == Activity.RESULT_OK) {
                String content = data.getStringExtra("SCAN_RESULT");
                newWhat.setText(content);
                newWhere.requestFocus();
            }
        }
    }

    private void updateUI() {
        Thing lastThing = ThingLab.getLastThing();

        if (lastThing != null) {
            lastAdded.setText(lastThing.toString());
        }

        //int size = sThingLab.size();
        //if (size > 0){
            //Thing lastThing = ThingLab.getLastThing();
            //lastAdded.setText(ThingLab.getLastThing().toString());
        //}
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        ThingLab.get(getActivity())
//                .updateThing(m);
//    }
    // TODO - added in Using ViewPager
}
