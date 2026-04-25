package com.ncy.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import com.ncy.datos.RepositorioConfiguracion;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestorTema {

    private static class Holder {
        static final GestorTema INSTANCIA = new GestorTema();
    }
    
    private RepositorioConfiguracion repositorio;
   
    private final Map<String, TemaVisual> temas = new LinkedHashMap<>();
    private TemaVisual temaActivo;
    private String idTemaActivo;
    private Context contextoApp;

    public interface OnTemaCambiadoListener {
        void onTemaCambiado(TemaVisual nuevoTema);
    }


    private final List<OnTemaCambiadoListener> oyentes = new java.util.concurrent.CopyOnWriteArrayList<>();


    private GestorTema() {
        temas.put("oscuro", new TemaOscuro());
        temas.put("claro", new TemaClaro()); 
        temas.put("neon verde", new TemaNeonVerde());

        idTemaActivo = "oscuro";
        temaActivo = temas.get(idTemaActivo);
    }

    public static GestorTema getInstance() {
        return Holder.INSTANCIA;
    }

    public void inicializar(Context contexto) {
        this.contextoApp = contexto.getApplicationContext();
        this.repositorio = new RepositorioConfiguracion(this.contextoApp); // Instanciamos el repositorio

        String idGuardado = repositorio.leerIdTemaTeclado(); // Usamos el repositorio en lugar de SharedPreferences directamente

        if (temas.containsKey(idGuardado)) {
            idTemaActivo = idGuardado;
            temaActivo = temas.get(idTemaActivo);
        }
    }

    public TemaVisual obtenerTemaActivo() {
        return temaActivo;
    }

    public String[] obtenerTemasDisponibles() {
        return temas.keySet().toArray(new String[0]);
    }

    public void cambiarTema(String idTema) {
        if (!temas.containsKey(idTema) || idTema.equals(idTemaActivo)) {
            return;
        }

        

        idTemaActivo = idTema;
        temaActivo = temas.get(idTema);

        if (contextoApp != null) {
            repositorio.guardarIdTemaTeclado(idTema); // Usamos el método del repositorio
        }

        for (OnTemaCambiadoListener oyente : oyentes) {
            oyente.onTemaCambiado(temaActivo);
        }
    }

    public void agregarOyente(OnTemaCambiadoListener oyente) {
        if (!oyentes.contains(oyente)) oyentes.add(oyente);
    }

    public void removerOyente(OnTemaCambiadoListener oyente) {
        oyentes.remove(oyente);
    }
}