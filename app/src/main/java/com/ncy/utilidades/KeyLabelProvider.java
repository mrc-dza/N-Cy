package com.ncy.utilidades;

public interface KeyLabelProvider {
    /**
     * Obtiene una etiqueta traducida según su alias en strings.xml.
     * Si no existe, devuelve la etiqueta por defecto.
     */
    String obtenerEtiqueta(String aliasStringXml, String etiquetaPorDefecto);
}