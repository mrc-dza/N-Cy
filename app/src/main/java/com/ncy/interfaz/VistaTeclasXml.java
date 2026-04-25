package com.ncy.interfaz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint; // AÑADIDO: Importación para el pincel
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.interfaz.teclas.DibujanteTecla;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.manejadores.ManejadorRepeticion;
import com.ncy.modelo.Tecla;
import com.ncy.teclado.LayoutTeclado;
import com.ncy.utilidades.Constantes;
import com.ncy.utilidades.DetectorGestos;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

import java.util.List;

public class VistaTeclasXml extends View {

    public interface OnModoCambiadoListener {
        void onModoCambiado();
    }

    private OnModoCambiadoListener listenerModo;

    private final ManejadorEntrada manejador;
    private final LayoutTeclado layout;
    private final EstadoTeclado estado;
    private final DibujanteTecla dibujante;
    private final DetectorGestos detectorGestos;
    private final DetectorToque detectorToque;
    private final ManejadorRepeticion manejadorRepeticion;
    private final MotorSlider motorSlider;
    private final ControladorBarraAlterna controladorBarra;

    private final int filaDesde;
    private final int filaHasta;

    private float xInicial;
    private float yInicial;
    private Tecla teclaPresionadaInicialmente;
    private boolean gestoEjecutado;
    private int gestoEnProgreso = 0;

    private VelocityTracker velocityTracker;

    // NUEVO: Pincel cacheado para el fondo exclusivo de la barra
    private final Paint pinturaFondoBarra = new Paint(Paint.ANTI_ALIAS_FLAG);

    public VistaTeclasXml(Context contexto, ManejadorEntrada manejador,
                          EstadoTeclado estado, LayoutTeclado layout,
                          int filaDesde, int filaHasta) {
        super(contexto);
        this.manejador = manejador;
        this.estado = estado;
        this.layout = layout;
        this.filaDesde = filaDesde;
        this.filaHasta = filaHasta;

        TemaVisual temaActual = GestorTema.getInstance().obtenerTemaActivo();
        this.dibujante = new DibujanteTecla(contexto, temaActual);

        // NUEVO: Inicializar la pintura del fondo de la barra
        this.pinturaFondoBarra.setStyle(Paint.Style.FILL);
        this.pinturaFondoBarra.setColor(temaActual.getColorFondoBarra());

        this.detectorGestos = new DetectorGestos();
        this.detectorToque = new DetectorToque();

        this.manejadorRepeticion = new ManejadorRepeticion(manejador);

        this.manejadorRepeticion.setListenerToqueLargo((tecla, indiceGesto) -> {
            if (tecla.obtenerCodigo() == Constantes.CODIGO_SHIFT) {
                manejador.procesarToqueLargoShift();
                invalidate();
                return true; // Evento consumido, NO repetir
            }
            return false; // Permite que otras teclas sí se repitan
        });

        this.motorSlider = new MotorSlider(contexto, (tecla, indiceGesto) -> {
            manejadorRepeticion.detener();
            manejador.procesarToque(tecla, indiceGesto);
        });

        this.controladorBarra = new ControladorBarraAlterna(
                contexto, estado, this::invalidate,
                () -> { if (listenerModo != null) listenerModo.onModoCambiado(); }
        );

        setBackgroundColor(temaActual.getColorFondoGeneral());

        
    }

    public VistaTeclasXml(Context contexto, ManejadorEntrada manejador,
                          EstadoTeclado estado, LayoutTeclado layout) {
        this(contexto, manejador, estado, layout, -1, -1);
    }

    public void setListenerModo(OnModoCambiadoListener listener) {
        this.listenerModo = listener;
    }

