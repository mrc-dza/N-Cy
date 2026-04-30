package com.ncy.utilidades;

import android.content.Context;
import com.ncy.R; 
import com.ncy.modelo.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FabricaTeclas {
    private static final Map<String, Function<Context, Tecla>> REGISTRO;

    static {
        Map<String, Function<Context, Tecla>> mapaTemporal = new java.util.HashMap<>();

        mapaTemporal.put("copy",   ctx -> con(new TeclaAccion(Constantes.CODIGO_COPIAR,          "Copiar"),       R.drawable.ic_copiar));
        mapaTemporal.put("paste",  ctx -> con(new TeclaAccion(Constantes.CODIGO_PEGAR,           "Pegar"),        R.drawable.ic_pegar));
        mapaTemporal.put("clear",  ctx -> con(new TeclaAccion(Constantes.CODIGO_LIMPIAR,         "Limpiar"),      R.drawable.ic_limpiar));
        mapaTemporal.put("space",  ctx -> con(new TeclaAccion(Constantes.CODIGO_ESPACIO,         "ESPACIO"),      R.drawable.ic_space));
        mapaTemporal.put("select_all", ctx -> con(new TeclaAccion(Constantes.CODIGO_SELECCIONAR_TODO, "Sel"),     R.drawable.ic_select_all));
        mapaTemporal.put("backspace", ctx -> con(new TeclaAccion(Constantes.CODIGO_BORRAR, "⌫"), R.drawable.ic_backspace));
        mapaTemporal.put("enter",  ctx -> con(new TeclaAccion(Constantes.CODIGO_ENTER,           "↵"),           R.drawable.ic_enter));

        mapaTemporal.put("change_method_prev", ctx -> con(new TeclaAccion(Constantes.CODIGO_CAMBIAR_TECLADO, "🌐"), R.drawable.ic_change_method_prev));
        mapaTemporal.put("change_method",      ctx -> con(new TeclaAccion(Constantes.CODIGO_ELEGIR_TECLADO,  "⌨️"), R.drawable.ic_teclado));

        mapaTemporal.put("sym", ctx -> con(new TeclaAccion(Constantes.CODIGO_IR_A_SIMBOLOS, "?123"), 0));
        mapaTemporal.put("abc", ctx -> con(new TeclaAccion(Constantes.CODIGO_IR_A_LETRAS,   "ABC"),  0));
        mapaTemporal.put("portapapeles", ctx -> con(new TeclaAccion(Constantes.CODIGO_IR_A_PORTAPAPELES, "📋"), R.drawable.ic_portapapeles));
        mapaTemporal.put("emoji", ctx -> new TeclaAccion(Constantes.CODIGO_IR_A_EMOJIS, "😀"));
        mapaTemporal.put("cambiar_barra", ctx -> con(new TeclaAccion(Constantes.CODIGO_CAMBIAR_BARRA, ""), 0));

        mapaTemporal.put("shift", ctx -> con(new TeclaModificador(Constantes.CODIGO_SHIFT, "⬆"), R.drawable.ic_shift));
        mapaTemporal.put("ctrl",  ctx -> new TeclaModificador(Constantes.CODIGO_CTRL, "Ctrl"));
        mapaTemporal.put("alt",   ctx -> new TeclaModificador(Constantes.CODIGO_ALT,  "Alt"));

        mapaTemporal.put("undo", ctx -> con(new TeclaAccion(Constantes.CODIGO_UNDO, "↶"), R.drawable.ic_undo));
        mapaTemporal.put("redo", ctx -> con(new TeclaAccion(Constantes.CODIGO_REDO, "↷"), R.drawable.ic_redo));

        mapaTemporal.put("tab", ctx -> con(new TeclaAccion(Constantes.CODIGO_TAB, "Tab"), R.drawable.ic_tab));
        mapaTemporal.put("cut", ctx -> con(new TeclaAccion(Constantes.CODIGO_CORTAR, "Cortar"), R.drawable.ic_cut));
        mapaTemporal.put("paste_plain", ctx -> new TeclaAccion(Constantes.CODIGO_PEGAR_TEXTO_PLANO, "Pegar Limpio"));

        mapaTemporal.put("esc",    ctx -> con(new TeclaAccion(Constantes.CODIGO_ESC,    "esc"), 0));
        mapaTemporal.put("up",    ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_ARRIBA,    "↑"), R.drawable.ic_up));
        mapaTemporal.put("down",  ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_ABAJO,     "↓"), R.drawable.ic_down));
        mapaTemporal.put("left",  ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_IZQUIERDA, "←"), R.drawable.ic_left));
        mapaTemporal.put("right", ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_DERECHA,   "→"), R.drawable.ic_right));
        mapaTemporal.put("page_up",   ctx -> con(new TeclaAccion(Constantes.CODIGO_REPAG, "⇞"),R.drawable.page_up ));
        mapaTemporal.put("page_down", ctx -> con(new TeclaAccion(Constantes.CODIGO_AVPAG, "⇟"),R.drawable.page_dow));

        mapaTemporal.put("home",   ctx -> new TeclaAccion(Constantes.CODIGO_HOME,         "Inicio"));
        mapaTemporal.put("end",    ctx -> new TeclaAccion(Constantes.CODIGO_END,          "Fin"));
        mapaTemporal.put("insert", ctx -> new TeclaAccion(Constantes.CODIGO_INSERT,       "Ins"));
        mapaTemporal.put("scroll_lock", ctx -> new TeclaAccion(Constantes.CODIGO_SCROLL_LOCK, "ScrLk"));
        mapaTemporal.put("supr",    ctx -> new TeclaAccion(Constantes.CODIGO_SUPRIMIR, "Supr"));
    
        mapaTemporal.put("cursor_up",    ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_ARRIBA,    "↑"), R.drawable.ic_up));
        mapaTemporal.put("cursor_down",  ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_ABAJO,     "↓"), R.drawable.ic_down));
        mapaTemporal.put("cursor_left",  ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_IZQUIERDA, "←"), R.drawable.ic_left));
        mapaTemporal.put("cursor_right", ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_DERECHA,   "→"), R.drawable.ic_right));
        
        for (int i = 1; i <= 12; i++) {
            final int n = i;
            mapaTemporal.put("f" + n, ctx -> new TeclaAccion(Constantes.CODIGO_F1 - (n - 1), "F" + n));
        }

        REGISTRO = java.util.Collections.unmodifiableMap(mapaTemporal);
    }

    
    public static Tecla crearDesdeAlias(String alias, Context contexto) {
        if (alias == null || alias.isEmpty()) {
            return new TeclaCaracter(32, " ");
        }
        Function<Context, Tecla> creador = REGISTRO.get(alias.toLowerCase());
        if (creador != null) {
            return creador.apply(contexto);
        }
        return new TeclaCaracter((int) alias.charAt(0), alias);
    }

    private static Tecla con(Tecla tecla, int resId) {
        if (resId != 0) tecla.setIconoResId(resId);
        return tecla;
    }
}