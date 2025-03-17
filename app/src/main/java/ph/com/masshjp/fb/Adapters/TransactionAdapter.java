package ph.com.masshjp.fb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ph.com.masshjp.fb.Controllers.Dashboard.TransactionDetailsActivity;
import ph.com.masshjp.fb.Models.Transactions;
import ph.com.masshjp.fb.R;

    public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

        private List<Transactions> transactionList;
        private Context context;

        public TransactionAdapter(Context context, List<Transactions> transactionList) {
            this.context = context;
            this.transactionList = transactionList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.rv_transaction_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Transactions transaction = transactionList.get(position);

            holder.tvTransactionTitle.setText(transaction.getSource());
            holder.tvTransactionDate.setText(transaction.getDate());

            // Format amount and set color
            if (transaction.getAmount() > 0) {
                holder.tvTransactionAmount.setText("+₱" + transaction.getAmount());
                holder.tvTransactionAmount.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.tvTransactionAmount.setText("-₱" + Math.abs(transaction.getAmount()));
                holder.tvTransactionAmount.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }

            // Add OnClickListener to open TransactionDetailsActivity with data
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, TransactionDetailsActivity.class);
                intent.putExtra("amount", transaction.getAmount());
                intent.putExtra("date", transaction.getDate());
                intent.putExtra("source", transaction.getSource());
                intent.putExtra("ar", transaction.getAr());
                intent.putExtra("counter", transaction.getCounter());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            });
        }

        @Override
        public int getItemCount() {
            return transactionList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTransactionTitle, tvTransactionDate, tvTransactionAmount, tvTransactionStatus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTransactionTitle = itemView.findViewById(R.id.tvTransactionSource);
                tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
                tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
                tvTransactionStatus = itemView.findViewById(R.id.tvTransactionStatus);
            }
        }
    }
