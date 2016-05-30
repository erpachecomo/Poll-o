package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.SweepGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Preguntas extends AppCompatActivity {
    LinearLayout layun;
    ImageView sigPregunta;
    CheckBox seleccion[];
    TextView pregunta;
    RadioGroup rdGroup;
    EditText txtRespuesta;
    RadioButton opcion[];
    TextView encuesta;
    ConexionBD conexion;
    int con =0;
    int nOpciones=0;

    String[] preguntas;

    String titulo_encuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        conexion=new ConexionBD(this,"Poll-oB2",null,1);
        layun = (LinearLayout) findViewById(R.id.linearLayou);
        encuesta=(TextView)findViewById(R.id.Encuesta_Preguntas);
        sigPregunta = (ImageView) findViewById(R.id.siguiente_pregunta);

        titulo_encuesta=getIntent().getStringExtra("seleccion");
        encuesta.setText(titulo_encuesta);

        String[] arreglo = titulo_encuesta.split("-");
        int ide = Integer.parseInt(arreglo[0]);
        consultaPreguntas(ide);
        cargarPreguntas(con);


        sigPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con++;
                if(con<preguntas.length) {
                    layun.removeViews(2, nOpciones);
                    cargarPreguntas(con);
                }
                else{
                    AlertDialog.Builder alertini = new AlertDialog.Builder(Preguntas.this);
                    alertini.setTitle("Atencion");
                    alertini.setMessage("La encuesta ha finalizado");
                    alertini.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertini.show();
                }
            }
        });

    }
    public  void  cargarOpciones(String tipo,String opc){

        switch(tipo){
            case "abierta":
                nOpciones =1;
                txtRespuesta = new EditText(this);
                layun.addView(txtRespuesta);

                break;
            case "opcion":
                String[] opciones = opc.split(",");

                opcion = new RadioButton[opciones.length];
                RadioGroup rg = new RadioGroup(this);
                rg.setOrientation(RadioGroup.VERTICAL);
                nOpciones = opciones.length;

                for(int i =0;i<opcion.length;i++) {
                    opcion[i] = new RadioButton(this);
                    opcion[i].setText(opciones[i]);
                    rg.addView(opcion[i]);
                }
                layun.addView(rg);
                break;
            case "seleccion":
                String[] opcioncini = opc.split(",");
                seleccion = new CheckBox[opcioncini.length];
                nOpciones = opcioncini.length;
                for(int i =0;i<seleccion.length;i++) {
                    seleccion[i] = new CheckBox(this);
                    seleccion[i].setText(opcioncini[i]);
                    layun.addView(seleccion[i]);
                }
                break;

        }




    }
    public void cargarPreguntas(int n){
        String[] valores = preguntas[n].split("-");
        encuesta.setText(valores[0]);
        cargarOpciones(valores[1],valores[2]);
    }

    public void consultaPreguntas(int id){
        String reg="";
        SQLiteDatabase base = conexion.getReadableDatabase();
        String SQL = "Select*from Pregunta where fk_idencuesta = "+id;
        Cursor res = base.rawQuery(SQL,null);

        if (res.moveToFirst()){
           do {
                reg = reg+res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4)+"-o-";
            }while(res.moveToNext());
             preguntas = reg.split("-o-");
        }
        else{
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
        }

        base.close();
    }
}
