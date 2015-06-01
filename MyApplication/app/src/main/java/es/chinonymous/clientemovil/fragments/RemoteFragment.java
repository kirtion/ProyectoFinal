package es.chinonymous.clientemovil.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.fragments.control.ControlAcelerometroFragment;
import es.chinonymous.clientemovil.fragments.control.ControlCrucetaFragment;
import es.chinonymous.clientemovil.fragments.control.ControlTouchpadFragment;
import es.chinonymous.clientemovil.utility.AjustesManager;
import es.chinonymous.clientemovil.utility.SocketManager;


public class RemoteFragment extends Fragment {

    private OnRemoteFragmentInteractionListener mListener;

    public static RemoteFragment newInstance() {
        RemoteFragment fragment = new RemoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RemoteFragment() {
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
        return inflater.inflate(R.layout.fragment_remote, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment f = null;
        switch(AjustesManager.MODO_CONTROL) {
            case AjustesManager.ACELEROMETRO:
                f = ControlAcelerometroFragment.newInstance();
                break;
            case AjustesManager.CRUCETA:
                f = ControlCrucetaFragment.newInstance();
                break;
            case AjustesManager.JOYSTICK:
                f = ControlTouchpadFragment.newInstance();
                break;
        }
        getFragmentManager().beginTransaction().replace(R.id.containerRemote, f).commit();
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onRemoteFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRemoteFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnRemoteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(SocketManager.getInstance().getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    SocketManager.getInstance().sendLine("0,0");
                }
            };
            t.start();
        }
        mListener = null;
    }

    public interface OnRemoteFragmentInteractionListener {
        public void onRemoteFragmentInteraction();
    }

}
