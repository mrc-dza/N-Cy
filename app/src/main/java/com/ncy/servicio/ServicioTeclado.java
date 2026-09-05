package com.ncy.servicio;

import com.ncy.utilidades.ContenedorNcy;
import com.ncy.utilidades.GestorTema;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.InputMethodService;

import com.ncy.estado.ModoTeclado;
import com.ncy.interfaz.ReconstruibleTeclado;
import com.ncy.interfaz.VistaTeclado;
import com.ncy.utilidades.TemaVisual;

public class ServicioTeclado extends InputMethodService implements ReconstruibleTeclado {

    private VistaTeclado vistaTeclado;
    private ContenedorNcy contenedor;

    private final Runnable oyenteConfiguracion = () -> {
        contenedor.obtenerGestorTema().recargarDesdePreferencias();
        if (vistaTeclado != null) {
            vistaTeclado.reconstruirYRedibujar();
            updateInputViewShown();
        }
    };

    private final GestorTema.OnTemaCambiadoListener oyenteTema = nuevoTema -> {
        if (vistaTeclado != null) {
            vistaTeclado.aplicarTema(nuevoTema);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        this.contenedor = new ContenedorNcy(this, this);
        contenedor.obtenerRepositorioConfig().setOyenteCambiosVisuales(oyenteConfiguracion);
        contenedor.obtenerGestorTema().agregarOyente(oyenteTema);
    }

    @Override
    public View onCreateInputView() {
        this.vistaTeclado = new VistaTeclado(
            this, 
            contenedor.obtenerManejadorEntrada(), 
            contenedor.obtenerEstadoTeclado(), 
            contenedor.obtenerManejadorPortapapeles(), 
            this, 
            contenedor.obtenerGestorTema()
        );
        return vistaTeclado;
    }
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        contenedor.obtenerEstadoTeclado().configurarContextoEntrada(info.imeOptions, info.inputType);
        contenedor.obtenerEstadoTeclado().limpiarModificadores();
        contenedor.obtenerEstadoTeclado().establecerModo(ModoTeclado.LETRAS);

        InputConnection conexion = getCurrentInputConnection();
        contenedor.obtenerManejadorEntrada().establecerConexion(conexion);

        actualizarMayusculasAutomaticas();
        if (vistaTeclado != null) vistaTeclado.reconstruirYRedibujar();
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        contenedor.obtenerManejadorEntrada().establecerConexion(null);
        contenedor.obtenerEstadoTeclado().limpiarModificadores();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contenedor != null) {
            contenedor.obtenerGestorTema().removerOyente(oyenteTema);
            contenedor.obtenerRepositorioConfig().setOyenteCambiosVisuales(null);
            contenedor.destruir();
        }
    }

    @Override
    public void refrescarTeclado() {
        if (vistaTeclado != null) {
            vistaTeclado.reconstruirYRedibujar();
        }
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        actualizarMayusculasAutomaticas();
    }

    private void actualizarMayusculasAutomaticas() {
        if (contenedor == null) return;
        
        com.ncy.estado.EstadoTeclado estado = contenedor.obtenerEstadoTeclado();
        
        if (estado == null) return;
        if (estado.obtenerModoActual() != ModoTeclado.LETRAS) return;
        if (estado.obtenerEstadoShift() == com.ncy.estado.EstadoShift.BLOQUEADO) return;
        if (estado.isShiftManual()) return;

        if (!contenedor.obtenerRepositorioConfig().leerAutoMayusculas()) return;
        
        InputConnection conexion = getCurrentInputConnection();
        if (conexion == null) return;

        int modoMayusculas = conexion.getCursorCapsMode(android.text.TextUtils.CAP_MODE_SENTENCES);
        
        com.ncy.estado.EstadoShift estadoAnterior = estado.obtenerEstadoShift();
        com.ncy.estado.EstadoShift estadoNuevo = 
                (modoMayusculas != 0) ? com.ncy.estado.EstadoShift.NORMAL : com.ncy.estado.EstadoShift.APAGADO;
                
        if (estadoAnterior != estadoNuevo) {
            estado.fijarShiftAutomatico(estadoNuevo);
            if (vistaTeclado != null) {
                vistaTeclado.redibujar();
            }
        }
    }


}