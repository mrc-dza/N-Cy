package com.ncy.interfaz.teclas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

public class CacheDrawables {

    private final SparseArray<SparseArray<Drawable>> cache = new SparseArray<>();
    private final Context contexto;

    public CacheDrawables(Context contexto) {
        this.contexto = contexto;
    }

    public Drawable obtener(int resId, int tinte) {
        if (resId == 0) return null;

        SparseArray<Drawable> porTinte = cache.get(resId);
        if (porTinte == null) {
            porTinte = new SparseArray<>();
            cache.put(resId, porTinte);
        }

        Drawable hit = porTinte.get(tinte);
        if (hit != null) return hit;

        Drawable nuevo = contexto.getDrawable(resId);
        if (nuevo == null) return null;
        nuevo = nuevo.mutate();
        nuevo.setTint(tinte);
        porTinte.put(tinte, nuevo);
        return nuevo;
    }

    public Drawable obtenerPorNombre(String nombre, int tinte) {
        int id = contexto.getResources().getIdentifier(
                nombre, "drawable", contexto.getPackageName());
        return obtener(id, tinte);
    }

    public void limpiar() {
        cache.clear();
    }
}