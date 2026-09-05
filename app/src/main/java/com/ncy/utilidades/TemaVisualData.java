package com.ncy.utilidades;

import android.graphics.Color;

public class TemaVisualData implements TemaVisual {

    private final int colorFondoGeneral, colorFondoBarra, colorTeclaNormal, colorTeclaAccion, 
                      colorTeclaEnter, colorTeclaMacro, colorTeclaModActivo;
    private final int colorTextoPrincipal, colorTextoEspecial, colorTextoModApagado, 
                      colorTextoModActivo, colorTextoVacio;
    private final int colorAcento, colorGestoActivo, colorPresionNormal, colorPresionFijado;
    private final int colorSeparador, colorTituloSeccion, colorIconoPin, colorIconoBorrar;
    private final int colorEtiquetaBarra, colorEtiquetaBarraPresionada, colorBordeTecla;
    
    private final float tamanioTextoTecla, tamanioTextoEspecial, tamanioTextoGesto, 
                        tamanioTextoGestoActivo, tamanioTextoLista, tamanioTextoSeccion, 
                        tamanioTextoVacio;
    private final float radioEsquinasTecla, factorOscurecimiento, anchoBordeTecla, 
                        separacionFilas, separacionColumnas;
    private final int alturaFilaBasePx;

    private TemaVisualData(Builder b) {
        this.colorFondoGeneral = b.colorFondoGeneral;
        this.colorFondoBarra = b.colorFondoBarra;
        this.colorTeclaNormal = b.colorTeclaNormal;
        this.colorTeclaAccion = b.colorTeclaAccion;
        this.colorTeclaEnter = b.colorTeclaEnter;
        this.colorTeclaMacro = b.colorTeclaMacro;
        this.colorTeclaModActivo = b.colorTeclaModActivo;
        this.colorTextoPrincipal = b.colorTextoPrincipal;
        this.colorTextoEspecial = b.colorTextoEspecial;
        this.colorTextoModApagado = b.colorTextoModApagado;
        this.colorTextoModActivo = b.colorTextoModActivo;
        this.colorTextoVacio = b.colorTextoVacio;
        this.colorAcento = b.colorAcento;
        this.colorGestoActivo = b.colorGestoActivo;
        this.colorPresionNormal = b.colorPresionNormal;
        this.colorPresionFijado = b.colorPresionFijado;
        this.colorSeparador = b.colorSeparador;
        this.colorTituloSeccion = b.colorTituloSeccion;
        this.colorIconoPin = b.colorIconoPin;
        this.colorIconoBorrar = b.colorIconoBorrar;
        this.colorEtiquetaBarra = b.colorEtiquetaBarra;
        this.colorEtiquetaBarraPresionada = b.colorEtiquetaBarraPresionada;
        this.colorBordeTecla = b.colorBordeTecla;
        this.tamanioTextoTecla = b.tamanioTextoTecla;
        this.tamanioTextoEspecial = b.tamanioTextoEspecial;
        this.tamanioTextoGesto = b.tamanioTextoGesto;
        this.tamanioTextoGestoActivo = b.tamanioTextoGestoActivo;
        this.tamanioTextoLista = b.tamanioTextoLista;
        this.tamanioTextoSeccion = b.tamanioTextoSeccion;
        this.tamanioTextoVacio = b.tamanioTextoVacio;
        this.radioEsquinasTecla = b.radioEsquinasTecla;
        this.factorOscurecimiento = b.factorOscurecimiento;
        this.anchoBordeTecla = b.anchoBordeTecla;
        this.separacionFilas = b.separacionFilas;
        this.separacionColumnas = b.separacionColumnas;
        this.alturaFilaBasePx = b.alturaFilaBasePx;
    }

