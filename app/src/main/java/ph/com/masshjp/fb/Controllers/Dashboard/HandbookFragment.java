package ph.com.masshjp.fb.Controllers.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ph.com.masshjp.fb.Adapters.HBCarouselAdapter;
import ph.com.masshjp.fb.Controllers.Handbook.Intro;
import ph.com.masshjp.fb.R;

public class HandbookFragment extends Fragment {

    private HBCarouselAdapter carouselAdapter;
    CardView cv_intro;
    private Handler handler;
    private ViewPager2 viewPager;
    private Runnable runnable;

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

        viewPager = rootView.findViewById(R.id.hb_viewPager);

        setupViewPager();

        // Initialize Handler and Runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Increment the current item index or set it back to 0 if it's the last item
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % carouselAdapter.getItemCount();
                viewPager.setCurrentItem(nextItem);

                // Schedule the next run after a delay (adjust the delay as needed)
                handler.postDelayed(this, 2000); // 3000 milliseconds (3 seconds) interval
            }
        };
        // Start the auto-scrolling when the activity is created
        startAutoScroll();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the auto-scrolling when the activity is destroyed to prevent memory leaks
        stopAutoScroll();
        super.onDestroy();
    }
    private void startAutoScroll() {
        // Schedule the first run with a delay
        handler.postDelayed(runnable, 3000); // 3000 milliseconds (3 seconds) delay
    }
    private void stopAutoScroll() {
        // Remove any scheduled callbacks to stop the auto-scrolling
        handler.removeCallbacks(runnable);
    }
    private void setupViewPager() {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.image2);
        imageList.add(R.drawable.image4);
        imageList.add(R.drawable.image7);
        imageList.add(R.drawable.image8);
        imageList.add(R.drawable.image10);
        imageList.add(R.drawable.image11);


        // Add more images as needed

        carouselAdapter = new HBCarouselAdapter(imageList);
        viewPager.setAdapter(carouselAdapter);
    }
}