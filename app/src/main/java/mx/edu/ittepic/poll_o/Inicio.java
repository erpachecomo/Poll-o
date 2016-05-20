package mx.edu.ittepic.poll_o;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class Inicio extends AppCompatActivity {
    ImageView letra, logo;
    CountDownTimer timer;
    float transparencia_letras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio);
        letra = (ImageView) findViewById(R.id.ivletras);
        logo = (ImageView) findViewById(R.id.ivlogo);
        transparencia_letras=0;

        letra.setAlpha(transparencia_letras);
        timer = new CountDownTimer(10000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                transparencia_letras+=0.09;
                letra.setAlpha(transparencia_letras);
                if(transparencia_letras>=0.5){
                    logo.setImageBitmap(BitmapFactory.decodeResource(Inicio.this.getResources(), R.drawable.logopollo));
                }
                if(transparencia_letras>=1){
                    timer.cancel();
                }
            }

            @Override
            public void onFinish() {
                if(transparencia_letras>=1){
                    Intent pantalla=new Intent(Inicio.this,Pantalla_Login.class);
                    startActivity(pantalla);

                }else{
                    timer.start();
                }
            }
        };

        timer.start();
    }
}

