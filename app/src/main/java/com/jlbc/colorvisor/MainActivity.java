package com.jlbc.colorvisor;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*
     *  PARA CADA ELEMENTO DE UI QUE QUERAMOS MANIPULAR
     *  EN CÓDIGO, DEBEMOS DECLARAR UNA VARIABLE DE LA
     *  CLASE CORRESPONDIENTE.
     */

    // Declaración de widgets a manipular
    private TextView background;
    private SeekBar redBar, greenBar, blueBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         *  EN EL EVENTO onCreate ES DONDE SE OBTIENEN LAS
         *  INSTANCIAS DE LOS OBJETOS A MANIPULAR.
         */

        // Obtener los objetos para manipularlos
        background = findViewById(R.id.background);
        redBar = findViewById(R.id.redBar);
        greenBar = findViewById(R.id.greenBar);
        blueBar = findViewById(R.id.blueBar);
        updateBackgroundColor();

        /*
         *  COMO LOS TRES ELEMENTOS SeekBar DE LA INTERFAZ
         *  REALIZAN LA MISMA ACCIÓN (cambiar el color del
         *  fondo) CREAMOS UN SOLO LISTENER PARA LOS TRES.
         */

        // Crear el listener
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateBackgroundColor();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // AUNQUE NO SE NECESITA, NO SE PUEDE ELIMINAR,
                // POR ESO LO DEJAMOS VACÍO.
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // AUNQUE NO SE NECESITA, NO SE PUEDE ELIMINAR,
                // POR ESO LO DEJAMOS VACÍO.
            }
        };

        // Asignar el mismo listener a todos los objetos SeekBar
        redBar.setOnSeekBarChangeListener(listener);
        greenBar.setOnSeekBarChangeListener(listener);
        blueBar.setOnSeekBarChangeListener(listener);
    }

    /*
     *  ESTE MÉTODO DE LA CLASE RESUELVE UN PROBLEMA ESPECÏFICO:
     *  a) CREAR UN COLOR A PARTIR DE LOS VALORES DE LOS WIDGETS
     *     DE LA INTERFAZ.
     *  b) CAMBIAR EL COLOR DE FONDO A LOS ELEMENTOS CORRESPONDIENTES
     *     DE LA INTERFAZ.
     */
    private void updateBackgroundColor() {

        // Crear un color
        int color = Color.argb(255, redBar.getProgress(),
                greenBar.getProgress(), blueBar.getProgress());

        // Cambiar color de fondo a un widget
        background.setBackgroundColor(color);
        redBar.setBackgroundColor(color);
        greenBar.setBackgroundColor(color);
        blueBar.setBackgroundColor(color);
    }
}