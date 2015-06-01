package es.chinonymous.clientemovil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import es.chinonymous.clientemovil.fragments.AcercaDeFragment;
import es.chinonymous.clientemovil.fragments.AjustesFragment;
import es.chinonymous.clientemovil.fragments.EncontrarClaptrapFragment;
import es.chinonymous.clientemovil.fragments.RoboTalkFragment;
import es.chinonymous.clientemovil.fragments.RemoteFragment;
import es.chinonymous.clientemovil.fragments.control.ControlAcelerometroFragment;
import es.chinonymous.clientemovil.fragments.control.ControlCrucetaFragment;
import es.chinonymous.clientemovil.fragments.control.ControlTouchpadFragment;
import es.chinonymous.clientemovil.utility.Acelerometro;
import es.chinonymous.clientemovil.utility.AjustesManager;
import es.chinonymous.clientemovil.utility.Reproductor;
import es.chinonymous.clientemovil.utility.SocketManager;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
                    RemoteFragment.OnRemoteFragmentInteractionListener,
                    EncontrarClaptrapFragment.OnEncontrarClaptrapFragmentInteractionListener,
                    RoboTalkFragment.OnParteChinoFragmentInteractionListener,
                    AjustesFragment.OnAjustesFragmentInteractionListener,
                    ControlAcelerometroFragment.OnAcelerometroFragmentInteractionListener,
                    ControlTouchpadFragment.OnControlJoystickFragmentInteractionListener,
                    ControlCrucetaFragment.OnControlCrucetaFragmentInteractionListener,
                    AcercaDeFragment.OnAcercaDeFragmentInteractionListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private final int REMOTE = 0;
    private final int PARTE_CHINO = 1;
    private final int ENCONTRAR_CLAPTRAP = 2;
    private final int AJUSTES = 3;
    private final int ACERCA_DE = 4;
    private int backPresionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);

        backPresionado = 0;
        Acelerometro.getInstance().setListener(getBaseContext());
        SocketManager.getInstance().setContext(getBaseContext());
        setContentView(R.layout.activity_main);
        Reproductor.getInstance().setContext(getBaseContext());
        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment f = null;
        switch(position) {
            case REMOTE:
                f = RemoteFragment.newInstance();
                break;
            case PARTE_CHINO:
                f = RoboTalkFragment.newInstance();
                break;
            case ENCONTRAR_CLAPTRAP:
                f = EncontrarClaptrapFragment.newInstance();
                break;
            case AJUSTES:
                f = AjustesFragment.newInstance();
                break;
            case ACERCA_DE:
                f = AcercaDeFragment.newInstance();
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.container, f).commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.hide();
    }

    @Override
    protected void onResume() {
        if(!AjustesManager.getInstance().getIp().equals("cabeza") && !SocketManager.getInstance().getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    SocketManager.getInstance().openConnection();
                }
            };
            t.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(SocketManager.getInstance().getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    SocketManager.getInstance().sendLine("close");
                    SocketManager.getInstance().closeConnection();
                }
            };
            t.start();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(SocketManager.getInstance().getSesionAbierta()) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    SocketManager.getInstance().sendLine("close");
                    SocketManager.getInstance().closeConnection();
                }
            };
            t.start();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(++backPresionado == 2)
            super.onBackPressed();
        else {
            Toast.makeText(getBaseContext(), "Pulsa otra vez para salir", Toast.LENGTH_SHORT).show();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            backPresionado = 0;
                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 1000);
        }
    }

    // FRAGMENT COMMUNICATION
    @Override
    public void onAjustesFragmentInteraction() {

    }

    @Override
    public void onEncontrarClaptrapFragmentInteraction() {

    }

    @Override
    public void onParteChinoFragmentInteraction() {

    }

    @Override
    public void onRemoteFragmentInteraction() {

    }

    @Override
    public void onAcelerometroFragmentInteraction() {

    }

    @Override
    public void onControlCrucetaFragmentInteraction() {

    }

    @Override
    public void onControlJoystickFragmentInteraction() {

    }

    @Override
    public void onAcercaDeFragmentInteraction() {

    }
}
