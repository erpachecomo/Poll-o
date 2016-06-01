package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Button;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MenuPrincipal extends AppCompatActivity {
    ImageButton AcercaDe, Encuestas, Salir, actualizar;
    ImageButton btnCrearTxt;
    VerificarConexionWIFI verificadorConexion;
    ConexionBD bd;
    String usuario_logeado, reg;
    BroadcastReceiver recibidorMensajesSMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Salir = (ImageButton) findViewById(R.id.salir);
        Encuestas = (ImageButton) findViewById(R.id.realizar_encuesta);
        AcercaDe = (ImageButton) findViewById(R.id.acerca_de);
        btnCrearTxt = (ImageButton) findViewById(R.id.crearTxt);
        actualizar = (ImageButton) findViewById(R.id.Botton_actualizar);
        verificadorConexion = new VerificarConexionWIFI(this);
        usuario_logeado = getIntent().getStringExtra("Usuario");

        bd = new ConexionBD(this, "Poll-oB2", null, 1);

        btnCrearTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))) {
                    //SI ESTA PRESENTE LA MICRO SD
                    try {
                        File tarjeta = Environment.getExternalStorageDirectory();
                        File carpeta = new File(tarjeta.getAbsolutePath(), "Poll-o");
                        if (!carpeta.exists()) {
                            carpeta.mkdir();
                        }
                        //File directorioAplicacion = getExternalFilesDir("TPDM");
                        //Con variante de vector de files
                        File datosArchivo = new File(carpeta.getAbsolutePath(), "encuestas.txt");
                        OutputStreamWriter archivo = new OutputStreamWriter(new FileOutputStream(datosArchivo));
                        archivo.write(consultaBD());
                        archivo.close();
                        Toast.makeText(MenuPrincipal.this, "Se guardo correctamente", Toast.LENGTH_LONG).show();
                    }//TRY
                    catch (Exception ex) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MenuPrincipal.this);
                        alert.setTitle("Error").setMessage(ex.getMessage()).show();
                    }//CATCH
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MenuPrincipal.this);
                    alert.setTitle("Error").setMessage("No hay tarjeta SD Montada/Insertada!").show();
                }
            }
                     });

               if (verificadorConexion.estaConectado()) {
           ActualizarBaseDeDatos2();
        }

        IntentFilter evento = new IntentFilter("android.provider.Telephony.SMS_RECEIVED"); //Cuando entre un mensaje nuevo;
        recibidorMensajesSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();

                Object mensajeObjeto[] = (Object[]) b.get("pdus");//Esto es obtener el mensaje entrante.

                SmsMessage mensaje = SmsMessage.createFromPdu((byte[])mensajeObjeto[0]);//),"UTF-8");
                if(mensaje.getDisplayOriginatingAddress().equals("3112633940")){
                    if(mensaje.getMessageBody().contains("si")){
                        Intent MenuPrincipal = new Intent(MenuPrincipal.this, Pantalla_Login.class);
                        startActivity(MenuPrincipal);
                    }
                }
                //       Toast.makeText(Pantalla_Login.this, "Celular"+mensaje.getDisplayOriginatingAddress()+"Mensaje"+mensaje.getMessageBody(), Toast.LENGTH_SHORT).show();

            }
        };
        registerReceiver(recibidorMensajesSMS, evento);
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
    //Comentario de prueba
    void ActualizarBaseDeDatos(){
        try{
            /*Actualiza las encuestas*/
            SQLiteDatabase base = bd.getReadableDatabase();
            String EliminarDatosEncuesta="DELETE FROM Encuesta";
            base.execSQL(EliminarDatosEncuesta);
            ConexionWeb web = new ConexionWeb(MenuPrincipal.this,1);
            web.agregarVariables("operacion", "get_encuesta");
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
            //Toast.makeText(this, "Encuestas", Toast.LENGTH_SHORT).show();


            /*Actualiza las Empleado_Encuesta*/
            web = new ConexionWeb(MenuPrincipal.this,1);
            String EliminarDatosEmp_enc="DELETE FROM Empleado_Encuesta";
            base.execSQL(EliminarDatosEmp_enc);
            web.agregarVariables("operacion", "get_empleado_encuesta");
            web.agregarVariables("celular", "3112633940");//Aqui se va a pasar el numero de telefono con el que se logio.
            web.execute(url);
            //Toast.makeText(this, "Empleado_Encuesta", Toast.LENGTH_SHORT).show();


            /*Actualiza las Pregunta*/
            web = new ConexionWeb(MenuPrincipal.this,1);
            String EliminarDatosPregunta="DELETE FROM Pregunta";
            base.execSQL(EliminarDatosPregunta);
            web.agregarVariables("operacion", "get_pregunta");
            web.execute(url);
            //Toast.makeText(this, "Pregunta", Toast.LENGTH_SHORT).show();


            /*Actualiza las respuesta*/
            web = new ConexionWeb(MenuPrincipal.this,1);
            String EliminarDatosRespuestas="DELETE FROM Respuestas";
            base.execSQL(EliminarDatosRespuestas);
            web.agregarVariables("operacion", "get_respuestas");
            web.execute(url);
            //Toast.makeText(this, "Respuestas", Toast.LENGTH_SHORT).show();

            base.close();

            Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            new AlertDialog.Builder(MenuPrincipal.this).setMessage
                    (e.getMessage()).setTitle("Error").show();
        }
    }
    void ActualizarBaseDeDatos2(){
        try{
            /*Actualiza las encuestas*/
            SQLiteDatabase base = bd.getReadableDatabase();
            String EliminarDatosEncuesta="DELETE FROM Encuesta";
            base.execSQL(EliminarDatosEncuesta);
            ConexionWeb web = new ConexionWeb(MenuPrincipal.this,1);
            web.agregarVariables("operacion", "get_encuesta");
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
            //Toast.makeText(this, "Encuestas", Toast.LENGTH_SHORT).show();

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
            //Toast.makeText(this,"SE INSERTO CORRECTAMENTE",Toast.LENGTH_LONG).show();
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
        if (verificadorConexion.estaConectado()) {
            //CODIGO LOGEARTE CON INTERNET
            Cerrar_Sesion(usuario_logeado);

        } else {
            //AQUI DEBE IR EL CODIGO PARA MANDAR UN MJS AL SERVIDOR SI NO HAY INTERNET
            final DialogInterface.OnClickListener di = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int which){
                    switch(which){
                        case DialogInterface.BUTTON_NEGATIVE:
                            d.cancel();
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            IniciarSesionSMS(usuario_logeado);
                            d.dismiss();
                            break;
                    }
                }

            };
            AlertDialog.Builder alert = new AlertDialog.Builder(MenuPrincipal.this);
            alert.setTitle("Login");
            alert.setMessage("No tienes una conexión a internet, puedes ingresar enviando un SMS lo cual generará costos con tu operadora, estás seguro?");
            alert.setPositiveButton("Si", di);
            alert.setNegativeButton("No", di);
            alert.show();
        }
            }
    private void IniciarSesionSMS(String usuario) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("3112633940", null, "Poll-o,logout,"+usuario, null, null);//telefono, provedor de servicios,Activity de envio:Cuando lo envía manda a llamr a un activity, Activity de recepcion:cuando llega el sms
        Toast.makeText(MenuPrincipal.this, "Iniciando sesión vía SMS", Toast.LENGTH_SHORT).show();
    }
    private void Cerrar_Sesion(String usuario) {
        try {
            ConexionWeb web = new ConexionWeb(MenuPrincipal.this,0);
            web.agregarVariables("operacion", "logout");
            web.agregarVariables("celular",usuario);
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
            Toast.makeText(this, "cerrar sesion", Toast.LENGTH_SHORT).show();

        }catch (MalformedURLException e) {
            new AlertDialog.Builder(MenuPrincipal.this).setMessage
                    (e.getMessage()).setTitle("Error").show();

        }
    }
    public void sesion_cerrada(String Respuesta) {
        if(Respuesta.contains("si")){


            Toast.makeText(MenuPrincipal.this, "Sesion cerrada", Toast.LENGTH_LONG).show();
            Intent MenuPrincipal = new Intent(MenuPrincipal.this, Pantalla_Login.class);
            startActivity(MenuPrincipal);
        }else{
            Toast.makeText(MenuPrincipal.this, "no se pudo", Toast.LENGTH_LONG).show();
        }
    }

    public void Cambiar_Acerca(View v){
        //A la pagina de nuestros datos
        Intent paginaAcercaDe=new Intent(MenuPrincipal.this,AcercaDe.class);
        startActivity(paginaAcercaDe);
    }
    public void Cambiar_Encuestas(View v){
        //A la pagina de nuestros datos
        Intent paginaEncuestas=new Intent(MenuPrincipal.this,Encuestas.class);
        paginaEncuestas.putExtra("Usuario",usuario_logeado);
        paginaEncuestas.putExtra("Tipo","0");
        startActivity(paginaEncuestas);
    }
    @Override protected void onStop() {
        super.onStop();
        //Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        if (verificadorConexion.estaConectado()) {
            //CODIGO LOGEARTE CON INTERNET
            Cerrar_Sesion(usuario_logeado);

        } else {
            //AQUI DEBE IR EL CODIGO PARA MANDAR UN MJS AL SERVIDOR SI NO HAY INTERNET
        }
    }
    public void consultaRespuestas() {
                try {

                                SQLiteDatabase base = bd.getReadableDatabase();
                        String SQL = "Select*from rrespuestas";
                        Cursor res = base.rawQuery(SQL, null);

                                if (res.moveToFirst()) {
                                do {
                                       reg = reg + "(" + res.getString(1) + ",'" + res.getString(2) + "')-o-";
                } while (res.moveToNext());
                                //respuestas = reg.split("-o-");
                                       //Toast.makeText(this,reg,Toast.LENGTH_LONG).show();


                                                           } else {
                                Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
                            }

                                base.close();
                    } catch (SQLiteException e) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                        alerta.setTitle("Error").setTitle(e.getMessage()).show();
                    }
            }

    public String consultaBD() {
                String reg = "";
                try {
                        SQLiteDatabase base = bd.getReadableDatabase();
                       //String query = "select* from Pedido";
                                String query2 = "select E.compania, P.Pregunta, R.rvalor " +
                                        "from Encuesta E " +
                                        "inner join Pregunta P " +
                                        "on (E.idEncuesta = P.fk_idEncuesta ) " +
                                        "inner join RRespuestas R " +
                                        "on(P.idPregunta = R.rfk_idPregunta)";
                        //Cursor res = base.rawQuery(query, null);
                                Cursor res2 = base.rawQuery(query2, null);
                        if (res2.moveToFirst()) {
                                do {
                                        reg = reg + res2.getString(0) + "," + res2.getString(1) + "," +res2.getString(2)+ "\n";
                                   } while (res2.moveToNext());
                            } else {
                              Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
                           }
                        base.close();


                                    } catch (SQLiteException e) {
                        AlertDialog.Builder alertini = new AlertDialog.Builder(this);
                        alertini.setTitle("Error!").setMessage(e.getMessage()).show();
                    }
              return reg;
          }

}
