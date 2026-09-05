package com.ncy.utilidades;

import android.graphics.Color;
import com.ncy.datos.RepositorioConfiguracion;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GestorTema extends GestorTemaBase<TemaVisual> {

    private final List<OnTemaCambiadoListener> oyentes = new CopyOnWriteArrayList<>();
    private final RepositorioConfiguracion repositorio;

    public interface OnTemaCambiadoListener {
        void onTemaCambiado(TemaVisual nuevoTema);
    }

    public GestorTema(RepositorioConfiguracion repo) {
        super(repo);
        this.repositorio = repo;
    }

    @Override
    protected void configurarTemas() {
        // 1. TEMA OSCURO (Por defecto)
        temas.put("oscuro", new TemaVisualData.Builder().build());

        // 2. TEMA CLARO
        temas.put("claro", new TemaVisualData.Builder()
            .colorFondoGeneral(Color.rgb(210, 214, 222))
            .colorTeclaNormal(Color.rgb(250, 251, 253))
            .colorTeclaAccion(Color.rgb(185, 193, 205))
            .colorTeclaEnter(Color.rgb(63, 81, 181))
            .colorTeclaMacro(Color.rgb(76, 175, 80))
            .colorTeclaModActivo(Color.rgb(197, 202, 233))
            .colorTextoPrincipal(Color.rgb(15, 15, 15))
            .colorTextoEspecial(Color.rgb(48, 63, 159))
            .colorTextoModApagado(Color.rgb(80, 100, 115))
            .colorTextoModActivo(Color.rgb(48, 63, 159))
            .colorTextoVacio(Color.rgb(144, 164, 174))
            .colorAcento(Color.rgb(0, 47, 255))
            .colorGestoActivo(Color.rgb(255, 64, 129))
            .colorPresionNormal(Color.rgb(185, 185, 185))
            .colorPresionFijado(Color.rgb(160, 175, 185))
            .colorSeparador(Color.rgb(176, 190, 197))
            .colorTituloSeccion(Color.rgb(63, 81, 181))
            .colorIconoPin(Color.rgb(33, 150, 243))
            .colorIconoBorrar(Color.rgb(244, 67, 54))
            .radioEsquinasTecla(12f)
            .factorOscurecimiento(0.15f)
            .colorEtiquetaBarra(Color.rgb(79, 79, 83))
            .colorEtiquetaBarraPresionada(Color.rgb(63, 81, 181))
            .colorFondoBarra(Color.rgb(210, 214, 222))
            .separacionFilas(8f)
            .colorBordeTecla(Color.rgb(190, 195, 205))
            .anchoBordeTecla(0.8f)
            .build());

        // 3. TEMA NEÓN VERDE
        temas.put("neon verde", new TemaVisualData.Builder()
            .colorFondoGeneral(Color.rgb(0, 0, 0))
            .colorTeclaNormal(Color.rgb(34, 34, 34))
            .colorTeclaAccion(Color.rgb(60, 60, 60))
            .colorTeclaEnter(Color.rgb(60, 60, 60))
            .colorTeclaMacro(Color.rgb(34, 34, 34))
            .colorTeclaModActivo(Color.rgb(22, 22, 22))
            .colorTextoPrincipal(Color.rgb(0, 255, 0))
            .colorTextoEspecial(Color.rgb(0, 255, 0))
            .colorTextoModApagado(Color.rgb(60, 60, 60))
            .colorTextoModActivo(Color.rgb(0, 140, 255))
            .colorTextoVacio(Color.rgb(0, 150, 0))
            .colorAcento(Color.rgb(0, 140, 255))
            .colorGestoActivo(Color.rgb(200, 255, 200))
            .colorSeparador(Color.rgb(34, 34, 34))
            .colorTituloSeccion(Color.rgb(0, 255, 0))
            .colorIconoPin(Color.rgb(0, 255, 0))
            .colorIconoBorrar(Color.rgb(255, 50, 50))
            .radioEsquinasTecla(8f)
            .factorOscurecimiento(0.4f)
            .colorEtiquetaBarra(Color.rgb(0, 150, 0))
            .colorEtiquetaBarraPresionada(Color.rgb(0, 140, 255))
            .colorFondoBarra(Color.rgb(0, 0, 0))
            .separacionFilas(8f)
            .colorBordeTecla(Color.rgb(0, 100, 0))
            .anchoBordeTecla(1f)
            .build());

        // 4. TEMA NES
        temas.put("nes", new TemaVisualData.Builder()
            .colorFondoGeneral(Color.rgb(10, 15, 35))
            .colorTeclaNormal(Color.rgb(22, 30, 58))
            .colorTeclaAccion(Color.rgb(35, 20, 20))
            .colorTeclaEnter(Color.rgb(180, 30, 25))
            .colorTeclaMacro(Color.rgb(35, 20, 20))
            .colorTeclaModActivo(Color.rgb(15, 20, 45))
            .colorTextoPrincipal(Color.rgb(235, 220, 180))
            .colorTextoEspecial(Color.rgb(220, 50, 47))
            .colorTextoModApagado(Color.rgb(70, 80, 110))
            .colorTextoModActivo(Color.rgb(235, 220, 180))
            .colorTextoVacio(Color.rgb(60, 70, 100))
            .colorAcento(Color.rgb(140, 150, 170))
            .colorGestoActivo(Color.rgb(235, 220, 180))
            .colorPresionNormal(Color.rgb(60, 30, 30))
            .colorPresionFijado(Color.rgb(40, 50, 80))
            .colorSeparador(Color.rgb(30, 38, 70))
            .colorTituloSeccion(Color.rgb(220, 50, 47))
            .colorIconoPin(Color.rgb(140, 150, 170))
            .colorIconoBorrar(Color.rgb(220, 50, 47))
            .radioEsquinasTecla(8f)
            .factorOscurecimiento(0.45f)
            .colorEtiquetaBarra(Color.rgb(140, 150, 170))
            .colorEtiquetaBarraPresionada(Color.rgb(235, 220, 180))
            .colorFondoBarra(Color.rgb(10, 15, 35))
            .separacionFilas(8f)
            .colorBordeTecla(Color.rgb(50, 60, 100))
            .anchoBordeTecla(1.5f)
            .build());
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
        if (!temas.containsKey(idTema) || idTema.equals(idTemaActivo)) return;

        idTemaActivo = idTema;
        temaActivo = temas.get(idTema);
        guardarId(repositorio, idTema);

        for (OnTemaCambiadoListener oyente : oyentes) {
            oyente.onTemaCambiado(temaActivo);
        }
    }

    public void recargarDesdePreferencias() {
        String idGuardado = leerIdGuardado(repositorio);
        if (!idGuardado.equals(idTemaActivo) && temas.containsKey(idGuardado)) {
            idTemaActivo = idGuardado;
            temaActivo = temas.get(idGuardado);
            for (OnTemaCambiadoListener oyente : oyentes) {
                oyente.onTemaCambiado(temaActivo);
            }
        }
    }

    public void agregarOyente(OnTemaCambiadoListener oyente) {
        if (!oyentes.contains(oyente)) oyentes.add(oyente);
    }

    public void removerOyente(OnTemaCambiadoListener oyente) {
        oyentes.remove(oyente);
    }
}