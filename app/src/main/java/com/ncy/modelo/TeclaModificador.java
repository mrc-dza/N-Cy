package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

public class TeclaModificador extends Tecla {

    private final String etiqueta;

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
        
        if (encendido) {
            // AÑADIDO: Pasamos el color específico del texto/icono cuando la tecla está activa
            return new InfoVisual(
                tema.getColorTeclaModActivo(), 
                etiqueta, 
                true, 
                obtenerIconoResId(), 
                tema.getColorTextoModActivo() // <--- El nuevo color personalizado
            );
        } else {
            // Cuando está apagado, usa el constructor tradicional (colorPersonalizado = null)
            return new InfoVisual(
                tema.getColorTextoModApagado(), 
                etiqueta, 
                false, 
                obtenerIconoResId()
            );
        }
    }

    
}