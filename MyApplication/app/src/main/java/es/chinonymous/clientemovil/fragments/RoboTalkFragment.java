package es.chinonymous.clientemovil.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.utility.SocketManager;

public class RoboTalkFragment extends Fragment implements View.OnClickListener {

    private OnParteChinoFragmentInteractionListener mListener;
    private Button hola, buenosDias, queTal, adios, queVayaBien, paz, socorro, policia, primo, enviar;
    private EditText textToSpeech;
    private String textoAEnviar;

    public static RoboTalkFragment newInstance() {
        RoboTalkFragment fragment = new RoboTalkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RoboTalkFragment() {
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
        return inflater.inflate(R.layout.fragment_robo_talk, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hola = (Button) view.findViewById(R.id.holaBtn);
        hola.setOnClickListener(this);
        buenosDias = (Button) view.findViewById(R.id.buenosDiasBtn);
        buenosDias.setOnClickListener(this);
        queTal = (Button) view.findViewById(R.id.queTalEstasBtn);
        queTal.setOnClickListener(this);
        adios = (Button) view.findViewById(R.id.adiosBtn);
        adios.setOnClickListener(this);
        queVayaBien = (Button) view.findViewById(R.id.queVayaBienBtn);
        queVayaBien.setOnClickListener(this);
        paz = (Button) view.findViewById(R.id.pazColegaBtn);
        paz.setOnClickListener(this);
        socorro = (Button) view.findViewById(R.id.socorroBtn);
        socorro.setOnClickListener(this);
        policia = (Button) view.findViewById(R.id.policiaBtn);
        policia.setOnClickListener(this);
        primo = (Button) view.findViewById(R.id.primoBtn);
        primo.setOnClickListener(this);
        enviar = (Button) view.findViewById(R.id.enviarTextoBtn);
        enviar.setOnClickListener(this);

        textToSpeech = (EditText) view.findViewById(R.id.textToSpeechET);
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onParteChinoFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnParteChinoFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnParteChinoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        textoAEnviar = "";
        if(v.getId() == R.id.enviarTextoBtn) {
            textoAEnviar = textToSpeech.getText().toString();
        } else {
            Button b = (Button) v.findViewById(v.getId());
            textoAEnviar = b.getText().toString();
        }

        if(SocketManager.getInstance().getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    SocketManager.getInstance().sendLine("hablar");
                    String s = SocketManager.getInstance().readLine();
                    if(s.equals("esperando"))
                        SocketManager.getInstance().sendLine(textoAEnviar);
                }
            };
            t.start();
        }
    }


    public interface OnParteChinoFragmentInteractionListener {
        public void onParteChinoFragmentInteraction();
    }

}
