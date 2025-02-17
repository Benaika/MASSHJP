package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ph.com.masshjp.fb.Adapters.PostAdapter;
import ph.com.masshjp.fb.Models.Feed;
import ph.com.masshjp.fb.R;

public class HomeFragment extends Fragment {


    private FirebaseAuth auth;
    private FirebaseFirestore fStore;

    private SwipeRefreshLayout srl;

    PostAdapter postAdapter;

    RecyclerView recyclerView;

    private CircleImageView civ_dp;

    private WebView webView;

    private CardView cv_post;
    CircleImageView feedDP;

    ArrayList<Feed> mFeed; //Declaring an ArrayList called "mFeed" that will contain objects of type "Feed".

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fStore.collection("posts");

        srl = rootView.findViewById(R.id.swipeRefreshLayout);

        //Retrieves data of current logged in user
        SharedPreferences preferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String userid = preferences.getString("USER_ID", "");

        // Initialization
        cv_post = rootView.findViewById(R.id.cv_post);
        civ_dp = rootView.findViewById(R.id.civ_dp);

        // Initialize RecyclerView and PostAdapter
        recyclerView = rootView.findViewById(R.id.rv_feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFeed = new ArrayList<Feed>();
        postAdapter = new PostAdapter(getContext(), mFeed);
        recyclerView.setAdapter(postAdapter); // Set PostAdapter on RecyclerView

        postMethod();

        // Set up SwipeRefreshLayout
        srl.setOnRefreshListener(() -> {
            // Call method to refresh feed data
            refreshFeedData();
        });
        
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

    private void postMethod() {
        // Show loading dialog before fetching data
        showLoadingDialog();

        fStore.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("FirestoreError", "Error fetching posts", error);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Feed feed = dc.getDocument().toObject(Feed.class);
                                feed.setVideoUrl(dc.getDocument().getString("videoUrl"));
                                feed.setMediaUrl(dc.getDocument().getString("mediaUrl"));
                                mFeed.add(feed);
                            }
                        }
                        postAdapter.notifyDataSetChanged();

                        // Dismiss loading dialog after data is loaded
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void refreshFeedData() {
        // Clear existing data and fetch fresh posts
        mFeed.clear();

        fStore.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Feed feed = document.toObject(Feed.class);
                            feed.setVideoUrl(document.getString("videoUrl"));
                            feed.setMediaUrl(document.getString("mediaUrl"));
                            mFeed.add(feed);
                        }

                        postAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error refreshing feed", task.getException());
                    }

                    // Stop refresh animation
                    srl.setRefreshing(false);
                });
    }
}