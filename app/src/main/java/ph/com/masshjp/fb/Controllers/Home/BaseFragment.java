package ph.com.masshjp.fb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class BaseFragment extends Fragment {

    private Fragment currentFragment;

    protected Toolbar setToolbar(Fragment fragment, int toolbar, String title) {
        AppCompatActivity currentActivity = (AppCompatActivity)fragment.getActivity();
        Toolbar toolbarFragment = null;
        if (getView() != null) {
            toolbarFragment = getView().findViewById(toolbar);
            Objects.requireNonNull(currentActivity).setSupportActionBar(toolbarFragment);
            toolbarFragment.setTitle(title);
            toolbarFragment.setContentInsetStartWithNavigation(0);
            toolbarFragment.setNavigationIcon(R.drawable.ic_back_arrow_white);
            toolbarFragment.setNavigationOnClickListener(view -> requireActivity().onBackPressed());
            this.currentFragment = this;
        }

        return toolbarFragment;
    }
}
