package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ph.com.masshjp.fb.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GospelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GospelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // WebView reference
    private WebView webView;
    ProgressDialog progressDialog;

    public GospelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GospelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GospelFragment newInstance(String param1, String param2) {
        GospelFragment fragment = new GospelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gospel, container, false);

        // Initialize WebView
        webView = rootView.findViewById(R.id.webViewGospel);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (android.webkit.WebView.getCurrentWebViewPackage() != null) {
                String provider = android.webkit.WebView.getCurrentWebViewPackage().packageName;
                android.util.Log.d("sdfsdfdsf", "Using provider: " + provider);
            } else {
                android.util.Log.e("asdsasaf", "No WebView provider found!");
            }
        }



        // Set WebViewClient to handle links inside the WebView and loading state
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                showLoadingDialog(); // Show the loading dialog when the page starts loading
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoadingDialog(); // Hide the loading dialog when the page finishes loading
            }
        });
        // Load a URL (you can replace this with your desired URL)
        String url = "https://sites.google.com/view/mas-gospel/home"; // Replace with your desired URL
        //String url = "https://www.awitatpapuri.com/2025/01/19/linggo-enero-19-2025/"; // Replace with your desired URL
        webView.loadUrl(url);

        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Make sure to destroy the WebView when the fragment is destroyed to avoid memory leaks
        if (webView != null) {
            webView.destroy();
        }
    }
    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading..."); // Set your message
        progressDialog.setCancelable(false); // Set if it can be canceled by tapping outside
        progressDialog.show();

        // Create a SpannableStringBuilder with black text
        SpannableStringBuilder spannableMessage = new SpannableStringBuilder("Loading...");
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