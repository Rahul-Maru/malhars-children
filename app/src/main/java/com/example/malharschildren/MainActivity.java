package com.example.malharschildren;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Dictionary;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    Resources resources;
    String thisPackage;
    int styles = 20;
    int[] quantities = new int[styles];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View view) {
        //gets tag (ex. b5) of view that calls this method
        char[] tag = view.getTag().toString().toCharArray();
        //gets second char in tag (ex. 5), textnum and the first in tag (ex. b), clicktype
        int textNum = tag[1];
        char clickType = tag[0];
        int quantity = quantities[textNum];

        resources = getResources();
        thisPackage = MainActivity.this.getPackageName();

        int textId = resources.getIdentifier("q" + textNum, "id", thisPackage);
        int sizeId = resources.getIdentifier("z" + textNum, "id", thisPackage);

        TextView quantityView = findViewById(textId);
        Spinner sizeView = findViewById(sizeId);

        if(clickType == 'm') {
            quantity--;
        }
        else {
            quantity++;
        }
        quantityView.setText("" + (quantity));
        quantities[textNum - 1] = quantity;


    }


    
}
