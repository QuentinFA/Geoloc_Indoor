package com.example.dianastoian.rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new HttpRequestTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, LocationHistory> {
        @Override
        protected LocationHistory doInBackground(Void... params) {
            try {
                final String url = "http://10.0.2.2:8080//devices/123/locationFin";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                LocationHistory location = restTemplate.getForObject(url, LocationHistory.class);
                return location;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(LocationHistory location) {
            TextView locationHistoryIdText = (TextView) findViewById(R.id.id_value);
            TextView locationHistoryDeviceIdText = (TextView) findViewById(R.id.deviceId_value);
            TextView locationHistoryDateText = (TextView) findViewById(R.id.date_value);
            TextView locationHistoryLatitudeText = (TextView) findViewById(R.id.latitude_value);
            TextView locationHistoryLongitudeText = (TextView) findViewById(R.id.longitude_value);
            TextView locationHistoryLevelText = (TextView) findViewById(R.id.level_value);
            locationHistoryIdText.setText(""+location.getId());
            locationHistoryDeviceIdText.setText(""+location.getDeviceId());
            locationHistoryDateText.setText(location.getDate());
            locationHistoryLatitudeText.setText(""+location.getLatitude());
            locationHistoryLongitudeText.setText(""+location.getLongitude());
            locationHistoryLevelText.setText(""+location.getLevel());
        }
    }
}
