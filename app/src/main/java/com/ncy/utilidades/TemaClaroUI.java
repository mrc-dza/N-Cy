package com.ncy.utilidades;

import android.graphics.Color;

public class TemaClaroUI implements TemaVisualUI {
    @Override public int getColorFondoGeneral() { return Color.rgb(236, 239, 241); }
    @Override public int getColorFondoCard() { return Color.rgb(255, 255, 255); }
    @Override public int getColorBordeCard() { return Color.rgb(207, 216, 220); }
    @Override public int getColorBordeCardActivo() { return Color.rgb(63, 81, 181); }
    @Override public int getColorTextoPrincipal() { return Color.rgb(33, 33, 33); }
    @Override public int getColorTextoSecundario() { return Color.rgb(85, 85, 85); }
    @Override public int getColorTextoSobreAcento() { return Color.rgb(255, 255, 255); }
    @Override public int getColorAcento() { return Color.rgb(63, 81, 181); }
    @Override public int getColorFondoBanner() { return Color.rgb(232, 234, 246); }
    @Override public int getColorFondoCirculo() { return Color.rgb(236, 239, 241); }
    @Override public String getNombre() { return "claro"; }
}
