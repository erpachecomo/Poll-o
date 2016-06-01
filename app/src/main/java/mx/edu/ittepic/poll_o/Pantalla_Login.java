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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;

public class Pantalla_Login extends AppCompatActivity {
   ConexionBD conexion;
    EditText Campo_Usuario,Campo_Contra;
    Button Ini_Sesion;
    String Usuario,Contrasena;
    BroadcastReceiver recibidorMensajesSMS;
    VerificarConexionWIFI verificadorConexion;
    boolean sesion_valida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__login);
        verificadorConexion=new VerificarConexionWIFI(this);
        Campo_Usuario=(EditText)findViewById(R.id.CampoUsuario);
        Campo_Contra=(EditText)findViewById(R.id.CampoContrasena);
        Ini_Sesion=(Button)findViewById(R.id.Boton_in_sesion);
        sesion_valida=false;
        conexion=new ConexionBD(this,"Poll-oB2",null,1);
        Usuario=Contrasena="";
        final DialogInterface.OnClickListener di = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int which){
                switch(which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        d.cancel();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        IniciarSesionSMS(Usuario, Contrasena);
                        d.dismiss();
                        break;
                }
            }

        };
        Ini_Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario = Campo_Usuario.getText().toString();
                Contrasena = Campo_Contra.getText().toString();
                if (!CamposVacios(Usuario, Contrasena)) {
                    /*LOGIN POR MEDIO DEL SERVIDOR*/
                    if (verificadorConexion.estaConectado()) {
                        //CODIGO LOGEARTE CON INTERNET
                        IniciarSesion(Usuario, Contrasena);

                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Pantalla_Login.this);
                        alert.setTitle("Login");
                        alert.setMessage("No tienes una conexión a internet, puedes ingresar enviando un SMS lo cual generará costos con tu operadora, estás seguro?");
                        alert.setPositiveButton("Si", di);
                        alert.setNegativeButton("No", di);
                        alert.show();

                    }
                            /*-------------------------------------*/
                } else {
                    Toast.makeText(Pantalla_Login.this, "Campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });
        IntentFilter evento = new IntentFilter("android.provider.Telephony.SMS_RECEIVED"); //Cuando entre un mensaje nuevo;
        recibidorMensajesSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();

                Object mensajeObjeto[] = (Object[]) b.get("pdus");//Esto es obtener el mensaje entrante.

                SmsMessage mensaje = SmsMessage.createFromPdu((byte[])mensajeObjeto[0]);//),"UTF-8");
                if(mensaje.getDisplayOriginatingAddress().equals("3111349397")){
                if(mensaje.getMessageBody().equals("si")){
                    Intent MenuPrincipal = new Intent(Pantalla_Login.this, mx.edu.ittepic.poll_o.MenuPrincipal.class);
                    MenuPrincipal.putExtra("Usuario", Usuario);
                    startActivity(MenuPrincipal);
                }if(mensaje.getMessageBody().equals("cliente")){
                    ActualizarBaseDeDatos();
                    //Toast.makeText(Pantalla_Login.this,"Inicio correcto",Toast.LENGTH_SHORT).show();
                    Intent Encuestas= new Intent(Pantalla_Login.this, mx.edu.ittepic.poll_o.Encuestas.class);
                    Encuestas.putExtra("Usuario",Usuario);
                    Encuestas.putExtra("Tipo","1");
                    startActivity(Encuestas);
                    //super.onResume();
                }
            }
                Toast.makeText(Pantalla_Login.this, "Celular"+mensaje.getDisplayOriginatingAddress()+"Mensaje"+mensaje.getMessageBody(), Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(recibidorMensajesSMS, evento);
    }
    private void IniciarSesionSMS(String usuario, String contrasena) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("3111349397", null, "Poll-o,"+usuario+","+contrasena, null, null);//telefono, provedor de servicios,Activity de envio:Cuando lo envía manda a llamr a un activity, Activity de recepcion:cuando llega el sms
        Toast.makeText(Pantalla_Login.this, "Iniciando sesión vía SMS", Toast.LENGTH_SHORT).show();
    }
    private void IniciarSesion(String usuario, String contrasena) {
        try {
            ConexionWeb web = new ConexionWeb(Pantalla_Login.this,0);
            web.agregarVariables("operacion", "login");
            web.agregarVariables("celular",usuario);
            web.agregarVariables("password", contrasena);
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
            Toast.makeText(this, "Iniciando sesión", Toast.LENGTH_SHORT).show();

        }catch (MalformedURLException e) {
            new AlertDialog.Builder(Pantalla_Login.this).setMessage
                    (e.getMessage()).setTitle("Error").show();

        }

    }

    public void sesion_correcta(String Respuesta) {
        if(Respuesta.equals("si")){
            //Toast.makeText(Pantalla_Login.this, "Inicio correcto", Toast.LENGTH_LONG).show();
            Intent MenuPrincipal = new Intent(Pantalla_Login.this, mx.edu.ittepic.poll_o.MenuPrincipal.class);
            MenuPrincipal.putExtra("Usuario", Usuario);
            startActivity(MenuPrincipal);
        }
        if(Respuesta.equals("cliente")){
            ActualizarBaseDeDatos();
            //Toast.makeText(Pantalla_Login.this,"Inicio correcto",Toast.LENGTH_SHORT).show();
            Intent Encuestas= new Intent(Pantalla_Login.this, mx.edu.ittepic.poll_o.Encuestas.class);
            Encuestas.putExtra("Usuario",Usuario);
            Encuestas.putExtra("Tipo","1");
            startActivity(Encuestas);
            super.onResume();
        }
    }
    void ActualizarBaseDeDatos(){
        try{
            /*Actualiza las encuestas*/
            SQLiteDatabase base = conexion.getReadableDatabase();
            String EliminarDatosEncuesta="DELETE FROM Encuesta";
            base.execSQL(EliminarDatosEncuesta);
            ConexionWeb web = new ConexionWeb(Pantalla_Login.this,1);
            web.agregarVariables("operacion", "get_encuesta_cliente");
            web.agregarVariables("celular", Usuario);
            URL url = new URL("http://poll-o.ueuo.com/basededatos.php");
            web.execute(url);
            //Toast.makeText(this, "Encuestas", Toast.LENGTH_SHORT).show();

            base.close();

        } catch (MalformedURLException e) {
            new AlertDialog.Builder(Pantalla_Login.this).setMessage
                    (e.getMessage()).setTitle("Error").show();
        }
    }
    void InsertarEnBaseDeDatos(String SQL){
        //Toast.makeText(Pantalla_Login.this, SQL, Toast.LENGTH_SHORT).show();
        try{

            SQLiteDatabase base = conexion.getReadableDatabase();

            /*----------------------------------------------------------------------------------*/
            base.execSQL(SQL);

            base.close();
            //Toast.makeText(this,"SE INSERTO CORRECTAMENTE",Toast.LENGTH_LONG).show();

        }catch (SQLiteException e){
            AlertDialog.Builder alerta= new AlertDialog.Builder(Pantalla_Login.this);
            alerta.setTitle("ERROR").setMessage(e.getMessage()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }

    }
    private boolean CamposVacios(String Usu,String Contra){
        if(Usu.equals("") || Contra.equals("")){
            //return true; LINEA CORRECTa
            return true;
        }
        return false;
    }
}
