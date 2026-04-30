package com.ncy.interfaz;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.TemaVisual; 

import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.manejadores.ManejadorPortapapeles;
import com.ncy.teclado.LayoutTeclado;

public class VistaTeclado extends LinearLayout {

    private final ManejadorEntrada      manejador;
    private final EstadoTeclado         estado;
    private final ManejadorPortapapeles manejadorPortapapeles;
    private final LayoutTeclado         layout;
    private final VistaTeclasXml        vistaTeclasXml;
    private final LinearLayout          contenedorPortapapeles;
    private final ReconstruibleTeclado  callbackReconstruccion;
    private boolean reconstruccionPendiente = false;

    private ModoTeclado modoActualMostrado = null;
    private int anchoAnterior = -1;
    private int altoAnterior = -1;

    public VistaTeclado(Context contexto, ManejadorEntrada manejador,
                        EstadoTeclado estado, ManejadorPortapapeles manejadorPortapapeles,
                        ReconstruibleTeclado callbackReconstruccion) {
        super(contexto);
        this.manejador             = manejador;
        this.estado                = estado;
        this.manejadorPortapapeles = manejadorPortapapeles;
        this.callbackReconstruccion = callbackReconstruccion;

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(GestorTema.getInstance().obtenerTemaActivo().getColorFondoGeneral());

        RepositorioConfiguracion repoCfg = new RepositorioConfiguracion(contexto);
        this.layout         = new LayoutTeclado(contexto, repoCfg, estado);
        this.vistaTeclasXml = new VistaTeclasXml(contexto, manejador, estado, layout);

        this.vistaTeclasXml.setListenerModo(() -> {
            reconstruirYRedibujar();
        });

        this.contenedorPortapapeles = new LinearLayout(contexto);
        this.contenedorPortapapeles.setOrientation(LinearLayout.VERTICAL);
        this.contenedorPortapapeles.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
   
        this.contenedorPortapapeles.setVisibility(GONE);

        addView(vistaTeclasXml, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(contenedorPortapapeles);
    }

    private void solicitarReconstruccion() {
        if (!reconstruccionPendiente) {
            reconstruccionPendiente = true;
            post(() -> {
                reconstruccionPendiente = false;
                reconstruirYRedibujar();
            });
        }
    }

    // MODIFICADO: Ahora vaciamos el contenedor del portapapeles
    public void aplicarTema(TemaVisual tema) {
        setBackgroundColor(tema.getColorFondoGeneral());
        if (vistaTeclasXml != null) {
            vistaTeclasXml.aplicarTema(tema);
        }
        
        // Al vaciarlo, reconstruirYRedibujar() se verá obligado a ensamblarlo de nuevo con el tema fresco
        if (contenedorPortapapeles != null) {
            contenedorPortapapeles.removeAllViews();
        }
        
        reconstruirYRedibujar();
    }

    public void reconstruirYRedibujar() {
        ModoTeclado modo = estado.obtenerModoActual();
        modoActualMostrado = modo;

        int anchoPantalla = getWidth() > 0 ? getWidth() : getResources().getDisplayMetrics().widthPixels;
        layout.construirTeclado(anchoPantalla, modo);
        int altoRefActual = layout.obtenerAltoReferencia();

        if ((anchoAnterior != -1 && anchoAnterior != anchoPantalla) || 
            (altoAnterior != -1 && altoAnterior != altoRefActual)) {
            contenedorPortapapeles.removeAllViews();
        }
        
        anchoAnterior = anchoPantalla;
        altoAnterior = altoRefActual;

        if (modo == ModoTeclado.PORTAPAPELES) {
            vistaTeclasXml.setVisibility(GONE);
            contenedorPortapapeles.setVisibility(VISIBLE);

            if (contenedorPortapapeles.getChildCount() == 0) {
                new ConstructorVistaPortapapeles(getContext(), manejador, estado, manejadorPortapapeles, layout)
                        .ensamblar(contenedorPortapapeles, this::solicitarReconstruccion, anchoPantalla);
            }
        } else {
            contenedorPortapapeles.setVisibility(GONE);
            vistaTeclasXml.setVisibility(VISIBLE);
        }

        vistaTeclasXml.requestLayout();
        contenedorPortapapeles.requestLayout();
        requestLayout();
        
        vistaTeclasXml.invalidate();
        contenedorPortapapeles.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (estado.obtenerModoActual() != modoActualMostrado) {
            solicitarReconstruccion();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void redibujar() {
        vistaTeclasXml.invalidate();
        contenedorPortapapeles.invalidate();
    }
}