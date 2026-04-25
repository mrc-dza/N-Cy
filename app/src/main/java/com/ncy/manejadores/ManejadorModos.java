package com.ncy.manejadores;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.utilidades.Constantes;

public class ManejadorModos {

    private final EstadoTeclado estado;
    private final InputMethodService servicioIme;

    public ManejadorModos(EstadoTeclado estado, InputMethodService servicioIme) {
        this.estado      = estado;
        this.servicioIme = servicioIme;
    }

    public void procesarAccion(int codigo) {
        switch (codigo) {
            case Constantes.CODIGO_IR_A_SIMBOLOS     -> estado.establecerModo(ModoTeclado.SIMBOLOS);
            case Constantes.CODIGO_IR_A_LETRAS       -> estado.establecerModo(ModoTeclado.LETRAS);
            case Constantes.CODIGO_IR_A_PORTAPAPELES -> estado.establecerModo(ModoTeclado.PORTAPAPELES);
            case Constantes.CODIGO_IR_A_EMOJIS       -> estado.establecerModo(ModoTeclado.EMOJIS);
            case Constantes.CODIGO_CAMBIAR_TECLADO   -> cambiarAlAnteriorIme();
            case Constantes.CODIGO_ELEGIR_TECLADO    -> mostrarSelectorIme();
        }
    }

    public static boolean esCodigo(int codigo) {
        return switch (codigo) {
            case Constantes.CODIGO_IR_A_SIMBOLOS,
                 Constantes.CODIGO_IR_A_LETRAS,
                 Constantes.CODIGO_IR_A_PORTAPAPELES,
                 Constantes.CODIGO_IR_A_EMOJIS,
                 Constantes.CODIGO_CAMBIAR_TECLADO,
                 Constantes.CODIGO_ELEGIR_TECLADO -> true;
            default -> false;
        };
    }

    private void cambiarAlAnteriorIme() {
        boolean ok = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ok = servicioIme.switchToPreviousInputMethod();
        }
        if (!ok && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            servicioIme.switchToNextInputMethod(false);
        }
    }

    private void mostrarSelectorIme() {
        InputMethodManager imm = (InputMethodManager)
                servicioIme.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.showInputMethodPicker();
    }
}