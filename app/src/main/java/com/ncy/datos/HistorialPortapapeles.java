package com.ncy.datos;

import com.ncy.utilidades.Constantes;
import java.util.ArrayList;
import java.util.List;

public class HistorialPortapapeles {

    private final List<String> temporales = new ArrayList<>();
    private final List<String> fijados    = new ArrayList<>();
    
    public void cargar(List<String> temps, List<String> fijos) {
        temporales.addAll(temps);
        fijados.addAll(fijos);
    }

    public boolean agregar(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        if (fijados.contains(texto)) return false;
        
        temporales.remove(texto);
        temporales.add(0, texto);
        
        if (temporales.size() > Constantes.MAX_TEMPORALES) {
            temporales.remove(temporales.size() - 1);
        }
        return true;
    }

    public boolean fijar(int indice) {
        if (indice < 0 || indice >= temporales.size()) return false;
        
        if (fijados.size() >= Constantes.MAX_FIJADOS) {
            fijados.remove(fijados.size() - 1);
        }
        
        fijados.add(0, temporales.remove(indice));
        return true;
    }

    public boolean eliminarFijado(int indice) {
        if (indice < 0 || indice >= fijados.size()) return false;
        fijados.remove(indice);
        return true;
    }

    public boolean limpiarTemporales() {
        if (temporales.isEmpty()) return false;
        temporales.clear();
        return true;
    }

    public List<String> obtenerTemporales() {
        return new ArrayList<>(temporales);
    }

    public List<String> obtenerFijados() {
        return new ArrayList<>(fijados);
    }
}