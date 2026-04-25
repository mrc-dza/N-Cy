package com.ncy.utilidades;

import android.content.Context;
import com.ncy.R; 
import com.ncy.modelo.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FabricaTeclas {

    private static final Map<String, Function<Context, Tecla>> REGISTRO = new HashMap<>();

    static {

        // --- Acciones básicas ---
        reg("copy",   ctx -> con(new TeclaAccion(Constantes.CODIGO_COPIAR,          "Copiar"),       R.drawable.ic_copiar));
        reg("paste",  ctx -> con(new TeclaAccion(Constantes.CODIGO_PEGAR,           "Pegar"),        R.drawable.ic_pegar));
        reg("clear",  ctx -> con(new TeclaAccion(Constantes.CODIGO_LIMPIAR,         "Limpiar"),      R.drawable.ic_limpiar));
        reg("space",  ctx -> con(new TeclaAccion(Constantes.CODIGO_ESPACIO,         "ESPACIO"),      R.drawable.ic_space));
        reg("select_all", ctx -> con(new TeclaAccion(Constantes.CODIGO_SELECCIONAR_TODO, "Sel"),     R.drawable.ic_select_all));
        reg("backspace", ctx -> con(new TeclaAccion(Constantes.CODIGO_BORRAR, "⌫"), R.drawable.ic_backspace));
        reg("enter",  ctx -> con(new TeclaAccion(Constantes.CODIGO_ENTER,           "↵"),           R.drawable.ic_enter));

        // --- Cambio de teclado ---
        reg("change_method_prev", ctx -> con(new TeclaAccion(Constantes.CODIGO_CAMBIAR_TECLADO, "🌐"), R.drawable.ic_change_method_prev));
        reg("change_method",      ctx -> con(new TeclaAccion(Constantes.CODIGO_ELEGIR_TECLADO,  "⌨️"), R.drawable.ic_teclado));

        // --- Navegación de vistas ---
        reg("sym", ctx -> con(new TeclaAccion(Constantes.CODIGO_IR_A_SIMBOLOS, "?123"), 0));
        reg("abc", ctx -> con(new TeclaAccion(Constantes.CODIGO_IR_A_LETRAS,   "ABC"),  0));
        reg("portapapeles", ctx -> con(new TeclaAccion(Constantes.CODIGO_IR_A_PORTAPAPELES, "📋"), R.drawable.ic_portapapeles));
        reg("emoji", ctx -> new TeclaAccion(Constantes.CODIGO_IR_A_EMOJIS, "😀"));
        reg("cambiar_barra", ctx -> con(new TeclaAccion(Constantes.CODIGO_CAMBIAR_BARRA, ""), 0));

        // --- Modificadores ---
        reg("shift", ctx -> con(new TeclaModificador(Constantes.CODIGO_SHIFT, "⬆"), R.drawable.ic_shift));
        reg("ctrl",  ctx -> new TeclaModificador(Constantes.CODIGO_CTRL, "Ctrl"));
        reg("alt",   ctx -> new TeclaModificador(Constantes.CODIGO_ALT,  "Alt"));

        // --- Historial ---
        reg("undo", ctx -> con(new TeclaAccion(Constantes.CODIGO_UNDO, "↶"), R.drawable.ic_undo));
        reg("redo", ctx -> con(new TeclaAccion(Constantes.CODIGO_REDO, "↷"), R.drawable.ic_redo));

        // --- Edición básica ---
        reg("tab", ctx -> con(new TeclaAccion(Constantes.CODIGO_TAB, "Tab"), R.drawable.ic_tab));
        reg("cut", ctx -> con(new TeclaAccion(Constantes.CODIGO_CORTAR, "Cortar"), R.drawable.ic_cut));
        
        // REFACTORIZACIÓN: Se eliminó el alias duplicado "pasteasplaintext"
        reg("paste_plain",      ctx -> new TeclaAccion(Constantes.CODIGO_PEGAR_TEXTO_PLANO, "Pegar Limpio"));

        // --- Flechas discretas (Un solo evento por pulsación, procesado por SimuladorHardware) ---
        reg("esc",    ctx -> con(new TeclaAccion(Constantes.CODIGO_ESC,    "esc"), 0));
        reg("up",    ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_ARRIBA,    "↑"), R.drawable.ic_up));
        reg("down",  ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_ABAJO,     "↓"), R.drawable.ic_down));
        reg("left",  ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_IZQUIERDA, "←"), R.drawable.ic_left));
        reg("right", ctx -> con(new TeclaAccion(Constantes.CODIGO_FLECHA_DERECHA,   "→"), R.drawable.ic_right));
        reg("page_up",   ctx -> con(new TeclaAccion(Constantes.CODIGO_REPAG, "⇞"),R.drawable.page_up ));
        reg("page_down", ctx -> con(new TeclaAccion(Constantes.CODIGO_AVPAG, "⇟"),R.drawable.page_dow));

        // --- Navegación PC ---
        reg("home",   ctx -> new TeclaAccion(Constantes.CODIGO_HOME,         "Inicio"));
        reg("end",    ctx -> new TeclaAccion(Constantes.CODIGO_END,          "Fin"));
        reg("insert", ctx -> new TeclaAccion(Constantes.CODIGO_INSERT,       "Ins"));
        reg("scroll_lock", ctx -> new TeclaAccion(Constantes.CODIGO_SCROLL_LOCK, "ScrLk"));
        reg("supr",    ctx -> new TeclaAccion(Constantes.CODIGO_SUPRIMIR, "Supr"));
    
        // --- Slider / trackpad continuo (Activan MotorSlider para repetición rápida y fluida) ---
        reg("cursor_up",    ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_ARRIBA,    "↑"), R.drawable.ic_up));
        reg("cursor_down",  ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_ABAJO,     "↓"), R.drawable.ic_down));
        reg("cursor_left",  ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_IZQUIERDA, "←"), R.drawable.ic_left));
        reg("cursor_right", ctx -> con(new TeclaAccion(Constantes.CODIGO_CURSOR_DERECHA,   "→"), R.drawable.ic_right));
        

        
        // --- Teclas de función ---
        // Reemplaza los 12 reg("f1"...) hasta reg("f12"...)
        for (int i = 1; i <= 12; i++) {
            final int n = i;
  
        reg("f" + n, ctx -> new TeclaAccion(Constantes.CODIGO_F1 - (n - 1), "F" + n));
        }
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

    public static void registrar(String alias, Function<Context, Tecla> creador) {
        REGISTRO.put(alias.toLowerCase(), creador);
    }

    private static void reg(String alias, Function<Context, Tecla> creador) {
        REGISTRO.put(alias, creador);
    }

    private static Tecla con(Tecla tecla, int resId) {
        if (resId != 0) tecla.setIconoResId(resId);
        return tecla;
    }
}