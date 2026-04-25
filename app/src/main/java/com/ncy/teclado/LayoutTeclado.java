package com.ncy.teclado;

import android.content.Context;

import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.estado.EstadoTeclado;
import com.ncy.estado.ModoTeclado;
import com.ncy.modelo.Tecla;
import com.ncy.utilidades.LectorXML;
import com.ncy.utilidades.GestorTema;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LayoutTeclado {

    private final Map<ModoTeclado, List<List<Tecla>>> cacheCalculado = new EnumMap<>(ModoTeclado.class);
    private List<List<Tecla>> matrizActual = new ArrayList<>();

    private int altoTotalTeclado;
    private int altoReferencia = 0;

    private final Context contexto;
    private final RepositorioConfiguracion repositorioConfig;
    private final EstadoTeclado estadoTeclado;

    private int anchoAnterior = -1;
    private int paginaCargada = 1;
    private int altoFilaBase = GestorTema.getInstance().obtenerTemaActivo().getAlturaFilaBasePx();
    
    private boolean mostrarBarraBase = true;

    public LayoutTeclado(Context contexto, RepositorioConfiguracion repositorioConfig, EstadoTeclado estadoTeclado) {
        this.contexto = contexto;
        this.repositorioConfig = repositorioConfig;
        this.estadoTeclado = estadoTeclado;
    }

    public int calcularAltoReferencia(int anchoPantalla) {
        actualizarConfiguracionSiCambio();
        
        // 1. OPTIMIZACIÓN: Si ya se calculó antes, lo devolvemos inmediatamente
        if (altoReferencia > 0) return altoReferencia;

        // 2. Si las teclas ya están en memoria, usamos esas en lugar de volver a leer el XML
        List<List<Tecla>> filasRef = cacheCalculado.get(ModoTeclado.LETRAS);
        if (filasRef == null) {
            filasRef = parsearXmlParaModo(ModoTeclado.LETRAS);
        }
        
        float sumaMultiplicadores = 0f;
        for (List<Tecla> fila : filasRef) {
            if (!fila.isEmpty()) {
                sumaMultiplicadores += fila.get(0).obtenerMultiplicadorAlto();
            }
        }

        if (sumaMultiplicadores == 0f) return 0;
        return Math.round(altoFilaBase * sumaMultiplicadores);
    }

    public void construirTeclado(int anchoPantalla, ModoTeclado modoActual) {
        actualizarConfiguracionSiCambio();
        int paginaActual = estadoTeclado.obtenerPaginaBarra();

        if (anchoPantalla != anchoAnterior || paginaActual != paginaCargada) {
            invalidarCache();
            anchoAnterior = anchoPantalla;
            paginaCargada = paginaActual;
        }

        if (altoReferencia == 0) {
            // PERF-3: Leemos el XML de las letras UNA SOLA VEZ
            List<List<Tecla>> letrasCrudas = parsearXmlParaModo(ModoTeclado.LETRAS);
            
            // Calculamos el alto de referencia usando estas mismas letras
            float sumaMultiplicadores = 0f;
            for (List<Tecla> fila : letrasCrudas) {
                if (!fila.isEmpty()) sumaMultiplicadores += fila.get(0).obtenerMultiplicadorAlto();
            }
            altoReferencia = (sumaMultiplicadores == 0f) ? 0 : Math.round(altoFilaBase * sumaMultiplicadores);

            // Pre-calculamos sus coordenadas y las guardamos en caché al instante
            aplicarCoordenadas(letrasCrudas, anchoPantalla, ModoTeclado.LETRAS, true);
            cacheCalculado.put(ModoTeclado.LETRAS, letrasCrudas);
        }

        matrizActual = cacheCalculado.computeIfAbsent(modoActual, modo -> {
            List<List<Tecla>> filas = parsearXmlParaModo(modo);
            aplicarCoordenadas(filas, anchoPantalla, modo, false);
            return filas;
        });
        
        altoTotalTeclado = calcularAltoTotalReal(matrizActual);
    }

    

    

    private void actualizarConfiguracionSiCambio() {
        int nuevaAltura = repositorioConfig.leerAlturaTeclado();
        boolean nuevaBarra = repositorioConfig.leerMostrarBarra();

        if (nuevaAltura != altoFilaBase || nuevaBarra != mostrarBarraBase) {
            altoFilaBase = nuevaAltura;
            mostrarBarraBase = nuevaBarra;
            invalidarCache();
        }
    }

    private List<List<Tecla>> parsearXmlParaModo(ModoTeclado modo) {
        boolean necesitaBarra = mostrarBarraBase
                && modo != ModoTeclado.PORTAPAPELES
                && modo != ModoTeclado.EMOJIS;
        List<List<Tecla>> todasLasFilas = new ArrayList<>();

        if (necesitaBarra) {
            String xmlBarra = "barra_universal_" + estadoTeclado.obtenerPaginaBarra();
            List<List<Tecla>> filasBarra = LectorXML.parsear(contexto, xmlBarra);
            for (List<Tecla> fila : filasBarra) {
                for (Tecla t : fila) t.setEstiloTransparente(true);
            }
            todasLasFilas.addAll(filasBarra);
        }

        if (!modo.archivoXml.isEmpty()) {
            todasLasFilas.addAll(LectorXML.parsear(contexto, modo.archivoXml));
        }

        return todasLasFilas;
    }

    private void aplicarCoordenadas(List<List<Tecla>> filas, int anchoPantalla, ModoTeclado modo, boolean esModoPrincipal) {
        float sumaMultiplicadores = 0f;
        for (List<Tecla> fila : filas) {
            if (!fila.isEmpty()) {
                sumaMultiplicadores += fila.get(0).obtenerMultiplicadorAlto();
            }
        }

        if (sumaMultiplicadores == 0f) return;
        int altoNatural = Math.round(altoFilaBase * sumaMultiplicadores);
        
        int altoTotalAsignado = altoNatural;
        if (esModoPrincipal) {
            altoReferencia = altoNatural;
        } else if (altoReferencia > 0) {
            if (modo != ModoTeclado.PORTAPAPELES) {
                altoTotalAsignado = altoReferencia;
            }
        }

        int posY = 0;
        for (List<Tecla> fila : filas) {
            if (fila.isEmpty()) continue;
            float multiplicador = fila.get(0).obtenerMultiplicadorAlto();
            int altoFila = Math.round(altoTotalAsignado * (multiplicador / sumaMultiplicadores));
            // CORRECCIÓN BUG RENDIMIENTO: Cálculos de ancho extraídos fuera del bucle interno
            float totalPesos = 0f;
            for (Tecla p : fila) totalPesos += p.obtenerPeso();
            float anchoUnidad = (float) anchoPantalla / totalPesos;
            
            int posX = 0;
            for (int i = 0; i < fila.size(); i++) {
                Tecla t = fila.get(i);
                int ancho = (i == fila.size() - 1) ? (anchoPantalla - posX) : Math.round(anchoUnidad * t.obtenerPeso());
                t.aplicarGeometria(posX, posY, ancho, altoFila);
                posX += ancho;
            }
            posY += altoFila;
        }
    }

    private int calcularAltoTotalReal(List<List<Tecla>> filas) {
        int alto = 0;
        for (List<Tecla> fila : filas) {
            if (!fila.isEmpty()) alto += fila.get(0).obtenerAlto();
        }
        
        return alto;
    }

    public void invalidarCache() {
        cacheCalculado.clear();
        altoReferencia = 0;
    }

    public List<List<Tecla>> obtenerMatriz() { return matrizActual; }
    public int obtenerAltoTotal() { return altoTotalTeclado; }
    public int obtenerAltoReferencia() { return altoReferencia > 0 ? altoReferencia : altoTotalTeclado; }
    public boolean isMostrarBarra() { return mostrarBarraBase; }
}