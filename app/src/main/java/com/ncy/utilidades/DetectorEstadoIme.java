package com.ncy.utilidades;

import android.content.Context;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public final class DetectorEstadoIme {

    private DetectorEstadoIme() {}

    public static boolean estaHabilitado(Context contexto) {
        InputMethodManager imm = (InputMethodManager)
                contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return false;
        for (InputMethodInfo info : imm.getEnabledInputMethodList()) {
            if (info.getPackageName().equals(contexto.getPackageName())) return true;
        }
        return false;
    }

    public static boolean estaSeleccionado(Context contexto) {
        String actual = Settings.Secure.getString(
                contexto.getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
        return actual != null && actual.startsWith(contexto.getPackageName());
    }
}