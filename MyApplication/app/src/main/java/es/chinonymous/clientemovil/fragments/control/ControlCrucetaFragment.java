package es.chinonymous.clientemovil.fragments.control;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.utility.SocketManager;

public class ControlCrucetaFragment extends Fragment implements View.OnClickListener {

    private OnControlCrucetaFragmentInteractionListener mListener;
    private Button arriba, abajo, izquierda, derecha, parar;
    private String mensajeAEnviar;

    public static ControlCrucetaFragment newInstance() {
        ControlCrucetaFragment fragment = new ControlCrucetaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControlCrucetaFragment() {
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
        return inflater.inflate(R.layout.fragment_control_cruceta, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arriba = (Button) view.findViewById(R.id.crucetaAdelanteBtn);
        arriba.setOnClickListener(this);

        abajo= (Button) view.findViewById(R.id.crucetaAtrasBtn);
        abajo.setOnClickListener(this);

        izquierda = (Button) view.findViewById(R.id.crucetaIzquierdaBtn);
        izquierda.setOnClickListener(this);

        derecha = (Button) view.findViewById(R.id.crucetaDerechaBtn);
        derecha.setOnClickListener(this);

        parar = (Button) view.findViewById(R.id.crucetaPararBtn);
        parar.setOnClickListener(this);

    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onControlCrucetaFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnControlCrucetaFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnControlCrucetaFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.crucetaAdelanteBtn:
                mensajeAEnviar = "100,100";
                break;
            case R.id.crucetaAtrasBtn:
                mensajeAEnviar = "-100,-100";
                break;
            case R.id.crucetaDerechaBtn:
                mensajeAEnviar = "0,100";
                break;
            case R.id.crucetaIzquierdaBtn:
                mensajeAEnviar = "100,0";
                break;
            case R.id.crucetaPararBtn:
                mensajeAEnviar = "0,0";
                break;
        }

        if(SocketManager.getInstance().getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    SocketManager.getInstance().sendLine(mensajeAEnviar);
                }
            };
            t.start();
        }
    }

    public interface OnControlCrucetaFragmentInteractionListener {
        public void onControlCrucetaFragmentInteraction();
    }

}
