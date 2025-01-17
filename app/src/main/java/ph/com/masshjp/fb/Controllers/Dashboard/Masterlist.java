package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ph.com.masshjp.fb.Adapters.MasterListAdapter;
import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.Models.Users;
import ph.com.masshjp.fb.R;

public class Masterlist extends BaseActivity {

    private FirebaseFirestore fStore;
    private RecyclerView recyclerView;
    private MasterListAdapter adapter;
    private List<Users> userList;
    private TextView tvMembers; // TextView for displaying member count

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fStore = FirebaseFirestore.getInstance();

        setToolbar(this, R.id.toolbar, "Masterlist ");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rv_masterList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvMembers = findViewById(R.id.tv_members);

        // Create user data
        userList = new ArrayList<>();
        adapter = new MasterListAdapter(userList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();

    }

    private void fetchDataFromFirestore() {
        CollectionReference usersCollection = fStore.collection("users");

        usersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();

                int userCount = 0; // Counter for user count

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String profileImage = document.getString("profileImage");
                    String firstname = document.getString("firstname");
                    String lastname = document.getString("lastname");
                    String nickname = document.getString("nickname");
                    String age = document.getString("age");
                    String order = document.getString("order");

                    // Create a Users object and add it to the list
                    userList.add(new Users(profileImage, firstname, lastname, nickname, age, order));
                    userCount++; // Increment user count
                }
                adapter.notifyDataSetChanged();

                // Update TextView with user count
                if(userCount == 0 || userCount == 1){
                    tvMembers.setText(userCount + " member");
                } else {
                    tvMembers.setText(userCount + " members");
                }

            } else {
                // Handle errors
                tvMembers.setText("Error fetching members");
                // Log.e("FirestoreError", "Error fetching users", task.getException());
            }
        });
    }
}