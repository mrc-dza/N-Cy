package com.ncy;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ncy.datos.ConfigItem;
import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.utilidades.ConversionAltura;
import com.ncy.utilidades.TemaVisualUI;
import com.ncy.utilidades.UiHelper;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends Activity {

    private LayoutInflater inflador;
    private int alturaBasePx;
    private TemaVisualUI temaUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppServicios.init(this);
        
        inflador = LayoutInflater.from(this);
        alturaBasePx = AppServicios.getGestorTema().obtenerTemaActivo().getAlturaFilaBasePx();
        temaUI = AppServicios.getGestorTemaUI().obtenerTemaActivo();
        
        setContentView(R.layout.activity_settings);

        // Pintar el fondo principal y cabeceras
        findViewById(R.id.raizSettings).setBackgroundColor(temaUI.getColorFondoGeneral());
        ((TextView) findViewById(R.id.txtSettingsTitle)).setTextColor(temaUI.getColorTextoPrincipal());
        ((TextView) findViewById(R.id.txtSettingsSubtitle)).setTextColor(temaUI.getColorTextoSecundario());
        
        // Estilizar botón de volver
        View btnBack = findViewById(R.id.btnBackSettings);
        ImageView imgBack = findViewById(R.id.imgBackIcon);
        if (btnBack != null && imgBack != null) {
            UiHelper.aplicarBotonVolver(btnBack, temaUI.getColorFondoCirculo(), temaUI.getColorBordeCard());
            imgBack.setColorFilter(temaUI.getColorTextoPrincipal());
            btnBack.setOnClickListener(v -> finish());
        }

        construirInterfazIterativa();
    }

    private void construirInterfazIterativa() {
        LinearLayout contenedor = findViewById(R.id.contenedorAjustes);
        contenedor.removeAllViews();
        RepositorioConfiguracion repo = AppServicios.getRepositorio();

        // SECCIÓN 1: APARIENCIA
        contenedor.addView(crearLabelSeccion("Apariencia"));
        LinearLayout grupoApariencia = crearGrupo();
        grupoApariencia.addView(crearTarjetaTema());
        contenedor.addView(grupoApariencia);

        // SECCIÓN 2: COMPORTAMIENTO
        contenedor.addView(crearLabelSeccion("Comportamiento"));
        LinearLayout grupoComportamiento = crearGrupo();

        List<ConfigItem.Switch> itemsComportamiento = Arrays.asList(
            new ConfigItem.Switch("Vibrar al pulsar", "Respuesta háptica en las teclas",
                repo::leerVibracion, repo::guardarVibracion),
                
            new ConfigItem.Switch("Barra de herramientas", "Atajos en la parte superior",
                repo::leerMostrarBarra, repo::guardarMostrarBarra),
                
            new ConfigItem.Switch("Mayúsculas automáticas", "Inicia oraciones con mayúscula",
                repo::leerAutoMayusculas, repo::guardarAutoMayusculas)
        );

        for (int i = 0; i < itemsComportamiento.size(); i++) {
            grupoComportamiento.addView(crearTarjetaSwitch(itemsComportamiento.get(i)));
            if (i < itemsComportamiento.size() - 1) agregarSeparador(grupoComportamiento);
        }
        contenedor.addView(grupoComportamiento);

        // SECCIÓN 3: TAMAÑO
        contenedor.addView(crearLabelSeccion("Tamaño"));
        ConfigItem.Slider itemTamano = new ConfigItem.Slider("Altura del Teclado", "Ajusta el espacio vertical de las teclas",
                0, ConversionAltura.getMax(),
                () -> ConversionAltura.porcentajeAProgreso(ConversionAltura.pxAPorcentaje(repo.leerAlturaTeclado(), alturaBasePx)),
                progreso -> repo.guardarAlturaTeclado(Math.round(alturaBasePx * (ConversionAltura.progresoAPorcentaje(progreso) / 100f))));
        
        contenedor.addView(crearTarjetaSlider(itemTamano));
    }

    // =========================================================
    // CONSTRUCTORES DE ESTRUCTURA CSS
    // =========================================================

    private TextView crearLabelSeccion(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto.toUpperCase());
        tv.setTextColor(temaUI.getColorAcento());
        tv.setTextSize(11f);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setLetterSpacing(0.08f);
        tv.setPadding(UiHelper.dpAPx(getResources(), 20), UiHelper.dpAPx(getResources(), 20), 
                      UiHelper.dpAPx(getResources(), 20), UiHelper.dpAPx(getResources(), 8));
        return tv;
    }

    private LinearLayout crearGrupo() {
        LinearLayout grupo = new LinearLayout(this);
        grupo.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(UiHelper.dpAPx(getResources(), 16), 0, UiHelper.dpAPx(getResources(), 16), 0);
        grupo.setLayoutParams(params);
        UiHelper.aplicarFondoGrupo(grupo, temaUI.getColorFondoCard(), temaUI.getColorBordeCard());
        return grupo;
    }

    private void agregarSeparador(LinearLayout grupo) {
        View separador = new View(this);
        int altoPx = Math.max(1, Math.round(0.5f * getResources().getDisplayMetrics().density));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, altoPx);
        params.setMargins(UiHelper.dpAPx(getResources(), 16), 0, UiHelper.dpAPx(getResources(), 16), 0);
        separador.setLayoutParams(params);
        separador.setBackgroundColor(temaUI.getColorBordeCard());
        grupo.addView(separador);
    }

    // =========================================================
    // CONSTRUCTORES DE ELEMENTOS
    // =========================================================

    private void configurarTextos(View card, ConfigItem item) {
        TextView txtTitulo = card.findViewById(R.id.txtTituloCard);
        TextView txtSubtitulo = card.findViewById(R.id.txtSubtituloCard);

        txtTitulo.setText(item.titulo);
        txtTitulo.setTextColor(temaUI.getColorTextoPrincipal());
        
        txtSubtitulo.setText(item.subtitulo);
        txtSubtitulo.setTextColor(temaUI.getColorTextoSecundario());
    }

    private View crearTarjetaTema() {
        String temaActual = AppServicios.getRepositorio().leerIdTemaTeclado();
        
        ConfigItem.Info itemTema = new ConfigItem.Info(
            "Tema del Teclado", "Actualmente activo", temaActual,
            () -> {
                String[] temasDisponibles = AppServicios.getGestorTema().obtenerTemasDisponibles();
                int indiceActivo = java.util.Arrays.asList(temasDisponibles).indexOf(temaActual);

                new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setTitle("Selecciona un tema")
                .setSingleChoiceItems(temasDisponibles, indiceActivo, (dialog, cualElijio) -> {
                    AppServicios.getGestorTema().cambiarTema(temasDisponibles[cualElijio]);
                    dialog.dismiss();
                    recreate(); 
                }).show();
            }
        );

        View card = inflador.inflate(R.layout.item_config_theme, null);
        configurarTextos(card, itemTema);

        TextView txtBadge = card.findViewById(R.id.txtBadge);
        txtBadge.setText(itemTema.valorActual);
        // Usa el color circular para el fondo de la pastilla y el acento para el texto
        UiHelper.aplicarFondoBadge(txtBadge, temaUI.getColorFondoCirculo(), temaUI.getColorAcento());

        card.setOnClickListener(v -> itemTema.accionClick.run());
        return card;
    }

    private View crearTarjetaSwitch(ConfigItem.Switch item) {
        View card = inflador.inflate(R.layout.item_config_switch, null);
        configurarTextos(card, item);

        Switch swOpcion = card.findViewById(R.id.switchConfig);
        swOpcion.setId(View.generateViewId()); 
        swOpcion.setOnCheckedChangeListener(null);
        swOpcion.setChecked(item.lector.getAsBoolean());
        swOpcion.setOnCheckedChangeListener((v, isChecked) -> item.escritor.accept(isChecked));
        
        card.setOnClickListener(v -> swOpcion.toggle());
        return card;
    }

    private View crearTarjetaSlider(ConfigItem.Slider item) {
        View card = inflador.inflate(R.layout.item_config_slider, null);
        UiHelper.aplicarFondoGrupo(card, temaUI.getColorFondoCard(), temaUI.getColorBordeCard()); 
        configurarTextos(card, item);
        
        TextView txtValor = card.findViewById(R.id.txtValorSlider);
        SeekBar seekbar = card.findViewById(R.id.seekbarConfig);

        int progresoActual = item.lector.getAsInt();
        txtValor.setText(ConversionAltura.progresoAPorcentaje(progresoActual) + "%");
        txtValor.setTextColor(temaUI.getColorAcento());
        
        seekbar.setMax(item.max);
        seekbar.setProgress(progresoActual);
        
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int porcentaje = ConversionAltura.progresoAPorcentaje(progress);
                txtValor.setText(porcentaje + "%");
                if (fromUser) item.escritor.accept(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        return card;
    }
}