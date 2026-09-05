package com.ncy.utilidades;

public class ConversionAltura {
    private static final int SEEKBAR_MAX = 50;
    private static final int PORCENTAJE_BASE = 50;

    public static int pxAPorcentaje(int px, int base) {
        return Math.round((px / (float) base) * 100);
    }

    public static int porcentajeAProgreso(int pct) {
        return Math.max(0, Math.min((pct - PORCENTAJE_BASE) / 2, SEEKBAR_MAX));
    }

    public static int progresoAPorcentaje(int progreso) {
        return (progreso * 2) + PORCENTAJE_BASE;
    }

    public static int getMax() { return SEEKBAR_MAX; }
}