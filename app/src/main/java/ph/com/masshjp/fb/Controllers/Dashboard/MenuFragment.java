package ph.com.masshjp.fb.Controllers.Dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WebView webView;
    ProgressDialog progressDialog;
    CardView cv_profile, cv_funds, cv_liveMass, cv_mpage, cv_website;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        cv_profile = rootView.findViewById(R.id.cv_profile);
        cv_funds = rootView.findViewById(R.id.cv_funds);
        cv_liveMass = rootView.findViewById(R.id.live_mass);
        cv_mpage = rootView.findViewById(R.id.ministry_page);
        cv_website = rootView.findViewById(R.id.website);

        cv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Profile.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        cv_funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Funds.class);
                startActivity(intent);
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

        return rootView;
    }
}