package com.ncy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ncy.utilidades.DetectorEstadoIme;
import com.ncy.utilidades.GestorTemaUI;
import com.ncy.utilidades.TemaVisualUI;
import com.ncy.utilidades.UiHelper;

public class MainActivity extends Activity {

    private ScrollView   raizMain;
    private TextView     txtTituloMain;
    private TextView     txtSubtituloMain;
    private TextView     tituloPaso1;

    private LinearLayout cardPaso1;
    private LinearLayout cardPaso2;
    private LinearLayout bannerActivo;
    private TextView     circuloPaso1;
    private TextView     circuloPaso2;
    private TextView     subtextoPaso1;
    private TextView     tituloPaso2;
    private TextView     subtextoPaso2;
    private TextView     btnPaso1;
    private TextView     btnPaso2;
    private View         lineaProgreso;
    private ImageView    btnConfiguraciones;
    private TextView     iconoBanner;
    private TextView     textoBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GestorTemaUI.getInstance().inicializar(this);

        setContentView(R.layout.activity_main);
        
        raizMain           = findViewById(R.id.raizMain);
        txtTituloMain      = findViewById(R.id.txtTituloMain);
        txtSubtituloMain   = findViewById(R.id.txtSubtituloMain);
        tituloPaso1        = findViewById(R.id.tituloPaso1);
        
        cardPaso1          = findViewById(R.id.cardPaso1);
        cardPaso2          = findViewById(R.id.cardPaso2);
        bannerActivo       = findViewById(R.id.bannerActivo);
        circuloPaso1       = findViewById(R.id.circuloPaso1);
        circuloPaso2       = findViewById(R.id.circuloPaso2);
        subtextoPaso1      = findViewById(R.id.subtextoPaso1);
        tituloPaso2        = findViewById(R.id.tituloPaso2);
        subtextoPaso2      = findViewById(R.id.subtextoPaso2);
        btnPaso1           = findViewById(R.id.btnPaso1);
        btnPaso2           = findViewById(R.id.btnPaso2);
        lineaProgreso      = findViewById(R.id.lineaProgreso);
        btnConfiguraciones = findViewById(R.id.btnConfiguraciones);
        iconoBanner        = findViewById(R.id.iconoBanner);
        textoBanner        = findViewById(R.id.textoBanner);

        // MODIFICADO: Cambiamos TextView por ImageView para usar los drawables XML
        ImageView btnToggleTemaUI = new ImageView(this);
        
        // Asignamos el icono correspondiente (asegúrate de tener ic_sol e ic_luna en res/drawable)
        btnToggleTemaUI.setImageResource(GestorTemaUI.getInstance().esOscuro() ? R.drawable.ic_sol : R.drawable.ic_luna);
        
        // Aplicamos el color del texto principal del tema activo para que combine
        btnToggleTemaUI.setColorFilter(GestorTemaUI.getInstance().obtenerTemaActivo().getColorTextoPrincipal());
        
        // Añadimos padding y el efecto visual de pulsación (ripple) nativo de Android
        btnToggleTemaUI.setPadding(48, 48, 48, 48);
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        btnToggleTemaUI.setBackgroundResource(outValue.resourceId);
        
        btnToggleTemaUI.setOnClickListener(v -> {
            GestorTemaUI.getInstance().alternarTema();
            recreate();
        });

        FrameLayout frameLayout = (FrameLayout) raizMain.getChildAt(0);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = android.view.Gravity.START | android.view.Gravity.TOP;
        frameLayout.addView(btnToggleTemaUI, params);

        cardPaso1.setOnClickListener(v ->
            startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        );
        
