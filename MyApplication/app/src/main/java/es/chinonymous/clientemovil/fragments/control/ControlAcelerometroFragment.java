package es.chinonymous.clientemovil.fragments.control;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.utility.Acelerometro;
import es.chinonymous.clientemovil.utility.AjustesManager;
import es.chinonymous.clientemovil.utility.SocketManager;


public class ControlAcelerometroFragment extends Fragment {

    private OnAcelerometroFragmentInteractionListener mListener;
    private Acelerometro acelerometro;
    private float deltaX, deltaY, deltaZ;
    private volatile boolean running;
    private AjustesManager ajustes;
    private ImageView flecha;
    private String aDondeApunta;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int op = msg.what;
            switch(op) {
                case 0:
                    updateAcelerometro();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public static ControlAcelerometroFragment newInstance() {
        ControlAcelerometroFragment fragment = new ControlAcelerometroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControlAcelerometroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acelerometro = Acelerometro.getInstance();
        ajustes = AjustesManager.getInstance();
        running = true;
        aDondeApunta = "";
        if (getArguments() != null) {
            startReceiveGiroscopeData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control_acelerometro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flecha = (ImageView) view.findViewById(R.id.flechaIv);
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onAcelerometroFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAcelerometroFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnAcelerometroFragmentInteractionListener");
        }
    }

    private void startReceiveGiroscopeData() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while(running && !currentThread().isInterrupted()) {
                    if(acelerometro != null) {
                        deltaX = (acelerometro.getDeltaX()-ajustes.getEjesAcelerometro()[0])*-1;
                        deltaY = acelerometro.getDeltaY()-ajustes.getEjesAcelerometro()[1];
                        deltaZ = acelerometro.getDeltaZ()-ajustes.getEjesAcelerometro()[2];

                        if(deltaX >= 2)
                            aDondeApunta = "0,100";
                        else if(deltaX <= -2)
                            aDondeApunta = "100,0";

                        if(deltaY >= 2)
                            aDondeApunta = "100,100";
                        else if(deltaY <= -2)
                            aDondeApunta = "-100,-100";

                        if((deltaX >= -1.9 && deltaX <= 1.9) && (deltaY >= -1.9 && deltaY <= 1.9))
                            aDondeApunta = "0,0";

                        if(SocketManager.getInstance().getSesionAbierta()) {
                            SocketManager.getInstance().sendLine(aDondeApunta);
                        }
                    }

                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    private void updateAcelerometro() {
        if(getActivity() != null)
            flecha.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrow));
        float angulo = 0;
        if(aDondeApunta.equals("100,100"))
            angulo=0;
        else if(aDondeApunta.equals("-100,-100"))
            angulo=180;
        else if(aDondeApunta.equals("0,100"))
            angulo=90;
        else if(aDondeApunta.equals("100,0"))
            angulo=270;
        else
            if(getActivity() != null)
                flecha.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.arrowstop));
        flecha.setRotation(angulo);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        running = false;
    }

    public interface OnAcelerometroFragmentInteractionListener {
        public void onAcelerometroFragmentInteraction();
    }

}
