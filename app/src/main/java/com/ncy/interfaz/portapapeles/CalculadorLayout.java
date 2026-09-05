package com.ncy.interfaz.portapapeles;

import android.graphics.Paint;
import com.ncy.interfaz.ProcesadorTexto;
import java.util.List;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

public class CalculadorLayout {

    public final int PADDING_H;
    public final int PADDING_V;
    public final int ANCHO_BOTON;
    public final int ALTO_SECCION;
    public final int ESPACIO_ENTRE;
    
    public static final int MAX_LINEAS_FIJO = 4;
    public static final int MAX_LINEAS_TEMP = 10;


    private final ProcesadorTexto procesador;
    private final TextPaint pinturaTexto;
    private int[] altoTemporales = new int[0];
    private int[] altoFijados = new int[0];

    // Ahora guardamos StaticLayouts pre-renderizados en C++
    private List<StaticLayout> layoutsTemporales = new java.util.ArrayList<>();
    private List<StaticLayout> layoutsFijadas = new java.util.ArrayList<>();

    public List<StaticLayout> getLayoutsTemporales() { return layoutsTemporales; }
    public List<StaticLayout> getLayoutsFijadas() { return layoutsFijadas; }

    public CalculadorLayout(ProcesadorTexto procesador, TextPaint pinturaTexto, float densidad) {
        this.procesador = procesador;
        this.pinturaTexto = new TextPaint(pinturaTexto);
        
        this.PADDING_H = (int) (24 * densidad);
        this.PADDING_V = (int) (6 * densidad);
        this.ANCHO_BOTON = (int) (48 * densidad); 
        this.ALTO_SECCION = (int) (30 * densidad);
        this.ESPACIO_ENTRE = (int) (4 * densidad);
    }

    public void recalcular(List<String> temps, List<String> fijos, int anchoVista) {
        int anchoTexto = (int) anchoTexto(anchoVista);

        altoTemporales = new int[temps.size()];
        layoutsTemporales.clear();
        for (int i = 0; i < temps.size(); i++) {
            // El Builder calcula el espaciado, los saltos de línea y los puntos suspensivos automáticamente
            StaticLayout sl = StaticLayout.Builder
                .obtain(temps.get(i), 0, temps.get(i).length(), pinturaTexto, anchoTexto)
                .setMaxLines(MAX_LINEAS_TEMP)
                .setEllipsize(TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build();
            layoutsTemporales.add(sl);
            altoTemporales[i] = PADDING_V * 2 + sl.getHeight();
        }

        altoFijados = new int[fijos.size()];
        layoutsFijadas.clear();
        for (int i = 0; i < fijos.size(); i++) {
            StaticLayout sl = StaticLayout.Builder
                .obtain(fijos.get(i), 0, fijos.get(i).length(), pinturaTexto, anchoTexto)
                .setMaxLines(MAX_LINEAS_FIJO)
                .setEllipsize(TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build();
            layoutsFijadas.add(sl);
            altoFijados[i] = PADDING_V * 2 + sl.getHeight();
        }
    }

    public int altoTotal(int totalTemps, int totalFijos) {
        int alto = 0;
        for (int h : altoTemporales) alto += h + ESPACIO_ENTRE;
        if (totalFijos > 0) {
            alto += ALTO_SECCION;
            for (int h : altoFijados) alto += h + ESPACIO_ENTRE;
        }
        return alto;
    }

    public int[] getAltoTemporales() { return altoTemporales; }
    public int[] getAltoFijados() { return altoFijados; }

    public enum Zona { TEMPORAL, FIJADO, NINGUNA }
    public record ResultadoToque(Zona zona, int indice, boolean tocoBoton) {}

    public ResultadoToque resolverToque(float x, float y, int totalTemps, int totalFijos, int anchoVista) {
        float posY = 0;
        for (int i = 0; i < altoTemporales.length && i < totalTemps; i++) {
            float bottom = posY + altoTemporales[i];
            if (y >= posY && y < bottom) {
                return new ResultadoToque(Zona.TEMPORAL, i, tocoBoton(x, anchoVista));
            }
            posY = bottom + ESPACIO_ENTRE;
        }

        if (totalFijos > 0) {
            posY += ALTO_SECCION;
            for (int i = 0; i < altoFijados.length && i < totalFijos; i++) {
                float bottom = posY + altoFijados[i];
                if (y >= posY && y < bottom) {
                    return new ResultadoToque(Zona.FIJADO, i, tocoBoton(x, anchoVista));
                }
                posY = bottom + ESPACIO_ENTRE;
            }
        }
        return new ResultadoToque(Zona.NINGUNA, -1, false);
    }

    public float anchoTexto(int anchoVista) {
        return anchoVista - PADDING_H * 2 - ANCHO_BOTON - 16;
    }

    private boolean tocoBoton(float x, int anchoVista) {
        return x >= anchoVista - PADDING_H - ANCHO_BOTON;
    }
}