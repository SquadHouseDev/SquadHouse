package com.pepetech.squadhouse.activities.Explore.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Explore.ExploreUserActivity;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.List;

/**
 * The UserAdapter takes in a List of users found from keyword search
 * performed in the ExploreActivity to bind to each cell in the ReyclerView.
 * This class maintains the state of a User's activity of following/unfollowing
 * another User.
 */
public class ExploreUserAdapter extends RecyclerView.Adapter<ExploreUserAdapter.ViewHolder> {
    public static final String TAG = "ExploreUserAdapter";

    private Context context;
    private List<User> allUsers;
    private User currentUser;
    private List<Boolean> allFollowings;

    public ExploreUserAdapter(Context context, List<User> users) {
        this.context = context;
        this.allUsers = users;
//        this.currentUser = currentUser;
//        queryFollowings();
    }

    @NonNull
    @Override
    public ExploreUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_explore_found, parent, false);
        return new ExploreUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreUserAdapter.ViewHolder holder, int position) {
        User user = allUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvFoundName;
        private TextView tvDescription;
        private ImageView ivFoundProfileImage;
        private ConstraintLayout clProfile;
        private Button btnFollow;
        private Follow follow;
        boolean wasFollowed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoundName = itemView.findViewById(R.id.tvFoundName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivFoundProfileImage = itemView.findViewById(R.id.ivFoundProfileImage);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            clProfile = itemView.findViewById(R.id.clProfile);
            wasFollowed = false;
            currentUser = new User(ParseUser.getCurrentUser());
            follow = null;
        }

        public void bind(User userElement) {
            // Bind data of post to the view element
            tvFoundName.setText(userElement.getFirstName() + " " + userElement.getLastName());
            tvDescription.setText(userElement.getBiography());
            ParseFile image = userElement.getImage();
            if (image != null)
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(ivFoundProfileImage);
//            wasFollowed = isInFollowingList(currentUser, userElement);
            setupFollowButton(userElement);
            // Navigate to Viewing a User's Profile
            clProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, userElement.getFirstName() + " was selected!");
                    Toast.makeText(v.getContext(), "Selected " + userElement.getFirstName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, ExploreUserActivity.class);
                    i.putExtra("user", Parcels.wrap(userElement));
                    context.startActivity(i);
                }
            });
        }

        /**
         * Main method for configuring the display of the follow button when the current User
         * toggles between following and unfollowing a User element in the cell.
         *
         * @param userElement: User to be followed or unfollowed
         */
        private void setupFollowButton(User userElement) {
            // if the user was in a previous state of being followed by the current user
            if (userElement.isFollowed) {
                // comment out the following line when debugging FollowersActivity
//                Log.i(TAG, userElement.getFirstName() + " is currently followed by " + currentUser.getFirstName());

                // show state of followed
                btnFollow.setText("Following");
                handleOnFollowButton(userElement);
            }
            // if the user was in a previous state of being not followed by the current user
            else {
                btnFollow.setText("Follow");
                handleOnFollowButton(userElement);
            }
        }

        /**
         * Helper function for processing user input given a User element pertaining to the cell
         *
         * @param userElement User to be followed or unfollowed
         */
        private void handleOnFollowButton(User userElement) {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Follow button clicked!");
                    // on button click and not followed --> action to follow
                    if (!userElement.isFollowed && !userElement.getParseUser().getObjectId().equals(currentUser.getParseUser().getObjectId())) {
                        // safety check for creating a new follow
                        for (ParseObject o : currentUser.getFollowing()) {
                            if (o.getObjectId().equals(userElement.getParseUser().getObjectId()))
                                return;
                        }
                        btnFollow.setText("Following");
                        // apply addition of a following on the user object
                        Log.i(TAG, "Creating Follow...");
                        Log.i(TAG, "From: " + currentUser.getFirstName());
                        Log.i(TAG, "To: " + userElement.getFirstName());
                        currentUser.follow(userElement.getParseUser());
                        // create the follow object for accessing followers
                        follow = new Follow();
                        follow.put(Follow.KEY_TO, userElement.getParseUser());
                        follow.put(Follow.KEY_FROM, currentUser.getParseUser());
                        follow.saveInBackground();
                        userElement.isFollowed = true;
                        notifyDataSetChanged();
                    }
                    // on button click and is followed --> action to unfollow
                    else {
                        btnFollow.setText("Follow");
                        //  this is potentially mobile data intensive
                        if (follow != null)
                            follow.deleteInBackground();
                        currentUser.unfollow(userElement.getParseUser());
                        userElement.isFollowed = false;
                        notifyDataSetChanged();
                    }
                }
            });
        }
    } // end of inner ViewHolder class
}