package com.ncy.interfaz.teclas;

import android.content.Context;
import com.ncy.R; // <-- IMPORTANTE: Importamos la clase R
import com.ncy.utilidades.Constantes;

public class ResolvedorIconoIme {

    private final int idSearch;
    private final int idSend;
    private final int idNext;
    private final int idDone;
    private final int idGo;


    public ResolvedorIconoIme() {
        idSearch = R.drawable.ic_search;
        idSend   = R.drawable.ic_send;
        idNext   = R.drawable.ic_next;
        idDone   = R.drawable.ic_done;
        idGo     = R.drawable.ic_go;
    }

    public int resolver(int iconoResId) {
        switch (iconoResId) {
            case Constantes.ICONO_IME_SEARCH: return idSearch;
            case Constantes.ICONO_IME_SEND:   return idSend;
            case Constantes.ICONO_IME_NEXT:   return idNext;
            case Constantes.ICONO_IME_DONE:   return idDone;
            case Constantes.ICONO_IME_GO:     return idGo;
            default:                          return iconoResId;
        }
    }
}