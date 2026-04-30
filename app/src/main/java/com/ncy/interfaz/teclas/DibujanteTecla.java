package com.ncy.interfaz.teclas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.ncy.estado.EstadoTeclado;
import com.ncy.modelo.InfoVisual;
import com.ncy.modelo.Tecla;
import com.ncy.utilidades.TemaVisual;

public class DibujanteTecla {

    private final Paint pinturaFondo;
    private Paint pinturaTexto;
    private Paint pinturaEspecial;
    private final CacheDrawables cache;
    private DibujanteGestos dibujanteGestos;
    private final ResolvedorIconoIme resolvedorIme;
    
    private final RectF rectanguloReusable = new RectF();
    private TemaVisual tema;

    private final Paint pinturaEtiquetaCache = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int colorEtiquetaCache = 0;
    private float tamanioEtiquetaCache = 0f;
    
    private final float densidadGrafica;
    private final float densidadTexto;

    public DibujanteTecla(Context contexto, TemaVisual tema) {
        this.resolvedorIme = new ResolvedorIconoIme();
        this.cache = new CacheDrawables(contexto);
        this.tema = tema;

        DisplayMetrics metricas = contexto.getResources().getDisplayMetrics();
        this.densidadGrafica = metricas.density;
        this.densidadTexto = metricas.scaledDensity;

        pinturaFondo = new Paint(Paint.ANTI_ALIAS_FLAG);
        pinturaFondo.setStyle(Paint.Style.FILL);
        
        pinturaTexto = construirPinturaTexto(tema.getColorTextoPrincipal(), tema.getTamanioTextoTecla() * densidadTexto);
        pinturaEspecial = construirPinturaTexto(tema.getColorTextoEspecial(), tema.getTamanioTextoEspecial() * densidadTexto);
        
        Paint pinturaGesto = construirPinturaTexto(tema.getColorAcento(), tema.getTamanioTextoGesto() * densidadTexto);
        Paint pinturaGestoActivo = construirPinturaTexto(tema.getColorGestoActivo(), tema.getTamanioTextoGestoActivo() * densidadTexto);
        this.dibujanteGestos = new DibujanteGestos(pinturaGesto, pinturaGestoActivo, cache, resolvedorIme, densidadGrafica);
    }

    public void actualizarTema(TemaVisual nuevoTema) {
        this.tema = nuevoTema;
        
        this.pinturaTexto.set(construirPinturaTexto(tema.getColorTextoPrincipal(), tema.getTamanioTextoTecla() * densidadTexto));
        this.pinturaEspecial.set(construirPinturaTexto(tema.getColorTextoEspecial(), tema.getTamanioTextoEspecial() * densidadTexto));
        
        Paint pinturaGesto = construirPinturaTexto(tema.getColorAcento(), tema.getTamanioTextoGesto() * densidadTexto);
        Paint pinturaGestoActivo = construirPinturaTexto(tema.getColorGestoActivo(), tema.getTamanioTextoGestoActivo() * densidadTexto);
        
        if (this.dibujanteGestos != null) {
            this.dibujanteGestos.actualizarPinturas(pinturaGesto, pinturaGestoActivo);
        } else {
            this.dibujanteGestos = new DibujanteGestos(pinturaGesto, pinturaGestoActivo, cache, resolvedorIme, densidadGrafica);
        }
        
        this.cache.limpiar();
    }

