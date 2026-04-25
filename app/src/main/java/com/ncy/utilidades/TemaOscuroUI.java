package com.ncy.utilidades;

import android.graphics.Color;

public class TemaOscuroUI implements TemaVisualUI {
    @Override public int getColorFondoGeneral() { return Color.rgb(0, 0, 0); }
    @Override public int getColorFondoCard() { return Color.rgb(26, 26, 26); }
    @Override public int getColorBordeCard() { return Color.rgb(42, 42, 42); }
    @Override public int getColorBordeCardActivo() { return Color.rgb(26, 92, 36); }
    @Override public int getColorTextoPrincipal() { return Color.rgb(255, 255, 255); }
    @Override public int getColorTextoSecundario() { return Color.rgb(136, 136, 136); }
    @Override public int getColorTextoSobreAcento() { return Color.rgb(0, 0, 0); }
    @Override public int getColorAcento() { return Color.rgb(0, 201, 33); }
    @Override public int getColorFondoBanner() { return Color.rgb(10, 42, 15); }
    @Override public int getColorFondoCirculo() { return Color.rgb(26, 26, 26); }
    @Override public String getNombre() { return "oscuro"; }
}
