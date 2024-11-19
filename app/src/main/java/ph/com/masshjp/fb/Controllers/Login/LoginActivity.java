package ph.com.masshjp.fb.Controllers.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ph.com.masshjp.fb.Controllers.Dashboard.DashboardActivity;
import ph.com.masshjp.fb.R;

/**
 * Developed by Benaika Lorenzo Paronable | 2024
 */

public class LoginActivity extends AppCompatActivity {

    private ImageView ivPassword, ivShow;
    private EditText etPassword;
    private Button btnProceed;
    boolean showPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivShow = findViewById(R.id.iv_show);
        ivPassword = findViewById(R.id.iv_password);
        etPassword = findViewById(R.id.et_password);
        etPassword.setText("");

        btnProceed = findViewById(R.id.btn_proceed);

        ivShow.setOnClickListener(v -> {
            showPassword = !showPassword;
            checkInputs();
            etPassword.setSelection(etPassword.getText().length());
            etPassword.requestFocus();
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
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
}