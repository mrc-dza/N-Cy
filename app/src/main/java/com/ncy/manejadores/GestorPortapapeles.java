package com.ncy.manejadores;

import android.content.Context;
import com.ncy.datos.HistorialPortapapeles;
import com.ncy.datos.RepositorioHistorial;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestorPortapapeles {

    public interface OnHistorialCambiadoListener {
        void onHistorialCambiado();
    }

    private final HistorialPortapapeles historial;
    private final RepositorioHistorial  repositorio;
    private WeakReference<OnHistorialCambiadoListener> listenerRef =
            new WeakReference<>(null);

    public GestorPortapapeles(Context contexto) {
        this.repositorio = new RepositorioHistorial(contexto);
        this.historial   = new HistorialPortapapeles();
        historial.cargar(repositorio.cargarTemporales(), repositorio.cargarFijados());
    }

    public void setListener(OnHistorialCambiadoListener listener) {
        this.listenerRef = new WeakReference<>(listener);
    }
    private final ExecutorService ejecutorFondo = Executors.newSingleThreadExecutor();

    public void agregar(String texto) {
        if (historial.agregar(texto)) persistirYNotificar();
    }

    public void fijar(int indice) {
        if (historial.fijar(indice)) persistirYNotificar();
    }

    public void eliminarFijado(int indice) {
        if (historial.eliminarFijado(indice)) persistirYNotificar();
    }

    public void limpiarTemporales() {
        if (historial.limpiarTemporales()) persistirYNotificar();
    }

    public List<String> obtenerTemporales() { return historial.obtenerTemporales(); }
    public List<String> obtenerFijados()    { return historial.obtenerFijados(); }

    private void persistirYNotificar() {
        // 1. Obtenemos copias seguras para el hilo en segundo plano
        List<String> tempsCopia = historial.obtenerTemporales();
        List<String> fijosCopia = historial.obtenerFijados();

        // 2. Usar el ejecutor reciclable en lugar de crear un Thread nuevo
        ejecutorFondo.execute(() -> {
            repositorio.guardar(tempsCopia, fijosCopia);
        });

        // 3. Notificar a la Vista en el hilo principal
        OnHistorialCambiadoListener listener = listenerRef.get();
        if (listener != null) listener.onHistorialCambiado();
    }

    public void destruir() {
        if (ejecutorFondo != null && !ejecutorFondo.isShutdown()) {
            ejecutorFondo.shutdown();
        }
    }



}