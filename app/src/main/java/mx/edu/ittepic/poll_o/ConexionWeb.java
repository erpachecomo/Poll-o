package mx.edu.ittepic.poll_o;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConexionWeb extends AsyncTask<URL,String,String> {
    List<String[]> variables;
    Context form;

    public ConexionWeb(Context f){
        variables = new ArrayList<String[]>();
        form = f;
    }

    public void agregarVariables(String id, String dato){
        String[] temp = {id, dato};
        variables.add(temp);
    }
    @Override
    protected String doInBackground(URL... params) {
    return "";
    }

    protected void onPostExecute(String res){

    }
}
