package ph.com.masshjp.fb.Controllers.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ph.com.masshjp.fb.Controllers.Dashboard.DashboardActivity;
import ph.com.masshjp.fb.Controllers.Login.LoginActivity;
import ph.com.masshjp.fb.R;

public class MainActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth auth;

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        // Check network connection before proceeding
        if (isNetworkAvailable()) {
            new Handler().postDelayed(this::checkUserStatus, SPLASH_TIME_OUT);
        } else {
            showNoInternetDialog();
        }
    }

    /**
     * Checks whether the device is connected to the internet.
     *
     * @return true if internet is available, false otherwise
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    /**
     * Displays an alert dialog prompting the user to check their internet connection.
     */
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("Retry", (dialog, which) -> {
                    // Retry checking the network
                    recreate();
                })
                .setNegativeButton("Exit", (dialog, which) -> finish());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Checks the user's authentication status and redirects them accordingly.
     */
    private void checkUserStatus() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, navigate to Dashboard
            Log.d("MainActivity", "User is logged in");
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        } else {
            // User is not logged in, navigate to Login
            Log.d("MainActivity", "User is not logged in");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        // Add transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

        // Close MainActivity
        finish();
    }
}
