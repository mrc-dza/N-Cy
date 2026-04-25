package com.ncy.manejadores;

import android.os.Handler;
import android.os.Looper;
import com.ncy.modelo.Tecla;

public class ManejadorRepeticion {

    public interface ListenerToqueLargo {
        // CORRECCIÓN BUG 3: Ahora devuelve un boolean
        boolean onToqueLargo(Tecla tecla, int indiceGesto);
    }

    private final Handler temporizador;
    private final ManejadorEntrada manejadorEntrada;
    private ListenerToqueLargo listenerToqueLargo;

    private Tecla teclaActual;
    private int indiceGestoActual;
    private boolean yaEjecutoAlMenosUnaVez;

    private static final int RETRASO_INICIAL    = 400;
    private static final int INTERVALO_REPETICION = 35;

    // OPTIMIZACIÓN: Uso de referencias a métodos en lugar de clases anónimas
    private final Runnable tareaToqueLargo = this::ejecutarToqueLargo;
    private final Runnable tareaRepeticion = this::ejecutarRepeticion;

    public ManejadorRepeticion(ManejadorEntrada manejadorEntrada) {
        this.temporizador = new Handler(Looper.getMainLooper());
        this.manejadorEntrada = manejadorEntrada;
    }

    public void setListenerToqueLargo(ListenerToqueLargo listener) {
        this.listenerToqueLargo = listener;
    }

    public void iniciar(Tecla tecla, int indiceGesto) {
        detener();
        this.teclaActual = tecla;
        this.indiceGestoActual = indiceGesto;
        this.yaEjecutoAlMenosUnaVez = false;

        temporizador.postDelayed(tareaToqueLargo, RETRASO_INICIAL);
    }

    public void detener() {
        temporizador.removeCallbacks(tareaToqueLargo);
        temporizador.removeCallbacks(tareaRepeticion);
        this.teclaActual = null;
    }

    public boolean isYaEjecutoAlMenosUnaVez() {
        return yaEjecutoAlMenosUnaVez;
    }

    private void ejecutarToqueLargo() {
        boolean consumido = false;
        if (teclaActual != null && listenerToqueLargo != null) {
            // CORRECCIÓN BUG 3: Guardamos si el evento fue consumido
            consumido = listenerToqueLargo.onToqueLargo(teclaActual, indiceGestoActual);
        }
        
        // CORRECCIÓN BUG 3: Solo repetimos si no fue consumido
        if (!consumido) {
            temporizador.postDelayed(tareaRepeticion, INTERVALO_REPETICION);
        }
    }

    private void ejecutarRepeticion() {
        if (teclaActual != null) {
            manejadorEntrada.procesarToque(teclaActual, indiceGestoActual);
            yaEjecutoAlMenosUnaVez = true;
            // Se vuelve a programar a sí mismo para crear el bucle
            temporizador.postDelayed(tareaRepeticion, INTERVALO_REPETICION);
        }
    }
}