    // Getters de la interfaz
    @Override public int getColorFondoGeneral() { return colorFondoGeneral; }
    @Override public int getColorFondoBarra() { return colorFondoBarra; }
    @Override public int getColorTeclaNormal() { return colorTeclaNormal; }
    @Override public int getColorTeclaAccion() { return colorTeclaAccion; }
    @Override public int getColorTeclaEnter() { return colorTeclaEnter; }
    @Override public int getColorTeclaMacro() { return colorTeclaMacro; }
    @Override public int getColorTeclaModActivo() { return colorTeclaModActivo; }
    @Override public int getColorTextoPrincipal() { return colorTextoPrincipal; }
    @Override public int getColorTextoEspecial() { return colorTextoEspecial; }
    @Override public int getColorTextoModApagado() { return colorTextoModApagado; }
    @Override public int getColorTextoModActivo() { return colorTextoModActivo; }
    @Override public int getColorTextoVacio() { return colorTextoVacio; }
    @Override public int getColorAcento() { return colorAcento; }
    @Override public int getColorGestoActivo() { return colorGestoActivo; }
    @Override public int getColorPresionNormal() { return colorPresionNormal; }
    @Override public int getColorPresionFijado() { return colorPresionFijado; }
    @Override public int getColorSeparador() { return colorSeparador; }
    @Override public int getColorTituloSeccion() { return colorTituloSeccion; }
    @Override public int getColorIconoPin() { return colorIconoPin; }
    @Override public int getColorIconoBorrar() { return colorIconoBorrar; }
    @Override public int getColorEtiquetaBarra() { return colorEtiquetaBarra; }
    @Override public int getColorEtiquetaBarraPresionada() { return colorEtiquetaBarraPresionada; }
    @Override public int getColorBordeTecla() { return colorBordeTecla; }
    @Override public float getTamanioTextoTecla() { return tamanioTextoTecla; }
    @Override public float getTamanioTextoEspecial() { return tamanioTextoEspecial; }
    @Override public float getTamanioTextoGesto() { return tamanioTextoGesto; }
    @Override public float getTamanioTextoGestoActivo() { return tamanioTextoGestoActivo; }
    @Override public float getTamanioTextoLista() { return tamanioTextoLista; }
    @Override public float getTamanioTextoSeccion() { return tamanioTextoSeccion; }
    @Override public float getTamanioTextoVacio() { return tamanioTextoVacio; }
    @Override public float getRadioEsquinasTecla() { return radioEsquinasTecla; }
    @Override public float getFactorOscurecimiento() { return factorOscurecimiento; }
    @Override public float getAnchoBordeTecla() { return anchoBordeTecla; }
    @Override public float getSeparacionFilas() { return separacionFilas; }
    @Override public float getSeparacionColumnas() { return separacionColumnas; }
    @Override public int getAlturaFilaBasePx() { return alturaFilaBasePx; }

    public static class Builder {
        // Valores por defecto (TemaOscuro)
        private int colorFondoGeneral = Color.rgb(30, 31, 33);
        private int colorFondoBarra = Color.rgb(30, 31, 33);
        private int colorTeclaNormal = Color.rgb(52, 53, 55);
        private int colorTeclaAccion = Color.rgb(69, 71, 70);
        private int colorTeclaEnter = Color.rgb(0, 120, 215);
        private int colorTeclaMacro = Color.rgb(0, 100, 0);
        private int colorTeclaModActivo = Color.rgb(39, 39, 39);
        private int colorTextoPrincipal = Color.rgb(255, 255, 255);
        private int colorTextoEspecial = Color.rgb(255, 255, 255);
        private int colorTextoModApagado = Color.rgb(69, 71, 70);
        private int colorTextoModActivo = Color.rgb(0, 120, 215);
        private int colorTextoVacio = Color.rgb(136, 136, 136);
        private int colorAcento = Color.rgb(0, 225, 255);
        private int colorGestoActivo = Color.rgb(255, 255, 255);
        private int colorPresionNormal = Color.rgb(60, 60, 60);
        private int colorPresionFijado = Color.rgb(60, 60, 60);
        private int colorSeparador = Color.rgb(60, 60, 60);
        private int colorTituloSeccion = Color.rgb(0, 225, 255);
        private int colorIconoPin = Color.rgb(0, 140, 255);
        private int colorIconoBorrar = Color.rgb(187, 43, 43);
        private int colorEtiquetaBarra = Color.rgb(255, 255, 255);
        private int colorEtiquetaBarraPresionada = Color.rgb(0, 120, 215);
        private int colorBordeTecla = Color.TRANSPARENT;
        
