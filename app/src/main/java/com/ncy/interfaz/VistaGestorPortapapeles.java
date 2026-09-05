package com.ncy.interfaz;

import com.ncy.R;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.ncy.interfaz.portapapeles.CalculadorLayout;
import com.ncy.interfaz.portapapeles.DibujanteLista;
import com.ncy.interfaz.portapapeles.FabricaPinturas;
import com.ncy.manejadores.GestorPortapapeles;
import com.ncy.manejadores.ManejadorPortapapeles;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual;

import java.util.List;

public class VistaGestorPortapapeles extends View
        implements GestorPortapapeles.OnHistorialCambiadoListener {

    private int anchoAnterior = -1;
    private boolean necesitaRecalcular = true;

    private final GestorPortapapeles    gestor;
    private final ManejadorPortapapeles manejadorPortapapeles;

    private final ProcesadorTexto  procesador;
    private final CalculadorLayout calculador;
    private final DibujanteLista   dibujante;
    
    private int     itemPresionado    = -1;
    private boolean presionandoFijado = false;

    private List<String> tempsActuales;
    private List<String> fijosActuales;

    public VistaGestorPortapapeles(Context contexto,
                                   GestorPortapapeles gestor,
                                   ManejadorPortapapeles manejadorPortapapeles,
                                   GestorTema gestorTema) {
        super(contexto);
        this.gestor                = gestor;
        this.manejadorPortapapeles = manejadorPortapapeles;

        gestor.setListener(this);
        this.procesador = new ProcesadorTexto();
        
        TemaVisual temaActual = gestorTema.obtenerTemaActivo();
        android.util.DisplayMetrics metricas = contexto.getResources().getDisplayMetrics();
        float densidadGrafica = metricas.density;
        float densidadTexto = metricas.scaledDensity;

        android.graphics.drawable.Drawable iconoPin = contexto.getDrawable(R.drawable.ic_pin);
        if (iconoPin != null) {
            iconoPin = iconoPin.mutate();
            iconoPin.setTint(temaActual.getColorIconoPin());
        }

        android.graphics.drawable.Drawable iconoBorrar = contexto.getDrawable(R.drawable.ic_borrar);
        if (iconoBorrar != null) {
            iconoBorrar = iconoBorrar.mutate();
            iconoBorrar.setTint(temaActual.getColorIconoBorrar());
        }

        this.calculador = new CalculadorLayout(procesador, FabricaPinturas.texto(temaActual, densidadTexto), densidadGrafica);
        this.dibujante  = new DibujanteLista(
                procesador,
                calculador,
                iconoPin,
                iconoBorrar,
                temaActual,
                densidadGrafica,
                densidadTexto);

        setBackgroundColor(temaActual.getColorFondoGeneral());
        actualizarListasDesdeGestor();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int ancho = MeasureSpec.getSize(widthSpec);

        if (ancho != anchoAnterior || necesitaRecalcular) {
            actualizarListasDesdeGestor();
            calculador.recalcular(tempsActuales, fijosActuales, ancho);
            anchoAnterior = ancho;
            necesitaRecalcular = false;
        }

        int alto = (tempsActuales.isEmpty() && fijosActuales.isEmpty())
                ? 120 : calculador.altoTotal(tempsActuales.size(), fijosActuales.size());
                
        setMeasuredDimension(ancho, alto);
    }

    @Override
    public void onHistorialCambiado() {
        post(() -> {
            necesitaRecalcular = true;
            requestLayout();
            invalidate();
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (w != oldW && w != anchoAnterior) {  
            calculador.recalcular(tempsActuales, fijosActuales, w);
            anchoAnterior = w;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dibujante.dibujarTodo(canvas, getWidth(),
                tempsActuales, fijosActuales,
                itemPresionado, presionandoFijado);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento) {
        float x = evento.getX();
        float y = evento.getY();

        return switch (evento.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                CalculadorLayout.ResultadoToque r = calculador.resolverToque(
                        x, y, tempsActuales.size(), fijosActuales.size(), getWidth());
                if (r.zona() != CalculadorLayout.Zona.NINGUNA) {
                    itemPresionado    = r.indice();
                    presionandoFijado = r.zona() == CalculadorLayout.Zona.FIJADO;
                    invalidate(); 
                }
                yield true; 
            }
            case MotionEvent.ACTION_MOVE -> {
                if (itemPresionado != -1) {
                    CalculadorLayout.ResultadoToque move = calculador.resolverToque(
                            x, y, tempsActuales.size(), fijosActuales.size(), getWidth());
                    if (move.indice() != itemPresionado || (move.zona() == CalculadorLayout.Zona.FIJADO) != presionandoFijado) {
                        resetearEstadoTactil();
                    }
                }
                yield true;
            }
            case MotionEvent.ACTION_UP -> {
                if (itemPresionado != -1) {
                    CalculadorLayout.ResultadoToque up = calculador.resolverToque(
                            x, y, tempsActuales.size(), fijosActuales.size(), getWidth());
                    if (up.indice() == itemPresionado && (up.zona() == CalculadorLayout.Zona.FIJADO) == presionandoFijado) {
                        ejecutarAccion(up);
                    }
                    resetearEstadoTactil();
                }
                yield true;
            }
            case MotionEvent.ACTION_CANCEL -> {
                if (itemPresionado != -1) resetearEstadoTactil();
                yield true;
            }
            default -> false;
        };
    }

    private void actualizarListasDesdeGestor() {
        tempsActuales = gestor.obtenerTemporales();
        fijosActuales = gestor.obtenerFijados();
    }

    private void resetearEstadoTactil() {
        if (itemPresionado != -1) {
            itemPresionado    = -1;
            presionandoFijado = false;
            invalidate();
        }
    }

    private void ejecutarAccion(CalculadorLayout.ResultadoToque resultado) {
        switch (resultado.zona()) {
            case TEMPORAL -> {
                if (resultado.tocoBoton()) gestor.fijar(resultado.indice());
                else manejadorPortapapeles.pegarTexto(tempsActuales.get(resultado.indice()));
            }
            case FIJADO -> {
                if (resultado.tocoBoton()) gestor.eliminarFijado(resultado.indice());
                else manejadorPortapapeles.pegarTexto(fijosActuales.get(resultado.indice()));
            }
            case NINGUNA -> {} 
        }
    }
}