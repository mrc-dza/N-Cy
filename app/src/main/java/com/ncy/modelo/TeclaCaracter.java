package com.ncy.modelo;

public class TeclaCaracter extends Tecla {
    private final String etiquetaPrincipal;
    
    public TeclaCaracter(int codigo, String etiqueta) {
        super(codigo);
        this.etiquetaPrincipal = etiqueta;
    }

    public String obtenerEtiqueta() {
        return etiquetaPrincipal;
    }
}