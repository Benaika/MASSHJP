package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.R;

public class PostActivity extends BaseActivity {

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;
    private StorageReference storageRef;
    private static final int PICK_IMAGES_VIDEOS_CODE = 1234;

    private LinearLayout btnAddFile;
    private ImageView imageView;
    private CardView btnPost;
    private EditText etCaption;
    private Uri selectedImageUri = null; // Stores selected image URI
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(0, insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars()).top, 0, 0);
            return insets;
        });

        // Setup toolbar
        setToolbar(this, R.id.toolbar, "Post Announcement");

        // Firebase initialization
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        storageRef = fStorage.getReference().child("media");

        // UI initialization
        imageView = findViewById(R.id.image_added);
        btnAddFile = findViewById(R.id.btn_addFile);
        btnPost = findViewById(R.id.btn_post);
        etCaption = findViewById(R.id.userCaption);

        // Disable post button initially
        btnPost.setEnabled(false);
        btnPost.setAlpha(0.5f); // Make button look disabled

        // Add listeners for text and image changes
        etCaption.addTextChangedListener(textWatcher);
        btnAddFile.setOnClickListener(view -> pickImageFromGallery());

        btnPost.setOnClickListener(view -> uploadPost());
    }

    // Listen for text changes
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkIfPostButtonShouldBeEnabled();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // Open gallery to pick an image
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Only allow images
        startActivityForResult(intent, PICK_IMAGES_VIDEOS_CODE);
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_VIDEOS_CODE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                checkIfPostButtonShouldBeEnabled();
            }
        }
    }

    // Enable/Disable Post Button based on conditions
    private void checkIfPostButtonShouldBeEnabled() {
        boolean hasText = !etCaption.getText().toString().trim().isEmpty();
        boolean hasImage = selectedImageUri != null;

        if (hasText || hasImage) {
            btnPost.setEnabled(true);
            btnPost.setAlpha(1.0f);
        } else {
            btnPost.setEnabled(false);
            btnPost.setAlpha(0.5f);
        }
    }
    private void uploadPost() {
        if (selectedImageUri == null && etCaption.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please add an image or write something before posting.", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoadingDialog();

        if (selectedImageUri != null) {
            // Upload image to Firebase Storage
            StorageReference fileRef = storageRef.child(selectedImageUri.getLastPathSegment());
            UploadTask uploadTask = fileRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> savePostToFirestore(uri.toString())))
                    .addOnFailureListener(e -> {
                        hideLoadingDialog();
                        Toast.makeText(PostActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // If no image, just save post with text
            savePostToFirestore(null);
        }
    }

    private void savePostToFirestore(String imageUrl) {
        SharedPreferences preferences = getSharedPreferences("USER", Context.MODE_PRIVATE);
        String userid = preferences.getString("USER_ID", "");

        DocumentReference docRef = fStore.collection("users").document(userid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String firstname = documentSnapshot.getString("firstname");
                String lastname = documentSnapshot.getString("lastname");
                String profileImage = documentSnapshot.getString("profileImage");
                String email = documentSnapshot.getString("email");
                String role = documentSnapshot.getString("order");

                // Get text from EditText
                String userCaption = etCaption.getText().toString().trim();

                // Prepare data to save
                Map<String, Object> post = new HashMap<>();
                post.put("mediaUrl", imageUrl);
                post.put("timestamp", FieldValue.serverTimestamp());
                post.put("userid", userid);
                post.put("firstname", firstname);
                post.put("lastname", lastname);
                post.put("order", role);
                post.put("profileImage", profileImage);
                post.put("caption", userCaption);
                post.put("email", email);

                fStore.collection("posts").add(post)
                        .addOnSuccessListener(documentReference -> {
                            hideLoadingDialog();
                            Toast.makeText(PostActivity.this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            hideLoadingDialog();
                            Toast.makeText(PostActivity.this, "Failed to upload post", Toast.LENGTH_SHORT).show();
                        });
            }
        }).addOnFailureListener(e -> {
            hideLoadingDialog();
            Toast.makeText(PostActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
        });
    }

    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set your message
        progressDialog.setCancelable(false); // Set if it can be canceled by tapping outside
        progressDialog.show();

        // Create a SpannableStringBuilder with black text
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("Posting Announcement");
        spannableMessage.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        progressDialog.setMessage(spannableMessage);

        // Set background color to white
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.white);
        }
    }
    // Function to hide the loading dialog
    private void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        // Create a SpannableStringBuilder with black text
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("Logging in...");
        spannableMessage.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set background color to white
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.white);
        }
    }
}
