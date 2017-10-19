package com.phive.mozzieearlywarning;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    /*-- NOTE: WHEN IN DOUBT OR CONFUSED ALWAYS CTRL+B --*/

    public static int REQUEST_BLUETOOTH = 1;
    BluetoothAdapter BTAdapter;
    BluetoothDevice device;
    ArrayAdapter<String> deviceList;
    ConnectThread newCon;
    static String info, address;
    AlertDialog.Builder bluetoothSelect, bloodSelect;
    int dataReceived, dataSent;
    String[] bloodTypes = {"A", "AB", "B", "O"};

    //onClick for spinner to select blood type
    public void onClick(View v) {
        final TextView bloodtxt = (TextView) findViewById(R.id.bloodType);
        final ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bloodTypes);
        bloodSelect = new AlertDialog.Builder(MainActivity.this);
        bloodSelect.setTitle("Select Blood type: ");
        bloodSelect.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        bloodSelect.setAdapter(
                bloodTypeAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bloodtxt.setText(bloodTypeAdapter.getItem(which));
                    }
                });
        bloodSelect.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        blueToothSetup();

        //setup of bluetoothSelect alertDialog
        bluetoothSelect = new AlertDialog.Builder(MainActivity.this);
        bluetoothSelect.setTitle("Select Bluetooth device:");
        bluetoothSelect.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        bluetoothSelect.setAdapter(
                deviceList,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        info = deviceList.getItem(which);
                        address = info.substring(info.length() - 17);

                        Log.e("Device info: ", info);
                        Log.e("MAC add: ", address);

                        device = BTAdapter.getRemoteDevice(address);
                        newCon = new ConnectThread(device);
                        //TODO: Move to Connect function
                        if (newCon.run(BTAdapter)) {
                            //TODO: Get coordinates
                            //Code works till here
                            try {
                                dataReceived = newCon.getData();
                                Log.e("Data get sucess", "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else Log.e("Error: ", "Failed connection D:");

                        dialog.dismiss();
                    }
                });
        bluetoothSelect.show(); //show dialogbox on startup

        //map button to bluetoothSelect alertdialog
        Button connectBt = (Button) findViewById(R.id.button);
        connectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueToothSetup();
                bluetoothSelect.show();
            }
        });
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

    //Connection setup
    private void blueToothSetup() {

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceList = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);

        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                deviceList.add(device.getName() + "\n" + device.getAddress());

            }
        }

        BTAdapter.startDiscovery();

        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    deviceList.add(device.getName() + "\n" + device.getAddress());
                    //Log.e("device",device.getName());
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        //deviceList.add("bob");
        //deviceList.add("cat");
        Log.e("Blutooth log: ", Integer.toString(deviceList.getCount()));
    }
}
