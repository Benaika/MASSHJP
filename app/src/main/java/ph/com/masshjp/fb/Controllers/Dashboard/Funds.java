package ph.com.masshjp.fb.Controllers.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import ph.com.masshjp.fb.Adapters.TransactionAdapter;
import ph.com.masshjp.fb.BaseActivity;
import ph.com.masshjp.fb.Models.Transactions;
import ph.com.masshjp.fb.R;

public class Funds extends BaseActivity {

    FirebaseFirestore fStore;
    ImageView iv_add;
    TextView tvTotalFunds, tvNoTransactionHistory;
    private RecyclerView rvTransactionHistory;
    private TransactionAdapter transactionAdapter;
    private List<Transactions> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setToolbar(this, R.id.toolbar, "Funds");

        // Initialize Firestore instance
        fStore = FirebaseFirestore.getInstance();

        //Initialization
        iv_add = findViewById(R.id.iv_add);
        tvTotalFunds = findViewById(R.id.tv_totalFunds);
        tvNoTransactionHistory = findViewById(R.id.tv_no_transactions);

        rvTransactionHistory = findViewById(R.id.rv_history);
        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(this));

        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(this, transactionList);
        rvTransactionHistory.setAdapter(transactionAdapter);

        fetchTotalFunds();
        fetchTransactionHistory();

        //Intent
        iv_add.setOnClickListener(view -> {
            startActivity(new Intent(Funds.this, AddDebit.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        });
    }
    /**
     * Fetches the total funds from Firestore and updates the UI.
     * The fetched amount is formatted with a thousands separator for better readability.
     */
    private void fetchTotalFunds() {
        // Initialize Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Listen for real-time updates to the "totalFunds" document in the "funds" collection
        firestore.collection("funds").document("totalFunds")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        // Handle Firestore errors
                        if (e != null) {
                            Log.e("FirestoreError", "Error fetching total funds", e);
                            return;
                        }

                        // Check if the document exists before attempting to retrieve data
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            // Retrieve the "total" field value, defaulting to 0.0 if null
                            Double totalFunds = documentSnapshot.getDouble("total");
                            if (totalFunds == null) {
                                totalFunds = 0.0;
                            }

                            // Format the total funds with a thousands separator (e.g., 15,000)
                            NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);
                            String formattedFunds = numberFormatter.format(totalFunds);

                            // Update the UI with the formatted total funds
                            tvTotalFunds.setText("PHP " + formattedFunds);
                        }
                    }
                });
    }

    private void fetchTransactionHistory() {
        CollectionReference transactionsRef = fStore.collection("debits");

        transactionsRef.orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("FirestoreError", "Error fetching transactions", error);
                            return;
                        }

                        if (value == null || value.isEmpty()) {
                            Log.d("FirestoreDebug", "No transactions found.");
                            return;
                        }

                        // Clear the list before adding new data
                        transactionList.clear();

                        // Loop through documents and add them to the list
                        for (QueryDocumentSnapshot document : value) {
                            Transactions transaction = document.toObject(Transactions.class);
                            transactionList.add(transaction);
                        }

                        // If list is still empty after attempting to add transactions, show "No transactions"
                        if (transactionList.isEmpty()) {
                            tvNoTransactionHistory.setVisibility(View.VISIBLE);
                            rvTransactionHistory.setVisibility(View.GONE);
                        } else {
                            tvNoTransactionHistory.setVisibility(View.GONE);
                            rvTransactionHistory.setVisibility(View.VISIBLE);
                        }

                        // Notify adapter of data changes
                        transactionAdapter.notifyDataSetChanged();
                    }
                });
    }


}