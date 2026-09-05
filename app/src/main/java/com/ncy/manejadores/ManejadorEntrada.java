package com.ncy.manejadores;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;

import com.ncy.estado.EstadoShift;
import com.ncy.estado.EstadoTeclado;
import com.ncy.interfaz.ReconstruibleTeclado;
import com.ncy.modelo.*;
import com.ncy.utilidades.Constantes;

public class ManejadorEntrada {

    private final EstadoTeclado estado;
    private final ManejadorModos modos;
    private final ManejadorPortapapeles portapapeles;
    private final SimuladorHardware simulador;
    private final ManejadorEdicion edicion;
    private final ReconstruibleTeclado teclado;

    private long tiempoUltimoShift = 0;
    private static final int UMBRAL_DOBLE_TOQUE = 400;

    public ManejadorEntrada(InputMethodService servicioIme,
                            EstadoTeclado estado,
                            ManejadorPortapapeles portapapeles,
                            ReconstruibleTeclado teclado) {
        this.estado       = estado;
        this.portapapeles = portapapeles;
        this.teclado      = teclado;
        this.simulador    = new SimuladorHardware(estado);
        this.edicion      = new ManejadorEdicion(estado, simulador);
        this.modos        = new ManejadorModos(estado, servicioIme);
    }

    public void establecerConexion(InputConnection conexion) {
        simulador.establecerConexion(conexion);
        edicion.establecerConexion(conexion);
        portapapeles.establecerConexion(conexion);
    }

    public void procesarToque(Tecla teclaRaiz, int indiceGesto) {
        if (indiceGesto != 0 && teclaRaiz.isEstiloTransparente()) {
            cambiarPaginaBarra();
            return;
        }

        Tecla teclaFinal = teclaRaiz.obtenerGesto(indiceGesto);

        if (teclaFinal instanceof TeclaCaracter tc) {
            edicion.procesarCaracter(tc);
        } else if (teclaFinal instanceof TeclaModificador tm) {
            procesarModificador(tm);
        } else if (teclaFinal instanceof TeclaMacro tma) {
            edicion.procesarMacro(tma);
        } else if (teclaFinal instanceof TeclaEmoji te) {
            edicion.procesarEmoji(te);
        } else if (teclaFinal instanceof TeclaAccion ta) {
            procesarAccion(ta);
        }
    }

    public void procesarToqueLargoShift() {
        estado.fijarEstadoShift(EstadoShift.BLOQUEADO);
    }

    private void procesarModificador(TeclaModificador tecla) {
        int codigo = tecla.obtenerCodigo();
        if (codigo == Constantes.CODIGO_SHIFT) {
            evaluarToqueShift();
        } else {
            estado.alternarModificador(codigo);
        }
    }

    private void evaluarToqueShift() {
        long ahora = System.currentTimeMillis();
        if (ahora - tiempoUltimoShift < UMBRAL_DOBLE_TOQUE) {
            estado.fijarEstadoShift(EstadoShift.BLOQUEADO);
        } else {
            EstadoShift nuevoEstado = (estado.obtenerEstadoShift() == EstadoShift.APAGADO) 
                    ? EstadoShift.NORMAL 
                    : EstadoShift.APAGADO;
            estado.fijarEstadoShift(nuevoEstado);
        }
        tiempoUltimoShift = ahora;
    }

    private void procesarAccion(TeclaAccion accion) {
        int codigo = accion.obtenerCodigo();
        
        if (codigo == Constantes.CODIGO_CAMBIAR_BARRA) {
            cambiarPaginaBarra();
            return;
        }

        if (SimuladorHardware.esCodigo(codigo)) {
            simulador.procesarTeclaHardware(codigo);
        } else if (ManejadorEdicion.esCodigo(codigo)) {
            edicion.procesarEdicion(codigo);
        } else if (ManejadorPortapapeles.esCodigo(codigo)) {
            portapapeles.procesarAccion(codigo);
        } else if (ManejadorModos.esCodigo(codigo)) {
            modos.procesarAccion(codigo);
        }
    }

    private void cambiarPaginaBarra() {
        estado.alternarPaginaBarra();
        teclado.refrescarTeclado();
    }
}