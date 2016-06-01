package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
    String tipoRespuesta="" ,cadRespuestas="";
    int con =0, idPregunta=0;
    int nOpciones=0;
    String[] opcioncini,valores;
    String[] preguntas,opciones;

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
                guardarRespuestas();

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
                            finish();
                        }
                    });
                    alertini.show();
                }
            }
        });

    }

    public void insertarBD(String idEncuesta,String val){
        int id = Integer.parseInt(idEncuesta);
        try{
            SQLiteDatabase base = conexion.getWritableDatabase();
            String SQL = "insert into rrespuestas(rfk_idpregunta, rvalor) values("+id+",'"+val+"')";
            base.execSQL(SQL);
            Toast.makeText(this,"Respuesta almacenada correctamente",Toast.LENGTH_SHORT).show();
            base.close();
        }
        catch(SQLiteException e){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);

            alerta.setTitle("Error").setTitle(e.getMessage()).show();
        }
    }
    public void guardarRespuestas(){
        switch(tipoRespuesta){
            case "abierta":
                insertarBD(valores[3],txtRespuesta.getText().toString());
                break;
            case "opcion":
                for(int i =0;i<opcion.length;i++) {
                    if(opcion[i].isChecked()==true){
                        //cadRespuestas = cadRespuestas+valores[0]+"-"+opciones[i]+"-"+valores[3]+"/";
                        insertarBD(valores[3],opciones[i]);
                    }
                }

                break;
            case "seleccion":
                for(int i =0;i<seleccion.length;i++) {
                    if(seleccion[i].isChecked()==true){
                        //cadRespuestas =cadRespuestas +valores[0]+"-"+opcioncini[i]+"-"+valores[3]+"/";
                        insertarBD(valores[3],opcioncini[i]);
                    }
                }
                break;
        }
    }

    public  void  cargarOpciones(String tipo,String opc){

        switch(tipo){
            case "abierta":

                nOpciones =1;
                txtRespuesta = new EditText(this);
                layun.addView(txtRespuesta);
                tipoRespuesta = "abierta";

                break;
            case "opcion":

                opciones = opc.split(",");
                opcion = new RadioButton[opciones.length];
                RadioGroup rg = new RadioGroup(this);
                rg.setOrientation(RadioGroup.VERTICAL);
                for(int i =0;i<opcion.length;i++) {
                    opcion[i] = new RadioButton(this);
                    opcion[i].setText(opciones[i]);
                    rg.addView(opcion[i]);
                }
                nOpciones = 1;
                layun.addView(rg);
                tipoRespuesta = "opcion";
                break;
            case "seleccion":
                opcioncini = opc.split(",");
                seleccion = new CheckBox[opcioncini.length];
                nOpciones = opcioncini.length;
                for(int i =0;i<seleccion.length;i++) {
                    seleccion[i] = new CheckBox(this);
                    seleccion[i].setText(opcioncini[i]);
                    layun.addView(seleccion[i]);
                    tipoRespuesta = "seleccion";
                }
                break;
        }
    }
    public void cargarPreguntas(int n){
        valores = preguntas[n].split("-");
        encuesta.setText(valores[0]);
        cargarOpciones(valores[1],valores[2]);
    }

    public void consultaPreguntas(int id) {
        try {
            String reg = "";
            SQLiteDatabase base = conexion.getReadableDatabase();
            String SQL = "Select*from Pregunta where fk_idencuesta = " + id;
            Cursor res = base.rawQuery(SQL, null);

            if (res.moveToFirst()) {
                do {
                    reg = reg + res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(0) + "-o-";
                } while (res.moveToNext());
                preguntas = reg.split("-o-");
                 // Toast.makeText(this,"Respuestas: "+reg,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }

            base.close();
        }
        catch (SQLiteException e) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Error").setTitle(e.getMessage()).show();
        }
    }

}