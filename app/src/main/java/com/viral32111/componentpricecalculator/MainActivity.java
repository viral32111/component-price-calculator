package com.viral32111.componentpricecalculator;

// Import necessary classes
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Main activity class
public class MainActivity extends AppCompatActivity {
    // Setup variables
    TextView tvApplicationTitle;
    TextView tvChassisPrice;
    TextView tvMemory;
    TextView tvProcessor;
    TextView tvOperatingSystem;
    TextView tvGraphicsCard;
    TextView tvDiscountCode;
    Spinner spMemory;
    Spinner spProcessor;
    Spinner spOperatingSystem;
    Spinner spGraphicsCard;
    EditText etDiscountCode;
    Button btnDiscountCodeApply;
    Button btnCalculatePrice;
    Button btnApplicationReset;
    Boolean blDiscountApplied=false;
    Float flPrice=75.0f; // Initial price is price of chassis
    Context ctApplicationContext;
    Pattern priceRegex=Pattern.compile("£(\\d+)");

    // Helper function to easily show a toast notification
    private void showNotification(String text, int duration){
        Toast.makeText(ctApplicationContext,text,duration).show();
    }

    // Helper function to extract the price from a string using regex
    private Integer extractPrice(String str){
        // Find content within the provided string matching the "priceRegex" regex pattern
        Matcher match=priceRegex.matcher(str);

        // Return an integer of the price if it was found, 0 otherwise.
        // This is so we don't need to manually check the options "No Dedicated GPU" & "No Operating System"
        return match.find()?Integer.parseInt(match.group(1)):0;
    }

    // Event to run when the form is launched
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the application view to be our Main Activity design;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set variables to their corresponding views
        tvApplicationTitle=findViewById(R.id.tvApplicationTitle);
        tvChassisPrice=findViewById(R.id.tvChassisPrice);
        tvMemory=findViewById(R.id.tvMemory);
        tvProcessor=findViewById(R.id.tvProcessor);
        tvOperatingSystem=findViewById(R.id.tvOperatingSystem);
        tvGraphicsCard=findViewById(R.id.tvGraphicsCard);
        tvDiscountCode=findViewById(R.id.tvDiscountCode);
        spMemory=findViewById(R.id.spMemory);
        spProcessor=findViewById(R.id.spProcessor);
        spOperatingSystem=findViewById(R.id.spOperatingSystem);
        spGraphicsCard=findViewById(R.id.spGraphicsCard);
        etDiscountCode=findViewById(R.id.etDiscountCode);
        btnDiscountCodeApply=findViewById(R.id.btnDiscountCodeApply);
        btnCalculatePrice=findViewById(R.id.btnCalculatePrice);
        btnApplicationReset=findViewById(R.id.btnApplicationReset);
        ctApplicationContext=getApplicationContext();

        // Setup memory array adapter
        spMemory.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String[]{
                "Please select...",
                "1GB RAM (£5, £5/GB)",
                "2GB RAM (£10, £5/GB)",
                "4GB RAM (£20, £5/GB)",
                "8GB RAM (£80, £10/GB)",
                "16GB RAM (£160, £10/GB)",
                "32GB RAM (£320, £10/GB)"
        }));

        // Setup processor array adapter
        spProcessor.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String[]{
                "Please select...",
                "Intel® Core™ i3 (£80)",
                "Intel® Core™ i5 (£115)",
                "Intel® Core™ i7 (£150)",
                "AMD FX-6300 (£80)"
        }));

        // Setup operating system array adapter
        spOperatingSystem.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String[]{
                "Please select...",
                "No Operating System",
                "Ubuntu Desktop (£5)",
                "Microsoft Windows 7 (£35)",
                "Microsoft Windows 10 (£75)"
        }));

        // Setup graphics card array adapter
        spGraphicsCard.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String[]{
                "Please select...",
                "No Dedicated GPU",
                "MSI GTX 750 (£100)",
                "MSI GS10 (£25)",
                "ASUS GTX 760 (£180)"
        }));
    }

    // Event handler for when the Discount Code Apply button is pressed
    public void onDiscountCodeApplyPressed(View view) {
        // Check if the entered discount code is valid
        if (etDiscountCode.getText().toString().equals("TRADE10")) {
            // Inform the user that it's valid
            showNotification("10% off will be applied when calculated!", Toast.LENGTH_SHORT);

            // Set discount variable to true
            blDiscountApplied=true;

            // Debugging
            Log.d("CUSTOM","The value of blDiscountApplied is: "+blDiscountApplied.toString());
        } else {
            // Inform the user that it's invalid
            showNotification("That discount code is invalid.", Toast.LENGTH_SHORT);

            // Clear the discount code so they can try again
            etDiscountCode.setText("");

            // Set discount variable to false
            blDiscountApplied=false;
        }
    }

    // Event handler for when the Calculate Price button is pressed
    public void onCalculatePricePressed(View view){
        // Fetch all the component choices
        String memory=spMemory.getSelectedItem().toString();
        String processor=spProcessor.getSelectedItem().toString();
        String operatingSystem=spOperatingSystem.getSelectedItem().toString();
        String graphicsCard=spGraphicsCard.getSelectedItem().toString();

        // Ensure that a choice was made for each component
        if(memory.equals("Please select...")||processor.equals("Please select...")||operatingSystem.equals("Please select")||graphicsCard.equals("Please select...")) {
            showNotification("You must make a choice for all components!", Toast.LENGTH_SHORT);
            return;
        }

        // Increment the total price by the sum of all the costs of the selected components
        flPrice+=extractPrice(memory)+extractPrice(processor)+extractPrice(operatingSystem)+extractPrice(graphicsCard);

        // Apply the discount if necessary
        if(blDiscountApplied)flPrice=(flPrice*90)/100;

        // Inform the user of the final system price
        showNotification("The price of this system is £"+flPrice.toString()+".",Toast.LENGTH_LONG);

        // Reset the total price back to default
        flPrice=75.0f;
    }

    // Event handler for when the Application Reset button is pressed
    public void onApplicationResetPressed(View view){
        // Reset spinners to "Please select..."
        spMemory.setSelection(0);
        spProcessor.setSelection(0);
        spOperatingSystem.setSelection(0);
        spGraphicsCard.setSelection(0);

        // Clear the discount code
        etDiscountCode.setText("");

        // Reset the total price
        flPrice=75.0f;

        // Set the discount applied to false
        blDiscountApplied=false;
    }

    // Event handler for when the Application About button is pressed
    public void onApplicationAboutPressed(View view){
        // Open the About blank activity
        startActivity(new Intent(this, About.class));
    }
}
