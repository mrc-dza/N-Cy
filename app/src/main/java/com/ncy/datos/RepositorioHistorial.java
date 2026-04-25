package com.ncy.datos;

import android.content.Context;
import android.content.SharedPreferences;
import com.ncy.utilidades.Constantes;
import java.util.ArrayList;
import java.util.List;

public class RepositorioHistorial {

    private static final String PREFS            = "ncy_historial";
    private static final String CLAVE_TOTAL_TEMP = "total_temp";
    private static final String CLAVE_TOTAL_FIJO = "total_fijo";
    private static final String CLAVE_TEMP       = "temp_";
    private static final String CLAVE_FIJO       = "fijo_";

    private final SharedPreferences prefs;
    
    public RepositorioHistorial(Context contexto) {
        this.prefs = contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void guardar(List<String> temporales, List<String> fijados) {
        SharedPreferences.Editor editor = prefs.edit();
        guardarLista(editor, CLAVE_TOTAL_TEMP, CLAVE_TEMP, temporales, Constantes.MAX_TEMPORALES);
        guardarLista(editor, CLAVE_TOTAL_FIJO, CLAVE_FIJO, fijados, Constantes.MAX_FIJADOS);
        
        editor.apply();
    }

    public List<String> cargarTemporales() {
        return cargarLista(CLAVE_TOTAL_TEMP, CLAVE_TEMP);
    }

    public List<String> cargarFijados() {
        return cargarLista(CLAVE_TOTAL_FIJO, CLAVE_FIJO);
    }

    private void guardarLista(SharedPreferences.Editor editor, String claveTotal, 
                              String prefijo, List<String> lista, int maxItems) {
        
        editor.putInt(claveTotal, lista.size());
        
        for (int i = 0; i < lista.size(); i++) {
            editor.putString(prefijo + i, lista.get(i));
        }
        
        for (int i = lista.size(); i < maxItems; i++) {
            editor.remove(prefijo + i);
        }
    }

    private List<String> cargarLista(String claveTotal, String prefijoClave) {
        List<String> lista = new ArrayList<>();
        int total = prefs.getInt(claveTotal, 0);
        
        for (int i = 0; i < total; i++) {
            String item = prefs.getString(prefijoClave + i, null);
            if (item != null) {
                lista.add(item);
            }
        }
        return lista;
    }
}