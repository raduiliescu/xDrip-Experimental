package com.eveningoutpost.dexdrip.Tables;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.eveningoutpost.dexdrip.Models.BgReading;
import com.eveningoutpost.dexdrip.NavigationDrawerFragment;
import com.eveningoutpost.dexdrip.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BgReadingTable extends ListActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_data_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        String menu_name = "BG Data Table";
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), menu_name, this);

        getData();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mNavigationDrawerFragment.swapContext(position);
    }

    private void getData() {
        final List<BgReading> latest = BgReading.latest(50);
        ListAdapter adapter = new BgReadingAdapter(this, latest);

        this.setListAdapter(adapter);
    }

    public static class BgReadingCursorAdapterViewHolder {
        TextView slope;
        TextView calculatedValue;
        TextView rawValue;
        TextView raw;
        TextView timestamp;

        public BgReadingCursorAdapterViewHolder(View root) {
            calculatedValue = (TextView) root.findViewById(R.id.list_calc_value);
            rawValue = (TextView) root.findViewById(R.id.list_raw_value);
            raw = (TextView) root.findViewById(R.id.list_bg_raw);
            slope = (TextView) root.findViewById(R.id.list_bg_slope);
            timestamp = (TextView) root.findViewById(R.id.list_data_bg_timestamp);
        }
    }

    public static class BgReadingAdapter extends BaseAdapter {
        private final Context         context;
        private final List<BgReading> readings;

        public BgReadingAdapter(Context context, List<BgReading> readings) {
            this.context = context;
            if (readings == null)
                readings = new ArrayList<>();

            this.readings = readings;
        }

        public View newView(Context context, ViewGroup parent) {
            final View view = LayoutInflater.from(context).inflate(R.layout.list_item_bg_reading, parent, false);

            final BgReadingCursorAdapterViewHolder holder = new BgReadingCursorAdapterViewHolder(view);
            view.setTag(holder);

            return view;
        }

        public void bindView(View view, Context context, BgReading bgReading) {
            final BgReadingCursorAdapterViewHolder tag = (BgReadingCursorAdapterViewHolder) view.getTag();
            tag.calculatedValue.setText(String.format("BG: %.2f", bgReading.calculated_value));
            tag.rawValue.setText(String.format("Raw(adj): %.2f", bgReading.age_adjusted_raw_value));
            tag.raw.setText(String.format("Raw: %.2f", bgReading.raw_data));
            tag.slope.setText(String.format("Slope: %.2f", bgReading.staticSlope()));
            tag.timestamp.setText(new Date(bgReading.timestamp).toString());
        }

        @Override
        public int getCount() {
            return readings.size();
        }

        @Override
        public BgReading getItem(int position) {
            return readings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = newView(context, parent);

            bindView(convertView, context, getItem(position));
            return convertView;
        }
    }
}
