package com.ncy.datos;

import android.content.Context;
import android.content.SharedPreferences;

public class RepositorioConfiguracion implements AjustesRepository, ThemeRepository {

    private static final String NOMBRE_PREFS         = "ncy_config";
    
    private static final int     ALTURA_DEFECTO          = 115; 
    private static final boolean MOSTRAR_BARRA_DEFECTO   = true;
    private static final boolean AUTO_MAYUSCULAS_DEFECTO = true;
    private static final String  TEMA_DEFECTO            = "oscuro";
    
    private final SharedPreferences prefs;
    private Runnable oyenteVisual;

    private final SharedPreferences.OnSharedPreferenceChangeListener listenerInterno = 
        (sharedPrefs, clave) -> {
            if ((ClavesPreferencia.ALTURA.equals(clave) || 
                 ClavesPreferencia.MOSTRAR_BARRA.equals(clave) || 
                 ClavesPreferencia.TEMA_TECLADO.equals(clave)) && oyenteVisual != null) {
                oyenteVisual.run();
            }
        };

    public RepositorioConfiguracion(Context contexto) {
        this.prefs = contexto.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public void setOyenteCambiosVisuales(Runnable oyente) {
        this.oyenteVisual = oyente;
        if (oyente != null) {
            prefs.registerOnSharedPreferenceChangeListener(listenerInterno);
        } else {
            prefs.unregisterOnSharedPreferenceChangeListener(listenerInterno);
        }
    }

    @Override public boolean leerVibracion() { return prefs.getBoolean(ClavesPreferencia.VIBRACION, true); }
    @Override public void guardarVibracion(boolean valor) { guardar(ClavesPreferencia.VIBRACION, valor); }

    @Override public String leerIdTemaTeclado() { return prefs.getString(ClavesPreferencia.TEMA_TECLADO, TEMA_DEFECTO); }
    @Override public void guardarIdTemaTeclado(String idTema) { guardar(ClavesPreferencia.TEMA_TECLADO, idTema); }

    @Override public String leerIdTemaUI() { return prefs.getString(ClavesPreferencia.TEMA_UI, TEMA_DEFECTO); }
    @Override public void guardarIdTemaUI(String idTema) { guardar(ClavesPreferencia.TEMA_UI, idTema); }

    @Override public int leerAlturaTeclado() { return prefs.getInt(ClavesPreferencia.ALTURA, ALTURA_DEFECTO); }
    @Override public void guardarAlturaTeclado(int altura) { guardar(ClavesPreferencia.ALTURA, altura); }

    @Override public boolean leerMostrarBarra() { return prefs.getBoolean(ClavesPreferencia.MOSTRAR_BARRA, MOSTRAR_BARRA_DEFECTO); }
    @Override public void guardarMostrarBarra(boolean mostrar) { guardar(ClavesPreferencia.MOSTRAR_BARRA, mostrar); }

    @Override public boolean leerAutoMayusculas() { return prefs.getBoolean(ClavesPreferencia.AUTO_MAYUSCULAS, AUTO_MAYUSCULAS_DEFECTO); }
    @Override public void guardarAutoMayusculas(boolean auto) { guardar(ClavesPreferencia.AUTO_MAYUSCULAS, auto); }

    private void guardar(String clave, int valor) { prefs.edit().putInt(clave, valor).apply(); }
    private void guardar(String clave, boolean valor) { prefs.edit().putBoolean(clave, valor).apply(); }
    private void guardar(String clave, String valor) { prefs.edit().putString(clave, valor).apply(); }
}