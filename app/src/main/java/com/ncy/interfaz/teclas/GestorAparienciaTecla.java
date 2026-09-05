package com.ncy.interfaz.teclas;

import android.content.Context;
import android.view.inputmethod.EditorInfo;
import com.ncy.estado.EstadoTeclado;
import com.ncy.modelo.*;
import com.ncy.utilidades.Constantes;
import com.ncy.utilidades.TemaVisual;
import com.ncy.utilidades.KeyLabelProvider;
import com.ncy.utilidades.AndroidKeyLabelProvider;

import java.util.IdentityHashMap;
import java.util.Map;

public class GestorAparienciaTecla {

    private final Map<Tecla, InfoVisual> cacheNormal = new IdentityHashMap<>();
    private final Map<Tecla, InfoVisual> cacheEspecial = new IdentityHashMap<>(); 
    private int accionImeEnterCache = -1;
    private final KeyLabelProvider provider;

    // NUEVO: Constructor para inicializar el proveedor de etiquetas
    public GestorAparienciaTecla(Context contexto) {
        this.provider = new AndroidKeyLabelProvider(contexto);
    }

    public void limpiarCache() {
        cacheNormal.clear();
        cacheEspecial.clear();
        accionImeEnterCache = -1;
    }

    public InfoVisual obtenerInfoVisual(Tecla tecla, EstadoTeclado estado, TemaVisual tema) {
        
        if (tecla instanceof TeclaCaracter tc) {
            boolean shiftActivo = estado != null && estado.isModificadorActivo(Constantes.CODIGO_SHIFT);
            Map<Tecla, InfoVisual> cache = shiftActivo ? cacheEspecial : cacheNormal;
            
            InfoVisual info = cache.get(tc);
            if (info == null) {
                String etiqueta = tc.obtenerEtiqueta();
                String texto = shiftActivo ? etiqueta.toUpperCase(java.util.Locale.getDefault()) : etiqueta.toLowerCase(java.util.Locale.getDefault());
                info = InfoVisual.of(tema.getColorTeclaNormal(), texto, false, tc.obtenerIconoResId());
                cache.put(tc, info);
            }
            return info;
        } 
        
        else if (tecla instanceof TeclaModificador tm) {
            boolean encendido = estado != null && estado.isModificadorActivo(tm.obtenerCodigo());
            Map<Tecla, InfoVisual> cache = encendido ? cacheEspecial : cacheNormal;
            
            InfoVisual info = cache.get(tm);
            if (info == null) {
                if (encendido) {
                    info = new InfoVisual(tema.getColorTeclaModActivo(), tm.obtenerEtiqueta(), true, tm.obtenerIconoResId(), null, tema.getColorTextoModActivo());
                } else {
                    info = InfoVisual.of(tema.getColorTextoModApagado(), tm.obtenerEtiqueta(), false, tm.obtenerIconoResId());
                }
                cache.put(tm, info);
            }
            return info;
        }
        
        else if (tecla instanceof TeclaAccion ta) {
            if (ta.obtenerCodigo() == Constantes.CODIGO_ENTER && estado != null) {
                int accion = estado.obtenerAccionIme();
                if (accionImeEnterCache != accion || !cacheNormal.containsKey(ta)) {
                    accionImeEnterCache = accion;
                    if (!estado.isMultilinea() && accion != EditorInfo.IME_ACTION_NONE && accion != EditorInfo.IME_ACTION_UNSPECIFIED) {
                        cacheNormal.put(ta, resolverEnterDinamico(accion, tema, ta.obtenerIconoResId()));
                    } else {
                        // ACTUALIZADO: Traduce el botón de Enter por defecto
                        cacheNormal.put(ta, InfoVisual.of(tema.getColorTeclaEnter(), provider.obtenerEtiqueta("key_enter", "↵"), false, ta.obtenerIconoResId()));
                    }
                }
                return cacheNormal.get(ta);
            }

            InfoVisual info = cacheNormal.get(ta);
            if (info == null) {
                boolean textoLargo = ta.obtenerEtiqueta().length() > 3;
                info = InfoVisual.of(tema.getColorTeclaAccion(), ta.obtenerEtiqueta(), textoLargo, ta.obtenerIconoResId());
                cacheNormal.put(ta, info);
            }
            return info;
        }
        
        else if (tecla instanceof TeclaEmoji te) {
            InfoVisual info = cacheNormal.get(te);
            if (info == null) {
                // Usamos ofExact para pasar el emoji completo a la pista del gesto
                info = InfoVisual.ofExact(tema.getColorTeclaNormal(), te.obtenerEmoji(), false, te.obtenerIconoResId(), te.obtenerEmoji());
                cacheNormal.put(te, info);
            }
            return info;
        }
        
        else if (tecla instanceof TeclaMacro tma) {
            InfoVisual info = cacheNormal.get(tma);
            if (info == null) {
                info = InfoVisual.of(tema.getColorTeclaMacro(), tma.obtenerEtiqueta(), true, tma.obtenerIconoResId());
                cacheNormal.put(tma, info);
            }
            return info;
        }
        
        return InfoVisual.of(tema.getColorTeclaNormal(), "", false, 0);
    }

    // ACTUALIZADO: Usa el provider para traducir las acciones del IME
    private InfoVisual resolverEnterDinamico(int accion, TemaVisual tema, int iconoResId) {
        int color = tema.getColorTeclaEnter();
        return switch (accion) {
            case EditorInfo.IME_ACTION_SEARCH -> InfoVisual.of(color, provider.obtenerEtiqueta("ime_search", "Buscar"), true, Constantes.ICONO_IME_SEARCH);
            case EditorInfo.IME_ACTION_SEND   -> InfoVisual.of(color, provider.obtenerEtiqueta("ime_send", "Env."),   true, Constantes.ICONO_IME_SEND);
            case EditorInfo.IME_ACTION_NEXT   -> InfoVisual.of(color, provider.obtenerEtiqueta("ime_next", "Sig."),   true, Constantes.ICONO_IME_NEXT);
            case EditorInfo.IME_ACTION_DONE   -> InfoVisual.of(color, provider.obtenerEtiqueta("ime_done", "Listo"),  true, Constantes.ICONO_IME_DONE);
            case EditorInfo.IME_ACTION_GO     -> InfoVisual.of(color, provider.obtenerEtiqueta("ime_go", "Ir"),     true, Constantes.ICONO_IME_GO);
            default                           -> InfoVisual.of(color, provider.obtenerEtiqueta("key_enter", "↵"),      false, iconoResId);
        };
    }
}