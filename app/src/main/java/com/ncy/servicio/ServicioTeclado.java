package com.ncy.servicio;

import com.ncy.utilidades.GestorTema;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.inputmethodservice.InputMethodService;

import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.interfaz.ReconstruibleTeclado;
import com.ncy.interfaz.VistaTeclado;
import com.ncy.manejadores.EscuchadorPortapapelesSistema;
import com.ncy.manejadores.GestorPortapapeles;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.manejadores.ManejadorPortapapeles;
import com.ncy.utilidades.TemaVisual;

public class ServicioTeclado extends InputMethodService implements ReconstruibleTeclado {

    private VistaTeclado          vistaTeclado;
    private ManejadorEntrada      manejadorEntrada;
    private EstadoTeclado         estadoTeclado;
    private ManejadorPortapapeles manejadorPortapapeles;

    private EscuchadorPortapapelesSistema escuchadorPortapapeles;
    private SharedPreferences.OnSharedPreferenceChangeListener listenerConfiguracion;
    
    // AÑADIDO: Campo para guardar la instancia del repositorio
    private RepositorioConfiguracion repositorioConfig;

    // NUEVO: Oyente para escuchar cambios de tema en tiempo real
    private final GestorTema.OnTemaCambiadoListener oyenteTema = nuevoTema -> {
        if (vistaTeclado != null) {
            vistaTeclado.aplicarTema(nuevoTema);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        GestorTema.getInstance().inicializar(this);
        this.estadoTeclado = new EstadoTeclado();
        
        // AÑADIDO: Instanciamos el repositorio una sola vez al crear el servicio
        this.repositorioConfig = new RepositorioConfiguracion(this);
        GestorPortapapeles gestor = new GestorPortapapeles(this);
        
        this.escuchadorPortapapeles = new EscuchadorPortapapelesSistema(this, gestor);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        this.manejadorPortapapeles = new ManejadorPortapapeles(this, gestor, clipboard);
        this.manejadorEntrada = new ManejadorEntrada(this, estadoTeclado, manejadorPortapapeles, this);

        SharedPreferences prefs = getSharedPreferences(RepositorioConfiguracion.NOMBRE_PREFS, Context.MODE_PRIVATE);
        listenerConfiguracion = (sharedPreferences, key) -> {
            if (RepositorioConfiguracion.CLAVE_ALTURA.equals(key) || 
                RepositorioConfiguracion.CLAVE_MOSTRAR_BARRA.equals(key)) {
                
                if (vistaTeclado != null) {
                    vistaTeclado.reconstruirYRedibujar();
                    updateInputViewShown();
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listenerConfiguracion);

        // NUEVO: Registrar el oyente de tema para que el servicio escuche los cambios
        GestorTema.getInstance().agregarOyente(oyenteTema);
    }

    @Override
    public View onCreateInputView() {
        // CORRECCIÓN: Ahora pasamos el 5to argumento (this) requerido por el constructor refactorizado
        this.vistaTeclado = new VistaTeclado(this, manejadorEntrada, estadoTeclado, manejadorPortapapeles, this);
        return vistaTeclado;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        estadoTeclado.configurarContextoEntrada(info.imeOptions, info.inputType);
        

        estadoTeclado.limpiarModificadores();
        estadoTeclado.establecerModo(ModoTeclado.LETRAS);

        InputConnection conexion = getCurrentInputConnection();
        manejadorEntrada.establecerConexion(conexion);

        actualizarMayusculasAutomaticas();
        if (vistaTeclado != null) {
            vistaTeclado.reconstruirYRedibujar();
        }
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        manejadorEntrada.establecerConexion(null);
        estadoTeclado.limpiarModificadores();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (escuchadorPortapapeles != null) {
            escuchadorPortapapeles.destruir();
        }
        
        // <-- NUEVA LÍNEA: Apagamos el hilo del portapapeles
        if (manejadorPortapapeles != null && manejadorPortapapeles.obtenerGestor() != null) {
            manejadorPortapapeles.obtenerGestor().destruir();
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
        if (estadoTeclado == null) return;
        if (estadoTeclado.obtenerModoActual() != ModoTeclado.LETRAS) return;
        if (estadoTeclado.obtenerEstadoShift() == com.ncy.estado.EstadoShift.BLOQUEADO) return;
        if (estadoTeclado.isShiftManual()) return;

        if (!repositorioConfig.leerAutoMayusculas()) return;
        
        InputConnection conexion = getCurrentInputConnection();
        if (conexion == null) return;

        int modoMayusculas = conexion.getCursorCapsMode(android.text.TextUtils.CAP_MODE_SENTENCES);
        
        com.ncy.estado.EstadoShift estadoAnterior = estadoTeclado.obtenerEstadoShift();
        com.ncy.estado.EstadoShift estadoNuevo = 
                (modoMayusculas != 0) 
                ?
                com.ncy.estado.EstadoShift.NORMAL 
                : com.ncy.estado.EstadoShift.APAGADO;
        if (estadoAnterior != estadoNuevo) {
            estadoTeclado.fijarShiftAutomatico(estadoNuevo);
            if (vistaTeclado != null) {
                vistaTeclado.redibujar();
            }
        }
    }
}