package com.ncy.utilidades;

import android.content.Context;

public class AndroidKeyLabelProvider implements KeyLabelProvider {
    
    private final Context contexto;

    public AndroidKeyLabelProvider(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    public String obtenerEtiqueta(String aliasStringXml, String etiquetaPorDefecto) {
        int resId = contexto.getResources().getIdentifier(aliasStringXml, "string", contexto.getPackageName());
        if (resId != 0) {
            return contexto.getString(resId);
        }
        return etiquetaPorDefecto;
    }
}