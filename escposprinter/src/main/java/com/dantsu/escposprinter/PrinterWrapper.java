package com.dantsu.escposprinter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.dantsu.escposprinter.async.AsyncBluetoothEscPosPrint;
import com.dantsu.escposprinter.async.AsyncEscPosPrint;
import com.dantsu.escposprinter.async.AsyncEscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PrinterWrapper
{
    private static DeviceConnection s_device;

    // must call first to construct static members
    public static void Init()
    {
        if (s_device == null)
        {
            s_device = BluetoothPrintersConnections.selectFirstPaired();
        }
    }

    public void Print()
    {
        DeviceConnection d = BluetoothPrintersConnections.selectFirstPaired();

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        String outputText =
                "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                        "[L]\n" +
                        "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                        "[C]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99€\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99€\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98€\n" +
                        "[R]TAX :[R]4.23€\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "\n" +
                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                        "[L]\n" +
                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n\n";

        try
        {
            EscPosPrinter p = new EscPosPrinter(
                    d,
                    203,
                    58f,
                    32,
                    new EscPosCharsetEncoding("windows-1252", 16)
            );

            p.printFormattedText(outputText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        d.disconnect();
    }

    public static void Print(String outputText)
    {
        try
        {
            EscPosPrinter p = new EscPosPrinter(
                    s_device,
                    203,
                    58f,
                    32,
                    new EscPosCharsetEncoding("windows-1252", 16)
            );

            p.printFormattedText(outputText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
