package com.ncy.interfaz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.view.animation.DecelerateInterpolator;

import com.ncy.estado.EstadoTeclado;
import com.ncy.interfaz.teclas.DibujanteTecla;
import com.ncy.modelo.Tecla;
import com.ncy.utilidades.LectorXML;

import java.util.List;

public class ControladorBarraAlterna {

    private static final float VELOCIDAD_FLING_PX_S = 1000f;
    private static final float VELOCIDAD_LENTA_PX_S = 500f;
    private static final float FACTOR_UMBRAL_DISTANCIA = 2.5f;
    private static final long DURACION_SNAP_MIN_MS = 100L;
    private static final long DURACION_SNAP_MAX_MS = 300L;
    private static final long DURACION_SNAP_LENTO_MS = 200L;

    private boolean arrastrando = false;
    private boolean animandoSnap = false;
    private float desplazamientoX = 0f;
    private List<Tecla> barraAlterna = null;
    private int paginaAlterna = 1;

    private final Context contexto;
    private final EstadoTeclado estado;
    private final Runnable alSolicitarRedibujo;
    private final Runnable alCambiarPagina;

    public ControladorBarraAlterna(Context contexto, EstadoTeclado estado,
                                   Runnable alSolicitarRedibujo, Runnable alCambiarPagina) {
        this.contexto = contexto;
        this.estado = estado;
        this.alSolicitarRedibujo = alSolicitarRedibujo;
        this.alCambiarPagina = alCambiarPagina;
    }

    public boolean isArrastrando() { return arrastrando; }
    public boolean isAnimando() { return animandoSnap; }

    public void iniciarArrastre(int anchoPantalla, int altoFila) {
        arrastrando = true;
        cargarBarraAlterna(anchoPantalla, altoFila);
    }

    public void actualizarArrastre(float dx) {
        desplazamientoX = dx;
        alSolicitarRedibujo.run();
    }

    // Actualizado: Ahora recibe velocidadX desde la Vista
    public void soltarArrastre(float ancho, float velocidadX) {
        arrastrando = false;
        animarSnapBarra(ancho, velocidadX);
    }

    public void dibujar(Canvas lienzo, List<Tecla> filaPrincipal, DibujanteTecla dibujante, int offsetY, int ancho) {
        lienzo.save();
        lienzo.translate(desplazamientoX, 0);
        for (Tecla tecla : filaPrincipal) {
            dibujante.dibujarConOffset(lienzo, tecla, estado, false, 0, offsetY);
        }
        lienzo.restore();

        if (barraAlterna != null) {
            lienzo.save();
            float offsetAlterno = (desplazamientoX > 0) ? desplazamientoX - ancho : desplazamientoX + ancho;
            lienzo.translate(offsetAlterno, 0);
            for (Tecla tecla : barraAlterna) {
                dibujante.dibujarConOffset(lienzo, tecla, estado, false, 0, offsetY);
            }
            lienzo.restore();
        }
    }

    private void cargarBarraAlterna(int anchoPantalla, int altoFila) {
        int paginaActual = estado.obtenerPaginaBarra();
        paginaAlterna = (paginaActual == 1) ? 2 : 1;
        String nombreXml = "barra_universal_" + paginaAlterna;

        List<List<Tecla>> filas = LectorXML.parsear(contexto, nombreXml);
        if (!filas.isEmpty()) {
            barraAlterna = filas.get(0);
            float totalPesos = 0f;
            for (Tecla t : barraAlterna) totalPesos += t.obtenerPeso();
            float anchoUnidad = (float) anchoPantalla / totalPesos;
            int posX = 0;
            for (int i = 0; i < barraAlterna.size(); i++) {
                Tecla t = barraAlterna.get(i);
                int ancho = (i == barraAlterna.size() - 1) ? (anchoPantalla - posX) : Math.round(anchoUnidad * t.obtenerPeso());
                t.aplicarGeometria(posX, 0, ancho, altoFila);
                t.setEstiloTransparente(true);
                posX += ancho;
            }
        }
    }

    private void animarSnapBarra(float ancho, float velocidadX) {
        float destinoX = 0f;
        boolean cambiarPagina = false;

        // Umbrales ajustados para mayor precisión
        float umbralDistanciaCambio = ancho / FACTOR_UMBRAL_DISTANCIA; // Necesitas mover un 40% para que cambie lento

        // 1. Decidir si cambiamos de página
        if (Math.abs(velocidadX) > VELOCIDAD_FLING_PX_S) {
            // Lanzamiento rápido: sigue la dirección del dedo
            if (velocidadX > 0 && desplazamientoX > -ancho/2) {
                destinoX = ancho;
                cambiarPagina = true;
            } else if (velocidadX < 0 && desplazamientoX < ancho/2) {
                destinoX = -ancho;
                cambiarPagina = true;
            }
        } else {
            // Deslizamiento lento: requiere haber superado el 40% del ancho
            if (desplazamientoX > umbralDistanciaCambio) {
                destinoX = ancho;
                cambiarPagina = true;
            } else if (desplazamientoX < -umbralDistanciaCambio) {
                destinoX = -ancho;
                cambiarPagina = true;
            }
        }

        // 2. Cálculo de Duración "Snap" (Aquí está el truco)
        long duracion;
        if (Math.abs(velocidadX) > VELOCIDAD_LENTA_PX_S) {
            // Si hay buena velocidad, calculamos tiempo proporcional para que sea fluido
            float distanciaRestante = Math.abs(destinoX - desplazamientoX);
            duracion = (long) (distanciaRestante / Math.abs(velocidadX) * 1000);
            duracion = Math.max(DURACION_SNAP_MIN_MS, Math.min(duracion, DURACION_SNAP_MAX_MS)); // Máximo 300ms para que no sea lento
        } else {
            // Si el movimiento es lento (< 500px/s), usamos un tiempo fijo y corto
            // Esto quita la sensación de "chicle" o "deslizamiento largo"
            duracion = DURACION_SNAP_LENTO_MS;
        }

        // 3. Ejecutar Animación
        animandoSnap = true;
        ValueAnimator snapAnimator = ValueAnimator.ofFloat(desplazamientoX, destinoX);
        snapAnimator.setDuration(duracion);
        snapAnimator.setInterpolator(new DecelerateInterpolator(2f));
        final boolean finalCambiarPagina = cambiarPagina;
        snapAnimator.addUpdateListener(anim -> {
            desplazamientoX = (float) anim.getAnimatedValue();
            alSolicitarRedibujo.run();
        });

        snapAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animandoSnap = false;
                if (finalCambiarPagina) {
                    estado.establecerPaginaBarra(paginaAlterna);
                    alCambiarPagina.run();
                }
                desplazamientoX = 0f;
                barraAlterna = null;
                alSolicitarRedibujo.run();
            }
        });
        snapAnimator.start();
    }
}