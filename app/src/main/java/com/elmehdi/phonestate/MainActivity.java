package com.elmehdi.phonestate;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import phonestatreceiver.com.testphonestatreceiver.R;

public class MainActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {
    Switch switchServiceEnabled;
    TextView textView;
    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 12;
    private TextView out;
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isServiceRunning = isServiceRunning(AppListener.class);
        out = (TextView) findViewById(R.id.tvBluetoothInfo);
        switchServiceEnabled = (Switch) findViewById(R.id.switch_enable_service);
        textView = (TextView) findViewById(R.id.textView);

        switchServiceEnabled.setChecked(isServiceRunning);
        if (isServiceRunning) {
            textView.setText("Disable service");
        } else {
            textView.setText("Enable service");
        }
        switchServiceEnabled.setOnCheckedChangeListener(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setBluetoothData();
        if (Connections.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        out.setText("");
        setBluetoothData();
    }

    private void setBluetoothData() {
        // Getting the Bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();
        out.append("\nAdapter: " + adapter.toString() + "\n\nName: "
                + adapter.getName() + "\nAddress: " + adapter.getAddress());
        // Check for Bluetooth support in the first place
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.", Toast.LENGTH_LONG).show();
        }
        // Starting the device discovery
        out.append("\n\nStarting discovery...");
        adapter.startDiscovery();
        out.append("\nDone with discovery...\n");
        // Listing paired devices
        out.append("\nDevices Pared:");
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            out.append("\nFound device: " + device.getName() + " Add: "
                    + device.getAddress());
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final Intent intent = new Intent(this, AppListener.class);
        if (isChecked) {
            startService(intent);
            textView.setText("Disable service");
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            Snackbar.make(buttonView, "Service started successfully", Snackbar.LENGTH_LONG).show();
        } else {
            stopService(intent);
            textView.setText("Enable service");
            Snackbar.make(buttonView, "Service stopped successfully", Snackbar.LENGTH_LONG).show();
        }

    }

    public static class Connections {

        private static boolean state = false;

        public static boolean blueTooth() {

            BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
            if (!bluetooth.isEnabled()) {
                System.out.println("Bluetooth is Disable...");
                state = true;
            } else if (bluetooth.isEnabled()) {
                String address = bluetooth.getAddress();
                String name = bluetooth.getName();
                System.out.println(name + " : " + address);
                state = false;
            }
            return state;
        }

    }
}
