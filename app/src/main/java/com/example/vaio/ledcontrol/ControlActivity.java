package com.example.vaio.ledcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Toolbar appbar;
    private String address = null, last_msg="";
    private BluetoothAdapter bluetoothAdapter=null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    private Button  btn_disconnect, btn_led1,btn_led2, btn_led3, btn_led4;
    private Button btn_send;
    private AutoCompleteTextView edt_input;
    private SeekBar seek_bar;
    private TextView tv_output,tv_status_connect, tv_temp, tv_brightness;
    private boolean checkLed1=false, checkLed2=false,checkLed3=false,checkLed4=false;
    private String [] list;
    private ArrayAdapter<String> arrayAdapter;
    private float temp_progress;

    private void initUI(){
        appbar=(Toolbar)findViewById(R.id.appbar);
        btn_send=(Button)findViewById(R.id.btn_send);
        btn_disconnect=(Button)findViewById(R.id.btn_disconnect);
        edt_input=(AutoCompleteTextView) findViewById(R.id.edt_input);
        tv_output=(TextView)findViewById(R.id.tv_output);
        tv_temp=(TextView)findViewById(R.id.tv_temp);
        tv_brightness=(TextView)findViewById(R.id.tv_brightness);
        btn_led1=(Button)findViewById(R.id.btn_led1);
        btn_led2=(Button) findViewById(R.id.btn_led2);
        btn_led3=(Button)findViewById(R.id.btn_led3);
        btn_led4=(Button)findViewById(R.id.btn_led4);
        tv_status_connect=(TextView)findViewById(R.id.tv_status_connect);
        seek_bar=(SeekBar)findViewById(R.id.seek_bar);
        seek_bar.setProgress(100);
    }

    private void Click(){
        btn_send.setOnClickListener(this);
        btn_disconnect.setOnClickListener(this);
        btn_led1.setOnClickListener(this);
        btn_led2.setOnClickListener(this);
        btn_led3.setOnClickListener(this);
        btn_led4.setOnClickListener(this);
        btn_disconnect.setOnClickListener(this);
        seek_bar.setOnSeekBarChangeListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                String string = edt_input.getText().toString();
                switch (string){
                    case "ON1":
                        checkLed1=true;
                        btn_led1.setBackgroundResource(R.color.led1On);
                        break;
                    case "OFF1":
                        checkLed1=false;
                        btn_led1.setBackgroundResource(R.color.led1Off);
                        break;
                    case "ON2":
                        checkLed2=true;
                        btn_led2.setBackgroundResource(R.color.led2On);
                        break;
                    case "OFF2":
                        checkLed2=false;
                        btn_led2.setBackgroundResource(R.color.led2Off);
                        break;
                    case "ON3":
                        checkLed3=true;
                        btn_led3.setBackgroundResource(R.color.led3On);
                        break;
                    case "OFF3":
                        checkLed3=false;
                        btn_led3.setBackgroundResource(R.color.led3Off);
                        break;
                    case "ON4":
                        checkLed4=true;
                        btn_led4.setBackgroundResource(R.color.led4On);
                        break;
                    case "OFF4":
                        checkLed4=false;
                        btn_led4.setBackgroundResource(R.color.led4Off);
                        break;
                    case "ON":
                        checkLed1=true; checkLed2=true; checkLed3=true; checkLed4=true;
                        btn_led1.setBackgroundResource(R.color.led1On);
                        btn_led2.setBackgroundResource(R.color.led2On);
                        btn_led3.setBackgroundResource(R.color.led3On);
                        btn_led4.setBackgroundResource(R.color.led4On);
                        break;
                    case "OFF":
                        checkLed1=false;checkLed2=false;checkLed3=false;checkLed4=false;
                        btn_led1.setBackgroundResource(R.color.led1Off);
                        btn_led2.setBackgroundResource(R.color.led2Off);
                        btn_led3.setBackgroundResource(R.color.led3Off);
                        btn_led4.setBackgroundResource(R.color.led4Off);
                        break;

                    default:
                        Toast.makeText(this, R.string.ma_nhap_khong_dung, Toast.LENGTH_SHORT).show();
                        break;
                }
                mConnectedThread.write(edt_input.getText().toString());
                break;
            case R.id.btn_led1:
                if(checkLed1){
                    checkLed1=false;
                    btn_led1.setBackgroundResource(R.color.led1Off);
                    mConnectedThread.write("OFF1");
                }
                else {checkLed1=true;
                    btn_led1.setBackgroundResource(R.color.led1On);
                    mConnectedThread.write("ON1");
                }
                break;



            case R.id.btn_led2:
                if(checkLed2){
                    checkLed2=false;
                    btn_led2.setBackgroundResource(R.color.led2Off);
                    mConnectedThread.write("OFF2");
                }
                else {checkLed2=true;
                    btn_led2.setBackgroundResource(R.color.led2On);
                    mConnectedThread.write("ON2");
                }
                break;


            case R.id.btn_led3:
                if(checkLed3){
                    checkLed3=false;
                    btn_led3.setBackgroundResource(R.color.led3Off);
                    mConnectedThread.write("OFF3");
                }
                else {checkLed3=true;
                    btn_led3.setBackgroundResource(R.color.led3On);
                    mConnectedThread.write("ON3");
                }
                break;


            case R.id.btn_led4:
                if(checkLed4){
                    checkLed4=false;
                    btn_led4.setBackgroundResource(R.color.led4Off);
                    mConnectedThread.write("OFF4");
                }
                else {checkLed4=true;
                    btn_led4.setBackgroundResource(R.color.led4On);
                    mConnectedThread.write("ON4");
                }
                break;

            case R.id.btn_disconnect:
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mConnectedThread.cancel();
                finish();
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent getIntent= getIntent();
        address=getIntent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        setContentView(R.layout.activity_control);
        initUI();
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle(R.string.led_control);

        list=getResources().getStringArray(R.array.lenh);
        arrayAdapter= new ArrayAdapter<>(ControlActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
        edt_input.setAdapter(arrayAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(this, R.string._false, Toast.LENGTH_SHORT).show();
        }

        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                //insert code to deal with this
            }
        }

        if (btSocket != null) {
            Toast.makeText(this, R.string.connect_successful, Toast.LENGTH_SHORT).show();
            tv_status_connect.setText(R.string.connect_successful);
            tv_status_connect.setBackgroundResource(R.color.colorFoundDevice);
        }

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        Click();




    }

    @Override
    protected void onPause() {
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mConnectedThread.cancel();
        finish();
        super.onPause();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(myUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }




    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            mmSocket=socket;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(ControlActivity.this, "False", Toast.LENGTH_SHORT).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[1024];
            int begin=0;
            int bytes=0;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes += mmInStream.read(buffer,bytes,buffer.length-bytes);        	//read bytes from input buffer
                    for(int i = begin; i < bytes; i++) {
                        if(buffer[i] == "#".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) { bytes = 0; begin = 0; }
                        }

                        if(buffer[i] == ".".getBytes()[0]) {
                            mHandler.obtainMessage(2, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) { bytes = 0; begin = 0; }
                        }

//                        if(buffer[i] == "?".getBytes()[0]) {
//                            mHandler.obtainMessage(3, begin, i, buffer).sendToTarget();
//                            begin = i + 1;
//                            if(i == bytes - 1) { bytes = 0; begin = 0; }
//                        }

                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(ControlActivity.this, R.string._false, Toast.LENGTH_SHORT).show();
                finish();

            }
        }
        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(ControlActivity.this, "Exit", Toast.LENGTH_SHORT).show();} }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = msg.arg1; int end = msg.arg2;
            switch(msg.what) {
                case 1:
                    String message1 = new String(writeBuf);
                    message1 = message1.substring(begin, end);
                    tv_output.setText(message1);
                    break;

                case 2:
                    String message2 = new String(writeBuf);
                    message2 = message2.substring(begin, end);
                    float a=Float.parseFloat(message2)/100;
                    last_msg= String.valueOf(a);
                    tv_temp.setText(last_msg);
                    Log.d("temperature",last_msg);
                    break;
//                case 3:
//                    String message3 = new String(writeBuf);
//                    message3 = message3.substring(begin, end);
//                    Log.d("receiver",message3);
//                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        mConnectedThread.cancel();
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }



    /////////////////////////////////Seekbar Control////////////////////////////////////////////////

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_brightness.setText("Độ sáng : " + progress+" %" );
        temp_progress= (float) progress * 2.5f;
        if(temp_progress<255 && temp_progress>=2.5) mConnectedThread.write(String.valueOf(temp_progress));
        if(temp_progress <2.5) mConnectedThread.write(String.valueOf(0.1));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(temp_progress<255 && temp_progress>=2.5) mConnectedThread.write(String.valueOf(temp_progress));
        if(temp_progress <2.5) mConnectedThread.write(String.valueOf(0.1));
    }
}
