package com.ncy.modelo;

public record InfoVisual(
    int     colorFondo,
    String  texto,
    boolean usarPinturaEspecial,
    int     iconoResId,
    String  pistaGestoNormal,
    Integer colorTextoPersonalizado
) {
    public InfoVisual {
        texto = (texto != null) ? texto : "";
        pistaGestoNormal = (pistaGestoNormal != null) ? pistaGestoNormal : texto;
    }

    // Fábrica estándar: trunca a 3 caracteres (ideal para textos como "Config", "Intro")
    public static InfoVisual of(int colorFondo, String texto, boolean usarPinturaEspecial, int iconoResId) {
        String pista = (texto != null && texto.length() > 3) ? texto.substring(0, 3) : texto;
        return new InfoVisual(colorFondo, texto, usarPinturaEspecial, iconoResId, pista, null); 
    }

    public static InfoVisual of(int colorFondo, String texto, boolean usarPinturaEspecial) {
        return of(colorFondo, texto, usarPinturaEspecial, 0);
    }
    
    // NUEVA FÁBRICA: Permite pasar la pista exacta sin que el sistema intente truncarla (ideal para Emojis)
    public static InfoVisual ofExact(int colorFondo, String texto, boolean usarPinturaEspecial, int iconoResId, String pistaExacta) {
        return new InfoVisual(colorFondo, texto, usarPinturaEspecial, iconoResId, pistaExacta, null);
    }
}