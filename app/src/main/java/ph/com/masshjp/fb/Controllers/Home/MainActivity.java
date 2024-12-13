package ph.com.masshjp.fb.Controllers.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
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

    //Firebase
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

        //Firebase
        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(this::checkUserStatus, SPLASH_TIME_OUT);
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in, navigate to Dashboard
            Log.d("MainActivity", "User is logged in");

            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            // Add the transition animation here
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        } else {
            // User is not logged in, navigate to Login
            Log.d("MainActivity", "User is not logged in");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            // Add the transition animation here
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }

        finish(); // Close MainActivity
    }
}