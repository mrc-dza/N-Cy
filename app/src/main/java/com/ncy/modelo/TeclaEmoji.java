package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

public class TeclaEmoji extends Tecla {

    private final String emoji;

    public TeclaEmoji(int codigo, String emoji) {
        super(codigo);
        this.emoji = emoji;
    }

    public String obtenerEmoji() { 
        return emoji;
    }

    @Override
    public InfoVisual obtenerInfoVisual(EstadoTeclado estado) {
        TemaVisual tema = GestorTema.getInstance().obtenerTemaActivo();
        return new InfoVisual(tema.getColorTeclaNormal(), emoji, false, obtenerIconoResId());
    }
}