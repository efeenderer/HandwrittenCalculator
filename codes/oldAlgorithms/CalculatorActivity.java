package com.enderefe.engineeringcalculator;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.enderefe.engineeringcalculator.Calculator;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result,solution;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEquals,buttonPower,buttonSin,buttonCos;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9,buttonPI;
    MaterialButton buttonAC,buttonDot;
    ImageView logo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        result = findViewById(R.id.operation);
        solution = findViewById(R.id.solution_tv);
        assignId(buttonC,R.id.button_c);
        assignId(buttonBrackOpen,R.id.button_open_bracket);
        assignId(buttonBrackClose,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonPlus,R.id.button_plus);
        assignId(buttonMinus,R.id.button_minus);
        assignId(buttonEquals,R.id.button_equals);
        assignId(button0,R.id.button_0);
        assignId(button1,R.id.button_1);
        assignId(button2,R.id.button_2);
        assignId(button3,R.id.button_3);
        assignId(button4,R.id.button_4);
        assignId(button5,R.id.button_5);
        assignId(button6,R.id.button_6);
        assignId(button7,R.id.button_7);
        assignId(button8,R.id.button_8);
        assignId(button9,R.id.button_9);
        assignId(buttonAC,R.id.button_ac);
        assignId(buttonDot,R.id.button_dot);
        assignId(buttonPI,R.id.button_PI);
        assignId(buttonPower,R.id.button_power);
        assignId(buttonSin,R.id.button_sin);
        assignId(buttonCos,R.id.button_cos);

        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        result.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                solution.setText(result.getText());
            }
        });

    }
    private void vibrate()  {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(75);
        }
    }

    private void assignId(MaterialButton btn,int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    boolean error = false;
    public void onClick(View view){
        vibrate();
        MaterialButton button =(MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solution.getText().toString();
        if(error){
            error = false;
            solution.setText("");
            result.setText("0");
        }

        if(buttonText.equals("AC")){
            solution.setText("");
            result.setText("0");
            return;
        }
        if(buttonText.equals("=")){
            String finalResult="";
            try{
                Calculator operation = new Calculator(dataToCalculate);
                finalResult = operation.getResult();
                solution.setText("");
            } catch (NumberFormatException x){
                solution.setText("İşlemlerde bir hata var!");
                error = true;
            } catch (Exception e){
                solution.setText("Eşlenmemiş parantez!!");
                error = true;
            }

            if(!finalResult.equals("Err")){
                result.setText(finalResult);
            }
            return;
        }
        if(buttonText.equals("C")){
            if(dataToCalculate.length()>0){
                if (dataToCalculate.charAt(dataToCalculate.length() - 1) == 'n' || dataToCalculate.charAt(dataToCalculate.length() - 1) == 's') {
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                } else {
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                }
            }
        }else{
            dataToCalculate = dataToCalculate+buttonText;
        }
        solution.setText(dataToCalculate);
    }
}
