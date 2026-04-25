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

import java.util.List;

public class ConstructorVistaPortapapeles {

    private final Context               contexto;
    private final ManejadorEntrada      manejador;
    private final EstadoTeclado         estado;
    private final ManejadorPortapapeles manejadorPortapapeles;
    private final LayoutTeclado         layout;

    public ConstructorVistaPortapapeles(Context contexto,
                                         ManejadorEntrada manejador,
                                         EstadoTeclado estado,
                                         ManejadorPortapapeles manejadorPortapapeles,
                                         LayoutTeclado layout) {
        this.contexto              = contexto;
        this.manejador             = manejador;
        this.estado                = estado;
        this.manejadorPortapapeles = manejadorPortapapeles;
        this.layout                = layout;
    }

    public void ensamblar(LinearLayout contenedor,
                          VistaTeclasXml.OnModoCambiadoListener listener,
                          int anchoPantalla) {
        
        // 1. REFACTORIZACIÓN: Obtenemos la altura de referencia sin corromper el estado actual
        int altoRef = layout.calcularAltoReferencia(anchoPantalla);

        // 2. Construimos el portapapeles (este sí debe mutar la matriz para dibujarse)
        layout.construirTeclado(anchoPantalla, ModoTeclado.PORTAPAPELES);
        List<List<Tecla>> filas = layout.obtenerMatriz();
        int totalFilas = filas.size();

        // 3. Calculamos la altura disponible para el gestor
        int altoBarraAbajo  = (totalFilas > 0) ? altoDeFila(filas, totalFilas - 1) : 0;
        int altoGestor      = Math.max(altoRef - altoBarraAbajo, 80);

        // 4. Renderizar el área deslizable
        agregarScrollGestor(contenedor, altoGestor);

        // 5. Renderizar la barra inferior
        if (totalFilas > 0) {
            agregarBarra(contenedor, totalFilas - 1, totalFilas - 1, listener);
        }
    }

    private void agregarBarra(LinearLayout contenedor,
                               int filaDesde, int filaHasta,
                               VistaTeclasXml.OnModoCambiadoListener listener) {
        VistaTeclasXml barra = new VistaTeclasXml(
                contexto, manejador, estado, layout, filaDesde, filaHasta);
        barra.setListenerModo(listener);
        contenedor.addView(barra, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void agregarScrollGestor(LinearLayout contenedor, int altoGestor) {
        VistaGestorPortapapeles vistaGestor = new VistaGestorPortapapeles(
                contexto,
                manejadorPortapapeles.obtenerGestor(),
                manejadorPortapapeles);
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