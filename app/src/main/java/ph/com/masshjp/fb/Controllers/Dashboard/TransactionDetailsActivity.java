package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.R;

public class TransactionDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setToolbar(this, R.id.toolbar, "Transaction Details");

        TextView tvDate = findViewById(R.id.tv_date_time);

        // Get the date from Intent
        String dateStr = getIntent().getStringExtra("date");

        // Format the date to "March 14, 2025"
        String formattedDate = formatDate(dateStr);

        // Set the formatted date
        tvDate.setText(formattedDate);

        // Get data from intent
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        String date = getIntent().getStringExtra("date");
        String source = getIntent().getStringExtra("source");
        String arNumber = getIntent().getStringExtra("ar");
        String counter = getIntent().getStringExtra("counter");

        // Bind data to UI
        TextView tvAmount = findViewById(R.id.tv_amount);
        TextView tvDateTime = findViewById(R.id.tv_date_time);
        TextView tvSource = findViewById(R.id.tv_source);
        TextView tvArNumber = findViewById(R.id.tv_ar_number);
        TextView tvCounter = findViewById(R.id.tv_counter);

        tvAmount.setText("₱" + amount);
        tvDateTime.setText(date);
        tvSource.setText(source);
        tvArNumber.setText(arNumber);
        tvCounter.setText(counter);
    }

    private String formatDate(String dateStr) {
        try {
            // Define the input format (adjust if necessary)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = inputFormat.parse(dateStr);

            // Define the output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr; // Return original if parsing fails
        }
    }
}