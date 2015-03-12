package com.eveningoutpost.dexdrip.Tables;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.activeandroid.Cache;
import com.eveningoutpost.dexdrip.NavigationDrawerFragment;
import com.eveningoutpost.dexdrip.R;

import java.util.ArrayList;


public class SensorDataTable extends ListActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private String menu_name = "Sensor Data Table";
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ArrayList<String> results = new ArrayList<String>();
    private View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_data_list);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), menu_name, this);
        getData();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mNavigationDrawerFragment.swapContext(position);
    }

    private void getData() {
        Cursor cursor = Cache.openDatabase().rawQuery("Select * from Sensors order by _ID desc", null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item_bg_reading,
                cursor,
                new String[] { "started_at", "latest_battery_level", "uuid", "uuid" },
                new int[] { R.id.list_calc_value, R.id.list_raw_value, R.id.list_bg_slope, R.id.list_data_bg_timestamp});

        this.setListAdapter(adapter);
//        ListView listView = (ListView) findViewById(R.id.list);
//        listView.setAdapter(adapter);
    }

}