    public void aplicarTema(TemaVisual tema) {
        setBackgroundColor(tema.getColorFondoGeneral());
        if (dibujante != null) {
            dibujante.actualizarTema(tema);
        }
        // NUEVO: Actualizar el color de fondo de la barra al cambiar de tema en tiempo real
        pinturaFondoBarra.setColor(tema.getColorFondoBarra());
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int anchoPantalla = MeasureSpec.getSize(widthMeasureSpec);
        if (filaDesde == -1) {
            layout.construirTeclado(anchoPantalla, estado.obtenerModoActual());
        }

        int alto = 0;
        List<List<Tecla>> todas = layout.obtenerMatriz();
        int limiteInferior = (filaDesde == -1) ? 0 : Math.max(filaDesde, 0);
        int limiteSuperior = (filaHasta == -1) ?
                todas.size() : Math.min(filaHasta + 1, todas.size());

        for (int i = limiteInferior; i < limiteSuperior; i++) {
            List<Tecla> fila = todas.get(i);
            if (!fila.isEmpty()) alto += fila.get(0).obtenerAlto();
        }

        setMeasuredDimension(anchoPantalla, alto);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (manejadorRepeticion != null) {
            manejadorRepeticion.detener();
        }
        teclaPresionadaInicialmente = null;
        gestoEnProgreso = 0;
    }

    @Override
    protected void onDraw(Canvas lienzo) {
        super.onDraw(lienzo);
        int offsetY = calcularOffsetY();
        int ancho = getWidth();
        
        List<List<Tecla>> todas = layout.obtenerMatriz();
        int limiteInferior = (filaDesde == -1) ? 0 : Math.max(filaDesde, 0);
        int limiteSuperior = (filaHasta == -1) ? todas.size() : Math.min(filaHasta + 1, todas.size());

        // NUEVO: Dibujar el rectángulo sólido de fondo exclusivamente para la barra de herramientas
        if (limiteInferior == 0 && estado.obtenerModoActual() != ModoTeclado.PORTAPAPELES && !todas.isEmpty() && !todas.get(0).isEmpty()) {
            int altoBarra = todas.get(0).get(0).obtenerAlto();
            lienzo.drawRect(0, -offsetY, ancho, altoBarra - offsetY, pinturaFondoBarra);
        }

        for (int i = limiteInferior; i < limiteSuperior; i++) {
            List<Tecla> fila = todas.get(i);
            boolean esFilaCinta = (i == 0 && estado.obtenerModoActual() != ModoTeclado.PORTAPAPELES
                    && (controladorBarra.isArrastrando() || controladorBarra.isAnimando()));

            if (esFilaCinta) {
                controladorBarra.dibujar(lienzo, fila, dibujante, offsetY, ancho);
                continue;
            }

            for (Tecla tecla : fila) {
                boolean estaPresionada = (!controladorBarra.isArrastrando() && !controladorBarra.isAnimando() && tecla == teclaPresionadaInicialmente);
                int gestoActivo = estaPresionada ? gestoEnProgreso : 0;
                dibujante.dibujarConOffset(lienzo, tecla, estado, estaPresionada, gestoActivo, offsetY);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        if (controladorBarra.isAnimando()) return true;


        // Limpiar el rastro anterior si empezamos un toque nuevo
        if (evento.getAction() == MotionEvent.ACTION_DOWN && velocityTracker != null) {
            velocityTracker.clear();
        }
        
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(evento);





        float xActual = evento.getX();
        float yActual = evento.getY() + calcularOffsetY();

        switch (evento.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                return procesarActionDown(xActual, yActual);
            }
            case MotionEvent.ACTION_MOVE -> {
                return procesarActionMove(xActual, yActual);
            }
            case MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                velocityTracker.computeCurrentVelocity(1000);
                float velX = velocityTracker.getXVelocity();
                
                boolean res = procesarActionUp(velX);
                
                velocityTracker.recycle();
                velocityTracker = null;
                return res;
            }
        }
        return super.onTouchEvent(evento);
    }

