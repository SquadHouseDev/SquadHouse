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
import com.pepetech.squadhouse.activities.Explore.ExploreClubActivity;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.List;

public class ExploreClubAdapter extends RecyclerView.Adapter<ExploreClubAdapter.ViewHolder> {
    public static final String TAG = "ExploreClubAdapter";

    private Context context;
    private List<Club> allClubs;
    private User currentUser;

    public ExploreClubAdapter(Context context, List<Club> clubs, User currentUser) {
        this.context = context;
        this.allClubs = clubs;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ExploreClubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_explore_found, parent, false);
        return new ExploreClubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreClubAdapter.ViewHolder holder, int position) {
        Club club = allClubs.get(position);
        holder.bind(club);
    }

    @Override
    public int getItemCount() {
        return allClubs.size();
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

        public void bind(Club clubElement) {
            // Bind data of post to the view element
            tvFoundName.setText(clubElement.getName());
            int memberCount = clubElement.getMembers().size();
            int followerCount = clubElement.getFollowers().size();

            String description = String.valueOf(memberCount) + " Members · " + String.valueOf(followerCount) + " Followers";
            tvDescription.setText(description);
            ParseFile image = clubElement.getImage();
            if (image != null)
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(ivFoundProfileImage);
            setupFollowButton(clubElement);
            clProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i(TAG, clubElement.getFirstName() + " was selected!");
                    Toast.makeText(v.getContext(), "Selected " + clubElement.getName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, ExploreClubActivity.class);
                    i.putExtra("club", Parcels.wrap(clubElement));
                    context.startActivity(i);
                }
            });
        }

        /**
         * Main method for configuring the follow button
         *
         * @param clubElement
         */
        private void setupFollowButton(Club clubElement) {
            if (isClubFollowedByUser(currentUser.getParseUser(), clubElement)) {
                Log.i(TAG, clubElement.getName() + " is currently followed by " + currentUser.getFirstName());
                btnFollow.setText("Following");
                setupCurrentlyFollowingButton(clubElement);
            } else {
                setupDefaultFollowButton(clubElement);
            }
        }

        /**
         * Current Club is not following the Club in the row therefore configure
         * buttons to reflect the default case of encouraging the Club to follow.
         *
         * @param clubElement
         */
        private void setupDefaultFollowButton(Club clubElement) {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Follow button clicked!");
                    // toggle button follow
                    if (wasFollowed) {
                        wasFollowed = false;
                        btnFollow.setText("Follow");
                        // apply removal of a following on the user object
                        currentUser.unfollow(clubElement);
                        // TODO: apply removal of a follower on the club object
                        clubElement.removeFollower(currentUser.getParseUser());

                    } else {
                        wasFollowed = true;
                        btnFollow.setText("Following");
                        // apply addition of a following on the user object
                        currentUser.follow(clubElement);
                        // TODO: apply addition of a follower on the club object
                        clubElement.addFollower(currentUser.getParseUser());
                    }
                }
            });
        }

        /**
         * Configuration of follow button when current Club is already following
         * a Club
         *
         * @param clubElement
         */
        private void setupCurrentlyFollowingButton(Club clubElement) {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Follow button clicked!");
                    // toggle button follow
                    if (wasFollowed) {
                        wasFollowed = false;
                        btnFollow.setText("Following");
                        // apply addition of a following on the user object
//                        currentUser.addFollowing(clubElement.getObjectId());
                        currentUser.follow(clubElement);
//                        currentUser.getParseUser().addUnique(User.KEY_FOLLOWING, clubElement);
                        currentUser.getParseUser().saveInBackground();
                        // TODO: apply addition of a follower on the club object
//                        clubElement.addFollower(currentUser.getParseUser());
//                        clubElement.addUnique(Club.KEY_FOLLOWERS, currentUser.getParseUser());
                        clubElement.saveInBackground();
                    } else {
                        wasFollowed = true;
                        btnFollow.setText("Follow");
                        // apply removal of a following on the user object
//                        currentUser.removeFollowing(clubElement.getObjectId());
                        currentUser.unfollow(clubElement);
                        currentUser.getParseUser().saveInBackground();
                        // TODO: apply removal of a follower on the club object
                        clubElement.removeFollower(currentUser.getParseUser());
                        clubElement.saveInBackground();

                    }
                }
            });
        }
    }

    /**
     * Method for validating if the input Club is currently followed by input User
     *
     * @param user
     * @param club
     * @return
     */
    boolean isClubFollowedByUser(ParseUser user, Club club) {
        boolean rv = false;
        for (ParseObject u : club.getFollowers()) {
            Log.i(TAG, "User: " + user.getObjectId() + "FollowerId: " + u.getObjectId());
            if (user.getObjectId().equals(u.getObjectId())) {
//                Log.i(TAG, "User: " + user.getObjectId() + "FollowerId: " + u.getObjectId());
                rv = true;
            }
        }
        return rv;
    }

}
