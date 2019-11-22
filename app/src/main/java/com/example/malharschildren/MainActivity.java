package com.example.malharschildren;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Context appContext;
    Resources resources;
    String thisPackage;

    int cart = 0;
    int total = 0;
    int styles = 15;
    int sizes = 5;
    int maxFlavors = 4;
    int[][][] quantities = new int[styles][sizes][maxFlavors];
    int[] temp = new int[styles];
    int[][] defaultPrices = new int[][]{{900}, {950}, {650, 650, 650}, {650}, {775, 680}, {950}, {850, 850, 800},
            {960, 960, 960, 960}, {1060, 1060}, {1450, 1450, 1450}, {1550, 1550}, {975, 975}, {1200, 1200}, {850}, {200}};
    int[][][] prices = new int[styles][sizes][maxFlavors];
    boolean[][][] inCart = new boolean[styles][sizes][maxFlavors];
    boolean flag = false;
    Toast toast;
    View toastView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Save bill number even when app is closed
        /*SharedPreferences prefs = getSharedPreferences("PreferencesName", MODE_PRIVATE);int myInt = prefs.getInt("myInt", 0); // 0 is defaultSharedPreferences.Editor editor = getSharedPreferences("PreferencesName", MODE_PRIVATE).edit();editor.putInt("billNumber", 4);editor.apply();*/
        appContext = getApplicationContext();
        resources = getResources();
        thisPackage = MainActivity.this.getPackageName();

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                for (int k = 0; k < maxFlavors; k++) {
                    quantities[i][j][k] = 0;
                    inCart[i][j][k] = false;
                }
            }
            temp[i] = 0;
            String hex = Integer.toHexString(i);
            //Method to adjust text size of spinner
            /*ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(
                   this, R.array.sizeDropdown, R.layout.spinner_layout);
            sizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
            sizeView.setAdapter(sizeAdapter);
            */
            Spinner sizeView = findViewByString("z" + hex);
            Spinner flavorView = findViewByString("f" + hex);
            sizeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String tag = parent.getTag().toString();
                    char tagNum = tag.charAt(1);

                    TextView quantityView = findViewByString("q" + tagNum);
                    Spinner flavorView = findViewByString("f" + tagNum);
                    EditText priceView = findViewByString("p" + tagNum);

                    int flavor = flavorView.getSelectedItemPosition();

                    tagNum = (char) Integer.parseInt(tagNum + "", 16);
                    int index = tagNum;

                    int priceMessage;

                    if (inCart[index][position][flavor]) {
                        temp[index] = quantities[index][position][flavor];
                        priceMessage = prices[index][position][flavor];
                        flag = true;

                    } else {
                        if (flag) {
                            temp[index] = quantities[index][position][flavor];
                        }
                        priceMessage = defaultPrices[tagNum][flavor];
                        flag = false;
                    }
                    priceView.setText(String.valueOf(priceMessage));
                    quantityView.setText(String.valueOf(temp[index]));
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
                    EditText priceView = findViewByString("p" + tagNum);

                    int size = sizeView.getSelectedItemPosition();

                    tagNum = (char) Integer.parseInt(tagNum + "", 16);
                    int index = tagNum;


                    int priceMessage;
                    int message;
                    if (inCart[index][size][position]) {
                        message = quantities[index][size][position];
                        flag = true;
                        temp[index] = quantities[index][size][position];
                        priceMessage = prices[index][size][position];
                    } else {
                        if (flag) {
                            message = 0;
                        } else {
                            message = temp[index];
                        }
                        priceMessage = defaultPrices[tagNum][position];
                        flag = false;
                    }
                    priceView.setText(String.valueOf(priceMessage));
                    quantityView.setText(String.valueOf(message));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Do nothing (how much Amma knows)
                }
            });
        }
    }

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


        textNum = (char) Integer.parseInt(textNum + "", 16);
        quantity = temp[textNum];

        if (clickType == 'm') {
            if (quantity > 0) {
                quantity--;
            }
        } else {
            quantity++;
        }
        quantityView.setText(String.valueOf(quantity));
        temp[textNum] = quantity;
    }


    public void add(View view) {
        char hex = view.getTag().toString().charAt(1);
        int tag = Integer.parseInt(hex + "", 16);
        TextView totalView = findViewByString("price");
        TextView nameView = findViewByString("n" + hex);
        TextView cartView = findViewByString("cart");
        TextView priceView = findViewByString("p" + hex);
        Spinner sizeView = findViewByString("z" + hex);
        Spinner flavorView = findViewByString("f" + hex);

        String name = nameView.getText().toString();
        int sizePos = sizeView.getSelectedItemPosition();
        int flavorPos = flavorView.getSelectedItemPosition();

        String size = sizeView.getItemAtPosition(sizePos).toString();
        String flavor = flavorView.getItemAtPosition(flavorPos).toString();

        size = (size.equalsIgnoreCase("free size") ? " " : " (" + size + ") ");
        flavor = (flavor.equalsIgnoreCase("none") ? "" : " " + flavor + " ");

        int quantity = temp[tag];
        quantities[tag][sizePos][flavorPos] = temp[tag];
        prices[tag][sizePos][flavorPos] = Integer.parseInt(priceView.getText().toString());


        cart = 0;
        total = 0;

        for (int i = 0; i < styles; i++) {
            for (int j = 0; j < sizes; j++) {
                for (int k = 0; k < maxFlavors; k++) {
                    cart += quantities[i][j][k];
                    total += prices[i][j][k] * quantities[i][j][k];
                }
            }
        }

        cartView.setText(String.valueOf(cart));
        totalView.setText(String.valueOf(total));

        if (quantity == 0) {
            toast = Toast.makeText(appContext, "Nothing to update", Toast.LENGTH_SHORT);
        } else {

            toast = Toast.makeText(appContext, "Updated " + flavor + name + size + "to " + quantity, Toast.LENGTH_SHORT);
            inCart[tag][sizePos][flavorPos] = true;
            flag = true;
        }

        toastView = toast.getView();
        //TextView toastText = toastView.findViewById(R.id.toast);
        //toastText.setTextColor(Color.parseColor("#000000"));

        // toastView.getBackground().setColorFilter(Color.parseColor("#FFD5CC5D"), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    public void submit(View view) {
        String name;
        int total = 0;
        EditText nameField = findViewByString("m0");
        EditText emailField = findViewByString("e0");
        EditText phoneField = findViewByString("phone");
        Spinner paymentField = findViewByString("pay");


        String username = nameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        String payment = paymentField.getSelectedItem().toString();

        // "\nNAME ✔ \tSIZE ✔ \tQUANTITY ✔ \tPRICE ✔ \tAMOUNT ✔"
        String message = "Dear " + username + "," +
                "\n\nThank you so much for shopping at Malhar's Children!" +
                "\n\nThis is your invoice:" + "\n\n" +
                "Name: " + username + "\n" +
                "Phone: " + phone + "\n" +
                "payment: " + payment + "\n\n";
        int unit = 0;

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                for (int k = 0; k < maxFlavors; k++) {
                    if (quantities[i][j][k] != 0) {
                        unit += 1;
                        String hex = Integer.toHexString(i);
                        Spinner sizeView = findViewByString("z" + hex);
                        TextView nameView = findViewByString("n" + hex);
                        Spinner flavorView = findViewByString("f" + hex);

                        name = nameView.getText().toString();
                        String flavor = flavorView.getItemAtPosition(k).toString();
                        flavor = (flavor.equalsIgnoreCase("none") ? "" : flavor);
                        String size = sizeView.getItemAtPosition(j).toString();
                        int quantity = quantities[i][j][k];
                        int price = prices[i][j][k];
                        int amount = quantity * price;
                        total += amount;

                        message += "    " + unit + ". " + flavor + " " + name + " X " + quantity + "\n"
                                + "        Size: " + size + "\n"
                                + "        Price: ₹" + price + "\n"
                                + "        Amount: ₹" + amount + "\n\n";
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
        intent.putExtra(Intent.EXTRA_CC, new String[]{"raina.malharschildren@gmail.com"});
        intent.putExtra(Intent.EXTRA_BCC, new String[]{"rahulmaru3507@gmail.com", "suma.maru@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Invoice from Malhar's Children");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void resetClick(View view) {
        reset();
    }

    public void reset() {
        cart = 0;
        total = 0;
        for (int i = 0; i < quantities.length; i++) {
            quantities[i] = new int[sizes][maxFlavors];
            temp[i] = 0;

            String hex = Integer.toHexString(i);

            TextView cartView = findViewByString("cart");
            TextView totalView = findViewByString("price");
            TextView quantityView = findViewByString("q" + hex);
            Spinner flavorView = findViewByString("f" + hex);
            Spinner sizeView = findViewByString("z" + hex);
            EditText priceView = findViewByString("p" + hex);
            EditText nameField = findViewByString("m0");
            EditText emailField = findViewByString("e0");
            EditText phoneField = findViewByString("phone");

            totalView.setText(String.valueOf(total));
            cartView.setText(String.valueOf(cart));
            quantityView.setText("0");
            sizeView.setSelection(0);
            flavorView.setSelection(0);
            priceView.setText(String.valueOf(defaultPrices[i][0]));
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
        }
    }


    public <T extends View> T findViewByString(String name) {
        int id = resources.getIdentifier(name, "id", thisPackage);
        return (T) findViewById(id);
    }
}
