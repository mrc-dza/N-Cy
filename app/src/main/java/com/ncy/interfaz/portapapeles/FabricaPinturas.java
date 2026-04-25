package com.ncy.interfaz.portapapeles;

import android.graphics.Paint;
import com.ncy.utilidades.TemaVisual;

public class FabricaPinturas {

    public static Paint texto(TemaVisual tema, float densidadTexto) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(tema.getColorTextoPrincipal());
        p.setTextSize(tema.getTamanioTextoLista() * densidadTexto);
        return p;
    }

    public static Paint separador(TemaVisual tema, float densidadGrafica) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(tema.getColorSeparador());
        p.setStrokeWidth(1f * densidadGrafica); // Línea fina y nítida de 1dp
        p.setStyle(Paint.Style.STROKE);
        return p;
    }

    public static Paint seccion(TemaVisual tema, float densidadTexto) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(tema.getColorTituloSeccion());
        p.setTextSize(tema.getTamanioTextoSeccion() * densidadTexto);
        return p;
    }

    public static Paint vacio(TemaVisual tema, float densidadTexto) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(tema.getColorTextoVacio());
        p.setTextSize(tema.getTamanioTextoVacio() * densidadTexto);
        p.setTextAlign(Paint.Align.CENTER);
        return p;
    }

    public static Paint fondoPresionado(TemaVisual tema) {
        return fondo(tema.getColorPresionNormal());
    }

    public static Paint fondoFijadoPresionado(TemaVisual tema) {
        return fondo(tema.getColorPresionFijado());
    }

    private static Paint fondo(int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        return p;
    }
}