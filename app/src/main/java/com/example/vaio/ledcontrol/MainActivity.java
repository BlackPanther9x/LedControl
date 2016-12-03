package com.example.vaio.ledcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;
    private TextView tv_status;
    private ListView listView;
    private BluetoothAdapter bluetoothAdapter = null;
    public static String EXTRA_ADDRESS = "device_address";
    private FloatingActionButton fab;



    private void initUI(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        tv_status=(TextView)findViewById(R.id.tv_status);
        listView=(ListView)findViewById(R.id.listView);
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setSupportActionBar(toolbar);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!bluetoothAdapter.isEnabled()) {
            Intent intentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentOn,1);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }

    private void pairedDevicesList(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size()>0) {
            tv_status.setText(R.string.found_devices);
            tv_status.setBackgroundResource(R.color.colorFoundDevice);
            for(BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else tv_status.setText(R.string.no_device);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length() - 17);
        Intent i = new Intent(MainActivity.this, ControlActivity.class);
        i.putExtra(EXTRA_ADDRESS, address);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
