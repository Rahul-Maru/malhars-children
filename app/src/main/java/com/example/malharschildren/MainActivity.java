package com.example.malharschildren;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Dictionary;
import java.util.Enumeration;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {
    Resources resources;
    String thisPackage;
    int styles = 1;
    int sizes = 5;
    int[][] quantities = new int[styles][sizes];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                quantities[i][j] = 0;
            }
        }
    }


    public void click(View view) {
        //gets tag (ex. b5) of view that calls this method
        char[] tag = view.getTag().toString().toCharArray();
        //gets second char in tag (ex. 5), textNum and the first in tag (ex. b), clickType
        int textNum = tag[1] - '0';
        int size;
        char clickType = tag[0];
        int quantity;


        resources = getResources();
        thisPackage = MainActivity.this.getPackageName();

        // Get ids of respective TextView and Size Spinner
        int textId = resources.getIdentifier("q" + textNum, "id", thisPackage);
        int sizeId = resources.getIdentifier("z" + textNum, "id", thisPackage);

        TextView quantityView = findViewById(textId);
        Spinner sizeView = findViewById(sizeId);

        String selection = sizeView.getSelectedItem().toString();

        //char - int conversion (Ascii): '0' - 48, '1' - 49 ... '8' - 56, '9' - 57.
        size = selection.charAt(0);

        if (size == '1') {
            size = 10 + selection.charAt(1);
        }

        size -= '8';
        Log.e("", "click: " + size);

        quantity = quantities[textNum ][size];

        if (clickType == 'm') {
            quantity--;
        } else {
            quantity++;
        }
        quantityView.setText("" + (quantity));
        quantities[textNum][size] = quantity;

    }

    public void submit(View view) {
        String name;

        // "\nNAME ✔ \tSIZE ✔ \tQUANTITY ✔ \tPRICE ✔ \tAMOUNT ✔"
        String message = "Thanks so much for buying my clothes!" + "\n" +
                "\nThis is your invoice:" + "\n" + "\nStyle Name\tFlavour\tSize\tQuantity\tPrice\tAmount\n" + "\n";

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                if (quantities[i][j] != 0) {
                    int nameId = resources.getIdentifier("n" + i, "id", thisPackage);
                    int priceId = resources.getIdentifier("p" + i, "id", thisPackage);
                    int flavorId = resources.getIdentifier("f" + i, "id", thisPackage);
                    int quantityId = resources.getIdentifier("q" + i, "id", thisPackage);

                    TextView nameView = findViewById(nameId);
                    EditText priceView = findViewById(priceId);
                    EditText flavorView = findViewById(flavorId);
                    TextView quantityView = findViewById(quantityId);

                    name = nameView.getText().toString() + "\t";
                    String flavor = flavorView.getText() + "\t";
                    String size = (j + 7) + " - " + round(1 + (j + 7) * 1.045) + "\t";
                    int quantity = quantities[i][j];
                    String quantityString = quantity + "\t";
                    int price = Integer.parseInt(priceView.getText().toString());
                    String priceString = "₹" + Integer.toString(price) + "\t";
                    String amount = "₹" + quantity * price + "\n";

                    message += name + flavor + size + quantityString + priceString + amount;

                    quantities[i][j] = 0;
                    quantityView.setText("0");
                    flavorView.setText("Vanilla");
 
                }
            }
        }
        message += "\n"
                + "\nThank you so much for all your support and for shopping at Malhar's Children!";
        Log.e("", "submit: " + message);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rahulmaru3507@gmail.com", "suma.maru@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Malhar's Children Invoice");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
