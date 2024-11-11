package ph.com.masshjp.fb.Controllers.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import ph.com.masshjp.fb.Controllers.Login.LoginActivity;
import ph.com.masshjp.fb.R;
import ph.com.masshjp.fb.databinding.ActivityDashboardBinding;
import ph.com.masshjp.fb.databinding.ActivityMainBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    DrawerLayout drawerLayout;
    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        // Set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlue));

        // Initialize binding using data binding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Ensure the status bar icons are light to contrast with the dark background
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // Set navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlue));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        replaceFragment(new ProfileFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new ProfileFragment());
            } else if (itemId == R.id.gospel) {
                replaceFragment(new GospelFragment());
            } else if (itemId == R.id.quiz) {
                replaceFragment(new SettingsFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new MenuFragment());
            }
            return true;
        });

        //imgUser = findViewById(R.id.contact_image);

//        imgUser.setOnClickListener(v-> {
//            //logEventToFirebase("account_hamburger","0");
//            drawerLayout.openDrawer(Gravity.LEFT);
//            //checkKycStatus();
//        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}