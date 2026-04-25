package com.ncy.manejadores;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class EscuchadorPortapapelesSistema {

    private final Context contexto;
    private final ClipboardManager clipboardManager;
    private final GestorPortapapeles gestor;
    
    // AÑADIDO: Guardamos la referencia del listener para poder eliminarlo después
    private final ClipboardManager.OnPrimaryClipChangedListener listener;

    public EscuchadorPortapapelesSistema(Context contexto, GestorPortapapeles gestor) {
        this.contexto = contexto;
        this.gestor = gestor;
        this.clipboardManager = (ClipboardManager) contexto.getSystemService(Context.CLIPBOARD_SERVICE);
        
        // Asignamos la lógica a la variable
        this.listener = () -> {
            if (!clipboardManager.hasPrimaryClip()) return;
            ClipData clip = clipboardManager.getPrimaryClip();
            if (clip == null || clip.getItemCount() == 0) return;
            CharSequence texto = clip.getItemAt(0).coerceToText(contexto);
            if (texto != null && texto.length() > 0) {
                gestor.agregar(texto.toString());
            }
        };
        
        // Registramos el listener
        clipboardManager.addPrimaryClipChangedListener(this.listener);
    }

    // AÑADIDO: Método para limpiar la memoria cuando el servicio se destruye
    public void destruir() {
        if (clipboardManager != null && listener != null) {
            clipboardManager.removePrimaryClipChangedListener(listener);
        }
    }

    public ClipboardManager obtenerClipboardManager() {
        return clipboardManager;
    }
}