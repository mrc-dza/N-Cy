package com.ncy.utilidades;

import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;

public class UiHelper {

    /**
     * Crea un fondo rectangular con bordes redondeados y un trazo opcional.
     */
    public static GradientDrawable crearFondo(int color, float radio, int colorBorde, int anchoBorde) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.RECTANGLE);
        d.setColor(color);
        d.setCornerRadius(radio);
        if (anchoBorde > 0) d.setStroke(anchoBorde, colorBorde);
        return d;
    }

    /**
     * Aplica el estilo visual de "tarjeta" (Card) a una vista.
     */
    public static void aplicarFondoCard(View v, int color, int borde) {
        Resources res = v.getContext().getResources();
        float density = res.getDisplayMetrics().density;
        
        // CORRECCIÓN: Uso del GestorTema para obtener el radio de esquinas dinámicamente
        float radio = GestorTema.getInstance().obtenerTemaActivo().getRadioEsquinasTecla() * density;
        int anchoBorde = Math.round(1 * density);
        
        v.setBackground(crearFondo(color, radio, borde, anchoBorde));
    }

    /**
     * Aplica un fondo plano que se fusiona con la pantalla y dibuja una línea separadora inferior.
     */
    public static void aplicarSeparadorInferior(View v, int colorFondo, int colorLinea) {
        Resources res = v.getContext().getResources();
        // Grosor de la línea (1dp, igual que en el portapapeles)
        int grosorPx = Math.round(1 * res.getDisplayMetrics().density);

        GradientDrawable fondo = new GradientDrawable();
        fondo.setColor(colorFondo);

        GradientDrawable linea = new GradientDrawable();
        linea.setColor(colorLinea);

        // Ponemos la línea detrás, y el fondo encima
        Drawable[] layers = {linea, fondo};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        
        // Encogemos el fondo desde abajo para dejar al descubierto la línea trasera
        layerDrawable.setLayerInset(1, 0, 0, 0, grosorPx);

        v.setBackground(layerDrawable);
    }

    /**
     * Convierte unidades DP a Píxeles según la densidad de la pantalla.
     */
    public static int dpAPx(Resources res, int dp) {
        return Math.round(dp * res.getDisplayMetrics().density);
    }
}