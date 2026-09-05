package com.ncy.utilidades;

import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

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
    public static void aplicarFondoCard(View v, int color, int borde, float radioEsquinasDp) {
        Resources res = v.getContext().getResources();
        float density = res.getDisplayMetrics().density;
        
        // El radio ahora viene inyectado como parámetro
        float radio = radioEsquinasDp * density;
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

    public static void aplicarFondoPill(TextView v, int color, Resources res) {
        v.setBackground(crearFondo(color, dpAPx(res, 12), 0, 0));
    }
    
    public static void aplicarFondoCirculo(View v, int color) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setColor(color);
        v.setBackground(d);
    }

    // ¡ÉSTE ES EL MÉTODO QUE FALTABA PARA QUE COMPILE MAINACTIVITY!
    public static void aplicarFondoBanner(View v, int colorFondo, int colorBorde) {
        v.setBackground(crearFondo(colorFondo, dpAPx(v.getResources(), 10), colorBorde, dpAPx(v.getResources(), 1)));
    }

    public static void aplicarFondoGrupo(View v, int colorFondo, int colorBorde) {
        float density = v.getResources().getDisplayMetrics().density;
        int bordePx = Math.max(1, Math.round(0.5f * density));
        v.setBackground(crearFondo(colorFondo, 16 * density, colorBorde, bordePx));
    }

    // Fiel al CSS: border-radius: 16px, background: #161616, border: 0.5px solid #2A2A2A
    /*public static void aplicarFondoGrupo(View v) {
        float density = v.getResources().getDisplayMetrics().density;
        int bordePx = Math.max(1, Math.round(0.5f * density));
        v.setBackground(crearFondo(0xFF161616, 16 * density, 0xFF2A2A2A, bordePx));
    }
    */

    // Fiel al CSS: border-radius: 10px para los iconos
    public static void aplicarFondoIcono(View v, int colorFondo) {
        v.setBackground(crearFondo(colorFondo, dpAPx(v.getResources(), 10), 0, 0));
    }

    // Fiel al CSS: padding 3px 8px, border-radius 6px
    public static void aplicarFondoBadge(android.widget.TextView v, int colorFondo, int colorTexto) {
        float density = v.getResources().getDisplayMetrics().density;
        int bordePx = Math.max(1, Math.round(0.5f * density));
        // El borde es una versión oscurecida del color del texto para simular el CSS
        v.setBackground(crearFondo(colorFondo, 6 * density, 0xFF1A4A1F, bordePx));
        v.setTextColor(colorTexto);
    }

    // Fiel al CSS: width/height 36px, border-radius 50%, bg #1A1A1A, border #2A2A2A
    /*public static void aplicarBotonVolver(View v) {
        float density = v.getResources().getDisplayMetrics().density;
        int bordePx = Math.max(1, Math.round(0.5f * density));
        v.setBackground(crearFondo(0xFF1A1A1A, 18 * density, 0xFF2A2A2A, bordePx));
    }
    */

    public static void aplicarBotonVolver(View v, int colorFondo, int colorBorde) {
        float density = v.getResources().getDisplayMetrics().density;
        int bordePx = Math.max(1, Math.round(0.5f * density));
        v.setBackground(crearFondo(colorFondo, 18 * density, colorBorde, bordePx));
    }
}