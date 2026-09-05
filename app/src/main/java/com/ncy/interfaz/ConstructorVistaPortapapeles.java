package com.ncy.interfaz;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.manejadores.ManejadorEntrada;
import com.ncy.manejadores.ManejadorPortapapeles;
import com.ncy.modelo.Tecla;
import com.ncy.teclado.LayoutTeclado;
import com.ncy.utilidades.GestorTema;

import java.util.List;

public class ConstructorVistaPortapapeles {

    private final Context               contexto;
    private final ManejadorEntrada      manejador;
    private final EstadoTeclado         estado;
    private final ManejadorPortapapeles manejadorPortapapeles;
    private final LayoutTeclado         layout;
    private final GestorTema            gestorTema;

    public ConstructorVistaPortapapeles(Context contexto,
                                         ManejadorEntrada manejador,
                                         EstadoTeclado estado,
                                         ManejadorPortapapeles manejadorPortapapeles,
                                         LayoutTeclado layout,
                                         GestorTema gestorTema) {
        this.contexto              = contexto;
        this.manejador             = manejador;
        this.estado                = estado;
        this.manejadorPortapapeles = manejadorPortapapeles;
        this.layout                = layout;
        this.gestorTema            = gestorTema;
    }

    public void ensamblar(LinearLayout contenedor,
                          VistaTeclasXml.OnModoCambiadoListener listener,
                          int anchoPantalla) {
        
        int altoRef = layout.calcularAltoReferencia(anchoPantalla);

        layout.construirTeclado(anchoPantalla, ModoTeclado.PORTAPAPELES);
        List<List<Tecla>> filas = layout.obtenerMatriz();
        int totalFilas = filas.size();

        int altoBarraAbajo  = (totalFilas > 0) ? altoDeFila(filas, totalFilas - 1) : 0;
        int altoGestor      = Math.max(altoRef - altoBarraAbajo, 80);

        agregarScrollGestor(contenedor, altoGestor);

        if (totalFilas > 0) {
            agregarBarra(contenedor, totalFilas - 1, totalFilas - 1, listener);
        }
    }

    private void agregarBarra(LinearLayout contenedor,
                               int filaDesde, int filaHasta,
                               VistaTeclasXml.OnModoCambiadoListener listener) {
        VistaTeclasXml barra = new VistaTeclasXml(
                contexto, manejador, estado, layout, filaDesde, filaHasta, gestorTema);
        barra.setListenerModo(listener);
        contenedor.addView(barra, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void agregarScrollGestor(LinearLayout contenedor, int altoGestor) {
        VistaGestorPortapapeles vistaGestor = new VistaGestorPortapapeles(
                contexto,
                manejadorPortapapeles.obtenerGestor(),
                manejadorPortapapeles,
                gestorTema);
        ScrollView scroll = new ScrollView(contexto);
        scroll.addView(vistaGestor, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        contenedor.addView(scroll, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, altoGestor));
    }

    private int altoDeFila(List<List<Tecla>> filas, int indiceFila) {
        if (indiceFila < 0 || indiceFila >= filas.size()) return 0;
        List<Tecla> fila = filas.get(indiceFila);
        return fila.isEmpty() ? 0 : fila.get(0).obtenerAlto();
    }
}