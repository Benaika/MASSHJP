package ph.com.masshjp.fb.Controllers.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ph.com.masshjp.fb.Controllers.Dashboard.DashboardActivity;
import ph.com.masshjp.fb.R;

/**
 * Developed by Benaika Lorenzo Paronable | 2024
 */

public class LoginActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth auth;

    // Create AlertDialog reference
    private AlertDialog progressDialog;

    private ImageView ivPassword, ivShow;
    private TextView tvError;
    private EditText etEmail, etPassword;
    private Button btnProceed;
    boolean showPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        //Firebase
        auth = FirebaseAuth.getInstance();

        tvError = findViewById(R.id.tv_error);
        etEmail = findViewById(R.id.et_email);
        ivShow = findViewById(R.id.iv_show);
        ivPassword = findViewById(R.id.iv_password);
        etPassword = findViewById(R.id.et_password);
        etPassword.setText("");

        btnProceed = findViewById(R.id.btn_proceed);

        ivShow.setOnClickListener(v -> {
            showPassword = !showPassword;
            togglePasswordVisibility();
            //checkInputs();
            etPassword.setSelection(etPassword.getText().length());
            etPassword.requestFocus();
        });

        // Initialize AlertDialog for progress loading
        createProgressDialog();

        btnProceed.setOnClickListener(view -> loginUser());
    }

    private void togglePasswordVisibility() {
        if (showPassword) {
            ivShow.setImageResource(R.drawable.ic_pswshow_gry_32);
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ivShow.setImageResource(etPassword.getText().toString().isEmpty() ? R.drawable.ic_pswshow_gry_32 : R.drawable.ic_pswhide_grn_32);
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
    private void checkInputs() {

        /**
         * Validates the user input and updates the UI elements accordingly.
         * This method ensures the "Proceed" button is only enabled when the input is valid
         * and handles password visibility toggling and icon updates.
         */

        // Flag to determine if the Proceed button should be enabled
        boolean enable = true;

        if (etPassword.getText().toString().isEmpty()) {
            enable = false;
            ivPassword.setImageResource(R.drawable.ic_pswshow_gry_32);
        } else {
            //parentActivity.password = PayChatApp.Security.encrypt(etPassword.getText().toString(), "PASSWORD");;
            ivPassword.setImageResource(R.drawable.ic_psw_gry_32);
        }

        // Handle password visibility toggle based on the showPassword flag
        if (showPassword) {
            ivShow.setImageResource(R.drawable.ic_pswshow_gry_32);
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ivShow.setImageResource(etPassword.getText().toString().isEmpty() ? R.drawable.ic_pswshow_gry_32 : R.drawable.ic_pswhide_grn_32);
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        // Enable or disable the Proceed button based on input validation
        btnProceed.setEnabled(enable);
        // Adjust the alpha (transparency) of the Proceed button to indicate its state
        btnProceed.getBackground().setAlpha(enable ? 255 :140);
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            tvError.setText("Please enter email and password");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        progressDialog.show();
        btnProceed.setEnabled(false); // Disable button to prevent multiple clicks

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    btnProceed.setEnabled(true); // Re-enable button

                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            tvError.setVisibility(View.GONE);

                            // Save user session inside loginUser method
                            SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("EMAIL", email); // Store user email
                            editor.putBoolean("IS_LOGGED_IN", true); // Mark user as logged in
                            editor.apply(); // Save changes

                            // Login successful, navigate to Dashboard
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                            finish(); // Close LoginActivity
                        }
                    } else {
                        Log.e("LoginError", "Error: ", task.getException());
                        tvError.setText("Wrong email or password!");
                        tvError.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createProgressDialog() {
        // Create a FrameLayout to hold the ProgressBar (to add a box around it)
        FrameLayout frameLayout = new FrameLayout(this);
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true); // Circular spinner
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));

        // Add the ProgressBar to the FrameLayout
        frameLayout.addView(progressBar);

        // Optional: Set background color, border, or padding if needed
        //frameLayout.setBackgroundResource(R.drawable.box_border); // Use a custom drawable for border (box)
        frameLayout.setPadding(20, 20, 20, 20); // Add padding around the ProgressBar

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(frameLayout); // Use the FrameLayout with ProgressBar as the view
        builder.setCancelable(false);  // Prevent the dialog from being canceled by tapping outside

        // Create and show the dialog
        progressDialog = builder.create();
    }

}