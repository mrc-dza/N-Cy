package com.ncy.utilidades;

import android.content.ClipboardManager;
import android.content.Context;
import android.inputmethodservice.InputMethodService;

import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.estado.EstadoTeclado;
import com.ncy.interfaz.ReconstruibleTeclado;
import com.ncy.manejadores.EscuchadorPortapapelesSistema;
import com.ncy.manejadores.GestorPortapapeles;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.manejadores.ManejadorPortapapeles;

public class ContenedorNcy {
    
    // Variables privadas para proteger la encapsulación
    private RepositorioConfiguracion repositorioConfig;
    private GestorTema gestorTema; 
    private EstadoTeclado estadoTeclado;
    private GestorPortapapeles gestorPortapapeles;
    private EscuchadorPortapapelesSistema escuchadorPortapapeles;
    private ManejadorPortapapeles manejadorPortapapeles;
    private ManejadorEntrada manejadorEntrada;

    private final InputMethodService servicio;
    private final ReconstruibleTeclado reconstruible;

    public ContenedorNcy(InputMethodService servicio, ReconstruibleTeclado reconstruible) {
        this.servicio = servicio;
        this.reconstruible = reconstruible;
        // Forzamos la creación del escuchador del sistema al iniciar
        obtenerEscuchadorPortapapeles();
    }

    public RepositorioConfiguracion obtenerRepositorioConfig() {
        if (repositorioConfig == null) repositorioConfig = new RepositorioConfiguracion(servicio);
        return repositorioConfig;
    }

    public GestorTema obtenerGestorTema() {
        if (gestorTema == null) gestorTema = new GestorTema(obtenerRepositorioConfig());
        return gestorTema;
    }

    public EstadoTeclado obtenerEstadoTeclado() {
        if (estadoTeclado == null) estadoTeclado = new EstadoTeclado();
        return estadoTeclado;
    }

    public GestorPortapapeles obtenerGestorPortapapeles() {
        if (gestorPortapapeles == null) gestorPortapapeles = new GestorPortapapeles(servicio);
        return gestorPortapapeles;
    }

    public EscuchadorPortapapelesSistema obtenerEscuchadorPortapapeles() {
        if (escuchadorPortapapeles == null) {
            escuchadorPortapapeles = new EscuchadorPortapapelesSistema(servicio, obtenerGestorPortapapeles());
        }
        return escuchadorPortapapeles;
    }

    public ManejadorPortapapeles obtenerManejadorPortapapeles() {
        if (manejadorPortapapeles == null) {
            ClipboardManager clipboard = (ClipboardManager) servicio.getSystemService(Context.CLIPBOARD_SERVICE);
            manejadorPortapapeles = new ManejadorPortapapeles(servicio, obtenerGestorPortapapeles(), clipboard);
        }
        return manejadorPortapapeles;
    }

    public ManejadorEntrada obtenerManejadorEntrada() {
        if (manejadorEntrada == null) {
            manejadorEntrada = new ManejadorEntrada(servicio, obtenerEstadoTeclado(), obtenerManejadorPortapapeles(), reconstruible);
        }
        return manejadorEntrada;
    }

    public void destruir() {
        if (escuchadorPortapapeles != null) escuchadorPortapapeles.destruir();
        if (gestorPortapapeles != null) gestorPortapapeles.destruir();
    }
}