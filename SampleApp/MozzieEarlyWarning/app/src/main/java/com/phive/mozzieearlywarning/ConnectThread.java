package com.phive.mozzieearlywarning;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Choco〜bourbon on 22-Nov-16.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    static ConnectedThread connected;

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public boolean run(BluetoothAdapter mBluetoothAdapter) {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return false;
        }

        // Do work to manage the connection (in a separate thread)
        connected = new ConnectedThread(mmSocket);
        return true;
    }

    public int getData() throws IOException {
        return connected.receiveData(mmSocket);
    }

    public boolean sendData(int data){
        try {
            connected.sendData(mmSocket,data);
        } catch (IOException e) {
            Log.e("Error: ","Data sending failed");
        }
        return true;
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