    public void dibujar(Canvas lienzo, Tecla tecla, EstadoTeclado estado, boolean estaPresionada, int gestoEnProgreso) {
        InfoVisual info = tecla.obtenerInfoVisual(estado);
        Paint pintura = info.usarPinturaEspecial ? pinturaEspecial : pinturaTexto;
        
        int colorOriginal = pintura.getColor();
        if (info.colorTextoPersonalizado != null) {
            pintura.setColor(info.colorTextoPersonalizado);
        }

        int iconoResId = resolvedorIme.resolver(info.iconoResId);

        int x = tecla.obtenerX(), y = tecla.obtenerY();
        int w = tecla.obtenerAncho(), h = tecla.obtenerAlto();

        if (tecla.isEstiloTransparente()) {
            int colorEtiqueta = estaPresionada
                ? tema.getColorEtiquetaBarraPresionada()
                : tema.getColorEtiquetaBarra();
            
            float tamanio = tema.getTamanioTextoEspecial() * densidadTexto;
            actualizarPinturaEtiqueta(colorEtiqueta, tamanio); // Usamos nuestra nueva caché

            if (iconoResId != 0) {
                dibujarIcono(lienzo, iconoResId, x, y, w, h, colorEtiqueta, 1f);
            } else {
                float cx = x + w / 2f;
                float cy = y + h / 2f - (pinturaEtiquetaCache.descent() + pinturaEtiquetaCache.ascent()) / 2f;
                lienzo.drawText(info.texto, cx, cy, pinturaEtiquetaCache); // Usamos la caché aquí también
            }

            if (info.colorTextoPersonalizado != null) {
                pintura.setColor(colorOriginal);
            }
            return;
        } else {
            dibujarFondo(lienzo, x, y, w, h, info.colorFondo, estaPresionada, tema.getRadioEsquinasTecla());
        }

        if (iconoResId != 0) {
            dibujarIcono(lienzo, iconoResId, x, y, w, h, pintura.getColor(), 1f);
        } else {
            float cx = x + w / 2f;
            float cy = y + h / 2f - (pintura.descent() + pintura.ascent()) / 2f;
            lienzo.drawText(info.texto, cx, cy, pintura);
        }

        dibujanteGestos.dibujarPistas(lienzo, tecla, estado, gestoEnProgreso);
        if (info.colorTextoPersonalizado != null) {
            pintura.setColor(colorOriginal);
        }
    }

    public void dibujarConOffset(Canvas lienzo, Tecla tecla, EstadoTeclado estado, boolean estaPresionada, int gestoEnProgreso, int offsetY) {
        lienzo.save();
        lienzo.translate(0, -offsetY);
        dibujar(lienzo, tecla, estado, estaPresionada, gestoEnProgreso);
        lienzo.restore();
    }

    public void limpiarCache() {
        cache.limpiar();
    }

  

    private void dibujarFondo(Canvas lienzo, int x, int y, int w, int h, int colorBase, boolean presionada, float radioEsquinas) {
        int color = presionada ? mezclarConNegro(colorBase, tema.getFactorOscurecimiento()) : colorBase;
        pinturaFondo.setColor(color);
        
        // 1. Obtenemos la separación TOTAL deseada del tema (escalada a píxeles de la pantalla)
        float separacionX = tema.getSeparacionColumnas() * densidadGrafica;
        float separacionY = tema.getSeparacionFilas() * densidadGrafica;
        
        // 2. Dividimos entre 2. 
        // ¿Por qué? Porque la tecla izquierda cede la mitad del espacio, y la derecha la otra mitad.
        // Al dibujarse juntas, el hueco resultante es exactamente el tamaño que pediste.
        float margenX = separacionX / 2f;
        float margenY = separacionY / 2f;
        
        // 3. Dibujamos el rectángulo encogido
        rectanguloReusable.set(x + margenX, y + margenY, x + w - margenX, y + h - margenY);
        lienzo.drawRoundRect(rectanguloReusable, radioEsquinas * densidadGrafica, radioEsquinas * densidadGrafica, pinturaFondo);
    }

    private void dibujarIcono(Canvas lienzo, int resId, int x, int y, int w, int h, int tinte, float escala) {
        Drawable icono = cache.obtener(resId, tinte);
        if (icono == null) return;
        
        int iw = (int) (icono.getIntrinsicWidth() * escala);
        int ih = (int) (icono.getIntrinsicHeight() * escala);
        int left = x + (w - iw) / 2;
        int top = y + (h - ih) / 2;
        icono.setBounds(left, top, left + iw, top + ih);
        icono.draw(lienzo);
    }

    private int mezclarConNegro(int color, float factor) {
        int r = (int) (Color.red(color) * (1f - factor));
        int g = (int) (Color.green(color) * (1f - factor));
        int b = (int) (Color.blue(color) * (1f - factor));
        return Color.rgb(r, g, b);
    }

    private void actualizarPinturaEtiqueta(int color, float tamanio) {
        if (colorEtiquetaCache != color || tamanioEtiquetaCache != tamanio) {
            pinturaEtiquetaCache.setColor(color);
            pinturaEtiquetaCache.setTextSize(tamanio);
            pinturaEtiquetaCache.setTextAlign(Paint.Align.CENTER);
            colorEtiquetaCache = color;
            tamanioEtiquetaCache = tamanio;
        }
    }

    private Paint construirPinturaTexto(int color, float tamanio) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setTextSize(tamanio);
        p.setTextAlign(Paint.Align.CENTER);
        return p;
    }
}