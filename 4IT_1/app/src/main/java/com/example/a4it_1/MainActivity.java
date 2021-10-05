package com.example.a4it_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String[] operation = {""};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ll);
        View.OnClickListener numberListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText = ((EditText) findViewById(R.id.edit1)).getText().toString();
                if (!(editText.equals("0"))) {
                    ((TextView) findViewById(R.id.edit1)).setText(editText + ((Button) view).getText());
                }
                else {
                    ((TextView) findViewById(R.id.edit1)).setText(((Button) view).getText());
                }
            }
        };
        View.OnClickListener clearOneListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText = ((EditText) findViewById(R.id.edit1)).getText().toString();
                if (!editText.equals("0")) {
                    if(editText.length()==1){
                        ((TextView) findViewById(R.id.edit1)).setText("0");
                    }
                    else{
                        ((TextView) findViewById(R.id.edit1)).setText(editText.substring(0, editText.length() - 1));
                    }
                }
            }
        };
        View.OnClickListener clearAllListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.edit1)).setText("0");
                ((TextView) findViewById(R.id.edit2)).setText("");
                operation[0] = "";

                ((Button) findViewById(R.id.bPlus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bPlus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMinus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMinus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMultip)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMultip)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bDivision)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bDivision)).setTextColor(Color.WHITE);

            }
        };
        View.OnClickListener plusMinusListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText = ((EditText) findViewById(R.id.edit1)).getText().toString();
                if (!editText.equals("0") && !editText.equals("")) {
                    if (!editText.contains(".")) {
                        int editNum = Integer.parseInt(editText);
                        ((TextView) findViewById(R.id.edit1)).setText(Integer.toString((-1) * editNum));
                    }
                    else{
                        double editNum = Double.parseDouble(editText);
                        ((TextView) findViewById(R.id.edit1)).setText(Double.toString((-1) * editNum));
                    }
                }
            }
        };
        View.OnClickListener dotListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText = ((EditText) findViewById(R.id.edit1)).getText().toString();
                if (!editText.equals("")) {
                    if (!editText.contains(".")) {
                        ((TextView) findViewById(R.id.edit1)).setText(editText + ((Button) view).getText());
                    }
                }
            }
        };
        View.OnClickListener plusListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText1 = ((EditText) findViewById(R.id.edit1)).getText().toString();
                String editText2 = ((EditText) findViewById(R.id.edit2)).getText().toString();
                if (!operation[0].equals("")){
                    operation[0] = equal(editText1, editText2, operation[0]);
                }
                else{
                    plus(editText1, editText2);
                }

                ((Button) findViewById(R.id.bPlus)).setBackgroundColor(Color.WHITE);
                ((Button) findViewById(R.id.bPlus)).setTextColor(0xFFFFBB33);

                ((Button) findViewById(R.id.bMinus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMinus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMultip)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMultip)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bDivision)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bDivision)).setTextColor(Color.WHITE);
                operation[0]="+";
            }
        };
        View.OnClickListener minusListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText1 = ((EditText) findViewById(R.id.edit1)).getText().toString();
                String editText2 = ((EditText) findViewById(R.id.edit2)).getText().toString();
                if (!operation[0].equals("")){
                    operation[0] = equal(editText1, editText2, operation[0]);
                }
                else {
                    minus(editText1, editText2);
                }
                operation[0]="-";

                ((Button) findViewById(R.id.bPlus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bPlus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMinus)).setBackgroundColor(Color.WHITE);
                ((Button) findViewById(R.id.bMinus)).setTextColor(0xFFFFBB33);

                ((Button) findViewById(R.id.bMultip)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMultip)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bDivision)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bDivision)).setTextColor(Color.WHITE);
            }
        };
        View.OnClickListener multipleListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText1 = ((EditText) findViewById(R.id.edit1)).getText().toString();
                String editText2 = ((EditText) findViewById(R.id.edit2)).getText().toString();
                if (!operation[0].equals("")){
                    operation[0] = equal(editText1, editText2, operation[0]);
                }
                else {
                    multiple(editText1, editText2);
                }
                operation[0] = "*";

                ((Button) findViewById(R.id.bPlus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bPlus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMinus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMinus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMultip)).setBackgroundColor(Color.WHITE);
                ((Button) findViewById(R.id.bMultip)).setTextColor(0xFFFFBB33);

                ((Button) findViewById(R.id.bDivision)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bDivision)).setTextColor(Color.WHITE);
            }
        };
        View.OnClickListener divisionListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText1 = ((EditText) findViewById(R.id.edit1)).getText().toString();
                String editText2 = ((EditText) findViewById(R.id.edit2)).getText().toString();
                if (!operation[0].equals("")){
                    operation[0] = equal(editText1, editText2, operation[0]);
                }
                else{
                    division(editText1, editText2);
                }
                operation[0]="/";

                ((Button) findViewById(R.id.bPlus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bPlus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMinus)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMinus)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bMultip)).setBackgroundColor(0xFFFFBB33);
                ((Button) findViewById(R.id.bMultip)).setTextColor(Color.WHITE);

                ((Button) findViewById(R.id.bDivision)).setBackgroundColor(Color.WHITE);
                ((Button) findViewById(R.id.bDivision)).setTextColor(0xFFFFBB33);
            }
        };
        View.OnClickListener equealListener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String editText1 = ((EditText) findViewById(R.id.edit1)).getText().toString();
                String editText2 = ((EditText) findViewById(R.id.edit2)).getText().toString();
                equal(editText1, editText2, operation[0]);
            }
        };
        ((Button) findViewById(R.id.b1)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b2)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b3)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b4)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b5)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b6)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b7)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b8)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b9)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.b0)).setOnClickListener(numberListener);
        ((Button) findViewById(R.id.bClearOne)).setOnClickListener(clearOneListener);
        ((Button) findViewById(R.id.bClearAll)).setOnClickListener(clearAllListener);
        ((Button) findViewById(R.id.bPlusMinus)).setOnClickListener(plusMinusListener);
        ((Button) findViewById(R.id.bDot)).setOnClickListener(dotListener);
        ((Button) findViewById(R.id.bPlus)).setOnClickListener(plusListener);
        ((Button) findViewById(R.id.bMinus)).setOnClickListener(minusListener);
        ((Button) findViewById(R.id.bMinus)).setOnClickListener(minusListener);
        ((Button) findViewById(R.id.bMultip)).setOnClickListener(multipleListener);
        ((Button) findViewById(R.id.bDivision)).setOnClickListener(divisionListener);
        ((Button) findViewById(R.id.bEqueal)).setOnClickListener(equealListener);
    }


    public void copyClick(View view) {
        String editText = ((EditText) findViewById(R.id.edit1)).getText().toString();
        ((TextView) findViewById(R.id.edit1)).setText(editText);
    }

    @SuppressLint("SetTextI18n")
    public void plus(String editText1, String editText2) {
        if (editText2.equals("")) {
            ((TextView) findViewById(R.id.edit2)).setText(editText1);
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bPlus)).setBackgroundColor(Color.WHITE);
            ((Button) findViewById(R.id.bPlus)).setTextColor(0xFFFFBB33);
        } else if (!editText1.equals("")) {
            if (editText1.contains(".") || editText2.contains(".")) {
                double editNum1 = Double.parseDouble(editText1);
                double editNum2 = Double.parseDouble(editText2);
                double editNum = editNum1 + editNum2;
                if (editNum % 1 == 0) {
                    ((TextView) findViewById(R.id.edit2)).setText(Integer.toString((int) editNum));
                } else {
                    ((TextView) findViewById(R.id.edit2)).setText(Double.toString(editNum));
                }
            } else {
                int editNum1 = Integer.parseInt(editText1);
                int editNum2 = Integer.parseInt(editText2);
                ((TextView) findViewById(R.id.edit2)).setText(Integer.toString(editNum1 + editNum2));
            }
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bPlus)).setBackgroundColor(0xFFFFBB33);
            ((Button) findViewById(R.id.bPlus)).setTextColor(Color.WHITE);
        }
    }
    @SuppressLint("SetTextI18n")
    public void minus(String editText1, String editText2){
        if (editText2.equals("")) {
            ((TextView) findViewById(R.id.edit2)).setText(editText1);
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bMinus)).setBackgroundColor(Color.WHITE);
            ((Button) findViewById(R.id.bMinus)).setTextColor(0xFFFFBB33);
        }
        else if (!editText1.equals("")) {
            if (editText1.contains(".")||editText2.contains(".")) {
                double editNum1 = Double.parseDouble(editText1);
                double editNum2 = Double.parseDouble(editText2);
                double editNum = editNum2-editNum1;
                if (editNum % 1 == 0) {
                    ((TextView) findViewById(R.id.edit2)).setText(Integer.toString((int)editNum));
                }
                else{
                    ((TextView) findViewById(R.id.edit2)).setText(Double.toString(editNum));
                }
            }
            else{
                int editNum1 = Integer.parseInt(editText1);
                int editNum2 = Integer.parseInt(editText2);
                ((TextView) findViewById(R.id.edit2)).setText(Integer.toString(editNum2-editNum1));
            }
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bMinus)).setBackgroundColor(0xFFFFBB33);
            ((Button) findViewById(R.id.bMinus)).setTextColor(Color.WHITE);
        }
    }
    @SuppressLint("SetTextI18n")
    public void multiple(String editText1, String editText2){
        if (editText2.equals("")) {
            ((TextView) findViewById(R.id.edit2)).setText(editText1);
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bMultip)).setBackgroundColor(Color.WHITE);
            ((Button) findViewById(R.id.bMultip)).setTextColor(0xFFFFBB33);
        }
        else if (!editText1.equals("0")){
            double editNum1 = Double.parseDouble(editText1);
            double editNum2 = Double.parseDouble(editText2);
            double editNum = editNum2*editNum1;
            if (editNum % 1 == 0) {
                ((TextView) findViewById(R.id.edit2)).setText(Integer.toString((int)editNum));
            }
            else{
                ((TextView) findViewById(R.id.edit2)).setText(Double.toString(editNum));
            }
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bMultip)).setBackgroundColor(0xFFFFBB33);
            ((Button) findViewById(R.id.bMultip)).setTextColor(Color.WHITE);
        }
    }
    @SuppressLint("SetTextI18n")
    public void division(String editText1, String editText2){
        if (editText2.equals("")) {
            ((TextView) findViewById(R.id.edit2)).setText(editText1);
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bDivision)).setBackgroundColor(Color.WHITE);
            ((Button) findViewById(R.id.bDivision)).setTextColor(0xFFFFBB33);
        }
        else if (!editText1.equals("0")){
            double editNum1 = Double.parseDouble(editText1);
            double editNum2 = Double.parseDouble(editText2);
            double editNum = editNum2/editNum1;
            if (editNum % 1 == 0) {
                ((TextView) findViewById(R.id.edit2)).setText(Integer.toString((int)editNum));
            }
            else{
                ((TextView) findViewById(R.id.edit2)).setText(Double.toString(editNum));
            }
            ((TextView) findViewById(R.id.edit1)).setText("");
            ((Button) findViewById(R.id.bDivision)).setBackgroundColor(0xFFFFBB33);
            ((Button) findViewById(R.id.bDivision)).setTextColor(Color.WHITE);
        }
    }
    public String equal(String editText1, String editText2, String Op){
        if(!editText1.equals("")&&!editText2.equals("")){
            if(Op.equals("+")){
                plus(editText1, editText2);
                return "";
            }
            else if(Op.equals("-")){
                minus(editText1, editText2);
                return "";
            }
            else if(Op.equals("*")){
                multiple(editText1, editText2);
                return "";
            }
            else if(Op.equals("/")){
                division(editText1, editText2);
                return "";
            }
        }
        else if(!editText1.equals("")){
            ((TextView) findViewById(R.id.edit2)).setText(editText1);
            ((TextView) findViewById(R.id.edit1)).setText("0");
        }
        return "";
    }
}