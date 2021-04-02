package com.pepetech.squadhouse.adapters;

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
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.ViewAUserActivity;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.List;

public class ExploreUserAdapter extends RecyclerView.Adapter<ExploreUserAdapter.ViewHolder> {
    public static final String TAG = "SearchAdapter";

    private Context context;
    private List<User> allUsers;
    private User currentUser;

    public ExploreUserAdapter(Context context, List<User> users, User currentUser) {
        this.context = context;
        this.allUsers = users;
        this.currentUser = currentUser;
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
        boolean wasFollowed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoundName = itemView.findViewById(R.id.tvFoundName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivFoundProfileImage = itemView.findViewById(R.id.ivFoundProfileImage);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            clProfile = itemView.findViewById(R.id.clProfile);
            wasFollowed = false;
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
            setupFollowButton(userElement);
            clProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, userElement.getFirstName() + " was selected!");
                    Toast.makeText(v.getContext(), "Selected " + userElement.getFirstName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, ViewAUserActivity.class);
                    i.putExtra("user", Parcels.wrap(userElement));
                    context.startActivity(i);
                }
            });
        }

        /**
         * Main method for configuring the follow button
         * @param userElement
         */
        private void setupFollowButton(User userElement) {
            String userElementId = userElement.getParseUser().getObjectId();
            if (currentUser.getFollowing().contains(userElementId)){
                Log.i(TAG, userElement.getFirstName() + " is currently followed by " + currentUser.getFirstName());
                btnFollow.setText("Following");
                setupCurrentlyFollowingButton(userElementId);
            }
            else {
                setupDefaultFollowButton(userElementId);
            }
        }

        /**
         * Current User is not following the User in the row therefore configure
         * buttons to reflect the default case of encouraging the User to follow.
         * @param userElementId
         */
        private void setupDefaultFollowButton(String userElementId){
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Follow button clicked!");
                    // toggle button follow
                    if (wasFollowed) {
                        wasFollowed = false;
                        btnFollow.setText("Follow");
                        // apply unfollow
                        currentUser.removeFollowing(userElementId);

                    } else {
                        wasFollowed = true;
                        btnFollow.setText("Following");
                        // apply follow
                        currentUser.addFollowing(userElementId);
                    }
                }
            });
        }

        /**
         * Configuration of follow button when current user is already following
         * a User
         * @param userElementId
         */
        private void setupCurrentlyFollowingButton(String userElementId){
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Follow button clicked!");
                    // toggle button follow
                    if (wasFollowed) {
                        wasFollowed = false;
                        btnFollow.setText("Following");
                        // apply follow
                        currentUser.addFollowing(userElementId);
                    } else {
                        wasFollowed = true;
                        btnFollow.setText("Follow");
                        // apply unfollow
                        currentUser.removeFollowing(userElementId);
                    }
                }
            });
        }
    }


}
