package es.chinonymous.clientemovil.fragments.control;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.fragments.custom_view.TouchPadView;
import es.chinonymous.clientemovil.fragments.custom_view.TouchpadOnTouchListener;

public class ControlTouchpadFragment extends Fragment {

    private OnControlJoystickFragmentInteractionListener mListener;
    private TouchPadView mTouchPadView;

    public static ControlTouchpadFragment newInstance() {
        ControlTouchpadFragment fragment = new ControlTouchpadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ControlTouchpadFragment() {
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
        return inflater.inflate(R.layout.fragment_control_touchpad, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTouchPadView = (TouchPadView) view.findViewById(R.id.touchpad);
        mTouchPadView.setOnTouchListener(new TouchpadOnTouchListener());
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onControlJoystickFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnControlJoystickFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnControlJoystickFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnControlJoystickFragmentInteractionListener {
        public void onControlJoystickFragmentInteraction();
    }

}
