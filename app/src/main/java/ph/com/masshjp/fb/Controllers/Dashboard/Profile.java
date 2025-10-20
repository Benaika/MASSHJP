package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.R;

public class Profile extends BaseActivity {


    private static final String TAG = "ProfileActivity";

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private CircleImageView profileImage;
    private TextView firstname, lastname, position, nickname, email, age, birthday, address, number, order, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.lightMaroon));
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        //setup toolbar
        setToolbar(this, R.id.toolbar, "Profile");

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        initializeViews();

        // Fetch and display the profile image
        fetchUserInfo();
    }
    private void initializeViews() {
        profileImage = findViewById(R.id.profileImage);
        firstname = findViewById(R.id.userFname);
        lastname = findViewById(R.id.userLname);
        position = findViewById(R.id.userPosition);
        nickname = findViewById(R.id.tv_nickname);
        email = findViewById(R.id.tv_email);
        age = findViewById(R.id.tv_age);
        birthday = findViewById(R.id.tv_birthday);
        address = findViewById(R.id.tv_address);
        number = findViewById(R.id.tv_number);
        order = findViewById(R.id.tv_order);
        userid = findViewById(R.id.tv_id);
    }
    private void fetchUserInfo() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e(TAG, "User not authenticated");
            return;
        }

        DocumentReference userRef = firestore.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                bindUserInfo(documentSnapshot);
            } else {
                Log.e(TAG, "User document does not exist");
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error fetching user information", e));
    }

    private void bindUserInfo(DocumentSnapshot documentSnapshot) {
        // Safely fetch and set user information
        firstname.setText(getSafeString(documentSnapshot, "firstname"));
        lastname.setText(getSafeString(documentSnapshot, "lastname"));
        position.setText(getSafeString(documentSnapshot, "position"));
        nickname.setText(getSafeString(documentSnapshot, "nickname"));
        email.setText(getSafeString(documentSnapshot, "email"));
        age.setText(getSafeString(documentSnapshot, "age") + " years old");
        birthday.setText(getSafeString(documentSnapshot, "birthday"));
        address.setText(getSafeString(documentSnapshot, "address"));
        number.setText(getSafeString(documentSnapshot, "number"));
        order.setText(getSafeString(documentSnapshot, "order"));
        userid.setText(getSafeString(documentSnapshot, "userid"));

        // Load profile image
        String imageUrl = documentSnapshot.getString("profileImage");
        if (imageUrl != null) {
            imageUrl = imageUrl.replace("\"", ""); // Remove any double quotes
            Log.d("ImageURL", imageUrl); // Debug log
            Glide.with(this)
                    .load(imageUrl.trim()) // Ensure no whitespace or invalid characters
                    .placeholder(R.drawable.img_dp_default)
                    .error(R.drawable.img_dp_default)
                    .into(profileImage);
        } else {
            Log.e("ProfileImageError", "Profile image URL is null or empty");
        }
    }

    private String getSafeString(DocumentSnapshot documentSnapshot, String field) {
        return documentSnapshot.getString(field) != null ? documentSnapshot.getString(field) : "N/A";
    }
}