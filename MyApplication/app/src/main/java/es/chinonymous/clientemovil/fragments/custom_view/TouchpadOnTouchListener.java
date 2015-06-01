package es.chinonymous.clientemovil.fragments.custom_view;

import android.view.MotionEvent;
import android.view.View;

import es.chinonymous.clientemovil.utility.SocketManager;

public class TouchpadOnTouchListener implements View.OnTouchListener {

    private String posAnterior, posActual;

    public TouchpadOnTouchListener() {
        posAnterior = "cabeza";
        posActual = "0,0";
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TouchPadView tpv = (TouchPadView) v;
        float x, y, power;
        int action = event.getAction();
        if ((action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_MOVE)) {
            x = (event.getX() - tpv.mCx) / tpv.mRadius;
            y = -1.0f * (event.getY() - tpv.mCy);
            if (y > 0f) {
                y -= tpv.mOffset;
                if (y < 0f) {
                    y = 0.01f;
                }
            } else if (y < 0f) {
                y += tpv.mOffset;
                if (y > 0f) {
                    y = -0.01f;
                }
            }
            y /= tpv.mRadius;
            float sqrt22 = 0.707106781f;
            float nx = x * sqrt22 + y * sqrt22;
            float ny = -x * sqrt22 + y * sqrt22;
            power = (float) Math.sqrt(nx * nx + ny * ny);
            if (power > 1.0f) {
                nx /= power;
                ny /= power;
                power = 1.0f;
            }
            float angle = (float) Math.atan2(y, x);
            float l, r;
            if (angle > 0f && angle <= Math.PI / 2f) {
                l = 1.0f;
                r = (float) (2.0f * angle / Math.PI);
            } else if (angle > Math.PI / 2f && angle <= Math.PI) {
                l = (float) (2.0f * (Math.PI - angle) / Math.PI);
                r = 1.0f;
            } else if (angle < 0f && angle >= -Math.PI / 2f) {
                l = -1.0f;
                r = (float) (2.0f * angle / Math.PI);
            } else if (angle < -Math.PI / 2f && angle > -Math.PI) {
                l = (float) (-2.0f * (angle + Math.PI) / Math.PI);
                r = -1.0f;
            } else {
                l = r = 0f;
            }
            l *= power;
            r *= power;
            String s = (byte)(100 * r) + "," + (byte)(100 * l);
            posActual = s;
        } else if ((action == MotionEvent.ACTION_UP) || (action == MotionEvent.ACTION_CANCEL)) {
            String s = (byte) (0) + "," + (byte) (0);
            posActual = s;
        }

        Thread t = new Thread() {
            @Override
            public void run() {
                if (SocketManager.getInstance().getSesionAbierta()) {
                    if(!posAnterior.equals(posActual)) {
                        posAnterior = posActual;
                        SocketManager.getInstance().sendLine(posActual);
                    }
                }
            }
        };
        t.start();
        return true;
    }
}
