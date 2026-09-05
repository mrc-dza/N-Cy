package com.ncy;

import android.content.Context;
import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.GestorTemaUI;

public class AppServicios {
    private static RepositorioConfiguracion repositorio;
    private static GestorTemaUI gestorTemaUI;
    private static GestorTema gestorTema;

    public static void init(Context ctx) {
        if (repositorio == null) {
            repositorio = new RepositorioConfiguracion(ctx.getApplicationContext());
            gestorTemaUI = new GestorTemaUI(repositorio);
            gestorTema = new GestorTema(repositorio);
        }
    }

    public static RepositorioConfiguracion getRepositorio() { return repositorio; }
    public static GestorTemaUI getGestorTemaUI() { return gestorTemaUI; }
    public static GestorTema getGestorTema() { return gestorTema; }
}