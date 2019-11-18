package com.example.malharschildren;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


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

        resources = getResources();
        thisPackage = MainActivity.this.getPackageName();

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                quantities[i][j] = 0;
            }
            // Spinner sizeView = findViewByString("z" + i);
        }
    }

   public void spinnerClick(View view) {
        Log.e("spinnywinnywoopypoo", "spinnerClick: TRIGGERED");
        Spinner sizeView = (Spinner) view;
        String selection = sizeView.getSelectedItem().toString();
        String tag = sizeView.getTag().toString();
        int tagNum = tag.charAt(1) - '0';

        int quantityId = resources.getIdentifier("q" + tagNum, "id", thisPackage);
        TextView quantityView = findViewById(quantityId);

        int size = selection.charAt(0);

        if (size == '1') {
            size = 10 + selection.charAt(1);
        }

        size -= '8';

        quantityView.setText(quantities[tagNum][size]);
    }

    public void click(View view) {
        //gets tag (ex. b5) of view that calls this method
        char[] tag = view.getTag().toString().toCharArray();
        //gets second char in tag (ex. 5), textNum and the first in tag (ex. b), clickType
        int textNum = tag[1] - '0';
        int size;
        char clickType = tag[0];
        int quantity;

        TextView quantityView = findViewByString("q" + textNum);
        Spinner sizeView = findViewByString("z" + textNum);

        String selection = sizeView.getSelectedItem().toString();

        //char - int conversion (Ascii): '0' - 48, '1' - 49 ... '8' - 56, '9' - 57.
        size = selection.charAt(0);

        if (size == '1') {
            size = 10 + selection.charAt(1);
        }

        size -= '8';

        quantity = quantities[textNum][size];

        if (clickType == 'm') {
            if (quantity > 0) {
                quantity--;
            }
            else {

            }
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

                    TextView nameView = findViewByString("n" + i);
                    EditText priceView = findViewByString("p" + i);
                    EditText flavorView = findViewByString("f" + i);
                    TextView quantityView = findViewByString("q" + i);

                    name = nameView.getText().toString() + "\t";
                    String flavor = flavorView.getText() + "\t";
                    String size = (j + 7) + " - " + round(1 + (j + 7) * 1.045) + "\t";
                    int quantity = quantities[i][j];
                    String quantityString = quantity + "\t";
                    int price = Integer.parseInt(priceView.getText().toString());
                    String priceString = "₹" + Integer.toString(price) + "\t";
                    String amount = "₹" + quantity * price + "\n";

                    message += name + flavor + size + quantityString + priceString + amount;


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
        reset();
    }

    public void resetClick(View view) {
        reset();
    }

    public void reset() {
        for(int i = 0; i < quantities.length; i++) {
            TextView quantityView = findViewByString("q" + i);
            EditText flavorView = findViewByString("f" + i);
            quantities[i] = new int[sizes];
            quantityView.setText("0");
            flavorView.setText("Default");
        }
    }

    public <T extends View> T findViewByString(String name) {
        int id = resources.getIdentifier(name, "id", thisPackage);
        return (T) findViewById(id);
    }


}

