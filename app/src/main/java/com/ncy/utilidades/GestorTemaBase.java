package com.ncy.utilidades;

import android.content.Context;
import com.ncy.datos.RepositorioConfiguracion;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GestorTemaBase<T> {
    protected final Map<String, T> temas = new LinkedHashMap<>();
    protected T temaActivo;
    protected String idTemaActivo;
    protected Context contextoApp;
    private boolean inicializado = false;

    protected abstract String leerIdGuardado(RepositorioConfiguracion repo);
    protected abstract void guardarId(RepositorioConfiguracion repo, String id);
    protected abstract String getIdDefecto();

    // Sincronizado para proteger contra doble inicialización (Advertencia 1 de la auditoría)
    public synchronized void inicializar(Context contexto) {
        if (inicializado) return; // Idempotente: seguro llamarlo múltiples veces
        this.contextoApp = contexto.getApplicationContext();
        RepositorioConfiguracion repo = new RepositorioConfiguracion(contextoApp);
        String id = leerIdGuardado(repo);
        if (temas.containsKey(id)) {
            idTemaActivo = id;
            temaActivo = temas.get(id);
        }
        inicializado = true;
    }

    public T obtenerTemaActivo() {
        return temaActivo;
    }

    public String[] obtenerTemasDisponibles() {
        return temas.keySet().toArray(new String[0]);
    }
    
    public boolean esOscuro() {
        return "oscuro".equals(idTemaActivo);
    }
}