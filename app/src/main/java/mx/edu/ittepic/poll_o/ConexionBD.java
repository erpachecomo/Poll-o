package mx.edu.ittepic.poll_o;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class ConexionBD extends SQLiteOpenHelper{

    public ConexionBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL("CREATE TABLE Usuario (celular VARCHAR(10) PRIMARY KEY ,nombre VARCHAR(100),tipo VARCHAR(10),domicilio VARCHAR(255), password varchar(15))");

        sql.execSQL("CREATE TABLE Encuesta (idencuesta INTEGER PRIMARY KEY,compania VARCHAR(50), nombre VARCHAR(50), estado VARCHAR(15),fecha_expiracion DATE, cantidad INTEGER)");

        sql.execSQL("CREATE TABLE Empleado_Encuesta (fk_celular VARCHAR(10),fk_idencuesta INTEGER," +
                "    FOREIGN KEY (fk_celular) REFERENCES Usuario (celular), FOREIGN KEY (fk_idencuesta) REFERENCES Encuesta (idencuesta))");

        sql.execSQL("CREATE TABLE Pregunta (idpregunta INTEGER PRIMARY KEY,fk_idencuesta INTEGER,pregunta VARCHAR(255),tipo VARCHAR(15), respuestas VARCHAR(1000)," +
                "   FOREIGN KEY (fk_idencuesta) REFERENCES Encuesta (idencuesta) )");


        sql.execSQL("CREATE TABLE Respuestas (idrespuesta VARCHAR(20),fk_idpregunta INTEGER,valor VARCHAR (10000)," +
                "   FOREIGN KEY (fk_idpregunta) REFERENCES Pregunta (idpregunta) )");

        //Usuarios

        sql.execSQL("delete from usuario ");

        //sql.execSQL("insert into usuario values('3111126818','Armando','encuestador','Calle Falsa #123','hola123')");
        //sql.execSQL("insert into usuario values('3111234567','Paloma','encuestador','Calle Libano #507','hola123')");
        //Encuesta
        //sql.execSQL("insert into Encuesta values(1,'Coca-Cola','Sabor que mas te gusta','Aprobada','2016-06-24',80)");
       // sql.execSQL("insert into Encuesta values(2,'Chivas','Refuerzo que te gustaria','Aprobada','2016-06-12',50)");
        //Empleado_encuesta
       // sql.execSQL("insert into Empleado_Encuesta values('3111126818',1)");
       // sql.execSQL("insert into Empleado_Encuesta values('3111234567',2)");
        //Pregunta
        //sql.execSQL("insert into Pregunta values(1,'¿Que tamaño de refresco consume regularmente?',1,'opcion multiple','Botella 2 litros')");
        //sql.execSQL("insert into Pregunta values(2,'¿Que refresco es el que consume mas?',1,'opcion multiple','Coca-Cola')");

        //sql.execSQL("insert into Pregunta values(10,'¿Qué horario prefiere para ver los partidos?',2,'opcion multiple','21:00 pm')");
        //sql.execSQL("insert into Pregunta values(11,'¿Qué dia prefiere para ver los partidos?',2,'opcion multiple','Sabado')");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public List<String> obtenerPreguntas(){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM Pregunta";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0)+"_("+cursor.getString(1)+")");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return labels;
    }
    public List<String> obtenerEncuestas(){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM Encuesta";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0)+"("+cursor.getString(1)+")");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return labels;
    }

}
