package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }




    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 0) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }



    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Find user name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();


        //Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();


        //Figure out if the user wants chocolate topping
        CheckBox chocolateCheckbbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckbbox.isChecked();

        //calculate price
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        //display order summary on the screen
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);






        //Use an intent to launch an email app.
        //Send the order summary in the email body.

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this

        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for" + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }








    }

    /**
     * Calculates the price of the order.
     *
     ** @param addWhippedCream is whether or not we should include whipped cream topping in the price
     ** @param addChocolate    is whether or not we should include chocolate topping in the price
     * @returing the price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {

        //Price of  1 cup of coffee
        int basePrice = 5;

        //Add $1 if the user wants whipped cream
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }

        //Add $2 if the user wants chocolate
        if (addChocolate) {
            basePrice = basePrice + 2;
        }


        //calculate the total order price by multiplying by quantity

        return quantity * basePrice;
    }







    /**This method displays the given person name ordering.
     * create summary of order
     * @param addWhippedCream  is whether or not the user wants whippped cream topping
     * @param  addchocolate is whether or not the user wants chocolate
     * @param price of order
     * return a text summary*/
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addchocolate) {

        String priceMessage =getString(R.string.order_summary_name, name);


        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);

        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addchocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }


    public void shareApp(View view) {
        Button sharebtn = (Button) findViewById(R.id.sharebtn);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Download this application now: https://play.google.com/store/apps/details?="
                        + BuildConfig.APPLICATION_ID + "\n\n";
                String sharesub = "Just Java App";

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, sharesub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            }
        });
    }
}


