package com.ncy.utilidades;
import android.graphics.Color;

public class TemaOscuro implements TemaVisual {

    @Override public int getColorFondoGeneral() { return Color.rgb(30, 31, 33); }
    @Override public int getColorTeclaNormal() { return Color.rgb(52, 53, 55); }
    @Override public int getColorTeclaAccion() { return Color.rgb(69, 71, 70); }
    @Override public int getColorTeclaEnter() { return Color.rgb(0, 120, 215); }
    @Override public int getColorTeclaMacro() { return Color.rgb(0, 100, 0); }
    @Override public int getColorTeclaModActivo() { return Color.rgb(39, 39, 39); }

    @Override public int getColorTextoPrincipal() { return Color.rgb(255, 255, 255); }
    @Override public int getColorTextoEspecial() { return Color.rgb(255, 255, 255); }
    @Override public int getColorTextoModApagado() { return Color.rgb(69, 71, 70); }
    @Override public int getColorTextoModActivo() { return Color.rgb(0, 120, 215); } 
    @Override public int getColorTextoVacio() { return Color.rgb(136, 136, 136); }

    @Override public int getColorAcento() { return Color.rgb(0, 225, 255); }
    @Override public int getColorGestoActivo() { return Color.rgb(255, 255, 255); }

    @Override public int getColorPresionNormal() { return Color.rgb(60, 60, 60); }
    @Override public int getColorPresionFijado() { return Color.rgb(60, 60, 60); }
   

    @Override public int getColorSeparador() { return Color.rgb(60, 60, 60); }
    @Override public int getColorTituloSeccion() { return Color.rgb(0, 225, 255); }
    @Override public int getColorIconoPin() { return Color.rgb(0, 140, 255); }
    @Override public int getColorIconoBorrar() { return Color.rgb(187, 43, 43); }

    @Override public float getTamanioTextoTecla() { return 16f; }
    @Override public float getTamanioTextoEspecial() { return 13f; }
    @Override public float getTamanioTextoGesto() { return 9f; }
    @Override public float getTamanioTextoGestoActivo() { return 10f; }
    @Override public float getTamanioTextoLista() { return 12f; }
    @Override public float getTamanioTextoSeccion() { return 10f; }
    @Override public float getTamanioTextoVacio() { return 11f; }

    @Override public float getRadioEsquinasTecla() { return 7f; }
    @Override public int getAlturaFilaBasePx() { return 140; }
    @Override public float getFactorOscurecimiento() { return 0.6f; }

    @Override public int getColorEtiquetaBarra()           { return Color.rgb(255, 255, 255); }
    @Override public int getColorEtiquetaBarraPresionada() { return Color.rgb(0, 120, 215); }
    @Override public int getColorFondoBarra() { return Color.rgb(30, 31 ,33); }

    @Override public float getSeparacionFilas()    { return 6f; } // 8dp entre fila y fila
    @Override public float getSeparacionColumnas() { return 5f; } // 5dp entre teclas juntas
}