package com.ncy.interfaz.portapapeles;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.ncy.interfaz.ProcesadorTexto;
import com.ncy.utilidades.TemaVisual;

import java.util.List;

import android.text.StaticLayout;
import android.text.TextPaint;

public class DibujanteLista {

    private final ProcesadorTexto procesador;
    private final CalculadorLayout calculador;
    private final float densidadGrafica; // Añadida la densidad gráfica
    private final TextPaint pinturaTexto;
    private final Paint pinturaSeparador;
    private final Paint pinturaSeccion;
    private final Paint pinturaVacio;
    private final Paint pinturaFondoPresionado;
    private final Paint pinturaFondoFijadoPresionado;
    private final Drawable iconoPin;
    private final Drawable iconoBasurero;

    public DibujanteLista(ProcesadorTexto procesador,
                          CalculadorLayout calculador,
                          Drawable iconoPin,
                          Drawable iconoBasurero,
                          TemaVisual tema,
                          float densidadGrafica, // Se recibe la densidad gráfica
                          float densidadTexto) { // Se recibe la densidad de texto
                          
        this.procesador = procesador;
        this.calculador = calculador;
        this.iconoPin = iconoPin;
        this.iconoBasurero = iconoBasurero;
        this.densidadGrafica = densidadGrafica;

        // Se inyectan las densidades para que los textos y separadores escalen correctamente
        this.pinturaTexto = FabricaPinturas.texto(tema, densidadTexto);
        this.pinturaSeparador = FabricaPinturas.separador(tema, densidadGrafica);
        this.pinturaSeccion = FabricaPinturas.seccion(tema, densidadTexto);
        this.pinturaVacio = FabricaPinturas.vacio(tema, densidadTexto);
        this.pinturaFondoPresionado = FabricaPinturas.fondoPresionado(tema);
        this.pinturaFondoFijadoPresionado = FabricaPinturas.fondoFijadoPresionado(tema);
    }

    public Paint obtenerPinturaTexto() { 
        return pinturaTexto;
    }

    
    public void dibujarTodo(Canvas canvas, int ancho,
                             List<String> temps, List<String> fijos,
                             int itemPresionado, boolean presionandoFijado) {
        if (temps.isEmpty() && fijos.isEmpty()) {
            canvas.drawText("Historial vacío", ancho / 2f, 70f, pinturaVacio);
            return;
        }

        int[] altosTemp = calculador.getAltoTemporales();
        int[] altosFijo = calculador.getAltoFijados();
        
        // Obtenemos los StaticLayouts pre-renderizados en C++
        List<StaticLayout> layoutsTemps = calculador.getLayoutsTemporales();
        List<StaticLayout> layoutsFijas = calculador.getLayoutsFijadas();
        
        float anchoTexto = calculador.anchoTexto(ancho);
        float posY = 0;
        
        for (int i = 0; i < temps.size(); i++) {
            boolean presionado = !presionandoFijado && i == itemPresionado;
            // Pasamos el StaticLayout directo a dibujarItem
            posY = dibujarItem(canvas, ancho, posY,
                    layoutsTemps.get(i), altosTemp[i], anchoTexto, presionado,
                    iconoPin, pinturaFondoPresionado);
            dibujarLinea(canvas, ancho, posY + calculador.ESPACIO_ENTRE / 2f);
            posY += calculador.ESPACIO_ENTRE;
        }

        if (!fijos.isEmpty()) {
            dibujarEncabezadoFijados(canvas, ancho, posY);
            posY += calculador.ALTO_SECCION;

            for (int i = 0; i < fijos.size(); i++) {
                boolean presionado = presionandoFijado && i == itemPresionado;
                posY = dibujarItem(canvas, ancho, posY,
                        layoutsFijas.get(i), altosFijo[i], anchoTexto, presionado,
                        iconoBasurero, pinturaFondoFijadoPresionado);
                dibujarLinea(canvas, ancho, posY + calculador.ESPACIO_ENTRE / 2f);
                posY += calculador.ESPACIO_ENTRE;
            }
        }
    }

    private float dibujarItem(Canvas canvas, int ancho, float top,
                              StaticLayout staticLayout, int altoItem,
                              float anchoTexto, boolean presionado,
                              Drawable icono, Paint pinturaFondo) {
                              
        if (presionado) canvas.drawRect(0, top, ancho, top + altoItem, pinturaFondo);
        
        canvas.save();
        canvas.translate(calculador.PADDING_H, top + calculador.PADDING_V);
        staticLayout.draw(canvas);
        canvas.restore();
 
        dibujarIcono(canvas, ancho, top, altoItem, icono);
        return top + altoItem;
    }

    

    private void dibujarLinea(Canvas canvas, int ancho, float y) {
        // Empieza en 0 (borde izquierdo) y termina en 'ancho' (borde derecho)
        canvas.drawLine(0, y, ancho, y, pinturaSeparador);
    }


    private void dibujarEncabezadoFijados(Canvas canvas, int ancho, float posY) {
        dibujarLinea(canvas, ancho, posY);
        float textY = posY + calculador.ALTO_SECCION / 2f
                - (pinturaSeccion.descent() + pinturaSeccion.ascent()) / 2f;
        canvas.drawText("  FIJADOS", 0, textY, pinturaSeccion);
    }

    // REEMPLAZA O MODIFICA ESTOS DOS MÉTODOS EN DibujanteLista.java

    

    



    private void dibujarIcono(Canvas canvas, int ancho, float top, int altoItem, Drawable icono) {
        if (icono == null) return;
        
        // REFACTORIZADO: Ahora usa dp (20 * densidad) y se pega a la derecha
        int iconSize = (int) (20 * densidadGrafica);
        int iconX = ancho - calculador.PADDING_H - iconSize;
        int iconY = (int)(top + altoItem / 2 - iconSize / 2);
        
        icono.setBounds(iconX, iconY, iconX + iconSize, iconY + iconSize);
        icono.draw(canvas);
    }
}