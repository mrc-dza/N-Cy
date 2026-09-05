package com.ncy.estado;

public enum EstadoShift {
    APAGADO,
    NORMAL,     // mayúscula para una sola letra
    BLOQUEADO;  // Caps Lock

    public boolean estaActivo() {
        return this != APAGADO;
    }

    /** Consume el shift de una sola letra y devuelve el siguiente estado. */
    public EstadoShift consumir() {
        return this == NORMAL ? APAGADO : this;
    }
}