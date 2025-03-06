package com.enderefe.engineeringcalculator;

import androidx.appcompat.app.AppCompatActivity;
import com.enderefe.engineeringcalculator.*;
import com.enderefe.engineeringcalculator.data_structure.*;
import com.enderefe.engineeringcalculator.Token.NumberToken;
import com.google.android.material.button.MaterialButton;

import java.util.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.*;
import android.view.View;
import android.widget.TextView;

import ru.noties.jlatexmath.JLatexMathDrawable;
import ru.noties.jlatexmath.JLatexMathView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView gotoDeriv,gotoCalc;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignId(gotoCalc,R.id.gotoCALCULATOR);
        assignId(gotoDeriv,R.id.gotoDERIVATIVE);
        logo =findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
            }
        });
    }
    private void vibrate()  {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(75);
        }
    }
    private void assignId(TextView btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        vibrate();
        TextView Button = (TextView) v;
        if(Button.getId() == R.id.gotoDERIVATIVE){
            Intent intent = new Intent(MainActivity.this, DerivativeActivity.class);
            startActivity(intent);
        }
        else if(Button.getId() == R.id.gotoCALCULATOR){
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        }
    }
}