package es.chinonymous.clientemovil.utility;

import android.content.Context;
import android.media.MediaPlayer;

import es.chinonymous.clientemovil.R;

/**
 * Created by kirti_000 on 23/05/2015.
 */
public class Reproductor {

    private static Reproductor reproductor = new Reproductor();
    private Context context;
    private MediaPlayer mp;
    private boolean sonando;

    private Reproductor() {
        mp = new MediaPlayer();
    }

    public static Reproductor getInstance() {
        return reproductor;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void startAcercaDeMusic(int random) {
        killMediaPlayer();
        sonando = true;
        switch(random) {
            case 0:
                mp = MediaPlayer.create(context, R.raw.i_believe_i_can_fly);
                break;
            case 1:
                mp = MediaPlayer.create(context, R.raw.lego);
                break;
            case 2:
                mp = MediaPlayer.create(context, R.raw.i_like_to_move_it);
                break;
        }
        mp.start();
    }

    public void stopMusic() {
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        sonando = false;
        if(mp != null)
            mp.release();
    }

    public boolean getSonando() {
        return sonando;
    }

}
