package com.ncy.modelo;

public class TeclaModificador extends Tecla {
    private final String etiqueta;

    public TeclaModificador(int codigo, String etiqueta) {
        super(codigo);
        this.etiqueta = etiqueta;
    }

    public String obtenerEtiqueta() {
        return etiqueta;
    }
}