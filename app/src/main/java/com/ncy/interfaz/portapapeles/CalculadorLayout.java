package com.ncy.interfaz.portapapeles;

import android.graphics.Paint;
import com.ncy.interfaz.ProcesadorTexto;
import java.util.List;

public class CalculadorLayout {

    public final int PADDING_H;
    public final int PADDING_V;
    public final int ANCHO_BOTON;
    public final int ALTO_SECCION;
    public final int ESPACIO_ENTRE;
    
    public static final int MAX_LINEAS_FIJO = 4;
    public static final int MAX_LINEAS_TEMP = 10;

    private final ProcesadorTexto procesador;
    private final Paint pinturaTexto;
    private int[] altoTemporales = new int[0];
    private int[] altoFijados = new int[0];

    private List<List<String>> lineasTemporales = new java.util.ArrayList<>();
    private List<List<String>> lineasFijadas = new java.util.ArrayList<>();

    public List<List<String>> getLineasTemporales() { return lineasTemporales; }
    public List<List<String>> getLineasFijadas() { return lineasFijadas; }

    public CalculadorLayout(ProcesadorTexto procesador, Paint pinturaTexto, float densidad) {
        this.procesador = procesador;
        this.pinturaTexto = new Paint(pinturaTexto);
        
        this.PADDING_H = (int) (24 * densidad);
        this.PADDING_V = (int) (6 * densidad);
        
        this.ANCHO_BOTON = (int) (48 * densidad); 
        this.ALTO_SECCION = (int) (30 * densidad);
        this.ESPACIO_ENTRE = (int) (4 * densidad);
    }

    public void recalcular(List<String> temps, List<String> fijos, int anchoVista) {
        float anchoTexto = anchoTexto(anchoVista);
        float lineaAlto = pinturaTexto.getTextSize() * 1.4f;

        altoTemporales = new int[temps.size()];
        lineasTemporales.clear();
        for (int i = 0; i < temps.size(); i++) {
            // TRUCO: Pedimos UNA línea extra para saber si el texto continúa
            List<String> lineas = procesador.dividirEnLineas(temps.get(i), anchoTexto, pinturaTexto, MAX_LINEAS_TEMP + 1);
            
            boolean hayMas = lineas.size() > MAX_LINEAS_TEMP;
            if (hayMas && !lineas.isEmpty()) {
                lineas.remove(lineas.size() - 1); // Borramos la línea extra
                int lastIdx = lineas.size() - 1;
                lineas.set(lastIdx, procesador.truncarConPuntos(lineas.get(lastIdx), anchoTexto, pinturaTexto));
            }
            lineasTemporales.add(lineas);
            altoTemporales[i] = (int) (PADDING_V * 2 + lineaAlto * lineas.size());
        }

        altoFijados = new int[fijos.size()];
        lineasFijadas.clear();
        for (int i = 0; i < fijos.size(); i++) {
            // TRUCO: Pedimos UNA línea extra para saber si el texto continúa
            List<String> lineas = procesador.dividirEnLineas(fijos.get(i), anchoTexto, pinturaTexto, MAX_LINEAS_FIJO + 1);
            
            boolean hayMas = lineas.size() > MAX_LINEAS_FIJO;
            if (hayMas && !lineas.isEmpty()) {
                lineas.remove(lineas.size() - 1); // Borramos la línea extra
                int lastIdx = lineas.size() - 1;
                lineas.set(lastIdx, procesador.truncarConPuntos(lineas.get(lastIdx), anchoTexto, pinturaTexto));
            }
            lineasFijadas.add(lineas);
            altoFijados[i] = (int) (PADDING_V * 2 + lineaAlto * lineas.size());
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

    public static class ResultadoToque {
        public enum Zona { TEMPORAL, FIJADO, NINGUNA }
        
        public final Zona zona;
        public final int indice;
        public final boolean tocoBoton;

        public ResultadoToque(Zona zona, int indice, boolean tocoBoton) {
            this.zona = zona;
            this.indice = indice;
            this.tocoBoton = tocoBoton;
        }
    }

    public ResultadoToque resolverToque(float x, float y, int totalTemps, int totalFijos, int anchoVista) {
        float posY = 0;
        for (int i = 0; i < altoTemporales.length && i < totalTemps; i++) {
            float bottom = posY + altoTemporales[i];
            if (y >= posY && y < bottom) {
                return new ResultadoToque(ResultadoToque.Zona.TEMPORAL, i, tocoBoton(x, anchoVista));
            }
            posY = bottom + ESPACIO_ENTRE;
        }

        if (totalFijos > 0) {
            posY += ALTO_SECCION;
            for (int i = 0; i < altoFijados.length && i < totalFijos; i++) {
                float bottom = posY + altoFijados[i];
                if (y >= posY && y < bottom) {
                    return new ResultadoToque(ResultadoToque.Zona.FIJADO, i, tocoBoton(x, anchoVista));
                }
                posY = bottom + ESPACIO_ENTRE;
            }
        }

        return new ResultadoToque(ResultadoToque.Zona.NINGUNA, -1, false);
    }

    public float anchoTexto(int anchoVista) {
        return anchoVista - PADDING_H * 2 - ANCHO_BOTON - 16;
    }

    private boolean tocoBoton(float x, int anchoVista) {
        return x >= anchoVista - PADDING_H - ANCHO_BOTON;
    }
}