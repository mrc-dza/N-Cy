package com.ncy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ncy.utilidades.DetectorEstadoIme;
import com.ncy.utilidades.TemaVisualUI;
import com.ncy.utilidades.UiHelper;

public class MainActivity extends Activity {

    private View raizMain, lineaProgreso;
    private TextView txtTituloMain, txtSubtituloMain, tituloPaso1, circuloPaso1, circuloPaso2, 
                     subtextoPaso1, tituloPaso2, subtextoPaso2, btnPaso1, btnPaso2, iconoBanner, textoBanner;
    private LinearLayout cardPaso1, cardPaso2, bannerActivo;
    private ImageView btnConfiguraciones, btnToggleTemaUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Garantizar init por si la app muere
        AppServicios.init(this);
        setContentView(R.layout.activity_main);
        
        raizMain = findViewById(R.id.raizMain);
        txtTituloMain = findViewById(R.id.txtTituloMain);
        txtSubtituloMain = findViewById(R.id.txtSubtituloMain);
        tituloPaso1 = findViewById(R.id.tituloPaso1);
        cardPaso1 = findViewById(R.id.cardPaso1);
        cardPaso2 = findViewById(R.id.cardPaso2);
        bannerActivo = findViewById(R.id.bannerActivo);
        circuloPaso1 = findViewById(R.id.circuloPaso1);
        circuloPaso2 = findViewById(R.id.circuloPaso2);
        subtextoPaso1 = findViewById(R.id.subtextoPaso1);
        tituloPaso2 = findViewById(R.id.tituloPaso2);
        subtextoPaso2 = findViewById(R.id.subtextoPaso2);
        btnPaso1 = findViewById(R.id.btnPaso1);
        btnPaso2 = findViewById(R.id.btnPaso2);
        lineaProgreso = findViewById(R.id.lineaProgreso);
        btnConfiguraciones = findViewById(R.id.btnConfiguraciones);
        iconoBanner = findViewById(R.id.iconoBanner);
        textoBanner = findViewById(R.id.textoBanner);
        btnToggleTemaUI = findViewById(R.id.btnToggleTemaUI);

        btnToggleTemaUI.setOnClickListener(v -> { 
            AppServicios.getGestorTemaUI().alternarTema(); 
            recreate(); 
        });
        
        cardPaso1.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)));
        cardPaso2.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.showInputMethodPicker();
        });
        btnConfiguraciones.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Solo actualizar estado visual, NO instanciar dependencias de nuevo
        TemaVisualUI temaUI = AppServicios.getGestorTemaUI().obtenerTemaActivo();

        raizMain.setBackgroundColor(temaUI.getColorFondoGeneral());
        txtTituloMain.setTextColor(temaUI.getColorAcento());
        txtSubtituloMain.setTextColor(temaUI.getColorTextoSecundario());
        tituloPaso1.setTextColor(temaUI.getColorTextoPrincipal());
        iconoBanner.setTextColor(temaUI.getColorAcento());
        textoBanner.setTextColor(temaUI.getColorAcento());
        btnConfiguraciones.setColorFilter(temaUI.getColorTextoPrincipal());
        
        btnToggleTemaUI.setImageResource(AppServicios.getGestorTemaUI().esOscuro() ? R.drawable.ic_sol : R.drawable.ic_luna);
        btnToggleTemaUI.setColorFilter(temaUI.getColorTextoPrincipal());
        UiHelper.aplicarFondoBanner(bannerActivo, temaUI.getColorFondoBanner(), temaUI.getColorBordeCardActivo());

        actualizarEstado(temaUI);
    }

    private void actualizarEstado(TemaVisualUI tema) {
        boolean hab = DetectorEstadoIme.estaHabilitado(this);
        boolean sel = DetectorEstadoIme.estaSeleccionado(this);
        float rCard = 12f;

        circuloPaso1.setText(hab ? "✓" : "1");
        circuloPaso1.setTextColor(tema.getColorTextoSobreAcento());
        UiHelper.aplicarFondoCirculo(circuloPaso1, tema.getColorAcento());
        lineaProgreso.setBackgroundColor(hab ? tema.getColorAcento() : tema.getColorBordeCard());
        subtextoPaso1.setText(getString(hab ? R.string.main_step1_desc_on : R.string.main_step1_desc_off));
        subtextoPaso1.setTextColor(hab ? tema.getColorAcento() : tema.getColorTextoSecundario());
        btnPaso1.setTextColor(tema.getColorTextoSobreAcento());
        UiHelper.aplicarFondoPill(btnPaso1, tema.getColorAcento(), getResources());
        UiHelper.aplicarFondoCard(cardPaso1, tema.getColorFondoCard(), hab ? tema.getColorBordeCard() : tema.getColorBordeCardActivo(), rCard);

        tituloPaso2.setTextColor(tema.getColorTextoPrincipal());
        subtextoPaso2.setTextColor(tema.getColorTextoSecundario());
        circuloPaso2.setText(sel ? "✓" : "2");
        circuloPaso2.setTextColor(hab ? tema.getColorTextoSobreAcento() : tema.getColorTextoPrincipal());
        UiHelper.aplicarFondoCirculo(circuloPaso2, hab ? tema.getColorAcento() : tema.getColorFondoCirculo());
        btnPaso2.setText(getString(hab ? R.string.main_step2_btn_on : R.string.main_step2_btn_off));
        btnPaso2.setTextColor(hab ? tema.getColorTextoSobreAcento() : tema.getColorTextoPrincipal());
        UiHelper.aplicarFondoPill(btnPaso2, hab ? tema.getColorAcento() : tema.getColorFondoCard(), getResources());
        UiHelper.aplicarFondoCard(cardPaso2, tema.getColorFondoCard(), (hab || sel) ? tema.getColorBordeCardActivo() : tema.getColorBordeCard(), rCard);

        bannerActivo.setVisibility(sel ? View.VISIBLE : View.GONE);
    }
}