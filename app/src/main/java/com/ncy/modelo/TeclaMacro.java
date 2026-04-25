package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

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

    @Override
    public InfoVisual obtenerInfoVisual(EstadoTeclado estado) {
        TemaVisual tema = GestorTema.getInstance().obtenerTemaActivo();
        return new InfoVisual(tema.getColorTeclaMacro(), etiquetaCorta, true, obtenerIconoResId());
    }
}