package com.ncy.datos;

import android.content.Context;
import android.content.SharedPreferences;

public class RepositorioConfiguracion {

    public static final String CLAVE_VIBRACION       = "pref_vibrar_pulsar";

    public static final String NOMBRE_PREFS          = "ncy_config";
    public static final String CLAVE_ALTURA          = "pref_altura_teclado";
    public static final String CLAVE_MOSTRAR_BARRA   = "pref_mostrar_barra";
    public static final String CLAVE_AUTO_MAYUSCULAS = "pref_auto_mayusculas";
    public static final String CLAVE_TEMA_TECLADO    = "pref_tema_teclado";
    public static final String CLAVE_TEMA_UI         = "pref_tema_ui";
    
    // Fallback seguro unificado en lugar de llamar a Tema.ALTURA_FILA_BASE_PX
    private static final int     ALTURA_DEFECTO          = 115; 
    private static final boolean MOSTRAR_BARRA_DEFECTO   = true;
    private static final boolean AUTO_MAYUSCULAS_DEFECTO = true;
    private static final String  TEMA_DEFECTO            = "oscuro";
    
    private final SharedPreferences prefs;


    public boolean leerVibracion() {
        return prefs.getBoolean(CLAVE_VIBRACION, true);
    }

    public void guardarVibracion(boolean valor) {
        guardar(CLAVE_VIBRACION, valor);
    }

    public RepositorioConfiguracion(Context contexto) {
        this.prefs = contexto.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE);
    }

    public String leerIdTemaTeclado() {
        return prefs.getString(CLAVE_TEMA_TECLADO, TEMA_DEFECTO);
    }

    public void guardarIdTemaTeclado(String idTema) {
        guardar(CLAVE_TEMA_TECLADO, idTema);
    }

    public String leerIdTemaUI() {
        return prefs.getString(CLAVE_TEMA_UI, TEMA_DEFECTO);
    }

    public void guardarIdTemaUI(String idTema) {
        guardar(CLAVE_TEMA_UI, idTema);
    }

    public int leerAlturaTeclado() {
        return prefs.getInt(CLAVE_ALTURA, ALTURA_DEFECTO);
    }

    public boolean leerMostrarBarra() {
        return prefs.getBoolean(CLAVE_MOSTRAR_BARRA, MOSTRAR_BARRA_DEFECTO);
    }

    public boolean leerAutoMayusculas() {
        return prefs.getBoolean(CLAVE_AUTO_MAYUSCULAS, AUTO_MAYUSCULAS_DEFECTO);
    }

    public void guardarAutoMayusculas(boolean auto) {
        guardar(CLAVE_AUTO_MAYUSCULAS, auto);
    }

    public void guardarAlturaTeclado(int altura) {
        guardar(CLAVE_ALTURA, altura);
    }

    public void guardarMostrarBarra(boolean mostrar) {
        guardar(CLAVE_MOSTRAR_BARRA, mostrar);
    }

    private void guardar(String clave, int valor) {
        prefs.edit().putInt(clave, valor).apply();
    }

    private void guardar(String clave, boolean valor) {
        prefs.edit().putBoolean(clave, valor).apply();
    }

    private void guardar(String clave, String valor) {
        prefs.edit().putString(clave, valor).apply();
    }
}