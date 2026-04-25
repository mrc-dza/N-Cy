package com.ncy.utilidades;

import android.content.Context;
import com.ncy.datos.RepositorioConfiguracion;

import java.util.LinkedHashMap;
import java.util.Map;

public class GestorTemaUI {
    
    private static class Holder {
        static final GestorTemaUI INSTANCIA = new GestorTemaUI();
    }

    private final Map<String, TemaVisualUI> temas = new LinkedHashMap<>();
    private TemaVisualUI temaActivo;
    private String idTemaActivo;
    private Context contextoApp;
    private RepositorioConfiguracion repositorio; 

    private GestorTemaUI() {
        temas.put("oscuro", new TemaOscuroUI());
        temas.put("claro", new TemaClaroUI());
        
        idTemaActivo = "oscuro";
        temaActivo = temas.get(idTemaActivo);
    }

    public static GestorTemaUI getInstance() {
        return Holder.INSTANCIA;
    }

    
    public void inicializar(Context contexto) {
        this.contextoApp = contexto.getApplicationContext();
        this.repositorio = new RepositorioConfiguracion(contextoApp); // Usamos el campo de la clase
        String idGuardado = repositorio.leerIdTemaUI(); // Leemos directamente

        if (temas.containsKey(idGuardado)) {
            idTemaActivo = idGuardado;
            temaActivo = temas.get(idTemaActivo);
        }
    }

    public TemaVisualUI obtenerTemaActivo() {
        return temaActivo;
    }

    public boolean esOscuro() {
        return "oscuro".equals(temaActivo.getNombre());
    }

    public void alternarTema() {
        idTemaActivo = esOscuro() ? "claro" : "oscuro";
        temaActivo = temas.get(idTemaActivo);
        
        if (repositorio != null) {
            repositorio.guardarIdTemaUI(idTemaActivo);
        }
    }
}