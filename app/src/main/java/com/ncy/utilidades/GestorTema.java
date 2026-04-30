package com.ncy.utilidades;

import com.ncy.datos.RepositorioConfiguracion;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GestorTema extends GestorTemaBase<TemaVisual> {

    private static class Holder {
        static final GestorTema INSTANCIA = new GestorTema();
    }

    public interface OnTemaCambiadoListener {
        void onTemaCambiado(TemaVisual nuevoTema);
    }

    private final List<OnTemaCambiadoListener> oyentes = new CopyOnWriteArrayList<>();

    private GestorTema() {
        temas.put("oscuro", new TemaOscuro());
        temas.put("claro", new TemaClaro());
        temas.put("neon verde", new TemaNeonVerde());

        idTemaActivo = getIdDefecto();
        temaActivo = temas.get(idTemaActivo);
    }

    public static GestorTema getInstance() {
        return Holder.INSTANCIA;
    }

    @Override
    protected String leerIdGuardado(RepositorioConfiguracion repo) {
        return repo.leerIdTemaTeclado();
    }

    @Override
    protected void guardarId(RepositorioConfiguracion repo, String id) {
        repo.guardarIdTemaTeclado(id);
    }

    @Override
    protected String getIdDefecto() {
        return "oscuro";
    }

    public void cambiarTema(String idTema) {
        if (!temas.containsKey(idTema) || idTema.equals(idTemaActivo)) {
            return;
        }

        idTemaActivo = idTema;
        temaActivo = temas.get(idTema);

        if (contextoApp != null) {
            RepositorioConfiguracion repo = new RepositorioConfiguracion(contextoApp);
            guardarId(repo, idTema);
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