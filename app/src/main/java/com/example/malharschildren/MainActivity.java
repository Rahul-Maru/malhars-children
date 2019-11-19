package com.example.malharschildren;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {
    Resources resources;
    String thisPackage;
    int styles = 14;
    int sizes = 5;
    int maxFlavors = 4;
    int bill = 4;
    int[][][] quantities = new int[styles][sizes][maxFlavors];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resources = getResources();
        thisPackage = MainActivity.this.getPackageName();

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                for (int k = 0; k < maxFlavors; k++) {
                    quantities[i][j][k] = 0;
                }
            }
            String hex = Integer.toHexString(i);
            Spinner sizeView = findViewByString("z" + hex);
            Spinner flavorView = findViewByString("f" + hex);
            sizeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String tag = parent.getTag().toString();
                    char tagNum = tag.charAt(1);
                    TextView quantityView = findViewByString("q" + tagNum);
                    Spinner flavorView = findViewByString("f" + tagNum);
                    int selectedPos = flavorView.getSelectedItemPosition();

                    tagNum = (char) Integer.parseInt(tagNum + "", 16);
                    int index = tagNum;
                    quantityView.setText(String.valueOf(quantities[index][position][selectedPos]));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Do nothing (how much Amma knows)
                }
            });
            flavorView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String tag = parent.getTag().toString();
                    char tagNum = tag.charAt(1);
                    TextView quantityView = findViewByString("q" + tagNum);
                    Spinner sizeView = findViewByString("z" + tagNum);
                    int selectedPos = sizeView.getSelectedItemPosition();

                    tagNum = (char) Integer.parseInt(tagNum + "", 16);
                    int index = tagNum;
                    quantityView.setText(String.valueOf(quantities[index][selectedPos][position]));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Do nothing (how much Amma knows)
                }
            });
        }
    }

    /*public void spinnerClick(View view) {Log.e("spinnywinnywoopypoo", "spinnerClick: TRIGGERED");Spinner sizeView = (Spinner) view;String selection = sizeView.getSelectedItem().toString();String tag = sizeView.getTag().toString();
        int tagNum = tag.charAt(1) - '0';int quantityId = resources.getIdentifier("q" + tagNum, "id", thisPackage);
        TextView quantityView = findViewById(quantityId);int size = selection.charAt(0);
        if (size == '1') {size = 10 + selection.charAt(1);}size -= '8';
        quantityView.setText(quantities[tagNum][size]);}*/

    public void click(View view) {
        //gets tag (ex. b5) of view that calls this method
        String tag = view.getTag().toString();
        //gets second char in tag (ex. 5), textNum and the first in tag (ex. b), clickType
        char textNum = tag.charAt(1);
        int size;
        char clickType = tag.charAt(0);
        int quantity;

        TextView quantityView = findViewByString("q" + textNum);
        Spinner sizeView = findViewByString("z" + textNum);
        Spinner flavorView = findViewByString("f" + textNum);

        String sizeSelection = sizeView.getSelectedItem().toString();
        int flavorSelection = flavorView.getSelectedItemPosition();


        //char - int conversion (Ascii): '0' - 48, '1' - 49 ... '8' - 56, '9' - 57.
        size = sizeSelection.charAt(0);

        if (size == '1') {
            size = 10 + sizeSelection.charAt(1);
        }

        size -= '8';

        textNum = (char) Integer.parseInt(textNum + "", 16);
        quantity = quantities[textNum][size][flavorSelection];

        if (clickType == 'm') {
            if (quantity > 0) {
                quantity--;

            }
        } else {
            quantity++;
        }
        quantityView.setText("" + (quantity));
        quantities[textNum][size][flavorSelection] = quantity;
    }

    public void submit(View view) {
        String name;
        int total = 0;

        EditText nameField = findViewByString("m0");
        EditText emailField = findViewByString("e0");
        EditText phoneField = findViewByString("c0");

        String username = nameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();

        // "\nNAME ✔ \tSIZE ✔ \tQUANTITY ✔ \tPRICE ✔ \tAMOUNT ✔"
        String message = "Dear " + username + "," +
                "\n\nThank you so much for shopping at Malhar's Children!" +
                "\n\nThis is your invoice:" + "\n\n" +
                "Name: " + username + "\n" +
                "Phone: " + phone + "\n\n";
        int unit = 0;
        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                for (int k = 0; k < maxFlavors; k++) {
                    if (quantities[i][j][k] != 0) {
                        unit += 1;
                        String hex = Integer.toHexString(i);
                        Spinner sizeView = findViewByString("z" + hex);
                        TextView nameView = findViewByString("n" + hex);
                        EditText priceView = findViewByString("p" + hex);
                        Spinner flavorView = findViewByString("f" + hex);
                        TextView quantityView = findViewByString("q" + hex);

                        name = nameView.getText().toString();
                        String flavor = flavorView.getItemAtPosition(k).toString();
                        flavor = (flavor.equalsIgnoreCase("Not available") ? "" : flavor);
                        String size = (j + 8) + " - " + round(1 + (j + 8) * 1.045);
                        int quantity = quantities[i][j][k];
                        int price = Integer.parseInt(priceView.getText().toString());
                        int amount = quantity * price;
                        total += amount;

                        message += "    " + unit + ". " + flavor + " " + name + " X " + quantity + "\n"
                                + "    Size: " + size + "\n"
                                + "    Price: ₹" + price + "\n"
                                + "    Amount: ₹" + amount + "\n\n";
                    }
                }
            }
        }
        message += "Grand Total: ₹" + total + "\n\n\n" +
                "Warmly,\n";

        Log.e("", "submit: " + message);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        // raina.malharschildren@gmail.com
        intent.putExtra(Intent.EXTRA_CC, new String[]{"rahulmar3507@gmail.com", "suma.maru@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Malhar's Children Invoice");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        reset();
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    public void resetClick(View view) {
        reset();
    }

    public void reset() {
        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                String hex = Integer.toHexString(i);
                TextView quantityView = findViewByString("q" + hex);
                quantities[i][j] = new int[maxFlavors];
                quantityView.setText("0");
            }
        }
    }

    public <T extends View> T findViewByString(String name) {
        int id = resources.getIdentifier(name, "id", thisPackage);
        return (T) findViewById(id);
    }


}