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

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {
    Resources resources;
    String thisPackage;
    int styles = 20;
    int sizes = 5;
    int[][] quantities = new int[styles][sizes];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //char - int conversion (Ascii): '0' - 48, '1' - 48 ... '8' - 56, '9' - 57.
        size = sizeView.getSelectedItem().toString().charAt(0) - '7';

        quantity = quantities[textNum - 1][size];

        if (clickType == 'm') {
            quantity--;
        } else {
            quantity++;
        }
        quantityView.setText("" + (quantity));
        quantities[textNum - 1][size] = quantity;

    }

    public void submit(View view) {
        String[] names = new String[styles];
        int[] prices = new int[styles];
        String[] sizes = new String[styles];

        // "\nNAME ✔ \tSIZE ✔ \tQUANTITY ✔ \tPRICE ✔ \tAMOUNT ✔"
        String message = "Thanks so much for buying my clothes!" + "\n" +
                "\nThis is your invoice:" + "\n" + "\nStyle Name\tSize\tQuantity\tPrice\tAmount\n" + "\n";

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes.length; j++) {
                if (quantities[i][j] != 0) {
                    int nameId = resources.getIdentifier("n" + i, "id", thisPackage);
                    int priceId = resources.getIdentifier("$" + i, "id", thisPackage);

                    TextView nameView = findViewById(nameId);
                    EditText priceView = findViewById(priceId);

                    String name = nameView.getText().toString() + "\t";
                    String size = (j + 7) + " - " + round(1 + (j + 7) * 1.045) + "\t";
                    int quantity = quantities[i][j];
                    String quantityString = "₹" + Integer.toString(quantity) + "\t";
                    int price = Integer.parseInt(nameView.getText().toString());
                    String priceString = "₹" + Integer.toString(quantity) + "\t";
                    String amount = "₹" + quantity * price;

                    message += name + size + quantityString + priceString + amount
                    + "\n"
                    + "\n"
                    + "\nThank you so much for all your support and for shopping at Malhar's Children!  ";
                }
            }
        }
    }
}
