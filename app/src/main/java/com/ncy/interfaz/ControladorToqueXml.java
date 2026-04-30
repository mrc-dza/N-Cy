package com.ncy.interfaz;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.manejadores.ManejadorRepeticion;
import com.ncy.modelo.Tecla;
import com.ncy.teclado.LayoutTeclado;
import com.ncy.utilidades.Constantes;
import com.ncy.utilidades.DetectorGestos;
import com.ncy.utilidades.GestorTema;

import java.util.List;

public class ControladorToqueXml {

    private final VistaTeclasXml vista;
    private final ManejadorEntrada manejador;
    private final EstadoTeclado estado;
    private final LayoutTeclado layout;
    private final ControladorBarraAlterna controladorBarra;
    private final int filaDesde;
    private final int filaHasta;

    private final DetectorGestos detectorGestos;
    private final DetectorToque detectorToque;
    private final ManejadorRepeticion manejadorRepeticion;
    private final MotorSlider motorSlider;

    private float xInicial;
    private float yInicial;
    private Tecla teclaPresionadaInicialmente;
    private boolean gestoEjecutado;
    private int gestoEnProgreso = 0;

    private VelocityTracker velocityTracker;

    public ControladorToqueXml(Context contexto, VistaTeclasXml vista, ManejadorEntrada manejador,
                               EstadoTeclado estado, LayoutTeclado layout,
                               ControladorBarraAlterna controladorBarra,
                               int filaDesde, int filaHasta) {
        this.vista = vista;
        this.manejador = manejador;
        this.estado = estado;
        this.layout = layout;
        this.controladorBarra = controladorBarra;
        this.filaDesde = filaDesde;
        this.filaHasta = filaHasta;

        this.detectorGestos = new DetectorGestos();
        this.detectorToque = new DetectorToque();

        this.manejadorRepeticion = new ManejadorRepeticion(manejador);
        this.manejadorRepeticion.setListenerToqueLargo((tecla, indiceGesto) -> {
            if (tecla.obtenerCodigo() == Constantes.CODIGO_SHIFT) {
                manejador.procesarToqueLargoShift();
                vista.invalidate();
                return true;
            }
            return false;
        });

        this.motorSlider = new MotorSlider(contexto, (tecla, indiceGesto) -> {
            manejadorRepeticion.detener();
            manejador.procesarToque(tecla, indiceGesto);
        });
    }

    public Tecla getTeclaPresionadaInicialmente() {
        return teclaPresionadaInicialmente;
    }

    public int getGestoEnProgreso() {
        return gestoEnProgreso;
    }

    public void liberarRecursos() {
        if (manejadorRepeticion != null) {
            manejadorRepeticion.detener();
        }
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
        teclaPresionadaInicialmente = null;
        gestoEnProgreso = 0;
    }

    public boolean procesarToque(MotionEvent evento) {
        if (controladorBarra.isAnimando()) return true;

        if (evento.getAction() == MotionEvent.ACTION_DOWN && velocityTracker != null) {
            velocityTracker.clear();
        }
        
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(evento);

        float xActual = evento.getX();
        float yActual = evento.getY() + vista.calcularOffsetY();

        switch (evento.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                return procesarActionDown(xActual, yActual);
            }
            case MotionEvent.ACTION_MOVE -> {
                return procesarActionMove(xActual, yActual);
            }
            case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                velocityTracker.computeCurrentVelocity(1000);
                float velX = velocityTracker.getXVelocity();
                
                boolean res = procesarActionUp(velX);
                
                velocityTracker.recycle();
                velocityTracker = null;
                return res;
            }
        }
        return false;
    }

    private boolean procesarActionDown(float xActual, float yActual) {
        xInicial = xActual;
        yInicial = yActual;
        gestoEnProgreso = 0;
        gestoEjecutado = false;
        motorSlider.reiniciar(xActual, yActual);

        List<List<Tecla>> todas = layout.obtenerMatriz();
        int limiteInferior = (filaDesde == -1) ? 0 : Math.max(filaDesde, 0);
        int limiteSuperior = (filaHasta == -1) ? todas.size() : Math.min(filaHasta + 1, todas.size());
        
        List<List<Tecla>> filasRango = todas.subList(limiteInferior, limiteSuperior);
        teclaPresionadaInicialmente = detectorToque.encontrar(xActual, yActual, filasRango);

        if (teclaPresionadaInicialmente != null) {
            manejadorRepeticion.iniciar(teclaPresionadaInicialmente, 0);
            vista.invalidate();
        }
        return true;
    }

    private boolean procesarActionMove(float xActual, float yActual) {
        if (teclaPresionadaInicialmente == null) return true;

        if (!gestoEjecutado && !controladorBarra.isArrastrando()) {
            float dx = xActual - xInicial;
            float dy = yActual - yInicial;

            if (Math.abs(dx) > 30 && Math.abs(dx) > Math.abs(dy)) {
                if (estado.obtenerModoActual() != ModoTeclado.PORTAPAPELES && teclaPresionadaInicialmente.obtenerY() == 0) {
                    manejadorRepeticion.detener();
                    int altoFila = GestorTema.getInstance().obtenerTemaActivo().getAlturaFilaBasePx();
                    if (!layout.obtenerMatriz().isEmpty() && !layout.obtenerMatriz().get(0).isEmpty()) {
                        altoFila = layout.obtenerMatriz().get(0).get(0).obtenerAlto();
                    }
                    controladorBarra.iniciarArrastre(vista.getWidth(), altoFila);
                    return true;
                }
            }
        }

        if (controladorBarra.isArrastrando()) {
            controladorBarra.actualizarArrastre(xActual - xInicial);
            return true;
        }

        if (motorSlider.evaluar(xActual, yActual, teclaPresionadaInicialmente)) {
            gestoEjecutado = true;
            vista.invalidate();
            return true;
        }

        if (!motorSlider.isActivo() && !gestoEjecutado) {
            if (detectorGestos.superoUmbral(xInicial, yInicial, xActual, yActual)) {
                int tipoGesto = detectorGestos.evaluarGesto(xInicial, yInicial, xActual, yActual);
                if (tipoGesto != gestoEnProgreso) {
                    gestoEnProgreso = tipoGesto;
                    vista.invalidate();
                }

                if (tipoGesto != 0 && detectorGestos.superoUmbralConfirmacion(xInicial, yInicial, xActual, yActual)) {
                    ModoTeclado modoAntes = estado.obtenerModoActual();
                    manejadorRepeticion.detener();
                    manejador.procesarToque(teclaPresionadaInicialmente, tipoGesto);
                    gestoEjecutado = true;
                    gestoEnProgreso = 0;

                    if (estado.obtenerModoActual() != modoAntes) vista.notificarCambioModo();
                    else vista.invalidate();
                }
            }
        }
        return true;
    }

    private boolean procesarActionUp(float velocidadX) {
        if (controladorBarra.isArrastrando()) {
            controladorBarra.soltarArrastre(vista.getWidth(), velocidadX);
            return true;
        }

        ModoTeclado modoAntes = estado.obtenerModoActual();
        if (teclaPresionadaInicialmente != null && !gestoEjecutado && !manejadorRepeticion.isYaEjecutoAlMenosUnaVez()) {
            manejador.procesarToque(teclaPresionadaInicialmente, 0);
        }

        manejadorRepeticion.detener();
        teclaPresionadaInicialmente = null;
        gestoEnProgreso = 0;

        if (estado.obtenerModoActual() != modoAntes) vista.notificarCambioModo();
        else vista.invalidate();

        return true;
    }
}