package com.ncy.utilidades;

import com.ncy.datos.RepositorioConfiguracion;

public class GestorTemaUI extends GestorTemaBase<TemaVisualUI> {

    private static class Holder {
        static final GestorTemaUI INSTANCIA = new GestorTemaUI();
    }

    private GestorTemaUI() {
        temas.put("oscuro", new TemaOscuroUI());
        temas.put("claro", new TemaClaroUI());
        
        idTemaActivo = getIdDefecto();
        temaActivo = temas.get(idTemaActivo);
    }

    public static GestorTemaUI getInstance() {
        return Holder.INSTANCIA;
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
        
        if (contextoApp != null) {
            RepositorioConfiguracion repo = new RepositorioConfiguracion(contextoApp);
            guardarId(repo, nuevoId);
        }
    }
}