package ph.com.masshjp.fb.Controllers.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ph.com.masshjp.fb.Controllers.Handbook.Intro;
import ph.com.masshjp.fb.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandbookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandbookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    CardView cv_intro;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HandbookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExampleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandbookFragment newInstance(String param1, String param2) {
        HandbookFragment fragment = new HandbookFragment();
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
        //return inflater.inflate(R.layout.fragment_handbook, container, false);
        View rootView = inflater.inflate(R.layout.fragment_handbook, container, false);

        cv_intro = rootView.findViewById(R.id.cv_intro);

        cv_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Intro.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        return rootView;
    }
}