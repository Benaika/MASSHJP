package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // Set WebViewClient to handle links inside the WebView
        webView.setWebViewClient(new WebViewClient());

        // Load a URL (you can replace this with your desired URL)
        String url = "https://sites.google.com/view/mas-gospel/home"; // Replace with your desired URL
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
}