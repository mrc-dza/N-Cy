package com.ncy.estado;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import com.ncy.utilidades.Constantes;
import java.util.HashSet;
import java.util.Set;

public class EstadoTeclado {

    private ModoTeclado modoActual  = ModoTeclado.LETRAS;
    private EstadoShift estadoShift = EstadoShift.APAGADO;
    private boolean shiftEsManual   = false; 
    private final Set<Integer> modificadoresActivos = new HashSet<>();

    private int imeOptions;
    private int inputType;
    private int paginaBarraActiva = 1;

    public int obtenerPaginaBarra() {
        return paginaBarraActiva;
    }

    public void alternarPaginaBarra() {
        paginaBarraActiva = (paginaBarraActiva == 1) ? 2 : 1;
    }

    public void establecerPaginaBarra(int pagina) {
        this.paginaBarraActiva = pagina;
    }

    public void configurarContextoEntrada(int imeOptions, int inputType) {
        this.imeOptions = imeOptions;
        this.inputType  = inputType;
    }

    public int obtenerAccionIme() {
        return imeOptions & EditorInfo.IME_MASK_ACTION;
    }

    public boolean isMultilinea() {
        return (inputType & InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0;
    }

    public ModoTeclado obtenerModoActual() { 
        return modoActual;
    }

    public void establecerModo(ModoTeclado modo) { 
        this.modoActual = modo;
        // BUGFIX: Limpia Shift, Ctrl y Alt cada vez que se cambia de modo
        limpiarModificadores();
    }

    public EstadoShift obtenerEstadoShift() { 
        return estadoShift;
    }
    
    public void fijarEstadoShift(EstadoShift nuevoEstado) { 
        this.estadoShift = nuevoEstado;
        this.shiftEsManual = true;
    }

    public void fijarShiftAutomatico(EstadoShift nuevoEstado) {
        this.estadoShift   = nuevoEstado;
        this.shiftEsManual = false;
    }
    public boolean isShiftManual() {
        return shiftEsManual;
    }

    public void consumirShiftSiEsNecesario() {
        estadoShift = estadoShift.consumir();
        shiftEsManual  = false;
    }

    public boolean isModificadorActivo(int codigoModificador) {
        if (codigoModificador == Constantes.CODIGO_SHIFT) {
            return estadoShift.estaActivo();
        }
        return modificadoresActivos.contains(codigoModificador);
    }

    public void alternarModificador(int codigoModificador) {
        if (!modificadoresActivos.remove(codigoModificador)) {
            modificadoresActivos.add(codigoModificador);
        }
    }

    public void limpiarModificadores() {
        modificadoresActivos.clear();
        estadoShift = EstadoShift.APAGADO;
        shiftEsManual  = false;
    }


}