package ph.com.masshjp.fb.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ph.com.masshjp.fb.Models.Users;
import ph.com.masshjp.fb.R;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.ViewHolder> {

    private final List<Users> userList;

    // Constructor
    public MasterListAdapter(List<Users> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_userlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.userFirstname.setText(user.getFirstname());
        holder.userLastname.setText(user.getLastname() + ", "); // Update if you have a `getLastName()` method
        holder.userNickname.setText(user.getNickname());
        holder.userAge.setText(user.getAge() + " yrs old |"); // Replace with age if available
        holder.userOrder.setText(user.getOrder()); // Replace with role if available

        // Load profile image using Glide
        Glide.with(holder.itemView.getContext())
                .load(user.getProfileImage())
                .placeholder(R.drawable.img_dp_default) // Default image
                .into(holder.userProfileImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userProfileImage;
        TextView userFirstname, userLastname, userNickname, userAge, userOrder;
        public ViewHolder(View itemView){
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userDP);
            userFirstname = itemView.findViewById(R.id.userFirstname);
            userLastname = itemView.findViewById(R.id.userLastname);
            userNickname = itemView.findViewById(R.id.userNickname);
            userAge = itemView.findViewById(R.id.userAge);
            userOrder = itemView.findViewById(R.id.userOrder);
        }
    }
}
