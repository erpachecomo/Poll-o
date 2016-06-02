package mx.edu.ittepic.poll_o;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MenuEncuesta extends AppCompatActivity {
    String encuesta_a_Realizar;
    TextView Encuesta;
    ImageButton Realizar_encuesta,Graficar,subirServer;
    int tipo_usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_encuesta);
        encuesta_a_Realizar=getIntent().getStringExtra("seleccion");
        tipo_usuario=Integer.parseInt(getIntent().getStringExtra("tipo"));
        Encuesta=(TextView)findViewById(R.id.Encuesta_a_Realizar);
        Realizar_encuesta= (ImageButton)findViewById(R.id.RealizarEncuesta);
        Graficar=(ImageButton)findViewById(R.id.Graficar_Resultados);
        if(tipo_usuario==1){
            Realizar_encuesta.setVisibility(View.INVISIBLE);
        }
        else{
            Realizar_encuesta.setVisibility(View.VISIBLE);
        }

        Encuesta.setText(encuesta_a_Realizar);

        Graficar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent PantallaGraficar = new Intent(MenuEncuesta.this, Graficar.class);
                PantallaGraficar.putExtra("seleccion", encuesta_a_Realizar);
                startActivity(PantallaGraficar);
            }
        });
        Realizar_encuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PantallaPreguntas = new Intent(MenuEncuesta.this, Preguntas.class);
                PantallaPreguntas.putExtra("seleccion", encuesta_a_Realizar);
                startActivity(PantallaPreguntas);
            }
        });

    }


}
