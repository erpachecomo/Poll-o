package mx.edu.ittepic.poll_o;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ConexionBD extends SQLiteOpenHelper{

    public ConexionBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL("CREATE TABLE Empleado (celular VARCHAR(10) PRIMARY KEY ,nombre VARCHAR(100),tipo VARCHAR(10),domicilio VARCHAR(255), password varchar(15))");

        sql.execSQL("CREATE TABLE Encuesta (idencuesta INTEGER PRIMARY KEY ,compania VARCHAR(50),estado VARCHAR(15),fecha_expiracion DATE, cantidad INTEGER)");

        sql.execSQL("CREATE TABLE Empleado_Encuesta (fk_celular VARCHAR(10),fk_idencuesta INTEGER," +
                "    FOREIGN KEY (fk_celular) REFERENCES Empleado (celular), FOREIGN KEY (fk_idencuesta) REFERENCES Encuesta (idencuesta)");

        sql.execSQL("CREATE TABLE Pregunta (idpregunta INTEGER PRIMARY KEY,fk_idencuesta INTEGER,pregunta VARCHAR(255),tipo VARCHAR(15), respuestas(1000)," +
                "   FOREIGN KEY (fk_idencuesta) REFERENCES Encuesta (idencuesta)");


        sql.execSQL("CREATE TABLE Respuestas (idrespuesta VARCHAR(20),fk_idpregunta INTEGER,valor VARCHAR (10000)," +
                "   FOREIGN KEY (fk_idpregunta) REFERENCES Pregunta (idpregunta)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
