package es.chinonymous.clientemovil.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.utility.AjustesManager;
import es.chinonymous.clientemovil.utility.SocketManager;

public class EncontrarClaptrapFragment extends Fragment {

    private OnEncontrarClaptrapFragmentInteractionListener mListener;
    private String dondeEstaClaptrap;
    private GoogleMap map;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                LatLng ltln = new LatLng(Double.valueOf(dondeEstaClaptrap.split(",")[0]), Double.valueOf(dondeEstaClaptrap.split(",")[1]));
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(ltln)
                        .zoom(17)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));

                CircleOptions area = new CircleOptions();
                area.center(ltln);
                area.fillColor(0x44DE1C1C);
                area.radius(80);
                area.strokeWidth(0);
                area.visible(true);
                map.addCircle(area);
            } catch (Exception e) {}
            super.handleMessage(msg);
        }
    };

    public static EncontrarClaptrapFragment newInstance() {
        EncontrarClaptrapFragment fragment = new EncontrarClaptrapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EncontrarClaptrapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_encontrar_claptrap, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        map = ((com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(AjustesManager.TIPO_MAPA);
        final SocketManager sm = SocketManager.getInstance();
        if(sm.getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    sm.sendLine("donde andas");
                    dondeEstaClaptrap = sm.readLine();
                    handler.sendEmptyMessage(0);
                }
            };
            t.start();
        }
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onEncontrarClaptrapFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEncontrarClaptrapFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnEncontrarClaptrapFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEncontrarClaptrapFragmentInteractionListener {
        public void onEncontrarClaptrapFragmentInteraction();
    }

}
