package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MenuPrincipal extends AppCompatActivity {
    ImageButton AcercaDe,Encuestas,Salir,actualizar;
    VerificarConexionWIFI verificadorConexion;
    ConexionBD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Salir=(ImageButton)findViewById(R.id.salir);
        Encuestas=(ImageButton)findViewById(R.id.realizar_encuesta);
        AcercaDe=(ImageButton)findViewById(R.id.acerca_de);
        actualizar=(ImageButton)findViewById(R.id.Botton_actualizar);
        verificadorConexion=new VerificarConexionWIFI(this);
        bd=new ConexionBD(this,"Poll-oB2",null,1);
    }
    /*APARTADO PARA ACTUALIZAR COMPLETAMENTE LA BASE DE DATOS BAJANDO LOS DATOS DEL SERVIDOR*/
    public void Actualizar(View v){
        if(verificadorConexion.estaConectado()) {
            ActualizarBaseDeDatos();
        }
        else{
            Toast.makeText(this,"No tienes conexion a internet",Toast.LENGTH_SHORT).show();
        }
    }
    void ActualizarBaseDeDatos(){
        try{
            /*Actualiza las encuestas*/
            SQLiteDatabase base = bd.getReadableDatabase();
            //String EliminarDatosEncuesta="DELETE FROM Encuesta";
            //base.execSQL(EliminarDatosEncuesta);
            ConexionWeb web = new ConexionWeb(MenuPrincipal.this);
            //web.agregarVariables("operacion", "get_encuesta");
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
           // web.execute(url);
            //Toast.makeText(this, "Encuestas", Toast.LENGTH_SHORT).show();


            /*Actualiza las Empleado_Encuesta
            web = new ConexionWeb(MenuPrincipal.this);
            String EliminarDatosEmp_enc="DELETE FROM Empleado_Encuesta";
            base.execSQL(EliminarDatosEmp_enc);
            web.agregarVariables("operacion", "get_empleado_encuesta");
            web.agregarVariables("celular", "3112633940");//Aqui se va a pasar el numero de telefono con el que se logio.
            web.execute(url);
            Toast.makeText(this, "Empleado_Encuesta", Toast.LENGTH_SHORT).show();
            */

            /*Actualiza las Pregunta*/
            web = new ConexionWeb(MenuPrincipal.this);
            String EliminarDatosPregunta="DELETE FROM Pregunta";
            base.execSQL(EliminarDatosPregunta);
            web.agregarVariables("operacion", "get_pregunta");
            web.execute(url);
            Toast.makeText(this, "Pregunta", Toast.LENGTH_SHORT).show();


            /*Actualiza las respuesta
            web = new ConexionWeb(MenuPrincipal.this);
            String EliminarDatosRespuestas="DELETE FROM Respuestas";
            base.execSQL(EliminarDatosRespuestas);
            web.agregarVariables("operacion", "get_respuestas");
            web.execute(url);
            Toast.makeText(this, "Respuestas", Toast.LENGTH_SHORT).show();
            */
            base.close();


        } catch (MalformedURLException e) {
            new AlertDialog.Builder(MenuPrincipal.this).setMessage
                    (e.getMessage()).setTitle("Error").show();
        }
    }
    void InsertarEnBaseDeDatos(String SQL){
        try{

            SQLiteDatabase base = bd.getReadableDatabase();

            /*----------------------------------------------------------------------------------*/
            base.execSQL(SQL);

            base.close();
            //Toast.makeText(this,"SE ELIMINO CORRECTAMENTE",Toast.LENGTH_LONG).show();
        }catch (SQLiteException e){
            AlertDialog.Builder alerta= new AlertDialog.Builder(MenuPrincipal.this);
            alerta.setTitle("ERROR").setMessage(e.getMessage()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }

    }






 /*------------------------------------------------------------------------------------------------*/
    public void Cambiar_Salir(View v){
        //Yo creo que aqui debe mandar el mensaje de desconexion
        //por mientras solo lo mandare a la pagina de login.
        Intent paginaLogin=new Intent(MenuPrincipal.this,Pantalla_Login.class);
        startActivity(paginaLogin);
    }
    public void Cambiar_Acerca(View v){
        //A la pagina de nuestros datos
        Intent paginaAcercaDe=new Intent(MenuPrincipal.this,AcercaDe.class);
        startActivity(paginaAcercaDe);
    }
    public void Cambiar_Encuestas(View v){
        //A la pagina de nuestros datos
        Intent paginaEncuestas=new Intent(MenuPrincipal.this,Encuestas.class);
        startActivity(paginaEncuestas);
    }
}
