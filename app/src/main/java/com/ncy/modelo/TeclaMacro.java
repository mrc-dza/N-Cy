package com.ncy.modelo;

public class TeclaMacro extends Tecla {
    private final String etiquetaCorta;
    private final String textoLargo;

    public TeclaMacro(int codigo, String etiquetaCorta, String textoLargo) {
        super(codigo);
        this.etiquetaCorta = etiquetaCorta;
        this.textoLargo    = textoLargo;
    }

    public String obtenerEtiqueta()    { return etiquetaCorta; }
    public String obtenerTextoLargo()  { return textoLargo; }
}