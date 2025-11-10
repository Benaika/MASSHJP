package ph.com.masshjp.fb;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    private AlertDialog progressDialog;
    protected Toolbar setToolbar(AppCompatActivity activity, int toolbar, String title) {
        Toolbar toolbarFragment = findViewById(toolbar);
        activity.setSupportActionBar(toolbarFragment);
        toolbarFragment.setTitle(title);
        toolbarFragment.setContentInsetStartWithNavigation(0);
        toolbarFragment.setNavigationIcon(R.drawable.ic_back_arrow_white);
        toolbarFragment.setNavigationOnClickListener(view -> {onBackPressed();
            activity.overridePendingTransition(0, R.anim.exit_to_right);
        });
        return toolbarFragment;
    }

    /**
     * Creates a reusable progress dialog with custom message.
     */
    protected void createProgressDialog(Context context, String message) {
        // Container for ProgressBar and TextView
        FrameLayout frameLayout = new FrameLayout(context);

        // ProgressBar (spinner)
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        FrameLayout.LayoutParams progressParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );
        progressBar.setLayoutParams(progressParams);

        // TextView for loading message
        TextView loadingText = new TextView(context);
        loadingText.setText(message);
        loadingText.setTextSize(16);
        loadingText.setTextColor(Color.WHITE);
        loadingText.setGravity(Gravity.CENTER);

        // Position text slightly below spinner
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
        );
        textParams.topMargin = 150;
        loadingText.setLayoutParams(textParams);

        // Add both elements to layout
        frameLayout.addView(progressBar);
        frameLayout.addView(loadingText);

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(frameLayout);
        builder.setCancelable(false);

        progressDialog = builder.create();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    /**
     * Shows the progress dialog with a given message.
     */
    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            createProgressDialog(this, message);
        } else {
            createProgressDialog(this, message); // Update message if already exists
        }
        progressDialog.show();
    }

    /**
     * Hides the progress dialog.
     */
    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}

