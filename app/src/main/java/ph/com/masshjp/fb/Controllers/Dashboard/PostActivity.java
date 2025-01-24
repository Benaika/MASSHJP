package ph.com.masshjp.fb.Controllers.Dashboard;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ph.com.masshjp.fb.R;

public class PostActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    StorageReference storageRef;
    private static final int PICK_IMAGES_VIDEOS_CODE = 1234;

    LinearLayout btn_add;
    ImageView btnDiscard, imageView;
    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Storage for posting images or videos
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        storageRef = fStorage.getReference().child("media");

        imageView = (ImageView) findViewById(R.id.image_added);

        btn_add = (LinearLayout) findViewById(R.id.btn_addFile);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnDiscard = (ImageView) findViewById(R.id.btnback);

        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGES_VIDEOS_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_VIDEOS_CODE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                // If the user picked only one item
                Uri selectedMediaUri = data.getData();
                StorageReference fileRef = storageRef.child(selectedMediaUri.getLastPathSegment());
                UploadTask uploadTask = fileRef.putFile(selectedMediaUri);
                String selectedMediaType = getContentResolver().getType(selectedMediaUri);
                if (selectedMediaType.startsWith("image/")) {
                    // Handle selected image
                    imageView.setImageURI(selectedMediaUri);
                }

                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        SharedPreferences preferences = PostActivity.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                                        String userid = preferences.getString("id", "CM9XCZugSIPZRKvUySwIYD2c5pe2");
                                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(userid);
                                        Intent intent = new Intent(PostActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        // Retrieve the document
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    // Get the data from the document
                                                    String firstname = documentSnapshot.getString("firstname");
                                                    String lastname = documentSnapshot.getString("lastname");
                                                    String imageUrl = documentSnapshot.getString("profileImage");
                                                    String email = documentSnapshot.getString("email");
                                                    String role = documentSnapshot.getString("order");

                                                    // Retrieves user input from an EditText and stores it in a String variable named userCaption.
                                                    EditText caption = findViewById(R.id.userCaption);
                                                    String userCaption = caption.getText().toString();

                                                    // HashMap
                                                    Map<String, Object> post = new HashMap<>();
                                                    post.put("mediaUrl", uri.toString());
                                                    post.put("timestamp", FieldValue.serverTimestamp());
                                                    post.put("userid", userid);
                                                    post.put("firstname", firstname);
                                                    post.put("lastname", lastname);
                                                    post.put("order", role);
                                                    post.put("profileImage", imageUrl);
                                                    post.put("caption", userCaption);
                                                    post.put("email", email);
                                                    fStore.collection("posts").add(post);
                                                    Toast.makeText(PostActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(PostActivity.this, DashboardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    // Do something with the data

                                                } else {
                                                    // Handle document does not exist case
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure case
                                                Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure case
                            }
                        });
                    }
                });
            } else if (data.getClipData() != null) {
                // If the user picked multiple items
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri selectedMediaUri = clipData.getItemAt(i).getUri();
                    String selectedMediaType = getContentResolver().getType(selectedMediaUri);
                    if (selectedMediaType.startsWith("image/")) {
                        // Handle selected image
                        imageView.setImageURI(selectedMediaUri);
                    }
                }
            }
        }
    }
}