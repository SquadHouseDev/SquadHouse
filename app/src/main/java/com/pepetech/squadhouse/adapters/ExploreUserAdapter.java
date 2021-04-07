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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.ExploreUserActivity;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.List;

/**
 * The UserAdapter takes in a List of users found from keyword search
 * performed in the ExploreActivity to bind to each cell in the ReyclerView.
 */
public class ExploreUserAdapter extends RecyclerView.Adapter<ExploreUserAdapter.ViewHolder> {
    public static final String TAG = "ExploreUserAdapter";

    private Context context;
    private List<User> allUsers;
    private User currentUser;
    private List<Boolean> allFollowings;

    public ExploreUserAdapter(Context context, List<User> users, User currentUser) {
        this.context = context;
        this.allUsers = users;
        this.currentUser = currentUser;
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
                Log.i(TAG, userElement.getFirstName() + " is currently followed by " + currentUser.getFirstName());
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
                    if (!userElement.isFollowed) {
                        btnFollow.setText("Following");
                        // apply addition of a following on the user object
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


        /**
         * Current User is not following the User in the row therefore configure
         * buttons to reflect the default case of encouraging the User to follow.
         *
         * @param userElement
         */
//        private void setupDefaultFollowButton(User userElement) {
//            btnFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "setupDefaultFollowButton");
//                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "Follow button clicked!");
//                    // toggle button follow
//                    if (wasFollowed) {
//                        wasFollowed = false;
//                        btnFollow.setText("Follow");
//                        // apply addition of a following on the user object
////                        userElement.removeFollower(currentUser.getParseUser());
////                        currentUser.removeFollowing(userElement.getParseUser());
//                        // TODO: apply addition of a follower on the club object
////                        List<ParseObject> toRemove = new ArrayList<>();
////                        toRemove.add(userElement.getParseUser());
////                        toRemove.add(currentUser.getParseUser());
////                        currentUser.getParseUser().removeAll(User.KEY_FOLLOWING, toRemove);
////                        userElement.getParseUser().addUnique(User.KEY_FOLLOWERS, toRemove);
////                        currentUser.getParseUser().saveInBackground();
//                        //  this is potentially mobile data intensive
//                        follow.deleteInBackground();
//                        userElement.isFollowed = false;
////                        userElement.getParseUser().saveInBackground();
////                        currentUser.removeFollowing(userElement.getParseUser());
////                        currentUser.getParseUser().saveInBackground();
////                        userElement.removeFollower(currentUser.getParseUser());
////                        userElement.getParseUser().saveInBackground();
//
////                        userElement.getParseUser().saveInBackground();
////                        notifyDataSetChanged();
//
//                    } else {
//                        wasFollowed = true;
//                        btnFollow.setText("Following");
//                        // apply addition of a following on the user object
////                        userElement.addFollower(currentUser.getParseUser());
////                        currentUser.addFollowing(userElement.getParseUser());
////                        currentUser.getParseUser().addUnique(User.KEY_FOLLOWING, userElement.getParseUser());
////                        userElement.getParseUser().addUnique(User.KEY_FOLLOWERS, currentUser.getParseUser());
////                        currentUser.getParseUser().saveInBackground();
////                        userElement.getParseUser().saveInBackground();
//
////                        currentUser.addFollowing(userElement.getParseUser());
////                        currentUser.getParseUser().saveInBackground();
////                        userElement.addFollower(currentUser.getParseUser());
////                        userElement.getParseUser().addUnique(User.KEY_FOLLOWERS,currentUser.getParseUser());
////                        userElement.getParseUser().saveInBackground();
////                        userElement.getParseUser().saveInBackground();
////                        userElement.getParseUser().saveInBackground();
//
//                        // apply addition of a following on the user object
//                        currentUser.follow(userElement.getParseUser());
//                        // create the follow object for accessing followers
//                        follow = new Follow();
//                        follow.put(Follow.KEY_TO, userElement.getParseUser());
//                        follow.put(Follow.KEY_FROM, currentUser.getParseUser());
//                        follow.saveInBackground();
//                        userElement.isFollowed = true;
//                        notifyDataSetChanged();
//                    }
//                }
//            });
//        }

        /**
         * Configuration of follow button when current user is already following
         * a User
         *
         * @param userElement
         */
//        private void setupCurrentlyFollowingButton(User userElement) {
//            btnFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "setupCurrentlyFollowingButton");
//                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "Follow button clicked!");
//
//                    // toggle button unfollow
//                    if (wasFollowed) {
//                        wasFollowed = false;
//                        btnFollow.setText("Following");
//                        // apply addition of a following on the user object
//                        currentUser.follow(userElement.getParseUser());
//                        // create the follow object for accessing followers
//                        follow = new Follow();
//                        follow.put(Follow.KEY_TO, userElement.getParseUser());
//                        follow.put(Follow.KEY_FROM, currentUser.getParseUser());
//                        follow.saveInBackground();
//                        // update on local side
//                        userElement.isFollowed = true;
//                        notifyDataSetChanged();
//                    }
//                    // toggle button to unfollow
//                    else {
//                        wasFollowed = true;
//                        btnFollow.setText("Follow");
//                        // apply unfollow
//
//                        // call a delete on the follow
//                        follow.deleteInBackground();
//                        userElement.isFollowed = false;
//                        // perform the usual change in the current User's following list
//                        currentUser.unfollow(userElement.getParseUser());
//                        // no need to update the follower of the userElement
////                        userElement.removeFollower(currentUser.getParseUser());
////                        currentUser.removeFollowing(userElement.getParseUser());
////                        currentUser.removeFollowing(userElement.getParseUser());
////                        currentUser.getParseUser().saveInBackground();
////                        userElement.removeFollower(currentUser.getParseUser());
////                        userElement.getParseUser().saveInBackground();
//
////                        userElement.getParseUser().saveInBackground();
////                        List<ParseObject> toRemove = new ArrayList<>();
////                        toRemove.add(userElement.getParseUser());
////                        toRemove.add(currentUser.getParseUser());
////                        currentUser.getParseUser().removeAll(User.KEY_FOLLOWING, toRemove);
////                        userElement.getParseUser().addUnique(User.KEY_FOLLOWERS, toRemove);
////                        currentUser.getParseUser().saveInBackground();
////                        userElement.getParseUser().saveInBackground();
//                        // TODO: apply addition of a follower on the club object
//                        notifyDataSetChanged();
//                    }
//                }
//            });
//        }

        /**
         * Method for validating if the input Club is currently followed by input User
         *
         * @param currentUser
         * @param userFollowed
         * @return
         */
//        boolean isInFollowingList(User currentUser, User userFollowed) {
//            List<ParseUser> followers = userFollowed.getFollowers();
////        userFollowed.getParseUser().addAllUnique(User.KEY_FOLLOWERS, followers);
////        userFollowed.getParseUser().saveInBackground();
//            boolean rv = false;
//            for (ParseObject u : followers) {
//                Log.i(TAG, "User: " + userFollowed.getParseUser().getObjectId() + "FollowerId: " + u.getObjectId());
//                if (currentUser.getParseUser().getObjectId().equals(u.getObjectId())) {
////                Log.i(TAG, "User: " + user.getObjectId() + "FollowerId: " + u.getObjectId());
//                    rv = true;
//                }
//            }
//            return rv;
//        }
    }

//    void queryFollowings() {
//        Log.i(TAG, "queryFollowings");
////        allFollowings.clear();
//        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
//        mainQuery.whereEqualTo(Follow.KEY_FROM, currentUser.getParseUser().getObjectId());
//        mainQuery.findInBackground(new FindCallback<Follow>() {
//            @Override
//            public void done(List<Follow> objects, ParseException e) {
//                Log.i(TAG, String.valueOf(objects.size()) + " followings");
//                if (e == null) {
//                    // iterate over all Users found from search
//                    for (User u : allUsers) {
//                        // iterate over all Follow entries
//                        for (Follow c : objects) {
//                            // compare
//                            Log.i(TAG, String.format("Current: %s From: %s To: %s",
//                                    u.getParseUser().getObjectId(),
//                                    c.getFollowFrom().getObjectId(),
//                                    c.getFollowTo().getObjectId()));
//                            if (u.getParseUser().getObjectId().equals(c.getFollowTo().getObjectId())) {
//                                u.isFollowed = true;
//                                notifyDataSetChanged();
//                            }
//                            Log.i(TAG, c.getFollowTo().getObjectId());
//                        }
//                    }
//                } else {
//                }
//                notifyDataSetChanged();
//            }
//
//        });
//    }
}
