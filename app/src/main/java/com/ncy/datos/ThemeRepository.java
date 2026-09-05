package com.ncy.datos;

public interface ThemeRepository {
    String leerIdTemaTeclado();
    void guardarIdTemaTeclado(String idTema);
    
    String leerIdTemaUI();
    void guardarIdTemaUI(String idTema);
}