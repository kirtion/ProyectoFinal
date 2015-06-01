package es.chinonymous.clientemovil.utility;

import com.google.android.gms.maps.GoogleMap;

public class AjustesManager {

    private String ip;
    private int puerto;
    private static AjustesManager ajustesManager = new AjustesManager();
    private float ejesAcelerometro[];
    public final static int ACELEROMETRO = 1;
    public final static int JOYSTICK = 2;
    public final static int CRUCETA = 3;
    public static int MODO_CONTROL = CRUCETA;
    public final static int MAPA_HIBRIDO = GoogleMap.MAP_TYPE_HYBRID;
    public final static int MAPA_SATELITE = GoogleMap.MAP_TYPE_SATELLITE;
    public final static int MAPA_NORMAL = GoogleMap.MAP_TYPE_NORMAL;
    public static int TIPO_MAPA = MAPA_NORMAL;

    private AjustesManager() {
        ip = "";
        puerto = 0;
        ejesAcelerometro = new float[3];
        ejesAcelerometro[0] = 0;
        ejesAcelerometro[1] = 0;
        ejesAcelerometro[2] = 0;
        ip = "cabeza";
    }

    public static AjustesManager getInstance() {
        return ajustesManager;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPuerto(int puerto) {
        if(puerto > 0 && puerto <= 65550)
            this.puerto = puerto;
    }

    public void setEjesAcelerometro(float x, float y, float z) {
        ejesAcelerometro[0] = x;
        ejesAcelerometro[1] = y;
        ejesAcelerometro[2] = z;
    }

    public float[] getEjesAcelerometro() {
        return ejesAcelerometro;
    }

}
