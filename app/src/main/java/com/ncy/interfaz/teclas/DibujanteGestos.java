package com.ncy.interfaz.teclas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.ncy.estado.EstadoTeclado;
import com.ncy.modelo.InfoVisual;
import com.ncy.modelo.Tecla;
import com.ncy.modelo.TeclaEmoji;

public class DibujanteGestos {

    private final Paint pinturaGesto;
    private final Paint pinturaGestoActivo;
    private final CacheDrawables cache;
    private final ResolvedorIconoIme resolvedorIme;
    private final float densidad;
    private final float[] coordsReusables = new float[2];

    public DibujanteGestos(Paint pinturaGesto, Paint pinturaGestoActivo,
                            CacheDrawables cache, ResolvedorIconoIme resolvedorIme, float densidad) {
        this.pinturaGesto      = pinturaGesto;
        this.pinturaGestoActivo = pinturaGestoActivo;
        this.cache             = cache;
        this.resolvedorIme     = resolvedorIme;
        this.densidad          = densidad;
    }

    public void dibujarPistas(Canvas lienzo, Tecla teclaRaiz,
                               EstadoTeclado estado, int gestoEnProgreso) {
        for (int i = 1; i <= 8; i++) {
            dibujarPista(lienzo, teclaRaiz, i, estado, i == gestoEnProgreso);
        }
    }

    private void dibujarPista(Canvas lienzo, Tecla teclaRaiz,
                               int indiceGesto, EstadoTeclado estado,
                               boolean esPistaActiva) {
                               
        if (teclaRaiz.isGestoOculto(indiceGesto)) return;
        Tecla gesto = teclaRaiz.obtenerGesto(indiceGesto);
        if (gesto == teclaRaiz) return;

        InfoVisual info = gesto.obtenerInfoVisual(estado);
        int iconoResId  = resolvedorIme.resolver(info.iconoResId);
        String pista = (gesto instanceof TeclaEmoji) ? info.texto : info.pistaGestoNormal;

        if (pista.isEmpty() && iconoResId == 0) return;

        float[] coords = calcularCoordenadas(teclaRaiz, indiceGesto);
        configurarAlineacion(indiceGesto);

        if (iconoResId != 0) {
            dibujarIconoPista(lienzo, iconoResId, coords[0], coords[1]);
        } else {
            Paint pintura = esPistaActiva ? pinturaGestoActivo : pinturaGesto;
            lienzo.drawText(pista, coords[0], coords[1], pintura);
        }
    }

    private float[] calcularCoordenadas(Tecla t, int indice) {
        int rx = t.obtenerX(), ry = t.obtenerY();
        int rw = t.obtenerAncho(), rh = t.obtenerAlto();

        float xLeft   = rx + (rw * 0.15f);
        float xCenter = rx + (rw * 0.50f);
        float xRight  = rx + (rw * 0.85f);

        float yTop    = ry + (rh * 0.28f);
        float yMid    = ry + (rh * 0.60f);
        float yBottom = ry + (rh * 0.88f);

        switch (indice) {
            case 1: coordsReusables[0] = xLeft;   coordsReusables[1] = yTop;    break;
            case 7: coordsReusables[0] = xCenter; coordsReusables[1] = yTop;    break;
            case 2: coordsReusables[0] = xRight;  coordsReusables[1] = yTop;    break;
            
            case 5: coordsReusables[0] = xLeft;   coordsReusables[1] = yMid;    break;
            case 6: coordsReusables[0] = xRight;  coordsReusables[1] = yMid;    break;
            
            case 3: coordsReusables[0] = xLeft;   coordsReusables[1] = yBottom; break;
            case 8: coordsReusables[0] = xCenter; coordsReusables[1] = yBottom; break;
            case 4: coordsReusables[0] = xRight;  coordsReusables[1] = yBottom; break;
            
            default: coordsReusables[0] = 0;      coordsReusables[1] = 0;
        }
        return coordsReusables;
    }


    private void configurarAlineacion(int indice) {
        switch (indice) {
            case 7: case 8:
                pinturaGesto.setTextAlign(Paint.Align.CENTER);
                pinturaGestoActivo.setTextAlign(Paint.Align.CENTER);
                break;
            case 2: case 4: case 6:
                pinturaGesto.setTextAlign(Paint.Align.RIGHT);
                pinturaGestoActivo.setTextAlign(Paint.Align.RIGHT);
                break;
            default:
                pinturaGesto.setTextAlign(Paint.Align.LEFT);
                pinturaGestoActivo.setTextAlign(Paint.Align.LEFT);
        }
    }

    private void dibujarIconoPista(Canvas lienzo, int resId, float coordX, float coordY) {
        Drawable icono = cache.obtener(resId, pinturaGesto.getColor());
        if (icono == null) return;
        int iw = (int) (icono.getIntrinsicWidth()  * 0.8f);
        int ih = (int) (icono.getIntrinsicHeight() * 0.8f);
        float left = coordX;
        Paint.Align alineacion = pinturaGesto.getTextAlign();
        if (alineacion == Paint.Align.CENTER) left = coordX - iw / 2f;
        else if (alineacion == Paint.Align.RIGHT) left = coordX - iw;

        float top = coordY - ih * 0.85f;
        icono.setBounds((int) left, (int) top, (int)(left + iw), (int)(top + ih));
        icono.draw(lienzo);
    }

    public void actualizarPinturas(Paint gesto, Paint gestoActivo) {
        this.pinturaGesto.set(gesto);
        this.pinturaGestoActivo.set(gestoActivo);
    }
}