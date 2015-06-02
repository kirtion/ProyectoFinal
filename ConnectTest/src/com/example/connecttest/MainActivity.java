package com.example.connecttest;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;



public class MainActivity extends Activity {

	public static final int ENABLE_BT = 1;
	public static final int CONNECT_DEVICE = 2;
	
	int cont = 0;
	private SocketManager sm;
	private AjustesManager am;
	private LinearLayout ly;
	private Location loc;
    private String lastLocation;
    private LocationListener lListener;
    private LocationManager lManager;

    Button start;
    
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ly.setBackground(getResources().getDrawable(R.drawable.back5));
				break;
			case 2:
				ly.setBackground(getResources().getDrawable(R.drawable.back2));
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start = (Button) findViewById(R.id.Start);
		sm = SocketManager.getInstance();
		am = AjustesManager.getInstance();
		ly = (LinearLayout)findViewById(R.id.linear);
		initTextToSpeech();
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}

            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location.getLatitude()+","+location.getLongitude();
            }
        };
        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, Criteria.ACCURACY_FINE, lListener);

        loc = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null)
            lastLocation = loc.getLatitude()+","+loc.getLongitude();
        else
            lastLocation = "41.5696657,1.996331";
	}
	
	private static int TTS_DATA_CHECK = 1;

	private TextToSpeech tts = null;
	private boolean ttsIsInit = false;


	private void initTextToSpeech() {
		Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA );
		startActivityForResult(intent, TTS_DATA_CHECK);
	}
	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_DATA_CHECK) {
			if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
				tts = new TextToSpeech(this, new OnInitListener() {
					public void onInit(int status) {
						if (status == TextToSpeech.SUCCESS) {
							ttsIsInit = true;
							tts.setLanguage(Locale.getDefault());
							tts.setPitch(0.8f); //FRECUENCIA
							tts.setSpeechRate(1.1f); //VELOCIDAD
						}
					}
				});
			} else {
				Intent installVoice = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installVoice);
			}
		}
	}

	public void start(View v) {
		if(sm.getSesionAbierta() && !am.getMac().equals("")) {
			handler.sendEmptyMessage(1);
			Thread t = new Thread() {
				@Override
				public void run() {
					while(!currentThread().isInterrupted() && sm.getSesionAbierta()) {
						String s = sm.readLine();
						if(s.equals("close")) {
							sm.closeConnection();
							handler.sendEmptyMessage(2);
						} else if(s.equals("hablar")) {
							sm.sendLine("esperando");
							String hablar = sm.readLine();
							if (tts != null && ttsIsInit) {
								HashMap<String, String> params = new HashMap<String, String>();
								String stringId = "stId" +  cont; cont++;
								params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, stringId );
								tts.speak(hablar, TextToSpeech.QUEUE_ADD, params); 
							}
						} else if(s.equals("donde andas")) {
							sm.sendLine(lastLocation);
						}
						else 
							am.getnt().motors(Byte.valueOf(s.split(",")[0]), Byte.valueOf(s.split(",")[1]), false, false);
					}
				}
			};
			t.start();
		} else {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
	}
}
