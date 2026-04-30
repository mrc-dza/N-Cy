package com.ncy.servicio;

import com.ncy.utilidades.ContenedorNcy;
import com.ncy.utilidades.GestorTema;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.InputMethodService;

import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.estado.ModoTeclado;
import com.ncy.interfaz.ReconstruibleTeclado;
import com.ncy.interfaz.VistaTeclado;
import com.ncy.utilidades.TemaVisual;

public class ServicioTeclado extends InputMethodService implements ReconstruibleTeclado {

    private VistaTeclado vistaTeclado;
    
    // ¡NUEVO! Todo el núcleo del teclado vive aquí adentro
    private ContenedorNcy contenedor;

    private final SharedPreferences.OnSharedPreferenceChangeListener listenerConfiguracion = 
        (sharedPreferences, key) -> {
            if (RepositorioConfiguracion.CLAVE_ALTURA.equals(key) || 
                RepositorioConfiguracion.CLAVE_MOSTRAR_BARRA.equals(key)) {
                
                if (vistaTeclado != null) {
                    vistaTeclado.reconstruirYRedibujar();
                    updateInputViewShown();
                }
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
        GestorTema.getInstance().inicializar(this);
        
        // ¡LA INYECCIÓN OCURRE AQUÍ! Creamos el contenedor una sola vez
        this.contenedor = new ContenedorNcy(this, this);

        SharedPreferences prefs = getSharedPreferences(RepositorioConfiguracion.NOMBRE_PREFS, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(listenerConfiguracion);

        GestorTema.getInstance().agregarOyente(oyenteTema);
    }

    @Override
    public View onCreateInputView() {
        // Obtenemos las dependencias desde el contenedor
        this.vistaTeclado = new VistaTeclado(this, contenedor.manejadorEntrada, contenedor.estadoTeclado, contenedor.manejadorPortapapeles, this);
        return vistaTeclado;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        contenedor.estadoTeclado.configurarContextoEntrada(info.imeOptions, info.inputType);

        contenedor.estadoTeclado.limpiarModificadores();
        contenedor.estadoTeclado.establecerModo(ModoTeclado.LETRAS);

        InputConnection conexion = getCurrentInputConnection();
        contenedor.manejadorEntrada.establecerConexion(conexion);

        actualizarMayusculasAutomaticas();
        if (vistaTeclado != null) {
            vistaTeclado.reconstruirYRedibujar();
        }
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        contenedor.manejadorEntrada.establecerConexion(null);
        contenedor.estadoTeclado.limpiarModificadores();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Delegamos la limpieza de recursos al contenedor
        if (contenedor != null) {
            contenedor.destruir();
        }
        
        SharedPreferences prefs = getSharedPreferences(RepositorioConfiguracion.NOMBRE_PREFS, Context.MODE_PRIVATE);
        prefs.unregisterOnSharedPreferenceChangeListener(listenerConfiguracion);

        GestorTema.getInstance().removerOyente(oyenteTema);
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
        if (contenedor == null || contenedor.estadoTeclado == null) return;
        if (contenedor.estadoTeclado.obtenerModoActual() != ModoTeclado.LETRAS) return;
        if (contenedor.estadoTeclado.obtenerEstadoShift() == com.ncy.estado.EstadoShift.BLOQUEADO) return;
        if (contenedor.estadoTeclado.isShiftManual()) return;

        if (!contenedor.repositorioConfig.leerAutoMayusculas()) return;
        
        InputConnection conexion = getCurrentInputConnection();
        if (conexion == null) return;

        int modoMayusculas = conexion.getCursorCapsMode(android.text.TextUtils.CAP_MODE_SENTENCES);
        
        com.ncy.estado.EstadoShift estadoAnterior = contenedor.estadoTeclado.obtenerEstadoShift();
        com.ncy.estado.EstadoShift estadoNuevo = 
                (modoMayusculas != 0) 
                ?
                com.ncy.estado.EstadoShift.NORMAL 
                : com.ncy.estado.EstadoShift.APAGADO;
        if (estadoAnterior != estadoNuevo) {
            contenedor.estadoTeclado.fijarShiftAutomatico(estadoNuevo);
            if (vistaTeclado != null) {
                vistaTeclado.redibujar();
            }
        }
    }
}