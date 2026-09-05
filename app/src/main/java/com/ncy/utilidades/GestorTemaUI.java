package com.ncy.utilidades;

import com.ncy.datos.RepositorioConfiguracion;

public class GestorTemaUI extends GestorTemaBase<TemaVisualUI> {

    private final RepositorioConfiguracion repositorio;

    public GestorTemaUI(RepositorioConfiguracion repo) {
        super(repo);
        this.repositorio = repo;
    }

    @Override
    protected void configurarTemas() {
        temas.put("oscuro", new TemaOscuroUI());
        temas.put("claro", new TemaClaroUI());
    }

    @Override
    protected String leerIdGuardado(RepositorioConfiguracion repo) {
        return repo.leerIdTemaUI();
    }

    @Override
    protected void guardarId(RepositorioConfiguracion repo, String id) {
        repo.guardarIdTemaUI(id);
    }

    @Override
    protected String getIdDefecto() {
        return "oscuro";
    }

    public void alternarTema() {
        String nuevoId = esOscuro() ? "claro" : "oscuro";
        idTemaActivo = nuevoId;
        temaActivo = temas.get(nuevoId);
        
        // Usamos el repositorio inyectado
        guardarId(repositorio, nuevoId);
    }
}