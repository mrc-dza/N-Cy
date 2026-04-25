package com.ncy.modelo;

public final class InfoVisual {

    public final int     colorFondo;
    public final String  texto;
    public final boolean usarPinturaEspecial;
    public final int     iconoResId;
    public final String  pistaGestoNormal;
    
    // NUEVO: Permite forzar un color de texto/ícono específico ignorando los Paint por defecto
    public final Integer colorTextoPersonalizado;

    // NUEVO CONSTRUCTOR PRINCIPAL
    public InfoVisual(int colorFondo, String texto, boolean usarPinturaEspecial, int iconoResId, Integer colorTextoPersonalizado) {
        this.colorFondo              = colorFondo;
        this.texto                   = texto != null ? texto : "";
        this.usarPinturaEspecial     = usarPinturaEspecial;
        this.iconoResId              = iconoResId;
        this.colorTextoPersonalizado = colorTextoPersonalizado;

        if (this.texto.length() > 3) {
            this.pistaGestoNormal = this.texto.substring(0, 3);
        } else {
            this.pistaGestoNormal = this.texto;
        }
    }

    // CONSTRUCTOR ANTIGUO (Se mantiene para compatibilidad con las demás teclas)
    public InfoVisual(int colorFondo, String texto, boolean usarPinturaEspecial, int iconoResId) {
        this(colorFondo, texto, usarPinturaEspecial, iconoResId, null);
    }

    // CONSTRUCTOR ANTIGUO MÁS SIMPLE
    public InfoVisual(int colorFondo, String texto, boolean usarPinturaEspecial) {
        this(colorFondo, texto, usarPinturaEspecial, 0, null);
    }
}