package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Graficar extends AppCompatActivity {
    TextView encuesta;
    Spinner pregunta;
    ConexionBD conexion;
    String titulo_encuesta;
    private PieChart pieChart;
    Button Graficar;
    int ide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficar);
        encuesta=(TextView)findViewById(R.id.Encuesta_grafica);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pregunta = (Spinner) findViewById(R.id.spinner);
        conexion=new ConexionBD(this,"Poll-oB2",null,1);
        Graficar= (Button)findViewById(R.id.Graficar_botton);

        titulo_encuesta=getIntent().getStringExtra("seleccion");
        String[] arreglo = titulo_encuesta.split("-");
        ide = Integer.parseInt(arreglo[0]);

        //Toast.makeText(this,"ID: "+arreglo[0],Toast.LENGTH_LONG).show();

        encuesta.setText(titulo_encuesta);
        cargarPreguntasSpinner(ide);
        /*ESTOY HACIENDO PRUEBAS CON LAS GRAFICAS*/




        /*----------------------------------------------------------------*/

    }
    public void Grafica(View v){
        try {
            String pre_seleccionada[] = pregunta.getSelectedItem().toString().split("-");
            List<Respuestas> respuestas_ = conexion.obtenerRespuestas(Integer.parseInt(pre_seleccionada[0].toString()));
        /*QUITO REPETIDOS */
            for(int a=0;a<respuestas_.size();a++){
                for(int b=a+1;b<respuestas_.size();b++){
                    if(respuestas_.get(a).getNombre().equalsIgnoreCase(respuestas_.get(b).getNombre())){
                        respuestas_.remove(b);
                        b--;
                    }
                }
            }

            /*----------*/
        /*definimos algunos atributos*/
            pieChart.setHoleRadius(40f);
            pieChart.setDrawYValues(true);
            pieChart.setDrawXValues(true);
            pieChart.setRotationEnabled(true);
            pieChart.animateXY(1500, 1500);

        /*creamos una lista para los valores Y*/
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            ArrayList<String> xVals = new ArrayList<String>();


            float total_votos = conexion.getTotalRespuestas(Integer.parseInt(pre_seleccionada[0].toString()));

            for (int index = 0; index < respuestas_.size(); index++) {

                float votos = (respuestas_.get(index).getValor() / total_votos) * 100f;
                yVals.add(new Entry(votos, index));
                xVals.add(respuestas_.get(index).getNombre());
            }

 		/*creamos una lista de colores*/
            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(getResources().getColor(R.color.red_flat));
            colors.add(getResources().getColor(R.color.green_flat));
            colors.add(Color.MAGENTA);
            colors.add(Color.CYAN);
            colors.add(Color.BLUE);
            colors.add(Color.RED);
            colors.add(Color.YELLOW);

 		/*seteamos los valores de Y y los colores*/
            PieDataSet set1 = new PieDataSet(yVals, "Resultados");
            set1.setSliceSpace(3f);
            set1.setColors(colors);

		/*seteamos los valores de X*/
            PieData data = new PieData(xVals, set1);
            pieChart.setData(data);
            pieChart.highlightValues(null);
            pieChart.invalidate();

        /*Ocutar descripcion*/
            pieChart.setDescription("");
        /*Ocultar leyenda*/
            pieChart.setDrawLegend(false);
        }catch (SQLiteException e){
            AlertDialog.Builder alerta= new AlertDialog.Builder(Graficar.this);
            alerta.setTitle("ERROR").setMessage(e.getMessage()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }catch (IllegalStateException xe){
            AlertDialog.Builder alerta= new AlertDialog.Builder(Graficar.this);
            alerta.setTitle("ERROR").setMessage(xe.getMessage()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }
    private void cargarPreguntasSpinner(int id) {

        List<String> lables = conexion.obtenerPreguntas(id);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pregunta.setAdapter(dataAdapter);
    }
}