        cardPaso2.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.showInputMethodPicker();
        });
        
        btnConfiguraciones.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GestorTemaUI.getInstance().inicializar(this);
        
        TemaVisualUI temaUI = GestorTemaUI.getInstance().obtenerTemaActivo();

        raizMain.setBackgroundColor(temaUI.getColorFondoGeneral());
        txtTituloMain.setTextColor(temaUI.getColorAcento());
        txtSubtituloMain.setTextColor(temaUI.getColorTextoSecundario());
        tituloPaso1.setTextColor(temaUI.getColorTextoPrincipal());
        
        iconoBanner.setTextColor(temaUI.getColorAcento());
        textoBanner.setTextColor(temaUI.getColorAcento());
        
        btnConfiguraciones.setColorFilter(temaUI.getColorTextoPrincipal());
        aplicarFondoBanner(bannerActivo, temaUI);

        actualizarEstado(temaUI);
    }

    private void actualizarEstado(TemaVisualUI temaUI) {
        boolean habilitado   = DetectorEstadoIme.estaHabilitado(this);
        boolean seleccionado = DetectorEstadoIme.estaSeleccionado(this);

        if (habilitado) {
            circuloPaso1.setText("✓");
            circuloPaso1.setTextColor(temaUI.getColorTextoSobreAcento());
            aplicarFondoCirculo(circuloPaso1, temaUI.getColorAcento());
            lineaProgreso.setBackgroundColor(temaUI.getColorAcento());
            subtextoPaso1.setText("Habilitado correctamente");
            subtextoPaso1.setTextColor(temaUI.getColorAcento());
            btnPaso1.setTextColor(temaUI.getColorTextoSobreAcento());
            aplicarFondoPill(btnPaso1, temaUI.getColorAcento());
            UiHelper.aplicarFondoCard(cardPaso1, temaUI.getColorFondoCard(), temaUI.getColorBordeCard());

            tituloPaso2.setTextColor(temaUI.getColorTextoPrincipal());
            subtextoPaso2.setTextColor(temaUI.getColorTextoSecundario());
            aplicarFondoCirculo(circuloPaso2, temaUI.getColorAcento());
            circuloPaso2.setTextColor(temaUI.getColorTextoSobreAcento());
            aplicarFondoPill(btnPaso2, temaUI.getColorAcento());
            btnPaso2.setText("Seleccionar →");
            btnPaso2.setTextColor(temaUI.getColorTextoSobreAcento());
        } else {
            circuloPaso1.setText("1");
            circuloPaso1.setTextColor(temaUI.getColorTextoSobreAcento());
            aplicarFondoCirculo(circuloPaso1, temaUI.getColorAcento());
            lineaProgreso.setBackgroundColor(temaUI.getColorBordeCard());
            subtextoPaso1.setText("Activa N-Cy en los ajustes del sistema");
            subtextoPaso1.setTextColor(temaUI.getColorTextoSecundario());
            btnPaso1.setTextColor(temaUI.getColorTextoSobreAcento());
            aplicarFondoPill(btnPaso1, temaUI.getColorAcento());
            UiHelper.aplicarFondoCard(cardPaso1, temaUI.getColorFondoCard(), temaUI.getColorBordeCardActivo());

            tituloPaso2.setTextColor(temaUI.getColorTextoPrincipal());
            subtextoPaso2.setTextColor(temaUI.getColorTextoSecundario());
            aplicarFondoCirculo(circuloPaso2, temaUI.getColorFondoCirculo());
            circuloPaso2.setTextColor(temaUI.getColorTextoPrincipal());
            aplicarFondoPill(btnPaso2, temaUI.getColorFondoCard());
            btnPaso2.setText("Pendiente");
            btnPaso2.setTextColor(temaUI.getColorTextoPrincipal());
        }

        if (seleccionado) {
            circuloPaso2.setText("✓");
            UiHelper.aplicarFondoCard(cardPaso2, temaUI.getColorFondoCard(), temaUI.getColorBordeCardActivo());
            bannerActivo.setVisibility(View.VISIBLE);
        } else {
            bannerActivo.setVisibility(View.GONE);
            if (habilitado) {
                UiHelper.aplicarFondoCard(cardPaso2, temaUI.getColorFondoCard(), temaUI.getColorBordeCardActivo());
            } else {
                UiHelper.aplicarFondoCard(cardPaso2, temaUI.getColorFondoCard(), temaUI.getColorBordeCard());
            }
        }
    }

    private void aplicarFondoPill(TextView v, int color) {
        v.setBackground(UiHelper.crearFondo(color, UiHelper.dpAPx(getResources(), 12), 0, 0));
    }

    private void aplicarFondoBanner(LinearLayout v, TemaVisualUI temaUI) {
        v.setBackground(UiHelper.crearFondo(temaUI.getColorFondoBanner(), UiHelper.dpAPx(getResources(), 10), temaUI.getColorBordeCardActivo(), UiHelper.dpAPx(getResources(), 1)));
    }

    private void aplicarFondoCirculo(TextView v, int color) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setColor(color);
        v.setBackground(d);
    }
}