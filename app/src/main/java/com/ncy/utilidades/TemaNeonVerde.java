package com.ncy.utilidades;
import android.graphics.Color;

public class TemaNeonVerde implements TemaVisual {

    @Override public int getColorFondoGeneral()    { return Color.rgb(0, 0, 0); } 
    @Override public int getColorTeclaNormal()     { return Color.rgb(34, 34, 34); }
    @Override public int getColorTeclaAccion()     { return Color.rgb(60, 60, 60); }
    @Override public int getColorTeclaEnter()      { return Color.rgb(60, 60, 60); }
    @Override public int getColorTeclaMacro()      { return Color.rgb(34, 34, 34); }
    @Override public int getColorTeclaModActivo()  { return Color.rgb(22, 22, 22); }

    @Override public int getColorTextoPrincipal()  { return Color.rgb(0, 255, 0); }
    @Override public int getColorTextoEspecial()   { return Color.rgb(0, 255, 0); }
    @Override public int getColorTextoModApagado() { return Color.rgb(60, 60, 60); }
    @Override public int getColorTextoModActivo()  { return Color.rgb(0, 140, 255); }
    @Override public int getColorTextoVacio()      { return Color.rgb(0, 150, 0); }

    @Override public int getColorAcento()          { return Color.rgb(0, 140, 255); }
    @Override public int getColorGestoActivo()     { return Color.rgb(200, 255, 200); } 

    @Override public int getColorPresionNormal()   { return Color.rgb(60, 60, 60); }
    @Override public int getColorPresionFijado()   { return Color.rgb(60, 60, 60); }


    @Override public int getColorSeparador()       { return Color.rgb(34, 34, 34); }
    @Override public int getColorTituloSeccion()   { return Color.rgb(0, 255, 0); }
    @Override public int getColorIconoPin()        { return Color.rgb(0, 255, 0); }
    @Override public int getColorIconoBorrar()     { return Color.rgb(255, 50, 50); }

    @Override public float getTamanioTextoTecla()        { return 16f; }
    @Override public float getTamanioTextoEspecial()     { return 13f; }
    @Override public float getTamanioTextoGesto()        { return 9f; }
    @Override public float getTamanioTextoGestoActivo()  { return 10f; }
    @Override public float getTamanioTextoLista()        { return 12f; }
    @Override public float getTamanioTextoSeccion()      { return 10f; }
    @Override public float getTamanioTextoVacio()        { return 11f; }

    @Override public float getRadioEsquinasTecla()       { return 16f; }
    @Override public int getAlturaFilaBasePx()           { return 140; }
    @Override public float getFactorOscurecimiento()     { return 0.4f; } 

    @Override public int getColorEtiquetaBarra()           { return Color.rgb(0, 255, 0); }
    @Override public int getColorEtiquetaBarraPresionada() { return Color.rgb(0, 140, 255); }
    @Override public int getColorFondoBarra()              { return Color.rgb(0, 0, 0); }

    @Override public float getSeparacionFilas()    { return 8f; } // 8dp entre fila y fila
    @Override public float getSeparacionColumnas() { return 5f; } // 5dp entre teclas juntas
}