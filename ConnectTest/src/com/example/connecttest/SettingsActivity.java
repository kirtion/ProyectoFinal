package com.example.connecttest;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity{

	public static final int ACTIVAR_BT = 1;
	public static final int CONECTAR_A_DISPOSITIVO = 2;
	
	private BluetoothAdapter ba;
	private NXTComunicacion nt = new NXTComunicacion();
	private SocketManager sm;
	private AjustesManager am;
	
	EditText ip;
	EditText puerto;
	EditText nombre;
	EditText mac;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		am = AjustesManager.getInstance();
		sm = SocketManager.getInstance();
		ba = BluetoothAdapter.getDefaultAdapter();	
		ip = (EditText)findViewById(R.id.etIP);
		ip.setText(getIPAddress(true));
		puerto = (EditText)findViewById(R.id.etPuerto);
		nombre = (EditText)findViewById(R.id.etNombre);
		mac = (EditText)findViewById(R.id.etMac);
		puerto.setText("44444");
		mac.setText(am.getMac());
		nombre.setText(am.getNombre());
		am.setIp(getIPAddress(true));
	}
	
	public void escuchar(View v){
		sm.setContext(getBaseContext());
		sm.openConnection(Integer.valueOf(puerto.getText().toString()));
		am.setPuerto(puerto.getText().toString());
	}
	
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
						if (useIPv4) {
							if (isIPv4) 
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%');
								return delim<0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			
		}
		return "";
	}
	
	public void buscar(View v){
		if(ba.isEnabled()) {
			buscarNXT();
		} else {
			activarBluetooth();
		}
	}
	
	public void activarBluetooth() {
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableIntent, ACTIVAR_BT);
	}

	public void buscarNXT() {
		Intent intent = new Intent(this, ChooseDeviceActivity.class);
		startActivityForResult(intent, CONECTAR_A_DISPOSITIVO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVAR_BT:
			if (resultCode == Activity.RESULT_OK) {
				buscarNXT();
			} else {
				Toast.makeText(this, "Necesario encender el Bluetooth", Toast.LENGTH_LONG).show();
			}
			break;
		case CONECTAR_A_DISPOSITIVO:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(ChooseDeviceActivity.EXTRA_DEVICE_ADDRESS);
				String name = data.getExtras().getString(ChooseDeviceActivity.EXTRA_DEVICE_NAME);
				mac.setText(address);
				nombre.setText(name);
				BluetoothDevice device = ba.getRemoteDevice(address);
				nt.connect(device);
				am.setMac(address);
				am.setNombre(name);
				am.setnt(nt);
				if(sm.getSesionAbierta()) {
					finish();
				}
			}
			break;
		}
	}
}
