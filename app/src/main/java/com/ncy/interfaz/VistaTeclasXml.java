package com.ncy.interfaz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.interfaz.teclas.DibujanteTecla;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.modelo.Tecla;
import com.ncy.teclado.LayoutTeclado;
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
    private final ControladorBarraAlterna controladorBarra;
    
    // NUEVO: La vista ahora solo conoce a su controlador
    private final ControladorToqueXml controladorToque;

    private final int filaDesde;
    private final int filaHasta;

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

        this.pinturaFondoBarra.setStyle(Paint.Style.FILL);
        this.pinturaFondoBarra.setColor(temaActual.getColorFondoBarra());

        this.controladorBarra = new ControladorBarraAlterna(
                contexto, estado, this::invalidate,
                () -> { if (listenerModo != null) listenerModo.onModoCambiado(); }
        );

        // Delegamos los rastreadores y gestos al controlador táctil
        this.controladorToque = new ControladorToqueXml(contexto, this, manejador, estado, layout, controladorBarra, filaDesde, filaHasta);

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
        if (controladorToque != null) {
            controladorToque.liberarRecursos();
        }
    }

    @Override
    protected void onDraw(Canvas lienzo) {
        super.onDraw(lienzo);
        int offsetY = calcularOffsetY();
        int ancho = getWidth();
        
        List<List<Tecla>> todas = layout.obtenerMatriz();
        int limiteInferior = (filaDesde == -1) ? 0 : Math.max(filaDesde, 0);
        int limiteSuperior = (filaHasta == -1) ? todas.size() : Math.min(filaHasta + 1, todas.size());

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
                // Le preguntamos al controlador táctil qué tecla dibujar como presionada
                boolean estaPresionada = (!controladorBarra.isArrastrando() && !controladorBarra.isAnimando() && tecla == controladorToque.getTeclaPresionadaInicialmente());
                int gestoActivo = estaPresionada ? controladorToque.getGestoEnProgreso() : 0;
                dibujante.dibujarConOffset(lienzo, tecla, estado, estaPresionada, gestoActivo, offsetY);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        if (controladorToque != null) {
            return controladorToque.procesarToque(evento);
        }
        return super.onTouchEvent(evento);
    }

    public void notificarCambioModo() {
        if (listenerModo != null) listenerModo.onModoCambiado();
        else invalidate();
    }

    public int calcularOffsetY() {
        if (filaDesde <= 0) return 0;
        List<List<Tecla>> todas = layout.obtenerMatriz();
        if (todas.isEmpty() || filaDesde >= todas.size()) return 0;
        List<Tecla> primeraFila = todas.get(filaDesde);
        if (primeraFila.isEmpty()) return 0;
        return primeraFila.get(0).obtenerY();
    }
}