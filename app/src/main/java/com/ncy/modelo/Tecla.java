package com.ncy.modelo;

import com.ncy.estado.EstadoTeclado;

public abstract class Tecla {

    private int x, y, ancho, alto;
    private float peso = 1f;
    private float multiplicadorAlto = 1f;

    protected int codigo;
    private int iconoResId = 0;

    private Tecla[]   gestos       = new Tecla[9];
    private boolean[] gestosOcultos = new boolean[9];

    private boolean estiloTransparente = false;

    public Tecla(int codigo) {
        this.codigo    = codigo;
        this.gestos[0] = this;
    }

    public boolean isEstiloTransparente()                      { return estiloTransparente; }
    public void setEstiloTransparente(boolean transparente)    { this.estiloTransparente = transparente; }

    public int obtenerX()     { return x; }
    public int obtenerY()     { return y; }
    public int obtenerAncho() { return ancho; }
    public int obtenerAlto()  { return alto; }

    public void aplicarGeometria(int x, int y, int ancho, int alto) {
        this.x     = x;
        this.y     = y;
        this.ancho = ancho;
        this.alto  = alto;
    }

    public float obtenerPeso()              { return peso; }
    public float obtenerMultiplicadorAlto() { return multiplicadorAlto; }

    public void fijarPeso(float peso)                           { this.peso = peso; }
    public void fijarMultiplicadorAlto(float multiplicadorAlto) { this.multiplicadorAlto = multiplicadorAlto; }

    public int obtenerIconoResId()       { return iconoResId; }
    public void setIconoResId(int resId) { this.iconoResId = resId; }

    public void establecerGesto(int indice, Tecla teclaGesto) {
        establecerGesto(indice, teclaGesto, false);
    }

    public void establecerGesto(int indice, Tecla teclaGesto, boolean oculto) {
        if (indice >= 1 && indice <= 8) {
            this.gestos[indice]       = teclaGesto;
            this.gestosOcultos[indice] = oculto;
        }
    }

    public boolean isGestoOculto(int indice) {
        if (indice >= 1 && indice <= 8) return gestosOcultos[indice];
        return false;
    }

    public Tecla obtenerGesto(int indice) {
        if (indice < 0 || indice > 8 || gestos[indice] == null) return gestos[0];
        return gestos[indice];
    }

    public int obtenerCodigo() { return codigo; }

    public abstract InfoVisual obtenerInfoVisual(EstadoTeclado estado);

    public boolean contiene(int posX, int posY) {
        return posX >= x && posX < x + ancho
            && posY >= y && posY < y + alto;
    }
}
