package com.ncy.datos;

public interface AjustesRepository {
    boolean leerVibracion();
    void guardarVibracion(boolean valor);
    
    int leerAlturaTeclado();
    void guardarAlturaTeclado(int altura);
    
    boolean leerMostrarBarra();
    void guardarMostrarBarra(boolean mostrar);
    
    boolean leerAutoMayusculas();
    void guardarAutoMayusculas(boolean auto);
    
    // Simplificamos el listener a un simple Runnable nativo de Java
    void setOyenteCambiosVisuales(Runnable oyente);
}