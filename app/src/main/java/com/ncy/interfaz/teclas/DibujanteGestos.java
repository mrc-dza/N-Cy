package com.ncy.interfaz.teclas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.ncy.estado.EstadoTeclado;
import com.ncy.modelo.InfoVisual;
import com.ncy.modelo.Tecla;
import com.ncy.modelo.TeclaEmoji;
import com.ncy.utilidades.TemaVisual;

public class DibujanteGestos {

    private final Paint pinturaGesto;
    private final Paint pinturaGestoActivo;
    private final CacheDrawables cache;
    private final ResolvedorIconoIme resolvedorIme;
    private final float densidad;
    private final float[] coordsReusables = new float[2];
    private TemaVisual tema;
    private final GestorAparienciaTecla gestorApariencia;

    public DibujanteGestos(Paint pinturaGesto, Paint pinturaGestoActivo,
                        CacheDrawables cache, ResolvedorIconoIme resolvedorIme, 
                        float densidad, TemaVisual tema, 
                        GestorAparienciaTecla gestorApariencia) {  
        this.pinturaGesto      = pinturaGesto;
        this.pinturaGestoActivo = pinturaGestoActivo;
        this.cache             = cache;
        this.resolvedorIme     = resolvedorIme;
        this.densidad          = densidad;
        this.tema              = tema;
        this.gestorApariencia  = gestorApariencia; 
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

        InfoVisual info = gestorApariencia.obtenerInfoVisual(gesto, estado, tema);

        int iconoResId  = resolvedorIme.resolver(info.iconoResId());
        
        // ¡CÓDIGO LIMPIO! El dibujante ya no sabe qué tipo de tecla es, solo pinta la pista dictada por InfoVisual
        String pista = info.pistaGestoNormal(); 
        
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

        // Mismo margen que aplica dibujarFondo() — área real visible de la tecla
        float mx = (tema.getSeparacionColumnas() * densidad) / 2f;
        float my = (tema.getSeparacionFilas()    * densidad) / 2f;

        float vx = rx + mx;
        float vy = ry + my;
        float vw = rw - mx * 2f;
        float vh = rh - my * 2f;

        // OPTIMIZACIÓN: 
        // 1. Más padding X (15%) para darle espacio a símbolos anchos como "[]"
        // 2. Menos padding Y (5%) para maximizar la separación vertical
        float padX = vw * 0.15f;
        float padY = vh * 0.05f;

        float xLeft   = vx + padX;
        float xCenter = vx + vw * 0.50f;
        float xRight  = vx + vw - padX;

        // --- LA MAGIA MATEMÁTICA ---
        // Ascent es negativo. Al restarlo, pegamos el texto exactamente al límite superior visible.
        float yTop    = vy + padY - pinturaGesto.ascent(); 
        
        // Centrado vertical perfecto al 50% de la tecla
        float yMid    = vy + (vh / 2f) - (pinturaGesto.ascent() + pinturaGesto.descent()) / 2f; 
     
        float yBottom = vy + vh - padY - pinturaGesto.descent();

        switch (indice) {
            case 1 -> { coordsReusables[0] = xLeft;   coordsReusables[1] = yTop;    }
            case 7 -> { coordsReusables[0] = xCenter; coordsReusables[1] = yTop;    }
            case 2 -> { coordsReusables[0] = xRight;  coordsReusables[1] = yTop;    }
            case 5 -> { coordsReusables[0] = xLeft;   coordsReusables[1] = yMid;    }
            case 6 -> { coordsReusables[0] = xRight;  coordsReusables[1] = yMid;    }
            case 3 -> { coordsReusables[0] = xLeft;   coordsReusables[1] = yBottom; }
            case 8 -> { coordsReusables[0] = xCenter; coordsReusables[1] = yBottom; }
            case 4 -> { coordsReusables[0] = xRight;  coordsReusables[1] = yBottom; }
            default -> { coordsReusables[0] = 0;      coordsReusables[1] = 0;       }
        }
        return coordsReusables;
    }

    private void configurarAlineacion(int indice) {
        Paint.Align alineacion = switch (indice) {
            case 7, 8       -> Paint.Align.CENTER;
            case 2, 4, 6    -> Paint.Align.RIGHT;
            default         -> Paint.Align.LEFT;
        };
        pinturaGesto.setTextAlign(alineacion);
        pinturaGestoActivo.setTextAlign(alineacion);
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


    public void actualizar(Paint gesto, Paint gestoActivo, TemaVisual tema) {
        this.pinturaGesto.set(gesto);
        this.pinturaGestoActivo.set(gestoActivo);
        this.tema = tema;
    }
}