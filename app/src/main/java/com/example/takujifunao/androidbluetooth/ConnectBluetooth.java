package com.example.takujifunao.androidbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by takujifunao on 16/03/09.
 */
public class ConnectBluetooth implements Runnable {

    /* tag */
    private static final String TAG = "BluetoothSample";

    /* Bluetooth Adapter */
    private static BluetoothAdapter mAdapter;

    /* Bluetoothデバイス */
    private static BluetoothDevice mDevice;

    /* Bluetooth UUID */
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //uuidは固定

    /* デバイス名 */
    private static final String DEVICE_NAME = "RNBT-BDA2";

    /* Soket */
    private BluetoothSocket mSocket;

    /* Thread */
    private Thread mThread;

    /* Threadの状態を表す */
    private boolean isRunning;

    /** Action(ステータス表示). */
    private static final int VIEW_STATUS = 0;

    /** Action(取得文字列). */
    private static final int VIEW_INPUT = 1;

    /** Connect確認用フラグ */
    private boolean connectFlg = false;

    /** BluetoothのOutputStream. */
    OutputStream mmOutputStream = null;

    public static void settingBluetooth(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        Set< BluetoothDevice > devices = mAdapter.getBondedDevices();
        for ( BluetoothDevice device : devices){

            if(device.getName().equals(DEVICE_NAME)){
                mDevice = device;
            }
        }
    }

    @Override
    public void run() {
        InputStream mmInStream = null;

        try{


            // 取得したデバイス名を使ってBluetoothでSocket接続
            mSocket = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
            mSocket.connect();   ///////////////////////////////////////////////////////////ここでえらっ

            mmInStream = mSocket.getInputStream();
            mmOutputStream = mSocket.getOutputStream();



            // InputStreamのバッファを格納
            byte[] buffer = new byte[1024];

            // 取得したバッファのサイズを格納
            int bytes;

            connectFlg = true;

            while(isRunning){

                // InputStreamの読み込み
                bytes = mmInStream.read(buffer);
                Log.i(TAG,"bytes="+bytes);
                // String型に変換
                String readMsg = new String(buffer, 0, bytes);

                // null以外なら表示
                if(readMsg.trim() != null && !readMsg.trim().equals("")){
                    Log.i(TAG, "value=" + readMsg.trim());

                }
                else{
                    // Log.i(TAG,"value=nodata");
                }

            }
        }catch(Exception e){

            try{
                mSocket.close();
            }catch(Exception ee){}
            isRunning = false;
            connectFlg = false;
        }
    }

    public void connectBluetooth(){
        if (!connectFlg) {
                mThread.start();
        }
    }

}
