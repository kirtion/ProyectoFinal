package es.chinonymous.clientemovil.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import es.chinonymous.clientemovil.R;
import es.chinonymous.clientemovil.utility.Reproductor;

public class AcercaDeFragment extends Fragment {

    private OnAcercaDeFragmentInteractionListener mListener;
    private final static String TAG = "clientemovil";
    private int posicionActualSV;
    private int numeroRandom;

    public static AcercaDeFragment newInstance() {
        AcercaDeFragment fragment = new AcercaDeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AcercaDeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posicionActualSV = 0;
        numeroRandom = (int)(Math.random()*3);
        Reproductor.getInstance().startAcercaDeMusic(numeroRandom);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_acerca_de, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ScrollView sv = (ScrollView) view.findViewById(R.id.acercaDeSV);
        sv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        while(!currentThread().isInterrupted() && posicionActualSV < 4650) {
                            sv.scrollTo(posicionActualSV, ++posicionActualSV);
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                t.start();
            }
        });

        final LinearLayout ll = (LinearLayout) view.findViewById(R.id.layoutBaseSV);
        final int colores[] = {0xff0101DF, 0xffFF0040, 0xffFE2EC8, 0xff00FFFF, 0xffFF8000, 0xff01DF01, 0xff0080FF,
                               0xffBFFF00, 0xff8000FF, 0xffF79F81, 0xff4B8A08};

        Thread t2 = new Thread() {
            @Override
            public void run() {
                while(!currentThread().isInterrupted()&& posicionActualSV < 4650) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll.setBackgroundColor(colores[(int)(Math.random()*colores.length)]);
                        }
                    });
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(numeroRandom == 1)
            t2.start();
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onAcercaDeFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAcercaDeFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnAcercaDeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        posicionActualSV = 4650;
        Reproductor.getInstance().stopMusic();
        mListener = null;
    }

    public interface OnAcercaDeFragmentInteractionListener {
        public void onAcercaDeFragmentInteraction();
    }

}
