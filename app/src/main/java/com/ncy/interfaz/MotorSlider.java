package com.ncy.interfaz;

import android.content.Context;
import com.ncy.modelo.Tecla;
import com.ncy.utilidades.Constantes;

public class MotorSlider {

    // Eliminamos el 'static final' porque ahora depende de la pantalla del dispositivo
    private final float umbralSlider; 

    private static final int GESTO_IZQUIERDA = 5;
    private static final int GESTO_DERECHA   = 6;
    private static final int GESTO_ARRIBA    = 7;
    private static final int GESTO_ABAJO     = 8;

    private float ultimoX;
    private float ultimoY;
    private boolean activo = false;

    public interface OnSliderListener {
        void onSlider(Tecla tecla, int indiceGesto);
    }

    private final OnSliderListener listener;

    // AÑADIDO: Recibimos el Context en el constructor para leer la densidad de la pantalla
    public MotorSlider(Context contexto, OnSliderListener listener) {
        this.listener = listener;
        
        // 20dp es un movimiento estándar muy cómodo. Lo multiplicamos por la densidad.
        float baseDp = 20f; 
        this.umbralSlider = baseDp * contexto.getResources().getDisplayMetrics().density;
    }

    public void reiniciar(float x, float y) {
        this.ultimoX = x;
        this.ultimoY = y;
        this.activo  = false;
    }

    public boolean isActivo() { 
        return activo;
    }

    public boolean evaluar(float xActual, float yActual, Tecla teclaActiva) {
        if (teclaActiva == null) return false;

        float deltaX = xActual - ultimoX;
        float deltaY = yActual - ultimoY;

        // Comparamos contra nuestro nuevo umbral dinámico
        if (Math.abs(deltaX) > umbralSlider) {
            return despacharSlider(teclaActiva, deltaX > 0 ? GESTO_DERECHA : GESTO_IZQUIERDA, xActual, ultimoY);
        }

        if (Math.abs(deltaY) > umbralSlider) {
            return despacharSlider(teclaActiva, deltaY > 0 ? GESTO_ABAJO : GESTO_ARRIBA, ultimoX, yActual);
        }

        return false;
    }

    private boolean despacharSlider(Tecla teclaActiva, int indiceGesto, float nuevoX, float nuevoY) {
        int codigoDestino = teclaActiva.obtenerGesto(indiceGesto).obtenerCodigo();

        if (esCodigoSlider(codigoDestino)) {
            listener.onSlider(teclaActiva, indiceGesto);
            this.ultimoX = nuevoX;
            this.ultimoY = nuevoY;
            this.activo = true;
            return true;
        }
        return false;
    }

    private boolean esCodigoSlider(int codigo) {
        return codigo == Constantes.CODIGO_CURSOR_IZQUIERDA
            || codigo == Constantes.CODIGO_CURSOR_DERECHA
            || codigo == Constantes.CODIGO_CURSOR_ARRIBA
            || codigo == Constantes.CODIGO_CURSOR_ABAJO;
    }
}