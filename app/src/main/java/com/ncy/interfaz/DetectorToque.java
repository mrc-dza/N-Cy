package com.ncy.interfaz;

import com.ncy.modelo.Tecla;
import java.util.List;

public class DetectorToque {

    // Devuelve la tecla que contiene el punto (x, y), o null si ninguna
    public Tecla encontrar(float x, float y, List<List<Tecla>> filas) {
        for (List<Tecla> fila : filas) {
            if (fila.isEmpty()) continue;
            Tecla primera = fila.get(0);
            // Optimización: descartar filas fuera del rango vertical
            if (y < primera.obtenerY() || y >= primera.obtenerY() + primera.obtenerAlto()) continue;
            for (Tecla tecla : fila) {
                if (tecla.contiene((int) x, (int) y)) return tecla;
            }
        }
        return null;
    }
}