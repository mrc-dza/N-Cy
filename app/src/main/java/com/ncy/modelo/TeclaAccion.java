package com.ncy.modelo;

import android.view.inputmethod.EditorInfo;
import com.ncy.estado.EstadoTeclado;
import com.ncy.utilidades.Constantes;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

public class TeclaAccion extends Tecla {

    private InfoVisual infoCache;
    private int colorFondoCache;
    private boolean cacheValido = false;
    private final String etiqueta;

    public TeclaAccion(int codigo, String etiqueta) {
        super(codigo);
        this.etiqueta = etiqueta;
    }

    public String obtenerEtiqueta() {
        return etiqueta;
    }

    @Override
    public InfoVisual obtenerInfoVisual(EstadoTeclado estado) {
        TemaVisual tema = GestorTema.getInstance().obtenerTemaActivo();
        
        int colorFondo = (codigo == Constantes.CODIGO_ENTER) ? 
                         tema.getColorTeclaEnter() : tema.getColorTeclaAccion();

        if (!cacheValido || colorFondoCache != colorFondo) {
            if (codigo == Constantes.CODIGO_ENTER && estado != null) {
                int accion = estado.obtenerAccionIme();
                if (!estado.isMultilinea()
                        && accion != EditorInfo.IME_ACTION_NONE
                        && accion != EditorInfo.IME_ACTION_UNSPECIFIED) {
                    infoCache = resolverEnterDinamico(accion, tema);
                } else {
                    infoCache = new InfoVisual(tema.getColorTeclaEnter(), "↵", false, obtenerIconoResId());
                }
            } else {
                boolean textoLargo = etiqueta.length() > 3;
                infoCache = new InfoVisual(tema.getColorTeclaAccion(), etiqueta, textoLargo, obtenerIconoResId());
            }
            colorFondoCache = colorFondo;
            cacheValido = true;
        }
        return infoCache;
    }

    private InfoVisual resolverEnterDinamico(int accion, TemaVisual tema) {
        switch (accion) {
            case EditorInfo.IME_ACTION_SEARCH: return new InfoVisual(tema.getColorTeclaEnter(), "Buscar", true, Constantes.ICONO_IME_SEARCH);
            case EditorInfo.IME_ACTION_SEND:   return new InfoVisual(tema.getColorTeclaEnter(), "Env.",   true, Constantes.ICONO_IME_SEND);
            case EditorInfo.IME_ACTION_NEXT:   return new InfoVisual(tema.getColorTeclaEnter(), "Sig.",   true, Constantes.ICONO_IME_NEXT);
            case EditorInfo.IME_ACTION_DONE:   return new InfoVisual(tema.getColorTeclaEnter(), "Listo",  true, Constantes.ICONO_IME_DONE);
            case EditorInfo.IME_ACTION_GO:     return new InfoVisual(tema.getColorTeclaEnter(), "Ir",     true, Constantes.ICONO_IME_GO);
            default:                           return new InfoVisual(tema.getColorTeclaEnter(), "↵",      false, 0);
        }
    }

    public void invalidarCache() {
        cacheValido = false;
    }
}