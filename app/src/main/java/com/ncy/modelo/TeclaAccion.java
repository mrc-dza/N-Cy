package com.ncy.modelo;

public class TeclaAccion extends Tecla {
    private final String etiqueta;

    public TeclaAccion(int codigo, String etiqueta) {
        super(codigo);
        this.etiqueta = etiqueta;
    }

    public String obtenerEtiqueta() {
        return etiqueta;
    }
}