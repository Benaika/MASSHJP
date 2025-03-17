package ph.com.masshjp.fb.Controllers.Dashboard;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ph.com.masshjp.fb.Adapters.MasterListAdapter;
import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.Models.Users;
import ph.com.masshjp.fb.R;

public class Masterlist extends BaseActivity {

    private FirebaseFirestore fStore;
    private RecyclerView recyclerView;
    private MasterListAdapter adapter;
    private List<Users> userList, filteredList;
    private TextView tvMembers;
    private SearchView searchView;
    private ImageView btnFilter; // Add filter button

    private String selectedCategory = ""; // Stores the currently selected category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterlist);

        fStore = FirebaseFirestore.getInstance();
        setToolbar(this, R.id.toolbar, "Masterlist");

        recyclerView = findViewById(R.id.rv_masterList);
        tvMembers = findViewById(R.id.tv_members);
        searchView = findViewById(R.id.searchView);
        btnFilter = findViewById(R.id.btn_filter); // Initialize filter button

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new MasterListAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        fetchDataFromFirestore();
        setupSearchView();
        setupFilterButton(); // Initialize the filter button
    }

    private void fetchDataFromFirestore() {
        CollectionReference usersCollection = fStore.collection("users");

        usersCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Users user = new Users(
                            document.getString("profileImage"),
                            document.getString("firstname"),
                            document.getString("lastname"),
                            document.getString("nickname"),
                            document.getString("age"),
                            document.getString("order") // This field determines the category
                    );
                    userList.add(user);
                }
                // Ensure filteredList is initially the same as userList
                filteredList.clear();
                filteredList.addAll(userList);

                // Update UI
                adapter.notifyDataSetChanged();
                updateMemberCount(filteredList.size());
            } else {
                tvMembers.setText("Error fetching members");
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
    }

    private void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(userList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Users user : userList) {
                if (user.getFirstname().toLowerCase().contains(lowerCaseQuery) ||
                        user.getLastname().toLowerCase().contains(lowerCaseQuery) ||
                        user.getNickname().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(user);
                }
            }
        }
        applyCategoryFilter();
    }

    private void setupFilterButton() {
        btnFilter.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, btnFilter);
            popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu()); // Create filter_menu.xml

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                selectedCategory = menuItem.getTitle().toString();
                applyCategoryFilter();
                return true;
            });

            popupMenu.show();
        });
    }

    private void applyCategoryFilter() {
        List<Users> tempFilteredList = new ArrayList<>();

        for (Users user : userList) {
            boolean matchesSearch = searchView.getQuery().toString().isEmpty() ||
                    user.getFirstname().toLowerCase().contains(searchView.getQuery().toString().toLowerCase()) ||
                    user.getLastname().toLowerCase().contains(searchView.getQuery().toString().toLowerCase()) ||
                    user.getNickname().toLowerCase().contains(searchView.getQuery().toString().toLowerCase());

            boolean matchesCategory = selectedCategory.isEmpty() || user.getOrder().equalsIgnoreCase(selectedCategory);

            if (matchesSearch && matchesCategory) {
                tempFilteredList.add(user);
            }
        }

        filteredList.clear();
        filteredList.addAll(tempFilteredList);
        adapter.notifyDataSetChanged();
        updateMemberCount(filteredList.size());
    }

    private void updateMemberCount(int count) {
        tvMembers.setText(count + (count == 1 ? " member" : " members"));
    }
}
