package com.bignerdranch.android.tinlge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.tinlge.database.CrimeDbSchema;
import com.bignerdranch.android.tinlge.database.ThingBaseHelper;
import com.bignerdranch.android.tinlge.database.ThingCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.tinlge.database.CrimeDbSchema.*;

/**
 * Created by Omer on 17.02.2016.
 */
public class ThingLab {
    private static ThingLab sThingLab;

    private Context mContext;
    private static SQLiteDatabase mDatabase;

    private ThingLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ThingBaseHelper(mContext)
                .getWritableDatabase(); // creating a new database file if it does not already exist.

        //mThingsDB= new ArrayList<Thing>();

        /*mThingsDB.add(new Thing("Android Phone", "Desk"));
        mThingsDB.add(new Thing("Jacket", "Wardrobe"));
        mThingsDB.add(new Thing("Laptop", "School bag"));
        mThingsDB.add(new Thing("Big Nerd book", "Desk"));
        mThingsDB.add(new Thing("thing1", "place1"));
        mThingsDB.add(new Thing("thing2", "place2"));
        mThingsDB.add(new Thing("thing3", "place3"));
        mThingsDB.add(new Thing("thing4", "place4"));
        mThingsDB.add(new Thing("thing5", "place5"));
        mThingsDB.add(new Thing("thing6", "place6"));
        mThingsDB.add(new Thing("thing7", "place7"));
        mThingsDB.add(new Thing("thing8", "place8"));
        mThingsDB.add(new Thing("thing9", "place9"));
        mThingsDB.add(new Thing("thing10", "place10"));*/

    }

    public static ThingLab get(Context context) {
        if (sThingLab == null){
            sThingLab = new ThingLab(context);
        }

        return sThingLab;
    }

    /*public static ThingCursorWrapper getCursorWrapperAllThings(){
        //List<Thing> things = new ArrayList<Thing>();
        ThingCursorWrapper cursorWrapper = queryThings(null, null);
        cursorWrapper.moveToFirst();
        Log.i("getCursorWrapperAllThings", Integer.toString(cursorWrapper.getPosition()));
        return cursorWrapper ;
    } */

    /*public List<Thing> getThingsByCursor(ThingCursorWrapper cursor){
        List<Thing> things = new ArrayList<Thing>();

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                things.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return  things;
    }*/


    public List<Thing> getThings(){
        //return this.mThingsDB;
        List<Thing> things = new ArrayList<Thing>();
        ThingCursorWrapper cursor = queryThings(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                things.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return  things;
    }

    public int size(){
        //return  this.mThingsDB.size();
        //TODO - get the size
        return 0;
    }

    public void removeAtPos(int pos) {
        Cursor cursor = mDatabase.rawQuery("Select " + ThingTable.Cols.ID +
                " from " + ThingTable.NAME + " limit 1 offset " + pos, null  );

        String id;
        try{
            cursor.moveToFirst();
            id = cursor.getString(cursor.getColumnIndex(ThingTable.Cols.ID));
        }
        finally {
            cursor.close();
        }

        mDatabase.delete(ThingTable.NAME,
                ThingTable.Cols.ID + " = ?",
                new String[]{id});

    }

    public void addThing(Thing newThing){
        ContentValues values = getContentValues(newThing);
        mDatabase.insert(ThingTable.NAME, null, values);
    }

    public Thing getThing(Integer id){
        //return mThingsDB.get(i);
        // TODO - get thing by "i"

        ThingCursorWrapper cursor = queryThings(
                ThingTable.Cols.ID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if(cursor.getCount() == 0){ return  null;}

            cursor.moveToFirst();
            return cursor.getThing();
        }
        finally {
            cursor.close();
        }
    }

    public static Thing getLastThing(){
        ThingCursorWrapper cursor = queryThings(
                  "_id DESC"
        );

        try {
            if(cursor.getCount() == 0){ return null;}

            cursor.moveToFirst();
            return cursor.getThing();
        }
        finally {
            cursor.close();
        }
    }

    public void updateThing(Thing thing) {
        String id =  thing.getId().toString();
        ContentValues values = getContentValues(thing);

        mDatabase.update(ThingTable.NAME, values,
                ThingTable.Cols.ID + " = ?",
                new String[] { id });
    }

    /*public void deleteThingById(Integer id){
        //String id =  thing.getId().toString();
        //ContentValues values = getContentValues(thing);

        mDatabase.delete(ThingTable.NAME,
                ThingTable.Cols.ID + " = ?",
                new String[]{Integer.toString(id)});
    }*/

    private static ContentValues getContentValues(Thing thing) {
        ContentValues values = new ContentValues();

        //values.put(ThingTable.Cols.ID, thing.getId().toString());
        values.put(ThingTable.Cols.WHAT, thing.getmWhat().toString());
        values.put(ThingTable.Cols.WHERE, thing.getmWhere().toString());

        return values;
    }

    //private Cursor queryCrimes(String whereClause, String[] whereArgs) {
    private static ThingCursorWrapper queryThings(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ThingTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new ThingCursorWrapper(cursor);
    }

    private static ThingCursorWrapper queryThings(String orderClause) {
        Cursor cursor = mDatabase.query(
                ThingTable.NAME,
                null, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                orderClause // orderBy
        );

        return new ThingCursorWrapper(cursor);
    }
}
