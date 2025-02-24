package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import ph.com.masshjp.fb.R;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView largerVideoView;
    private ImageView iv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        largerVideoView = findViewById(R.id.vv_videoview);
        iv_close = findViewById(R.id.iv_close);

        // Retrieve the image URL from the intent extras
        String imageUrl = getIntent().getStringExtra("mediaUrl");

        // Close button click listener
        iv_close.setOnClickListener(v -> onBackPressed());
    }
}