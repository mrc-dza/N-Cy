package com.ncy.utilidades;

public class DetectorGestos {

    private static final int UMBRAL_MINIMO_PX = 40;
    private static final int UMBRAL_CONFIRMACION_PX = 80;

    private static final int UMBRAL_MINIMO_SQ = UMBRAL_MINIMO_PX * UMBRAL_MINIMO_PX;
    private static final int UMBRAL_CONFIRMACION_SQ = UMBRAL_CONFIRMACION_PX * UMBRAL_CONFIRMACION_PX;

    public boolean superoUmbral(float xInicial, float yInicial, float xActual, float yActual) {
        return distanciaSq(xInicial, yInicial, xActual, yActual) >= UMBRAL_MINIMO_SQ;
    }

    public boolean superoUmbralConfirmacion(float xInicial, float yInicial, float xActual, float yActual) {
        return distanciaSq(xInicial, yInicial, xActual, yActual) >= UMBRAL_CONFIRMACION_SQ;
    }

    public int evaluarGesto(float xInicial, float yInicial, float xFinal, float yFinal) {
        if (!superoUmbral(xInicial, yInicial, xFinal, yFinal)) return 0;
        float deltaX = xFinal - xInicial;
        float deltaY = yFinal - yInicial;

        double angulo = Math.toDegrees(Math.atan2(deltaY, deltaX));
        return determinarIndicePorAngulo(angulo);
    }

    private float distanciaSq(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1, dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    private int determinarIndicePorAngulo(double angulo) {
        int octante = (int) Math.round(angulo / 45.0);
        return switch (octante) {
            case 0       -> 6; // Derecha (E)
            case 1       -> 4; // Abajo-Derecha (SE)
            case 2       -> 8; // Abajo (S)
            case 3       -> 3; // Abajo-Izquierda (SW)
            case 4, -4   -> 5; // Izquierda (W)
            case -3      -> 1; // Arriba-Izquierda (NW)
            case -2      -> 7; // Arriba (N)
            case -1      -> 2; // Arriba-Derecha (NE)
            default      -> 0;
        };
    }
}