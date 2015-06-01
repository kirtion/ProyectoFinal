package es.chinonymous.clientemovil.fragments.adapter;

import android.graphics.drawable.Drawable;

public class DrawerOption {

    private String nombre;
    private Drawable image;

    public DrawerOption() {

    }

    public DrawerOption(String nombre) {
        this.nombre = nombre;
    }

    public DrawerOption(String nombre, Drawable image) {
        this.nombre = nombre;
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable drawable) {
        this.image = image;
    }

}
