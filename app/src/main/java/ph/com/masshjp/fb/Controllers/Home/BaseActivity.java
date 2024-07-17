package ph.com.masshjp.fb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar setToolbar(AppCompatActivity activity, int toolbar, String title) {
        Toolbar toolbarFragment = findViewById(toolbar);
        activity.setSupportActionBar(toolbarFragment);
        toolbarFragment.setTitle(title);
        toolbarFragment.setContentInsetStartWithNavigation(0);
        toolbarFragment.setNavigationIcon(R.drawable.ic_back_arrow_white);
        toolbarFragment.setNavigationOnClickListener(view -> onBackPressed());
        return toolbarFragment;
    }
}
