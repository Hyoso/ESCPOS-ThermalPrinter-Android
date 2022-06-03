package com.dantsu.escposprinter.connection.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Debug;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dantsu.escposprinter.exceptions.EscPosConnectionException;

import java.util.logging.Logger;

public class BluetoothPrintersConnections extends BluetoothConnections {

    /**
     * Easy way to get the first bluetooth printer paired / connected.
     *
     * @return a EscPosPrinterCommands instance
     */
    @Nullable
    public static BluetoothConnection selectFirstPaired() {
        BluetoothPrintersConnections printers = new BluetoothPrintersConnections();
        BluetoothConnection[] bluetoothPrinters = printers.getList();

        Log.i("Unity", "" + bluetoothPrinters.length);

        if (bluetoothPrinters != null && bluetoothPrinters.length > 0) {
            for (BluetoothConnection printer : bluetoothPrinters) {
                try {
                    return printer.connect();
                } catch (EscPosConnectionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Get a list of bluetooth printers.
     *
     * @return an array of EscPosPrinterCommands
     */
    @SuppressLint("MissingPermission")
    @Nullable
    public BluetoothConnection[] getList() {
        BluetoothConnection[] bluetoothDevicesList = super.getList();

        Log.i("Unity", "list len " + bluetoothDevicesList.length);

        if (bluetoothDevicesList == null) {
            return null;
        }

        int i = 0;
        BluetoothConnection[] printersTmp = new BluetoothConnection[bluetoothDevicesList.length];
        for (BluetoothConnection bluetoothConnection : bluetoothDevicesList) {
            BluetoothDevice device = bluetoothConnection.getDevice();
            Log.i("Unity", "device name " + device.getName());

            int majDeviceCl = device.getBluetoothClass().getMajorDeviceClass(),
                    deviceCl = device.getBluetoothClass().getDeviceClass();
            boolean hasPrinterInName = device.getName().contains("Printer");

            if ((majDeviceCl == BluetoothClass.Device.Major.IMAGING && (deviceCl == 1664 || deviceCl == BluetoothClass.Device.Major.IMAGING)) || hasPrinterInName)
            {
                printersTmp[i++] = new BluetoothConnection(device);
                Log.i("Unity", "added");
            }
        }
        BluetoothConnection[] bluetoothPrinters = new BluetoothConnection[i];
        System.arraycopy(printersTmp, 0, bluetoothPrinters, 0, i);

//        Log.i("Unity", "num of bluetooth connections " + bluetoothPrinters.length);
        return bluetoothPrinters;
    }

    public String messageFromJava()
    {
        return "message from java";
    }
}
