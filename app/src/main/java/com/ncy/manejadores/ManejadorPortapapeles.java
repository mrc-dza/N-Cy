package com.ncy.manejadores;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputConnection;
import com.ncy.utilidades.Constantes;

public class ManejadorPortapapeles {

    private final ClipboardManager  clipboardManager;
    private final Context           contexto;
    private final GestorPortapapeles gestor;
    private InputConnection         conexion;

    public ManejadorPortapapeles(Context contexto,
                                  GestorPortapapeles gestor,
                                  ClipboardManager clipboardManager) {
        this.contexto         = contexto;
        this.gestor           = gestor;
        this.clipboardManager = clipboardManager;
    }

    public void establecerConexion(InputConnection conexion) {
        this.conexion = conexion;
    }

    public GestorPortapapeles obtenerGestor() { 
        return gestor;
    }

    public static boolean esCodigo(int codigo) {
        return switch (codigo) {
            case Constantes.CODIGO_COPIAR,
                 Constantes.CODIGO_PEGAR,
                 Constantes.CODIGO_SELECCIONAR_TODO,
                 Constantes.CODIGO_CORTAR,
                 Constantes.CODIGO_PEGAR_TEXTO_PLANO -> true;
            default -> false;
        };
    }

    public void procesarAccion(int codigo) {
        if (conexion == null) return;
        switch (codigo) {
            case Constantes.CODIGO_COPIAR -> copiar();
            case Constantes.CODIGO_PEGAR -> pegar();
            case Constantes.CODIGO_SELECCIONAR_TODO -> conexion.performContextMenuAction(android.R.id.selectAll);
            case Constantes.CODIGO_CORTAR -> conexion.performContextMenuAction(android.R.id.cut);
            case Constantes.CODIGO_PEGAR_TEXTO_PLANO -> pegarTextoPlano();
        }
    }

    public void pegarTexto(String texto) {
        if (conexion == null || texto == null) return;
        conexion.commitText(texto, 1);
    }

    private void copiar() {
        if (conexion == null) return;
        CharSequence sel = conexion.getSelectedText(0);
        if (sel != null && sel.length() > 0) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Copiado N-Cy", sel));
        }
    }

    private void pegar() {
        if (conexion == null || !clipboardManager.hasPrimaryClip()) return;
        ClipData clip = clipboardManager.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            CharSequence texto = clip.getItemAt(0).coerceToText(contexto);
            if (texto != null) conexion.commitText(texto, 1);
        }
    }

    private void pegarTextoPlano() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            conexion.performContextMenuAction(android.R.id.pasteAsPlainText);
        } else {
            pegar();
        }
    }
}