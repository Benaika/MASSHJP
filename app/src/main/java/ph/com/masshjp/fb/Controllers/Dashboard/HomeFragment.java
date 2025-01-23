package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import ph.com.masshjp.fb.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private FirebaseFirestore fStore;

    private CircleImageView civ_dp;

    private WebView webView;

    private CardView cv_post;

    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initialization
        webView = rootView.findViewById(R.id.webViewProfile);
        cv_post = rootView.findViewById(R.id.cv_post);
        civ_dp = rootView.findViewById(R.id.civ_dp);

        //Fetching display picture
        fetchProfileImage();

        cv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

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
    private void fetchProfileImage() {
        String userId = auth.getCurrentUser().getUid();
        DocumentReference userRef = fStore.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                String imageUrl = documentSnapshot.getString("profileImage");
                if (imageUrl != null) {
                    imageUrl = imageUrl.replace("\"", ""); // Remove any double quotes
                    Glide.with(requireContext()) // Use the fragment's context
                            .load(imageUrl.trim()) // Ensure no whitespace or invalid characters
                            .placeholder(R.drawable.img_dp_default)
                            .error(R.drawable.img_dp_default)
                            .into(civ_dp);
                } else {
                    Log.e("asjdioausdi", "Profile image URL is null or empty");
                }
            } else {
                Log.e("jhsiodfyhoisadsa", "Document does not exist");
            }
        }).addOnFailureListener(e -> Log.e("ausgdiuoasd", "Error fetching profile image", e));
    }
}