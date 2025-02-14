package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.R;

public class AddDebit extends BaseActivity {

    EditText et_date;
    private EditText sourceEditText, dateEditText, amountEditText, arNumberEditText, counterNameEditText;
    private Button saveButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setToolbar(this, R.id.toolbar, "Add Transactions");

        //Initialization
        et_date = findViewById(R.id.et_date);
        sourceEditText = findViewById(R.id.et_source);
        dateEditText = findViewById(R.id.et_date);
        amountEditText = findViewById(R.id.et_amount);
        arNumberEditText = findViewById(R.id.et_ar);
        counterNameEditText = findViewById(R.id.et_counter);
        saveButton = findViewById(R.id.btn_save);

        setupToolbar();
        datePicker();
        setupFirestore();
        setupDatePicker();
        setupSaveButton();

    }
    //Date Picker
    private void datePicker(){
        et_date.setOnClickListener(v -> {
            // Get current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format date as MM/dd/yyyy
                        String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                        et_date.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    /**
     * Configures the toolbar with a title
     */
    private void setupToolbar() {
        setToolbar(this, R.id.toolbar, "Add Debit Transaction");
    }

    /**
     * Initializes Firestore database instance
     */
    private void setupFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Sets up the date picker dialog for selecting a date
     */
    private void setupDatePicker() {
        dateEditText.setOnClickListener(v -> showDatePickerDialog());
    }

    /**
     * Displays a DatePickerDialog to select a date
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%02d/%02d/%04d", selectedMonth + 1, selectedDay, selectedYear);
                    dateEditText.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    /**
     * Configures the save button click listener
     */
    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveTransactionToFirestore());
    }

    /**
     * Saves transaction details to Firestore after validation
     */
    private void saveTransactionToFirestore() {
        String source = sourceEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String amount = amountEditText.getText().toString().trim();
        String arNumber = arNumberEditText.getText().toString().trim();
        String counterName = counterNameEditText.getText().toString().trim();

        if (!areFieldsValid(source, date, amount, arNumber, counterName)) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("source", source);
        transactionData.put("date", date);
        transactionData.put("amount", amount);
        transactionData.put("arNumber", arNumber);
        transactionData.put("counterName", counterName);

        firestore.collection("debits")
                .add(transactionData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Transaction successfully saved!", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error saving transaction: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    /**
     * Validates if all required fields are filled
     */
    private boolean areFieldsValid(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears input fields after a successful save
     */
    private void clearInputFields() {
        sourceEditText.setText("");
        dateEditText.setText("");
        amountEditText.setText("");
        arNumberEditText.setText("");
        counterNameEditText.setText("");
    }
}