package com.ncy.datos;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Encapsula la configuración de un interruptor (Switch) en la UI.
 * Permite delegar cómo se lee y se guarda el valor sin usar condicionales if/else.
 */
public class ConfiguracionItem {
    public final int idVista;
    public final String titulo;
    public final String subtitulo;
    public final BooleanSupplier lector;
    public final Consumer<Boolean> escritor;

    public ConfiguracionItem(int idVista, String titulo, String subtitulo, 
                             BooleanSupplier lector, Consumer<Boolean> escritor) {
        this.idVista = idVista;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.lector = lector;
        this.escritor = escritor;
    }
}