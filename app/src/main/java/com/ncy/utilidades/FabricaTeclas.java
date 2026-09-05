package com.ncy.utilidades;

import android.content.Context;
import com.ncy.R; 
import com.ncy.modelo.*;

import java.util.HashMap;
import java.util.Map;

public class FabricaTeclas {
    
    // Interfaz funcional para crear teclas inyectando el proveedor de traducciones
    private interface CreadorTecla {
        Tecla crear(KeyLabelProvider p);
    }

    private static final Map<String, CreadorTecla> REGISTRO = new HashMap<>();

    static {
        REGISTRO.put("copy",   p -> con(new TeclaAccion(Constantes.CODIGO_COPIAR,          p.obtenerEtiqueta("key_copy", "Copiar")),       R.drawable.ic_copiar));
        REGISTRO.put("paste",  p -> con(new TeclaAccion(Constantes.CODIGO_PEGAR,           p.obtenerEtiqueta("key_paste", "Pegar")),        R.drawable.ic_pegar));
        REGISTRO.put("clear",  p -> con(new TeclaAccion(Constantes.CODIGO_LIMPIAR,         p.obtenerEtiqueta("key_clear", "Limpiar")),      R.drawable.ic_limpiar));
        REGISTRO.put("space",  p -> con(new TeclaAccion(Constantes.CODIGO_ESPACIO,         p.obtenerEtiqueta("key_space", "ESPACIO")),      R.drawable.ic_space));
        REGISTRO.put("select_all", p -> con(new TeclaAccion(Constantes.CODIGO_SELECCIONAR_TODO, p.obtenerEtiqueta("key_select_all", "Sel")),     R.drawable.ic_select_all));
        REGISTRO.put("backspace", p -> con(new TeclaAccion(Constantes.CODIGO_BORRAR, "⌫"), R.drawable.ic_backspace));
        REGISTRO.put("enter",  p -> con(new TeclaAccion(Constantes.CODIGO_ENTER,           p.obtenerEtiqueta("key_enter", "↵")),           R.drawable.ic_enter));

        REGISTRO.put("change_method_prev", p -> con(new TeclaAccion(Constantes.CODIGO_CAMBIAR_TECLADO, "🌐"), R.drawable.ic_change_method_prev));
        REGISTRO.put("change_method",      p -> con(new TeclaAccion(Constantes.CODIGO_ELEGIR_TECLADO,  "⌨️"), R.drawable.ic_teclado));

        REGISTRO.put("sym", p -> con(new TeclaAccion(Constantes.CODIGO_IR_A_SIMBOLOS, "?123"), 0));
        REGISTRO.put("abc", p -> con(new TeclaAccion(Constantes.CODIGO_IR_A_LETRAS,   "ABC"),  0));
        REGISTRO.put("portapapeles", p -> con(new TeclaAccion(Constantes.CODIGO_IR_A_PORTAPAPELES, "📋"), R.drawable.ic_portapapeles));
        REGISTRO.put("emoji", p -> new TeclaAccion(Constantes.CODIGO_IR_A_EMOJIS, "😀"));
        REGISTRO.put("cambiar_barra", p -> con(new TeclaAccion(Constantes.CODIGO_CAMBIAR_BARRA, ""), 0));

        REGISTRO.put("shift", p -> con(new TeclaModificador(Constantes.CODIGO_SHIFT, "⬆"), R.drawable.ic_shift));
        REGISTRO.put("ctrl",  p -> new TeclaModificador(Constantes.CODIGO_CTRL, "Ctrl"));
        REGISTRO.put("alt",   p -> new TeclaModificador(Constantes.CODIGO_ALT,  "Alt"));

        REGISTRO.put("undo", p -> con(new TeclaAccion(Constantes.CODIGO_UNDO, "↶"), R.drawable.ic_undo));
        REGISTRO.put("redo", p -> con(new TeclaAccion(Constantes.CODIGO_REDO, "↷"), R.drawable.ic_redo));

        REGISTRO.put("tab", p -> con(new TeclaAccion(Constantes.CODIGO_TAB, p.obtenerEtiqueta("key_tab", "Tab")), R.drawable.ic_tab));
        REGISTRO.put("cut", p -> con(new TeclaAccion(Constantes.CODIGO_CORTAR, p.obtenerEtiqueta("key_cut", "Cortar")), R.drawable.ic_cut));
        REGISTRO.put("paste_plain", p -> new TeclaAccion(Constantes.CODIGO_PEGAR_TEXTO_PLANO, p.obtenerEtiqueta("key_paste_plain", "Pegar Limpio")));

        REGISTRO.put("esc",    p -> con(new TeclaAccion(Constantes.CODIGO_ESC,    "esc"), 0));
        REGISTRO.put("up",    p -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_ARRIBA,    "↑"), R.drawable.ic_up));
        REGISTRO.put("down",  p -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_ABAJO,     "↓"), R.drawable.ic_down));
        REGISTRO.put("left",  p -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_IZQUIERDA, "←"), R.drawable.ic_left));
        REGISTRO.put("right", p -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_DERECHA,   "→"), R.drawable.ic_right));
        REGISTRO.put("page_up",   p -> con(new TeclaAccion(Constantes.CODIGO_REPAG, "⇞"),R.drawable.page_up ));
        REGISTRO.put("page_down", p -> con(new TeclaAccion(Constantes.CODIGO_AVPAG, "⇟"),R.drawable.page_dow));

        REGISTRO.put("home",   p -> new TeclaAccion(Constantes.CODIGO_HOME,         p.obtenerEtiqueta("key_home", "Inicio")));
        REGISTRO.put("end",    p -> new TeclaAccion(Constantes.CODIGO_END,          p.obtenerEtiqueta("key_end", "Fin")));
        REGISTRO.put("insert", p -> new TeclaAccion(Constantes.CODIGO_INSERT,       p.obtenerEtiqueta("key_insert", "Ins")));
        REGISTRO.put("scroll_lock", p -> new TeclaAccion(Constantes.CODIGO_SCROLL_LOCK, p.obtenerEtiqueta("key_scroll_lock", "ScrLk")));
        REGISTRO.put("supr",    p -> new TeclaAccion(Constantes.CODIGO_SUPRIMIR, p.obtenerEtiqueta("key_delete", "Supr")));
    
        REGISTRO.put("cursor_up",    p -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_ARRIBA,    "↑"), R.drawable.ic_up));
        REGISTRO.put("cursor_down",  p -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_ABAJO,     "↓"), R.drawable.ic_down));
        REGISTRO.put("cursor_left",  p -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_IZQUIERDA, "←"), R.drawable.ic_left));
        REGISTRO.put("cursor_right", p -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_DERECHA,   "→"), R.drawable.ic_right));
        
        for (int i = 1; i <= 12; i++) {
            final int n = i;
            REGISTRO.put("f" + n, p -> new TeclaAccion(Constantes.CODIGO_F1 - (n - 1), "F" + n));
        }
    }

    public static Tecla crearDesdeAlias(String alias, Context contexto) {
        KeyLabelProvider provider = new AndroidKeyLabelProvider(contexto);
        if (alias == null || alias.isEmpty()) {
            return new TeclaCaracter(32, " ");
        }
        CreadorTecla creador = REGISTRO.get(alias.toLowerCase());
        if (creador != null) {
            return creador.crear(provider);
        }
        return new TeclaCaracter((int) alias.charAt(0), alias);
    }

    private static Tecla con(Tecla tecla, int resId) {
        if (resId != 0) tecla.setIconoResId(resId);
        return tecla;
    }
}