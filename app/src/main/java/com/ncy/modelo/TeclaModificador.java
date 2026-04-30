package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

public class TeclaModificador extends Tecla {

    private final String etiqueta;
    private InfoVisual infoCacheActivo;
    private InfoVisual infoCacheApagado;
    private boolean cacheValido = false;

    public TeclaModificador(int codigo, String etiqueta) {
        super(codigo);
        this.etiqueta = etiqueta;
    }

    public String obtenerEtiqueta() {
        return etiqueta;
    }

    @Override
    public InfoVisual obtenerInfoVisual(EstadoTeclado estado) {
        TemaVisual tema = GestorTema.getInstance().obtenerTemaActivo();
        boolean encendido = estado != null && estado.isModificadorActivo(codigo);
        
        if (!cacheValido) {
            infoCacheActivo = new InfoVisual(
                tema.getColorTeclaModActivo(), 
                etiqueta, 
                true, 
                obtenerIconoResId(), 
                tema.getColorTextoModActivo()
            );
            
            infoCacheApagado = new InfoVisual(
                tema.getColorTextoModApagado(), 
                etiqueta, 
                false, 
                obtenerIconoResId()
            );
            cacheValido = true;
        }
        
        return encendido ? infoCacheActivo : infoCacheApagado;
    }

    
    public void invalidarCache() {
        cacheValido = false;
    }

    
}