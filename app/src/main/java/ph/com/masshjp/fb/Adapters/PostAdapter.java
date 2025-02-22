package ph.com.masshjp.fb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ph.com.masshjp.fb.Controllers.Dashboard.ImageViewActivity;
import ph.com.masshjp.fb.Models.Feed;
import ph.com.masshjp.fb.R;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_VIDEO = 1;
    Context context;
    ArrayList<Feed> mFeed;

    public PostAdapter(Context context, ArrayList<Feed> mFeed){
        this.context = context;
        this.mFeed = mFeed;
    }
    @Override
    public int getItemViewType(int position) {
        Feed feed = mFeed.get(position);

        if (feed.getMediaUrl() != null) {
            return VIEW_TYPE_POST;
        } else {
            return VIEW_TYPE_VIDEO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_VIDEO) {
            View videoView = inflater.inflate(R.layout.rv_post, parent, false);
            return new VideoViewHolder(videoView);
        } else {
            View postView = inflater.inflate(R.layout.rv_post, parent, false);
            return new PostViewHolder(postView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Feed feed = mFeed.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());

        if (holder instanceof VideoViewHolder) {
            VideoViewHolder videoHolder = (VideoViewHolder) holder;
            ((VideoViewHolder) holder).videoView.setVideoURI(Uri.parse(feed.getVideoUrl()));
            ((VideoViewHolder) holder).videoView.start();
            // Bind video data to the VideoViewHolder
            // Customize this part based on your rv_video_item layout
            videoHolder.firstname.setText(feed.getFirstname() + " ");
            videoHolder.lastname.setText(feed.getLastname() + " ");
            videoHolder.role.setText(feed.getOrder());
            // Safely handle timestamp
            if (feed.getTimestamp() != null) {
                videoHolder.timestamp.setText(feed.getTimestamp().toDate().toString());
            } else {
                videoHolder.timestamp.setText("No date available"); // Default text when timestamp is null
            }
            videoHolder.caption.setText(feed.caption);
            Glide.with(context)
                    .load(feed.getProfileImage())
                    .placeholder(ph.com.masshjp.fb.R.drawable.img_dp_default)
                    .centerInside()
                    .into(videoHolder.userDP);
            // Load and display video using a video library like ExoPlayer or MediaPlayer
            // You can use feed.getVideoUrl() to get the video URL
            // Set video URL to the VideoView
            // Set the video URL and start/pause video playback
        } else if (holder instanceof PostViewHolder) {
            PostViewHolder postHolder = (PostViewHolder) holder;
            // Bind post data to the PostViewHolder
            // Customize this part based on your rv_post_item layout
            postHolder.firstname.setText(feed.getFirstname() + " ");
            postHolder.lastname.setText(feed.getLastname() + " ");
            postHolder.role.setText(feed.getOrder());
            // Safely handle timestamp
            if (feed.getTimestamp() != null) {
                Date timestampDate = feed.getTimestamp().toDate();
                postHolder.timestamp.setText(dateFormat.format(timestampDate));
            } else {
                postHolder.timestamp.setText("No date available");
            }
            postHolder.caption.setText(feed.caption);
            Glide.with(context)
                    .load(feed.getProfileImage())
                    .placeholder(R.drawable.img_dp_default)
                    .centerInside()
                    .into(postHolder.userDP);
            // Load and display image using an image loading library like Glide or Picasso
            // You can use feed.getMediaUrl() to get the image URL
            Glide.with(context)
                    .load(feed.getMediaUrl())
                    .centerInside()
                    .into(postHolder.imageView);

            postHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle click event for opening the image in a larger view
                    // You can customize this part based on your requirements
                    // For example, start a new activity or show a dialog with the larger image
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("mediaUrl", feed.getMediaUrl());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mFeed != null) {
            return mFeed.size();
        } else {
            return 0;
        }
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView firstname;
        TextView lastname;
        TextView role;
        TextView timestamp;
        TextView caption;
        ImageView userDP;
        ImageView imageView;

        public PostViewHolder(View itemView){
            super(itemView);

            firstname = itemView.findViewById(R.id.userFirstname);
            lastname = itemView.findViewById(R.id.userLastname);
            role = itemView.findViewById(R.id.userRole);
            timestamp = itemView.findViewById(R.id.timestamp);
            caption = itemView.findViewById(R.id.caption);
            userDP = itemView.findViewById(R.id.userDP);
            imageView = itemView.findViewById(R.id.posted_image);
        }
    }
    public static class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView firstname;
        TextView lastname;
        TextView role;
        TextView timestamp;
        TextView caption;
        ImageView userDP;
        VideoView videoView;

        public VideoViewHolder(View itemView){
            super(itemView);

            firstname = itemView.findViewById(R.id.userFirstname);
            lastname = itemView.findViewById(R.id.userLastname);
            role = itemView.findViewById(R.id.userRole);
            timestamp = itemView.findViewById(R.id.timestamp);
            caption = itemView.findViewById(R.id.caption);
            userDP = itemView.findViewById(R.id.userDP);
            //videoView = itemView.findViewById(R.id.posted_video);
        }
    }
}
