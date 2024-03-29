package com.example.malharschildren;

import android.content.Context;
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
    int[][] defaultPrices = new int[][]{{875}, {950, 950, 950}, {650, 650, 650}, {650}, {775, 680}, {950}, {850, 850, 800},
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
            //Method to adjust text size of spinner
            /*ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(
                   this, R.array.sizeDropdown, R.layout.spinner_layout);
            sizeAdapter.setDropDownViewResource(R.layout.spinner_layout);
            sizeView.setAdapter(sizeAdapter);
            */
            Spinner sizeView = findViewByString("z" + i);
            Spinner flavorView = findViewByString("f" + i);
            sizeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String tag = parent.getTag().toString();
                    int index = Integer.parseInt(tag.substring(1));

                    TextView quantityView = findViewByString("q" + index);
                    Spinner flavorView = findViewByString("f" + index);
                    EditText priceView = findViewByString("p" + index);

                    int flavor = flavorView.getSelectedItemPosition();

                    int priceMessage;

                    if (inCart[index][position][flavor]) {
                        temp[index] = quantities[index][position][flavor];
                        priceMessage = prices[index][position][flavor];
                        flag = true;

                    } else {
                        if (flag) {
                            temp[index] = quantities[index][position][flavor];
                        }
                        priceMessage = defaultPrices[index][flavor];
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
                    int index = Integer.parseInt(tag.substring(1));
                    TextView quantityView = findViewByString("q" + index);
                    Spinner sizeView = findViewByString("z" + index);
                    EditText priceView = findViewByString("p" + index);

                    int size = sizeView.getSelectedItemPosition();

                    int priceMessage;
                    int message;
//                    if (inCart[index][position][flavor]) {
//                        temp[index] = quantities[index][position][flavor];
//                        priceMessage = prices[index][position][flavor];
//                        flag = true;
//
//                    } else {
//                        if (flag) {
//                            temp[index] = quantities[index][position][flavor];
//                        }
//                        priceMessage = defaultPrices[index][flavor];
//                        flag = false;
//                    }
                    if (inCart[index][size][position]) {
                        flag = true;
                        temp[index] = quantities[index][size][position];
                        priceMessage = prices[index][size][position];
                    } else {
                        if (flag) {
                            temp[index] = 0;
                        }
                        priceMessage = defaultPrices[index][position];
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
        }
    }

    public void click(View view) {
        //gets tag (ex. b15) of view that calls this method
        String tag = view.getTag().toString();
        //gets the second and after chars in tag (ex. 15), textNum and the first in tag (ex. b), clickType
        int textNum = Integer.parseInt(tag.substring(1));
        int size;
        char clickType = tag.charAt(0);
        int quantity;


        TextView quantityView = findViewByString("q" + textNum);
        Spinner sizeView = findViewByString("z" + textNum);
        Spinner flavorView = findViewByString("f" + textNum);


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
        int tag = Integer.parseInt(view.getTag().toString().substring(1));
        TextView totalView = findViewByString("total");
        TextView nameView = findViewByString("n" + tag);
        TextView cartView = findViewByString("cart");
        TextView priceView = findViewByString("p" + tag);
        Spinner sizeView = findViewByString("z" + tag);
        Spinner flavorView = findViewByString("f" + tag);

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

            toast = Toast.makeText(appContext, "Updated " + flavor + name + size + " qty to " + quantity, Toast.LENGTH_SHORT);
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
        EditText nameField = findViewByString("m0");
        EditText emailField = findViewByString("e0");
        EditText phoneField = findViewByString("phone");
        Spinner paymentField = findViewByString("pay");
        TextView totalView = findViewByString("total");


        String username = nameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        String payment = paymentField.getSelectedItem().toString();

        total = Integer.parseInt(totalView.getText().toString());
        String message = "Dear " + username + "," +
                "\n\nThank you so much for shopping at Malhar's Children!" +
                "\n\nThis is your invoice:" + "\n\n" +
                "Name: " + username + "\n" +
                "Phone: " + phone + "\n" +
                "Payment: " + payment + "\n\n";
        int itemNumber = 0;

        for (int i = 0; i < quantities.length; i++) {
            for (int j = 0; j < sizes; j++) {
                for (int k = 0; k < maxFlavors; k++) {
                    if (quantities[i][j][k] != 0) {
                        itemNumber += 1;
                        Spinner sizeView = findViewByString("z" + i);
                        TextView nameView = findViewByString("n" + i);
                        Spinner flavorView = findViewByString("f" + i);

                        name = nameView.getText().toString();
                        String flavor = flavorView.getItemAtPosition(k).toString();
                        flavor = (flavor.equalsIgnoreCase("none") ? "" : flavor);
                        String size = sizeView.getItemAtPosition(j).toString();
                        int quantity = quantities[i][j][k];
                        int price = prices[i][j][k];
                        int amount = quantity * price;

                        message += "    " + itemNumber + ". " + flavor + " " + name + " X " + quantity + "\n"
                                + "        Size: " + size + "\n"
                                + "        Price: ₹" + price + "\n"
                                + "        Amount: ₹" + amount + "\n\n";
                    }
                }
            }
        }
        message += "Grand Total: ₹" + total + "\n\n\n" +
                "Warmly,";

        Log.e("", "submit: " + message);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_CC, new String[]{"malharschildren@gmail.com"});
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


            TextView cartView = findViewByString("cart");
            TextView totalView = findViewByString("total");
            TextView quantityView = findViewByString("q" + i);
            Spinner flavorView = findViewByString("f" + i);
            Spinner sizeView = findViewByString("z" + i);
            EditText priceView = findViewByString("p" + i);
            EditText nameField = findViewByString("m0");
            EditText emailField = findViewByString("e0");
            EditText phoneField = findViewByString("phone");
            Spinner paymentField = findViewByString("pay");

            totalView.setText(String.valueOf(total));
            cartView.setText(String.valueOf(cart));
            quantityView.setText("0");
            sizeView.setSelection(0);
            flavorView.setSelection(0);
            priceView.setText(String.valueOf(defaultPrices[i][0]));
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            paymentField.setSelection(0);
        }
    }


    public <T extends View> T findViewByString(String name) {
        int id = resources.getIdentifier(name, "id", thisPackage);
        return (T) findViewById(id);
    }
}
