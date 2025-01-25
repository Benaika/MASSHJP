package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import ph.com.masshjp.fb.Controllers.Login.LoginActivity;
import ph.com.masshjp.fb.R;

public class MenuFragment extends Fragment {

    //Firebase
    private CircleImageView profileImage;
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;

    ProgressDialog progressDialog;
    CardView cv_profile, cv_masterlist, cv_attendance, cv_scheduling, cv_funds, cv_liveMass, cv_mpage, cv_website, cv_support, cv_logout;
    TextView tv_fullname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        profileImage = rootView.findViewById(R.id.profileImage);
        tv_fullname = rootView.findViewById(R.id.tv_fullname);
        cv_profile = rootView.findViewById(R.id.cv_profile);
        cv_masterlist = rootView.findViewById(R.id.cv_masterlist);
        cv_attendance = rootView.findViewById(R.id.cv_attendance);
        cv_scheduling = rootView.findViewById(R.id.cv_scheduling);
        cv_funds = rootView.findViewById(R.id.cv_funds);
        cv_liveMass = rootView.findViewById(R.id.live_mass);
        cv_mpage = rootView.findViewById(R.id.ministry_page);
        cv_website = rootView.findViewById(R.id.website);
        cv_support = rootView.findViewById(R.id.cv_support);
        cv_logout = rootView.findViewById(R.id.cv_logout);

        cv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Profile.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        cv_masterlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Masterlist.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        cv_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Attendance.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        cv_scheduling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Scheduling.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        cv_funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Funds.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        cv_liveMass.setOnClickListener(view ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SHJPmb ")))
        );

        cv_mpage.setOnClickListener(view ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MAS.SHJPmbs")))
        );

        cv_website.setOnClickListener(view ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://enzoparonable.wixsite.com/my-site")))
        );

        cv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
            }
        });

        Logout();
        fetchProfileImage();

        return rootView;
    }

    private void Logout(){
        // Set an onClickListener for the CardView
        cv_logout.setOnClickListener(view -> {
            // Show confirmation dialog
            new AlertDialog.Builder(requireContext())
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Sign out the user
                        FirebaseAuth.getInstance().signOut();

                        // Navigate back to LoginActivity
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        // Close the dialog
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Dismiss the dialog
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    private void fetchProfileImage() {
        String userId = auth.getCurrentUser().getUid();
        DocumentReference userRef = fStore.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                tv_fullname.setText(getSafeString(documentSnapshot, "firstname") + " " + getSafeString(documentSnapshot, "lastname"));

                String imageUrl = documentSnapshot.getString("profileImage");
                if (imageUrl != null) {
                    imageUrl = imageUrl.replace("\"", ""); // Remove any double quotes
                    Glide.with(requireContext()) // Use the fragment's context
                            .load(imageUrl.trim()) // Ensure no whitespace or invalid characters
                            .placeholder(R.drawable.img_dp_default)
                            .error(R.drawable.img_dp_default)
                            .into(profileImage);
                } else {
                    Log.e("asjdioausdi", "Profile image URL is null or empty");
                }
            } else {
                Log.e("jhsiodfyhoisadsa", "Document does not exist");
            }
        }).addOnFailureListener(e -> Log.e("ausgdiuoasd", "Error fetching profile image", e));
    }
    private String getSafeString(DocumentSnapshot documentSnapshot, String field) {
        return documentSnapshot.getString(field) != null ? documentSnapshot.getString(field) : "N/A";
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "simple_notification_channel";
            CharSequence channelName = "Simple Notification";
            String channelDescription = "Channel for simple notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification() {
        // For Android 13+, check notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                return; // Wait for user permission
            }
        }

        String channelId = "simple_notification_channel"; // Same ID as in createNotificationChannel()

        // Create a Bitmap for the large icon (your logo)
        Bitmap largeIcon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ico_mas); // Replace with your logo's resource

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Default small icon
                .setContentTitle("Hello!")
                .setContentText("You clicked the button!") // Message shown in notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Auto-dismiss when clicked
                .setLargeIcon(largeIcon) // Set the large icon (app logo)
                .setStyle(new NotificationCompat.BigPictureStyle().bigLargeIcon((Bitmap) null).bigPicture(largeIcon)); // Optional for a larger view

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        // Vibrate the phone for 500 milliseconds
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For Android 8.0 and above, use vibrate with a pattern
                VibrationEffect effect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(effect);
            } else {
                // For older Android versions, use the old vibrate method
                vibrator.vibrate(500); // Vibrate for 500 milliseconds
            }
        }

        // Show the notification
        notificationManager.notify(1, builder.build()); // Show the notification
    }


}