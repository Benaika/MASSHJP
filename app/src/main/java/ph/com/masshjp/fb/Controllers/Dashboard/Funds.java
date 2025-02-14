package ph.com.masshjp.fb.Controllers.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.R;

public class Funds extends BaseActivity {

    FirebaseFirestore fStore;
    ImageView iv_add;
    TextView tvTotalFunds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setToolbar(this, R.id.toolbar, "Funds");

        // Initialize Firestore instance
        fStore = FirebaseFirestore.getInstance();

        //Initialization
        iv_add = findViewById(R.id.iv_add);
        tvTotalFunds = findViewById(R.id.tv_totalFunds);

        //Intent
        iv_add.setOnClickListener(view -> {
            startActivity(new Intent(Funds.this, AddDebit.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        });

    }
}