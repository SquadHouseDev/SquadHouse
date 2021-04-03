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
import com.pepetech.squadhouse.activities.ViewAUserProfileActivity;
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

            String description = String.valueOf(memberCount) + " Members·" + String.valueOf(followerCount) + " Follows";
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
                    Intent i = new Intent(context, ViewAUserProfileActivity.class);
                    i.putExtra("Club", Parcels.wrap(clubElement));
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
//            String userElementId = clubElement.getParseUser().getObjectId();
//            if (currentUser.getFollowing().contains(userElementId)) {
//                Log.i(TAG, clubElement.getFirstName() + " is currently followed by " + currentUser.getFirstName());
//                btnFollow.setText("Following");
//                setupCurrentlyFollowingButton(userElementId);
//            } else {
//                setupDefaultFollowButton(userElementId);
//            }
        }

        /**
         * Current Club is not following the Club in the row therefore configure
         * buttons to reflect the default case of encouraging the Club to follow.
         *
         * @param userElementId
         */
        private void setupDefaultFollowButton(String userElementId) {
//            btnFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "Follow button clicked!");
//                    // toggle button follow
//                    if (wasFollowed) {
//                        wasFollowed = false;
//                        btnFollow.setText("Follow");
//                        // apply unfollow
//                        currentUser.removeFollowing(userElementId);
//                    } else {
//                        wasFollowed = true;
//                        btnFollow.setText("Following");
//                        // apply follow
//                        currentUser.addFollowing(userElementId);
//                    }
//                }
//            });
        }

        /**
         * Configuration of follow button when current Club is already following
         * a Club
         *
         * @param userElementId
         */
        private void setupCurrentlyFollowingButton(String userElementId) {
//            btnFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "Follow button clicked!");
//                    // toggle button follow
//                    if (wasFollowed) {
//                        wasFollowed = false;
//                        btnFollow.setText("Following");
//                        // apply follow
//                        currentUser.addFollowing(userElementId);
//                    } else {
//                        wasFollowed = true;
//                        btnFollow.setText("Follow");
//                        // apply unfollow
//                        currentUser.removeFollowing(userElementId);
//                    }
//                }
//            });
        }
    }


}
