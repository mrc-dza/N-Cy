package com.ncy.estado;

public enum ModoTeclado {
    LETRAS("qwerty"),
    SIMBOLOS("simbolos"),
    PORTAPAPELES("portapapeles"),
    EMOJIS("emojis");

    public final String archivoXml;

    ModoTeclado(String archivoXml) {
        this.archivoXml = archivoXml;
    }
}