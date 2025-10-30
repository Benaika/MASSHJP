package ph.com.masshjp.fb.Controllers.Login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.R;

public class RegistrationActivity extends BaseActivity {

    private WebView webView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.lightMaroon));
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        //setup toolbar
        setToolbar(this, R.id.toolbar, "Register");

        // Initialize WebView
        webView = findViewById(R.id.wv_registration);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // Handle web loading inside WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                showLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoadingDialog();
            }
        });

        // 🔗 Google Form URL (replace with your own form link)
        String googleFormUrl = "https://forms.gle/JeXdyGMwhkaJFGDE6";

        webView.loadUrl(googleFormUrl);
    }

    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.white);
        }
    }

    private void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        // Go back inside WebView if possible
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.exit_to_right);
        }
    }
}