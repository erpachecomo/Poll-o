package mx.edu.ittepic.poll_o;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class VerificarConexionWIFI {

    Context ventana;
    VerificarConexionWIFI(Context info_ventana){
        ventana=info_ventana;
    }
    protected Boolean estaConectado(){
        ConnectivityManager cm = (ConnectivityManager) ventana.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //Toast.makeText(ventana, "Tienes conexion a internet con WIFI", Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                //Toast.makeText(ventana, "Tienes conexion a internet con DATOS", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            Toast.makeText(ventana, "No tienes conexion a internet", Toast.LENGTH_SHORT).show();
           return false;
        }
        return false;
    }



}
