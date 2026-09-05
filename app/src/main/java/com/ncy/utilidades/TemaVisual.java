package com.ncy.utilidades;

public interface TemaVisual extends TemaBase {

    // ── Fondos
    int getColorFondoBarra();
    int getColorTeclaNormal();
    int getColorTeclaAccion();
    int getColorTeclaEnter();
    int getColorTeclaMacro();
    int getColorTeclaModActivo();

    // ── Textos 
    int getColorTextoEspecial();
    int getColorTextoModApagado();
    int getColorTextoModActivo();
    int getColorTextoVacio();

    // ── Acentos y gestos 
    int getColorGestoActivo();

    // ── Feedback de presión 
    int getColorPresionNormal();
    int getColorPresionFijado();

    // ── Portapapeles y secciones
    int getColorSeparador();
    int getColorTituloSeccion();
    int getColorIconoPin();
    int getColorIconoBorrar();

    // ── Barra alterna 
    int getColorEtiquetaBarra();
    int getColorEtiquetaBarraPresionada();

    // ── Tipografía
    float getTamanioTextoTecla();
    float getTamanioTextoEspecial();
    float getTamanioTextoGesto();
    float getTamanioTextoGestoActivo();
    float getTamanioTextoLista();
    float getTamanioTextoSeccion();
    float getTamanioTextoVacio();

    // ── Geometría 
    float getRadioEsquinasTecla();
    int   getAlturaFilaBasePx();
    float getFactorOscurecimiento();
    int   getColorBordeTecla();      // Color.TRANSPARENT = sin borde
    float getAnchoBordeTecla();      // dp. 0f = sin borde

    // ── Espaciado 
    float getSeparacionFilas();      // dp entre filas
    float getSeparacionColumnas();   // dp entre teclas
}