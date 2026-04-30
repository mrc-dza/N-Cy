package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.Constantes;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

public class TeclaCaracter extends Tecla {

    private InfoVisual infoCacheShiftActivo;
    private InfoVisual infoCacheNormal;
    private boolean cacheValido = false;
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
        
        if (!cacheValido) {
            infoCacheShiftActivo = new InfoVisual(tema.getColorTeclaNormal(), etiquetaPrincipal.toUpperCase(java.util.Locale.getDefault()), false, obtenerIconoResId());
            infoCacheNormal = new InfoVisual(tema.getColorTeclaNormal(), etiquetaPrincipal.toLowerCase(java.util.Locale.getDefault()), false, obtenerIconoResId());
            cacheValido = true;
        }
        
        return shiftActivo ? infoCacheShiftActivo : infoCacheNormal;
    }

    public void invalidarCache() {
        cacheValido = false;
    }
}