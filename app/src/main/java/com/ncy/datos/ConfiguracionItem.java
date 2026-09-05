package com.ncy.datos;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Encapsula la configuración de un interruptor (Switch) en la UI.
 * Permite delegar cómo se lee y se guarda el valor sin usar condicionales if/else.
 */
public record ConfiguracionItem(
    int idVista,
    String titulo,
    String subtitulo,
    BooleanSupplier lector,
    Consumer<Boolean> escritor
) {}