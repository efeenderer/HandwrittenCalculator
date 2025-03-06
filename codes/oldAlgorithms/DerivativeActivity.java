package com.enderefe.engineeringcalculator;

import androidx.appcompat.app.AppCompatActivity;
import com.enderefe.engineeringcalculator.*;
import com.enderefe.engineeringcalculator.Token.Token;
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

public class DerivativeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView logo;
    EditText fonksiyon, x;
    TextView uyari, ButtonTakeDerivative, ButtonCheck, OP_POW;
    JLatexMathView Latex,xLatex;
    private boolean isFirstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.derivative);

        fonksiyon = findViewById(R.id.deriv_function);
        Latex = findViewById(R.id.Latex);
        xLatex = findViewById(R.id.Latex_derivative);
        logo = findViewById(R.id.logo);
        x = findViewById(R.id.x);
        assignId(ButtonCheck, R.id.buttonCheck);  // Bu satır, ButtonCheck TextView'i id'si ile ilişkilendirir ve tıklama işlemini dinler.
        assignId(ButtonTakeDerivative, R.id.takeDeriv); // Bu satır, ButtonTakeDerivative TextView'i id'si ile ilişkilendirir ve tıklama işlemini dinler.
        assignId(OP_POW, R.id.OP_POW); // Bu satır, OP_POW TextView'i id'si ile ilişkilendirir ve tıklama işlemini dinler.
        uyari = findViewById(R.id.uyari_deriv);
        @SuppressLint("ResourceAsColor") final JLatexMathDrawable drawable = JLatexMathDrawable.builder("x=")
                .textSize(50)
                .color(R.color.secondary_color_lighter)
                .build();
        xLatex.setLatexDrawable(drawable);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Logo resmine tıklanıldığında aktiviteyi sonlandırır.
            }
        });
    }

    private void assignId(TextView btn, int id) {
        btn = findViewById(id); // Belirtilen TextView'i verilen id ile ilişkilendirir.
        btn.setOnClickListener(this); // Tıklama işlemini dinler.
    }

    private void assignId(ImageView btn, int id) {
        btn = findViewById(id); // Belirtilen ImageView'i verilen id ile ilişkilendirir.
        btn.setOnClickListener(this); // Tıklama işlemini dinler.
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(75); // Titreşim başlatır.
        }
    }

    private String LatexToText(String latexInput) {
        // LaTeX formatını metin formatına dönüştürür.
        latexInput = latexInput.replace("\\cdot", "*");
        latexInput = latexInput.replace("\\frac{", "(").replace("}", ")");
        latexInput = latexInput.replace("\\sqrt{", "√(").replace("}", ")");
        latexInput = latexInput.replace("^", "^");

        return latexInput;
    }

    private String LatexString;

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        this.vibrate(); // Tıklamada titreşim yapar.
        TextView button = (TextView) view;
        uyari.setVisibility(View.INVISIBLE);
        @SuppressLint("ResourceAsColor") JLatexMathDrawable drawable_del = JLatexMathDrawable.builder("")
                .textSize(40)
                .color(R.color.secondary_color_lighter)
                .build();
        Latex.setLatexDrawable(drawable_del);

        if (button.getId() == R.id.buttonCheck) {

            try {
                // Girdiyi tokenize eder ve LaTeX formatına dönüştürür.
                Tokenize tokenize = new Tokenize(fonksiyon.getText().toString());
                LatexConvertion latexForm = new LatexConvertion(tokenize.tokenizedExpression);
                this.LatexString = latexForm.LaTeX;
                Calculator islem = new Calculator(tokenize.tokenizedExpression);
                @SuppressLint("ResourceAsColor") final JLatexMathDrawable drawable =
                        x.getText().toString().isEmpty()
                                ?
                        JLatexMathDrawable.builder("F(x)=" + latexForm.LaTeX)
                        .textSize(50)
                        .color(R.color.secondary_color_lighter)
                        .build()
                                :
                        JLatexMathDrawable.builder("F(x)=" + latexForm.LaTeX+"\\\\"+"F("+x.getText().toString()+")="+islem.function(x.getText().toString()))
                        .textSize(50)
                        .color(R.color.secondary_color_lighter)
                        .build() ;


                Latex.setLatexDrawable(drawable);
                Latex.setClickable(true);
            } catch (Exception | StackOverflowError e) {
                uyari.setText("Girdide hata var!"); // Hata durumunda uyarı gösterir.
                uyari.setVisibility(View.VISIBLE);
            }

        }
        else if (button.getId() == R.id.takeDeriv) {
            try {
                // Girdiyi tokenize eder ve türevini alır, LaTeX formatına dönüştürür.
                Tokenize tokenize = new Tokenize(fonksiyon.getText().toString());
                Derivative derivative = new Derivative(LatexConvertion.getPostfixExpression(tokenize.tokenizedExpression));
                this.LatexString = derivative.LaTex;
                Calculator Turev = new Calculator(derivative.DerivativeList);
                @SuppressLint("ResourceAsColor") final JLatexMathDrawable drawable =
                        x.getText().toString().isEmpty()
                            ?
                        JLatexMathDrawable.builder("F'(x)="+derivative.LaTex)
                        .textSize(50)
                        .color(R.color.secondary_color_lighter)
                        .build()
                            :
                        JLatexMathDrawable.builder("F'(x)="+derivative.LaTex+"\\\\"+"F'("+x.getText().toString()+")=" + Turev.function(x.getText().toString()))
                        .textSize(50)
                        .color(R.color.secondary_color_lighter)
                        .build();

                Latex.setLatexDrawable(drawable);
                Latex.setClickable(true);
            } catch (StackOverflowError | Exception e) {
                uyari.setText("Girdide hata var!"); // Hata durumunda uyarı gösterir.
                uyari.setVisibility(View.VISIBLE);
            }

        }
        else if (button.getId() == R.id.OP_POW) {
            fonksiyon.setText(fonksiyon.getText().toString() + "√"); // "√" işaretini EditText'e ekler.
        }
    }
}
