package com.ncy.datos;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class ConfigItem {
    public final String titulo;
    public final String subtitulo;

    public ConfigItem(String t, String s) { 
        this.titulo = t; 
        this.subtitulo = s; 
    }

    public static class Switch extends ConfigItem {
        public final BooleanSupplier lector;
        public final Consumer<Boolean> escritor;
        public Switch(String t, String s, BooleanSupplier l, Consumer<Boolean> e) {
            super(t, s); 
            this.lector = l; 
            this.escritor = e;
        }
    }
    
    public static class Slider extends ConfigItem {
        public final int min, max;
        public final java.util.function.IntSupplier lector;
        public final java.util.function.IntConsumer escritor;
        public Slider(String t, String s, int min, int max, java.util.function.IntSupplier l, java.util.function.IntConsumer e) {
            super(t, s); 
            this.min = min; this.max = max; this.lector = l; this.escritor = e;
        }
    }

    public static class Info extends ConfigItem {
        public final String valorActual;
        public final Runnable accionClick;
        
        public Info(String t, String s, String valor, Runnable accion) {
            super(t, s);
            this.valorActual = valor;
            this.accionClick = accion;
        }
    }
}