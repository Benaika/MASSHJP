package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import ph.com.masshjp.fb.R;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView largerImageView, iv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        largerImageView = findViewById(R.id.iv_imageview);
        iv_close = findViewById(R.id.iv_close);

        // Retrieve the image URL from the intent extras
        String imageUrl = getIntent().getStringExtra("mediaUrl");

        // Load the image into the ImageView using an image loading library like Glide or Picasso
        Glide.with(this)
                .load(imageUrl)
                .into(largerImageView);

        // Close button click listener
        iv_close.setOnClickListener(v -> onBackPressed());
    }
}