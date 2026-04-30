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
    
    // Aquí residen todas las instancias críticas de tu teclado
    public final RepositorioConfiguracion repositorioConfig;
    public final EstadoTeclado estadoTeclado;
    public final GestorPortapapeles gestorPortapapeles;
    public final EscuchadorPortapapelesSistema escuchadorPortapapeles;
    public final ManejadorPortapapeles manejadorPortapapeles;
    public final ManejadorEntrada manejadorEntrada;

    // El contenedor recibe lo mínimo necesario para construir el teclado
    public ContenedorNcy(InputMethodService servicio, ReconstruibleTeclado reconstruible) {
        
        // 1. Inicializar estado y configuración básica
        this.repositorioConfig = new RepositorioConfiguracion(servicio);
        this.estadoTeclado = new EstadoTeclado();
        
        // 2. Inicializar sistema de portapapeles
        this.gestorPortapapeles = new GestorPortapapeles(servicio);
        this.escuchadorPortapapeles = new EscuchadorPortapapelesSistema(servicio, gestorPortapapeles);
        ClipboardManager clipboard = (ClipboardManager) servicio.getSystemService(Context.CLIPBOARD_SERVICE);
        this.manejadorPortapapeles = new ManejadorPortapapeles(servicio, gestorPortapapeles, clipboard);
        
        // 3. Inicializar el manejador principal (que depende de los anteriores)
        this.manejadorEntrada = new ManejadorEntrada(servicio, estadoTeclado, manejadorPortapapeles, reconstruible);
    }

    // Centralizamos la limpieza de memoria para cuando el teclado se cierre
    public void destruir() {
        if (escuchadorPortapapeles != null) {
            escuchadorPortapapeles.destruir();
        }
        if (gestorPortapapeles != null) {
            gestorPortapapeles.destruir();
        }
    }
}