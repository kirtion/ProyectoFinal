package es.chinonymous.clientemovil.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.utility.Acelerometro;
import es.chinonymous.clientemovil.utility.AjustesManager;
import es.chinonymous.clientemovil.utility.SocketManager;

public class AjustesFragment extends Fragment {

    private OnAjustesFragmentInteractionListener mListener;
    private AjustesManager ajustes;
    private EditText ipET, puertoET;
    private RadioButton acelerometroRb, joystickRb, crucetaRb;
    private Button calibrarAcelerometro;

    public static AjustesFragment newInstance() {
        AjustesFragment fragment = new AjustesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AjustesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ajustes = AjustesManager.getInstance();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ipET = (EditText) view.findViewById(R.id.ipET);
        puertoET = (EditText) view.findViewById(R.id.puertoET);

        final Button conectarBtn = (Button) view.findViewById(R.id.conectarBtn);
        //setConectarBotonNombre(conectarBtn);
        conectarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(SocketManager.getInstance().getSesionAbierta()) {
                        SocketManager.getInstance().sendLine("close");
                        SocketManager.getInstance().closeConnection();
                    } else {
                        String ip = ipET.getText().toString();
                        int puerto = Integer.valueOf(puertoET.getText().toString());
                        SocketManager.getInstance().openConnection(ip, puerto);
                    }
                    //setConectarBotonNombre(conectarBtn);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getBaseContext(), "Error 20: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        calibrarAcelerometro = (Button) view.findViewById(R.id.calibrarAcelerometroBTN);
        calibrarAcelerometro.setVisibility((AjustesManager.MODO_CONTROL==AjustesManager.ACELEROMETRO)?Button.VISIBLE:Button.GONE);
        calibrarAcelerometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Acelerometro ac = Acelerometro.getInstance();
                    ajustes.setEjesAcelerometro(ac.getDeltaX(), ac.getDeltaY(), ac.getDeltaZ());
                    Toast.makeText(getActivity().getBaseContext(), "CALIBRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity().getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        acelerometroRb = (RadioButton) view.findViewById(R.id.acelerometroRadio);
        joystickRb = (RadioButton) view.findViewById(R.id.joystickRadio);
        crucetaRb = (RadioButton) view.findViewById(R.id.crucetaRadio);
        switch(AjustesManager.MODO_CONTROL) {
            case AjustesManager.ACELEROMETRO:
                acelerometroRb.setChecked(true);
                break;
            case AjustesManager.JOYSTICK:
                joystickRb.setChecked(true);
                break;
            case AjustesManager.CRUCETA:
                crucetaRb.setChecked(true);
                break;
        }

        acelerometroRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acelerometroRb.setChecked(true);
                AjustesManager.MODO_CONTROL = AjustesManager.ACELEROMETRO;
                calibrarAcelerometro.setVisibility(Button.VISIBLE);
            }
        });

        joystickRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joystickRb.setChecked(true);
                AjustesManager.MODO_CONTROL = AjustesManager.JOYSTICK;
                calibrarAcelerometro.setVisibility(Button.GONE);
            }
        });

        crucetaRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crucetaRb.setChecked(true);
                AjustesManager.MODO_CONTROL = AjustesManager.CRUCETA;
                calibrarAcelerometro.setVisibility(Button.GONE);
            }
        });

        final RadioButton mapaNormalRB = (RadioButton) view.findViewById(R.id.mapaNormalRadio);
        final RadioButton mapaSateliteRB = (RadioButton) view.findViewById(R.id.mapaSateliteRadio);
        final RadioButton mapaHibridoRB = (RadioButton) view.findViewById(R.id.mapaHibridoRadio);
        switch(AjustesManager.TIPO_MAPA) {
            case AjustesManager.MAPA_NORMAL:
                mapaNormalRB.setChecked(true);
                break;
            case AjustesManager.MAPA_SATELITE:
                mapaSateliteRB.setChecked(true);
                break;
            case AjustesManager.MAPA_HIBRIDO:
                mapaHibridoRB.setChecked(true);
                break;
        }

        mapaNormalRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapaNormalRB.setChecked(true);
                AjustesManager.TIPO_MAPA = AjustesManager.MAPA_NORMAL;
            }
        });

        mapaSateliteRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapaSateliteRB.setChecked(true);
                AjustesManager.TIPO_MAPA = AjustesManager.MAPA_SATELITE;
            }
        });

        mapaHibridoRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapaHibridoRB.setChecked(true);
                AjustesManager.TIPO_MAPA = AjustesManager.MAPA_HIBRIDO;
            }
        });
    }

    private void setConectarBotonNombre(Button b) {
        b.setText(SocketManager.getInstance().getSesionAbierta() ? "DESCONECTAR" : "CONECTAR");
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onAjustesFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAjustesFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnAjustesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAjustesFragmentInteractionListener {
        public void onAjustesFragmentInteraction();
    }

}
