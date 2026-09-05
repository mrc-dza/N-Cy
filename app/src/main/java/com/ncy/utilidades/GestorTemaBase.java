package com.ncy.utilidades;

import com.ncy.datos.RepositorioConfiguracion;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GestorTemaBase<T> {
    protected final Map<String, T> temas = new LinkedHashMap<>();
    protected T temaActivo;
    protected String idTemaActivo;

    protected abstract String leerIdGuardado(RepositorioConfiguracion repo);
    protected abstract void guardarId(RepositorioConfiguracion repo, String id);
    protected abstract String getIdDefecto();
    
    // NUEVO: Método para poblar los temas
    protected abstract void configurarTemas(); 

    // NUEVO: Constructor que recibe el repositorio y configura el estado inicial
    public GestorTemaBase(RepositorioConfiguracion repo) {
        configurarTemas();
        String id = leerIdGuardado(repo);
        if (temas.containsKey(id)) {
            idTemaActivo = id;
            temaActivo = temas.get(id);
        } else {
            idTemaActivo = getIdDefecto();
            temaActivo = temas.get(idTemaActivo);
        }
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