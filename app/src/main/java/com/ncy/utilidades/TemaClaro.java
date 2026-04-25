package com.ncy.utilidades;
import android.graphics.Color;

public class TemaClaro implements TemaVisual {

    @Override public int getColorFondoGeneral()    { return Color.rgb(235, 235, 235); }
    @Override public int getColorTeclaNormal()     { return Color.rgb(255, 255, 255); }
    @Override public int getColorTeclaAccion()     { return Color.rgb(207, 216, 220); }
    @Override public int getColorTeclaEnter()      { return Color.rgb(63, 81, 181); }
    @Override public int getColorTeclaMacro()      { return Color.rgb(76, 175, 80); }
    @Override public int getColorTeclaModActivo()  { return Color.rgb(197, 202, 233); }

    @Override public int getColorTextoPrincipal()  { return Color.rgb(33, 33, 33); }
    @Override public int getColorTextoEspecial()   { return Color.rgb(48, 63, 159); }
    @Override public int getColorTextoModApagado() { return Color.rgb(96, 125, 139); }
    @Override public int getColorTextoModActivo()  { return Color.rgb(48, 63, 159); } 
    @Override public int getColorTextoVacio()      { return Color.rgb(144, 164, 174); }

    @Override public int getColorAcento()          { return Color.rgb(103, 58, 183); }
    @Override public int getColorGestoActivo()     { return Color.rgb(255, 64, 129); }

    @Override public int getColorPresionNormal()   { return Color.rgb(200, 200, 200); }
    @Override public int getColorPresionFijado()   { return Color.rgb(176, 190, 197); }


    @Override public int getColorSeparador()       { return Color.rgb(176, 190, 197); }
    @Override public int getColorTituloSeccion()   { return Color.rgb(63, 81, 181); }
    @Override public int getColorIconoPin()        { return Color.rgb(33, 150, 243); }
    @Override public int getColorIconoBorrar()     { return Color.rgb(244, 67, 54); }

    @Override public float getTamanioTextoTecla()        { return 16f; }
    @Override public float getTamanioTextoEspecial()     { return 13f; }
    @Override public float getTamanioTextoGesto()        { return 9f; }
    @Override public float getTamanioTextoGestoActivo()  { return 10f; }
    @Override public float getTamanioTextoLista()        { return 12f; }
    @Override public float getTamanioTextoSeccion()      { return 10f; }
    @Override public float getTamanioTextoVacio()        { return 11f; }

    @Override public float getRadioEsquinasTecla()       { return 14f; }
    @Override public int getAlturaFilaBasePx()           { return 140; }
    @Override public float getFactorOscurecimiento()     { return 0.2f; } 

    @Override public int getColorEtiquetaBarra()           { return Color.rgb(136, 136, 136); }
    @Override public int getColorEtiquetaBarraPresionada() { return Color.rgb(63, 81, 181); }
    @Override public int getColorFondoBarra() { return Color.rgb(235, 235, 235); }

    @Override public float getSeparacionFilas()    { return 8f; } // 8dp entre fila y fila
    @Override public float getSeparacionColumnas() { return 5f; } // 5dp entre teclas juntas
}