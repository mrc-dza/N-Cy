package com.ncy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ncy.datos.ConfiguracionItem;
import com.ncy.datos.RepositorioConfiguracion;
import com.ncy.utilidades.GestorTema;
import com.ncy.utilidades.GestorTemaUI;
import com.ncy.utilidades.TemaVisualUI;
import com.ncy.utilidades.UiHelper;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends Activity {

    private RepositorioConfiguracion repositorio;
    
    private int alturaBasePx; 
    private static final int SEEKBAR_MAX    = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GestorTema.getInstance().inicializar(this);
        GestorTemaUI.getInstance().inicializar(this);
        this.alturaBasePx = GestorTema.getInstance().obtenerTemaActivo().getAlturaFilaBasePx();
        
        setContentView(R.layout.activity_settings);
        
        TemaVisualUI temaUI = GestorTemaUI.getInstance().obtenerTemaActivo();
        
        View raiz = findViewById(android.R.id.content);
        if (raiz != null) {
            raiz.setBackgroundColor(temaUI.getColorFondoGeneral());
        }

        TextView txtTituloPrincipal = findViewById(R.id.txtTituloPrincipalSettings);
        if (txtTituloPrincipal != null) {
            txtTituloPrincipal.setTextColor(temaUI.getColorTextoPrincipal());
        }

        repositorio = new RepositorioConfiguracion(this);

        configurarTarjetaTema(temaUI);

        List<ConfiguracionItem> opcionesSwitch = Arrays.asList(
            new ConfiguracionItem(
                R.id.cardVibracion, "Vibrar al pulsar", "Respuesta haptica en las teclas",
                repositorio::leerVibracion,
                repositorio::guardarVibracion
            ),
            new ConfiguracionItem(
                R.id.cardBarra, "Barra de herramientas", "Mostrar atajos en la parte superior",
                repositorio::leerMostrarBarra,
                repositorio::guardarMostrarBarra
            ),
            new ConfiguracionItem(
                R.id.cardAutoMayusculas, "Mayúsculas automáticas", "Iniciar oraciones con mayúscula",
                repositorio::leerAutoMayusculas,
                repositorio::guardarAutoMayusculas
            )
        );

        for (ConfiguracionItem item : opcionesSwitch) {
            configurarTarjetaSwitch(item, temaUI);
        }

        configurarSliderAltura(temaUI);
    }

    private void configurarTarjetaTema(TemaVisualUI temaUI) {
        View card = findViewById(R.id.cardTema);
        if (card == null) return;
        formatearComoLista(card);
        
        TextView txtTitulo = card.findViewById(R.id.txtTituloConfig);
        TextView txtSubtitulo = card.findViewById(R.id.txtSubtituloConfig);
        Switch swOpcion = card.findViewById(R.id.switchConfig);

        swOpcion.setVisibility(View.GONE);
        txtTitulo.setText("Tema del Teclado");
        String temaActual = repositorio.leerIdTemaTeclado();
        txtSubtitulo.setText("Tema actual: " + temaActual.toUpperCase());
        
        txtTitulo.setTextColor(temaUI.getColorTextoPrincipal());
        txtSubtitulo.setTextColor(temaUI.getColorTextoSecundario());
        
        // Usamos el color de fondo general para que se fusione con la pantalla y parezca una lista continua
        UiHelper.aplicarSeparadorInferior(card, temaUI.getColorFondoGeneral(), temaUI.getColorBordeCard());

        card.setOnClickListener(v -> {
            String[] temasDisponibles = GestorTema.getInstance().obtenerTemasDisponibles();
            
            int indiceActivo = -1;
            for (int i = 0; i < temasDisponibles.length; i++) {
                if (temasDisponibles[i].equals(temaActual)) {
                    indiceActivo = i;
                    break;
                }
            }

            new AlertDialog.Builder(this)
                .setTitle("Selecciona un tema para el teclado")
                .setSingleChoiceItems(temasDisponibles, indiceActivo, (dialog, cualElijio) -> {
                    String temaElegido = temasDisponibles[cualElijio];
                    GestorTema.getInstance().cambiarTema(temaElegido);
                    dialog.dismiss();
                    recreate(); 
                })
                .show();
        });
    }

    private void configurarTarjetaSwitch(ConfiguracionItem item, TemaVisualUI temaUI) {
        View card = findViewById(item.idVista);
        if (card == null) return;
        formatearComoLista(card);
        
        TextView txtTitulo = card.findViewById(R.id.txtTituloConfig);
        TextView txtSubtitulo = card.findViewById(R.id.txtSubtituloConfig);
        Switch swOpcion = card.findViewById(R.id.switchConfig);

        // ¡LA SOLUCIÓN! Le damos un ID único en memoria a cada switch
        // Así Android nunca más mezclará sus estados al hacer recreate()
        swOpcion.setId(View.generateViewId()); 

        txtTitulo.setText(item.titulo);
        txtSubtitulo.setText(item.subtitulo);
        
        txtTitulo.setTextColor(temaUI.getColorTextoPrincipal());
        txtSubtitulo.setTextColor(temaUI.getColorTextoSecundario());
        
        // Usamos el color de fondo general para que se fusione con la pantalla y parezca una lista continua
        UiHelper.aplicarSeparadorInferior(card, temaUI.getColorFondoGeneral(), temaUI.getColorBordeCard());

        // Ahora podemos usar la forma tradicional de escuchar eventos sin miedo
        swOpcion.setOnCheckedChangeListener(null);
        swOpcion.setChecked(item.lector.getAsBoolean());
        swOpcion.setOnCheckedChangeListener((v, isChecked) -> item.escritor.accept(isChecked));
        
        card.setOnClickListener(v -> swOpcion.toggle());
    }




    private interface AccionSlider {
        String obtenerTextoIndicador(int progreso);
        void guardarCambio(int progreso);
    }

    private void configurarTarjetaSlider(int idIncluido, String titulo, String subtitulo, int maximo, int progresoActual, AccionSlider accion, TemaVisualUI temaUI) {
        View card = findViewById(idIncluido);
        if (card == null) return;
        formatearComoLista(card);
        
        TextView txtTitulo = card.findViewById(R.id.txtTituloSlider);
        TextView txtSubtitulo = card.findViewById(R.id.txtSubtituloSlider);
        TextView txtValor = card.findViewById(R.id.txtValorSlider);
        SeekBar seekbar = card.findViewById(R.id.seekbarConfig);

        txtTitulo.setText(titulo);
        txtSubtitulo.setText(subtitulo);
        txtValor.setText(accion.obtenerTextoIndicador(progresoActual));
        
        txtTitulo.setTextColor(temaUI.getColorTextoPrincipal());
        txtSubtitulo.setTextColor(temaUI.getColorTextoSecundario());
        txtValor.setTextColor(temaUI.getColorAcento());
        
        seekbar.setProgressTintList(ColorStateList.valueOf(temaUI.getColorAcento()));
        seekbar.setThumbTintList(ColorStateList.valueOf(temaUI.getColorAcento()));
        seekbar.setProgressBackgroundTintList(ColorStateList.valueOf(temaUI.getColorBordeCardActivo()));

        // Usamos el color de fondo general para que se fusione con la pantalla y parezca una lista continua
        UiHelper.aplicarSeparadorInferior(card, temaUI.getColorFondoGeneral(), temaUI.getColorBordeCard());

        seekbar.setMax(maximo);
        seekbar.setProgress(progresoActual);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtValor.setText(accion.obtenerTextoIndicador(progress));
                if (fromUser) {
                    accion.guardarCambio(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void configurarSliderAltura(TemaVisualUI temaUI) {
        int alturaActualPx = repositorio.leerAlturaTeclado();
        int porcentajeActual = Math.round((alturaActualPx / (float) alturaBasePx) * 100);
        int progresoActual = Math.max(0, Math.min((porcentajeActual - 50) / 2, SEEKBAR_MAX));
        configurarTarjetaSlider(R.id.cardAltura, "Altura del Teclado", "Ajusta el tamaño vertical de las teclas.", SEEKBAR_MAX, progresoActual, new AccionSlider() {
            @Override
            public String obtenerTextoIndicador(int progreso) {
                return ((progreso * 2) + 50) + "%";
            }

            @Override
            public void guardarCambio(int progreso) {
                int porcentaje = (progreso * 2) + 50;
                int nuevosPixeles = Math.round(alturaBasePx * (porcentaje / 100f));
                repositorio.guardarAlturaTeclado(nuevosPixeles);
            }
        }, temaUI);


        
    }

    private void formatearComoLista(View card) {
        if (card == null) return;
        
        

        // 2. Quitar los márgenes de la vista actual
        if (card.getLayoutParams() instanceof android.view.ViewGroup.MarginLayoutParams params) {
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.topMargin = 0;
            params.bottomMargin = 0;
            card.setLayoutParams(params);
        }

        // 3. Restaurar el espacio interno para que el texto y el switch no choquen con la orilla
        int paddingH = UiHelper.dpAPx(getResources(), 24); 
        int paddingV = UiHelper.dpAPx(getResources(), 20); 
        card.setPadding(paddingH, paddingV, paddingH, paddingV);
    }


    
}