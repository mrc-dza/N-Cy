package com.ncy.utilidades;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import com.ncy.modelo.*;
import java.util.ArrayList;
import java.util.List;

public class LectorXML {

    private static final String TAG = "LectorXML";

    public static List<List<Tecla>> parsear(Context contexto, String nombreArchivo) {
        List<List<Tecla>> filas = new ArrayList<>();
        List<Tecla> filaActual = null;
        float alturaFilaActual = 1f;

        // Validamos si el recurso existe antes de intentar abrirlo (Fail-Fast)
        int idRecurso = contexto.getResources().getIdentifier(nombreArchivo, "xml", contexto.getPackageName());
        if (idRecurso == 0) {
            Log.e(TAG, "No se encontró el recurso XML: " + nombreArchivo);
            return filas;
        }

        // OPTIMIZACIÓN: try-with-resources cierra el XML de la memoria RAM automáticamente
        try (XmlResourceParser parser = contexto.getResources().getXml(idRecurso)) {
            int tipoEvento = parser.getEventType();

            while (tipoEvento != XmlResourceParser.END_DOCUMENT) {
                if (tipoEvento == XmlResourceParser.START_TAG) {
                    String nombreEtiqueta = parser.getName();

                    // Uso de comparaciones seguras (Yoda conditions)
                    if ("row".equals(nombreEtiqueta)) {
                        filaActual = new ArrayList<>();
                        String alturaStr = parser.getAttributeValue(null, "altura");
                        alturaFilaActual = (alturaStr != null) ? Float.parseFloat(alturaStr) : 1f;
                        
                    } else if ("key".equals(nombreEtiqueta) && filaActual != null) {
                        String pesoStr = parser.getAttributeValue(null, "peso");
                        String key0    = parser.getAttributeValue(null, "key0");

                        String aliasCentral = (key0 != null) ? key0 : " ";
                        Tecla teclaCentral = FabricaTeclas.crearDesdeAlias(aliasCentral, contexto);

                        teclaCentral.fijarPeso((pesoStr != null) ? Float.parseFloat(pesoStr) : 1f);
                        teclaCentral.fijarMultiplicadorAlto(alturaFilaActual);

                        for (int i = 1; i <= 8; i++) {
                            String aliasGesto  = parser.getAttributeValue(null, "key"    + i);
                            String aliasOculto = parser.getAttributeValue(null, "oculto" + i);

                            if (aliasGesto != null && aliasOculto != null) {
                                Log.w(TAG, "key" + i + " y oculto" + i + " definidos a la vez en tecla '" + aliasCentral + "'. Se usa oculto" + i + ".");
                            }

                            if (aliasOculto != null) {
                                teclaCentral.establecerGesto(i, FabricaTeclas.crearDesdeAlias(aliasOculto, contexto), true);
                            } else if (aliasGesto != null) {
                                teclaCentral.establecerGesto(i, FabricaTeclas.crearDesdeAlias(aliasGesto, contexto), false);
                            }
                        }

                        filaActual.add(teclaCentral);
                    }

                } else if (tipoEvento == XmlResourceParser.END_TAG) {
                    if ("row".equals(parser.getName()) && filaActual != null) {
                        filas.add(filaActual);
                        filaActual = null;
                    }
                }

                tipoEvento = parser.next();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error parseando: " + nombreArchivo, e);
        }

        return filas;
    }
}