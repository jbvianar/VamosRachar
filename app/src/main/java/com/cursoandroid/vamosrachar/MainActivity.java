package com.cursoandroid.vamosrachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {


    EditText edtValor,edtDiv;
    TextView tvResult;
    FloatingActionButton share,play;
    TextToSpeech ttsPlayer;
    int grupo=2;
    double valor=0.0;
    String resultFormatado= "0,00";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtValor=(EditText) findViewById(R.id.editValor);
        edtValor.addTextChangedListener(this);
        edtDiv=(EditText) findViewById(R.id.editDiv);
        edtDiv.addTextChangedListener(this);

        tvResult= (TextView) findViewById(R.id.tvResultado);
        share = (FloatingActionButton) findViewById(R.id.btShare);
        share.setOnClickListener(this);

        play=(FloatingActionButton) findViewById(R.id.btPlay);
        play.setOnClickListener(this);
        // check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);

    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1122){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                //the user has the necessary data - create the TTS
                ttsPlayer = new TextToSpeech(this,this);
            }else {
                // no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
                //startActivityForResult(installTTSIntent, 1122);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.v("PDM",edtValor.getText().toString());

        try {
            valor =Double.parseDouble(edtValor.getText().toString());
            grupo = Integer.parseInt(edtDiv.getText().toString());

            if(grupo != 0){
                double res= valor/grupo;
                DecimalFormat df= new DecimalFormat("#.00");
                tvResult.setText("R$ "+df.format(res));

            }else{
                tvResult.setText("R$ 0,00");
            }




        }catch (Exception e){
            tvResult.setText("R$ 0,00");
            Log.v("Erro","Numeros usados geram erro no resultado");
        }



    }

    @Override
    public void onClick(View v) {
        if (v==share){
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "O valor para cada pessoa foi de:" + tvResult.getText().toString());
            startActivity(intent);
        }

        if(v==play){
            //Log.v("PDM Play",tvResult.getText().toString());
            if (ttsPlayer!=null){
               // Log.v("PDM Play2",tvResult.getText().toString());
               ttsPlayer.speak("O valor para cada pessoa foi de:" + tvResult.getText().toString(),TextToSpeech.QUEUE_FLUSH,null, "ID1");
            }
        }

    }



    @Override
    public void onInit(int initStatus) {

        //checando a inicializacao
        if(initStatus == TextToSpeech.SUCCESS){
            Toast.makeText(this, "TTS ativado...",
                    Toast.LENGTH_LONG).show();
        }else if (initStatus == TextToSpeech.ERROR){
            Toast.makeText(this,"Sem TTS habilidtado...", Toast.LENGTH_LONG).show();
        }
    }
}