package com.ncy.modelo;

public class TeclaEmoji extends Tecla {
    private final String emoji;

    public TeclaEmoji(int codigo, String emoji) {
        super(codigo);
        this.emoji = emoji;
    }

    public String obtenerEmoji() { 
        return emoji;
    }
}