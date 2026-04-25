package com.ncy.utilidades;

public interface TemaVisual {
    // Colores de Fondo
    int getColorFondoGeneral();
    int getColorFondoBarra();
    int getColorTeclaNormal();
    int getColorTeclaAccion();
    int getColorTeclaEnter();
    int getColorTeclaMacro();
    int getColorTeclaModActivo();

    // Colores de Texto
    int getColorTextoPrincipal();
    int getColorTextoEspecial();
    int getColorTextoModApagado();
    int getColorTextoModActivo(); 
    int getColorTextoVacio();

    // Acentos y Gestos
    int getColorAcento();
    int getColorGestoActivo();
    
    // Feedback de Presión
    int getColorPresionNormal();
    int getColorPresionFijado();
    
    // Portapapeles y Secciones
    int getColorSeparador();
    int getColorTituloSeccion();
    int getColorIconoPin();
    int getColorIconoBorrar();

    // Tipografía
    float getTamanioTextoTecla();
    float getTamanioTextoEspecial();
    float getTamanioTextoGesto();
    float getTamanioTextoGestoActivo();
    float getTamanioTextoLista();
    float getTamanioTextoSeccion();
    float getTamanioTextoVacio();

    // Geometría y Física
    float getRadioEsquinasTecla();
    int getAlturaFilaBasePx();
    float getFactorOscurecimiento();

    // NUEVO: Colores para la barra alterna
    int getColorEtiquetaBarra();
    int getColorEtiquetaBarraPresionada();

   
    
    // NUEVO: Control de espaciado (gap) entre columnas y filas

    float getSeparacionFilas();      // Espacio vertical entre filas
    float getSeparacionColumnas();


}