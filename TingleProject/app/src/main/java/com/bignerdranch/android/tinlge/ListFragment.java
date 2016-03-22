package com.bignerdranch.android.tinlge;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.android.tinlge.database.ThingCursorWrapper;

import java.util.List;

/**
 * Created by Omer on 01.03.2016.
 */
public class ListFragment extends Fragment {

    private static ThingLab sThingLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list, container, false);

        sThingLab = ThingLab.get(getActivity());
        List<Thing> thingList = sThingLab.getThings();

        ArrayAdapter<Thing> adapter =
                new ArrayAdapter<Thing>(getActivity(), R.layout.list_item, R.id.textViewItem, thingList );

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // new
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ListFragment.this.getContext(), Show.class);

                        String itemValue = ((TextView) view.findViewById(R.id.textViewItem)).getText().toString();
                        intent.putExtra("itemValue", itemValue);
                        intent.putExtra("itemPos", position);
                        startActivity(intent);
                    }
                }
        );

        return  v;
    }
}

