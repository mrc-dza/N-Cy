package com.ncy.interfaz;

import android.graphics.Paint;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorTexto {

    public List<String> dividirEnLineas(String texto, float anchoMax, Paint pintura, int maxLineas) {
        List<String> lineas = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) {
            return lineas;
        }

        String[] parrafos = texto.split("\n", -1);
        for (String parrafo : parrafos) {
            if (maxLineas > 0 && lineas.size() >= maxLineas) break;

            if (parrafo.isEmpty()) {
                lineas.add("");
                continue;
            }

            String[] palabras = parrafo.split("[ \\t]+");
            StringBuilder lineaActual = new StringBuilder();
            
            for (String palabra : palabras) {
                if (palabra.isEmpty()) continue;
                String prueba = lineaActual.length() == 0 ? palabra : lineaActual.toString() + " " + palabra;
                
                if (pintura.measureText(prueba) <= anchoMax) {
                    lineaActual.setLength(0);
                    lineaActual.append(prueba);
                } else {
                    if (lineaActual.length() > 0) {
                        lineas.add(lineaActual.toString());
                        if (maxLineas > 0 && lineas.size() >= maxLineas) {
                            return lineas;
                        }
                    }
                    lineaActual.setLength(0);
                    lineaActual.append(palabra);
                }
            }
            
            if (lineaActual.length() > 0) {
                lineas.add(lineaActual.toString());
            }
        }
        
        return lineas;
    }


    public String truncarConPuntos(String linea, float anchoMax, Paint pintura) {
        if (linea == null || linea.isEmpty()) {
            return "";
        }
        
        final String puntos = "…";
        float anchoPuntos = pintura.measureText(puntos);
        
        // Si el espacio es tan pequeño que ni siquiera caben los puntos, devolvemos solo puntos
        if (anchoMax <= anchoPuntos) {
            return puntos;
        }
        
        // breakText calcula instantáneamente cuántos caracteres caben en el ancho disponible
        int cantidadCaracteres = pintura.breakText(linea, true, anchoMax - anchoPuntos, null);
        
        return linea.substring(0, cantidadCaracteres) + puntos;
    }

    
}