        private float tamanioTextoTecla = 16f;
        private float tamanioTextoEspecial = 13f;
        private float tamanioTextoGesto = 9f;
        private float tamanioTextoGestoActivo = 10f;
        private float tamanioTextoLista = 12f;
        private float tamanioTextoSeccion = 10f;
        private float tamanioTextoVacio = 11f;
        private float radioEsquinasTecla = 7f;
        private float factorOscurecimiento = 0.6f;
        private float anchoBordeTecla = 0f;
        private float separacionFilas = 6f;
        private float separacionColumnas = 5f;
        private int alturaFilaBasePx = 140;

        // Setters encadenables
        public Builder colorFondoGeneral(int c) { this.colorFondoGeneral = c; return this; }
        public Builder colorFondoBarra(int c) { this.colorFondoBarra = c; return this; }
        public Builder colorTeclaNormal(int c) { this.colorTeclaNormal = c; return this; }
        public Builder colorTeclaAccion(int c) { this.colorTeclaAccion = c; return this; }
        public Builder colorTeclaEnter(int c) { this.colorTeclaEnter = c; return this; }
        public Builder colorTeclaMacro(int c) { this.colorTeclaMacro = c; return this; }
        public Builder colorTeclaModActivo(int c) { this.colorTeclaModActivo = c; return this; }
        public Builder colorTextoPrincipal(int c) { this.colorTextoPrincipal = c; return this; }
        public Builder colorTextoEspecial(int c) { this.colorTextoEspecial = c; return this; }
        public Builder colorTextoModApagado(int c) { this.colorTextoModApagado = c; return this; }
        public Builder colorTextoModActivo(int c) { this.colorTextoModActivo = c; return this; }
        public Builder colorTextoVacio(int c) { this.colorTextoVacio = c; return this; }
        public Builder colorAcento(int c) { this.colorAcento = c; return this; }
        public Builder colorGestoActivo(int c) { this.colorGestoActivo = c; return this; }
        public Builder colorPresionNormal(int c) { this.colorPresionNormal = c; return this; }
        public Builder colorPresionFijado(int c) { this.colorPresionFijado = c; return this; }
        public Builder colorSeparador(int c) { this.colorSeparador = c; return this; }
        public Builder colorTituloSeccion(int c) { this.colorTituloSeccion = c; return this; }
        public Builder colorIconoPin(int c) { this.colorIconoPin = c; return this; }
        public Builder colorIconoBorrar(int c) { this.colorIconoBorrar = c; return this; }
        public Builder colorEtiquetaBarra(int c) { this.colorEtiquetaBarra = c; return this; }
        public Builder colorEtiquetaBarraPresionada(int c) { this.colorEtiquetaBarraPresionada = c; return this; }
        public Builder colorBordeTecla(int c) { this.colorBordeTecla = c; return this; }
        public Builder tamanioTextoTecla(float v) { this.tamanioTextoTecla = v; return this; }
        public Builder tamanioTextoEspecial(float v) { this.tamanioTextoEspecial = v; return this; }
        public Builder tamanioTextoGesto(float v) { this.tamanioTextoGesto = v; return this; }
        public Builder tamanioTextoGestoActivo(float v) { this.tamanioTextoGestoActivo = v; return this; }
        public Builder tamanioTextoLista(float v) { this.tamanioTextoLista = v; return this; }
        public Builder tamanioTextoSeccion(float v) { this.tamanioTextoSeccion = v; return this; }
        public Builder tamanioTextoVacio(float v) { this.tamanioTextoVacio = v; return this; }
        public Builder radioEsquinasTecla(float v) { this.radioEsquinasTecla = v; return this; }
        public Builder factorOscurecimiento(float v) { this.factorOscurecimiento = v; return this; }
        public Builder anchoBordeTecla(float v) { this.anchoBordeTecla = v; return this; }
        public Builder separacionFilas(float v) { this.separacionFilas = v; return this; }
        public Builder separacionColumnas(float v) { this.separacionColumnas = v; return this; }
        public Builder alturaFilaBasePx(int v) { this.alturaFilaBasePx = v; return this; }

        public TemaVisualData build() {
            return new TemaVisualData(this);
        }
    }
}