    private boolean procesarActionDown(float xActual, float yActual) {
        xInicial = xActual;
        yInicial = yActual;
        gestoEnProgreso = 0;
        gestoEjecutado = false;
        motorSlider.reiniciar(xActual, yActual);

        List<List<Tecla>> todas = layout.obtenerMatriz();
        int limiteInferior = (filaDesde == -1) ? 0 : Math.max(filaDesde, 0);
        int limiteSuperior = (filaHasta == -1) ? todas.size() : Math.min(filaHasta + 1, todas.size());
        
        List<List<Tecla>> filasRango = todas.subList(limiteInferior, limiteSuperior);
        teclaPresionadaInicialmente = detectorToque.encontrar(xActual, yActual, filasRango);

        if (teclaPresionadaInicialmente != null) {
            manejadorRepeticion.iniciar(teclaPresionadaInicialmente, 0);
            invalidate();
        }
        return true;
    }

    private boolean procesarActionMove(float xActual, float yActual) {
        if (teclaPresionadaInicialmente == null) return true;

        if (!gestoEjecutado && !controladorBarra.isArrastrando()) {
            float dx = xActual - xInicial;
            float dy = yActual - yInicial;

            if (Math.abs(dx) > 30 && Math.abs(dx) > Math.abs(dy)) {
                if (estado.obtenerModoActual() != ModoTeclado.PORTAPAPELES && teclaPresionadaInicialmente.obtenerY() == 0) {
                    manejadorRepeticion.detener();
                    int altoFila = GestorTema.getInstance().obtenerTemaActivo().getAlturaFilaBasePx();
                    if (!layout.obtenerMatriz().isEmpty() && !layout.obtenerMatriz().get(0).isEmpty()) {
                        altoFila = layout.obtenerMatriz().get(0).get(0).obtenerAlto();
                    }
                    controladorBarra.iniciarArrastre(getWidth(), altoFila);
                    return true;
                }
            }
        }

        if (controladorBarra.isArrastrando()) {
            controladorBarra.actualizarArrastre(xActual - xInicial);
            return true;
        }

        if (motorSlider.evaluar(xActual, yActual, teclaPresionadaInicialmente)) {
            gestoEjecutado = true;
            invalidate();
            return true;
        }

        if (!motorSlider.isActivo() && !gestoEjecutado) {
            if (detectorGestos.superoUmbral(xInicial, yInicial, xActual, yActual)) {
                int tipoGesto = detectorGestos.evaluarGesto(xInicial, yInicial, xActual, yActual);
                if (tipoGesto != gestoEnProgreso) {
                    gestoEnProgreso = tipoGesto;
                    invalidate();
                }

                if (tipoGesto != 0 && detectorGestos.superoUmbralConfirmacion(xInicial, yInicial, xActual, yActual)) {
                    ModoTeclado modoAntes = estado.obtenerModoActual();
                    manejadorRepeticion.detener();
                    manejador.procesarToque(teclaPresionadaInicialmente, tipoGesto);
                    gestoEjecutado = true;
                    gestoEnProgreso = 0;

                    if (estado.obtenerModoActual() != modoAntes) notificarCambioModo();
                    else invalidate();
                }
            }
        }
        return true;
    }

    private boolean procesarActionUp(float velocidadX) {
        if (controladorBarra.isArrastrando()) {
            controladorBarra.soltarArrastre(getWidth(), velocidadX);
            return true;
        }

        ModoTeclado modoAntes = estado.obtenerModoActual();
        if (teclaPresionadaInicialmente != null && !gestoEjecutado && !manejadorRepeticion.isYaEjecutoAlMenosUnaVez()) {
            manejador.procesarToque(teclaPresionadaInicialmente, 0);
        }

        manejadorRepeticion.detener();
        teclaPresionadaInicialmente = null;
        gestoEnProgreso = 0;

        if (estado.obtenerModoActual() != modoAntes) notificarCambioModo();
        else invalidate();

        return true;
    }

    private void notificarCambioModo() {
        if (listenerModo != null) listenerModo.onModoCambiado();
        else invalidate();
    }

    private int calcularOffsetY() {
        if (filaDesde <= 0) return 0;
        List<List<Tecla>> todas = layout.obtenerMatriz();
        if (todas.isEmpty() || filaDesde >= todas.size()) return 0;
        List<Tecla> primeraFila = todas.get(filaDesde);
        if (primeraFila.isEmpty()) return 0;
        return primeraFila.get(0).obtenerY();
    }
}