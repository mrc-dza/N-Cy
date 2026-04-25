package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.Constantes;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

public class TeclaCaracter extends Tecla {

    private final String etiquetaPrincipal;
    
    public TeclaCaracter(int codigo, String etiqueta) {
        super(codigo);
        this.etiquetaPrincipal = etiqueta;
    }

    public String obtenerEtiqueta() {
        return etiquetaPrincipal;
    }

    @Override
    public InfoVisual obtenerInfoVisual(EstadoTeclado estado) {
        TemaVisual tema = GestorTema.getInstance().obtenerTemaActivo();
        boolean shiftActivo = estado != null && estado.isModificadorActivo(Constantes.CODIGO_SHIFT);
        
        if (shiftActivo) {
            return new InfoVisual(tema.getColorTeclaNormal(), etiquetaPrincipal.toUpperCase(java.util.Locale.getDefault()), false, obtenerIconoResId());
        } else {
            return new InfoVisual(tema.getColorTeclaNormal(), etiquetaPrincipal.toLowerCase(java.util.Locale.getDefault()), false, obtenerIconoResId());
        }
    }
}