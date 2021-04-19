package com.pepetech.squadhouse.activities.Following.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Explore.ExploreClubActivity;
import com.pepetech.squadhouse.activities.Explore.ExploreUserActivity;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.List;

/**
 * Adapter for handling different types of views existing in one recycler view.
 * Addition of new views types requiring adding to the following methods:
 * <ul>
 *     <li>onCreateViewHolder</li>
 *     <li>onBindViewHolder</li>
 *     <li>getItemViewType</li>
 * </ul>
 * <p>
 * Declaration and definition of a new View Holder Class and View Holder Configuration
 * method is necessary to extend.
 */
public class FollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "FollowingAdapter";
    private Context context;
    private List<Object> objectList;
    User currentUser;

    // View Holder Item Type Mapping
    private final int CLUB = 0, USER = 1;

    /**
     * @param context
     * @param objectList collection of items ordered by view usage
     */
    public FollowingAdapter(Context context, List<Object> objectList) {
        this.objectList = objectList;
        this.context = context;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof Club) {
            Log.i(TAG, "getItemViewType: " + ((Club) objectList.get(position)).getName());
            return CLUB;
        } else if (objectList.get(position) instanceof User) {
            Log.i(TAG, "getItemViewType: " + ((User) objectList.get(position)).getFirstName());
            return USER;
        }
        return -1;
    }

    /**
     * Method for mapping different view holders and inflating the associated layout
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case CLUB:
                Log.i(TAG, "onCreateViewHolder: " + String.valueOf(viewType));
                View v1 = inflater.inflate(R.layout.cell_following_club, parent, false);
                viewHolder = new ViewHolderClub(v1);
                break;
            case USER:
                Log.i(TAG, "onCreateViewHolder: " + String.valueOf(viewType));
                View v2 = inflater.inflate(R.layout.cell_explore_found, parent, false);
                viewHolder = new ViewHolderUser(v2);
                break;
            default:
                Log.i(TAG, "onCreateViewHolder: " + String.valueOf(viewType));
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                //TODO: Need to Update to proper ViewHolder Object
//                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                viewHolder = new ViewHolderClub(v);
                break;
        }
        return viewHolder;
    }


    class ViewHolderClub extends DefaultViewHolder {
        // view elements
        private TextView tvFoundName;
        private ImageView ivFoundProfileImage;
        private ConstraintLayout clClubFollowing;

        // TODO: add swipe right on cell to reveal a button to hide the recommended active room
        public ViewHolderClub(@NonNull View itemView) {
            super(itemView);
            tvFoundName = itemView.findViewById(R.id.tvFoundName);
            ivFoundProfileImage = itemView.findViewById(R.id.ivFoundProfileImage);
            clClubFollowing = itemView.findViewById(R.id.clClubFollowing);
        }

        public void bind(Club club) {
            // Bind data of post to the view element
            tvFoundName.setText(club.getName());
            ParseFile image = club.getImage();
            if (image != null)
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(ivFoundProfileImage);
            // On click for the active room and routing to new activities
            clClubFollowing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i(TAG, clubElement.getFirstName() + " was selected!");
                    Toast.makeText(v.getContext(), "Selected " + club.getName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, ExploreClubActivity.class);
                    i.putExtra("club", Parcels.wrap(club));
                    context.startActivity(i);
                }
            });
        }
    }

    class ViewHolderUser extends DefaultViewHolder {
        // view elements
        private TextView tvFoundName;
        private TextView tvDescription;
        private ImageView ivFoundProfileImage;
        private ConstraintLayout clExploreFound;
        private Button btnFollow;
        private Follow follow;
        boolean wasFollowed;

        public ViewHolderUser(View itemView) {
            super(itemView);
            tvFoundName = itemView.findViewById(R.id.tvFoundName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivFoundProfileImage = itemView.findViewById(R.id.ivFoundProfileImage);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            clExploreFound = itemView.findViewById(R.id.clExploreFound);
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
            // Navigate to Viewing a User's Profile
            clExploreFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, userElement.getFirstName() + " was selected!");
                    Toast.makeText(v.getContext(), "Selected " + userElement.getFirstName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, ExploreUserActivity.class);
                    i.putExtra("user", Parcels.wrap(userElement));
                    context.startActivity(i);
                }
            });
            // Apply button handling for follow/unfollow actions
            configureUserSelection(userElement);
        }
        
        private void configureUserSelection(User userElement){
            Log.i(TAG, "Configuring interest for " + userElement.getFirstName() + " such that selection: " + userElement.isFollowed);
            if (userElement.isFollowed) {
                // apply background to show pre-existing selection
                btnFollow.setText("Following");
                handleFollowToggle(userElement);

            } else {
                // apply background to show no existing selection
                btnFollow.setText("Follow");
                handleFollowToggle(userElement);
            }
        }

        private void handleFollowToggle(User userElement) {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // on interest toggle and current not select --> user has toggled to add the interest to list
                    if (!userElement.isFollowed) {
                        // safety check for interest existing
                        currentUser.follow(userElement.getParseUser());
                        // update interest state to be selected
                        follow = new Follow();
                        follow.put(Follow.KEY_TO, userElement.getParseUser());
                        follow.put(Follow.KEY_FROM, currentUser.getParseUser());
                        follow.saveInBackground();
                        userElement.isFollowed = true;
                        // update GUI to show selection
                        btnFollow.setText("Following");
                    }
                    // on interest toggle and currently selected --> user has toggled to remove the interest to list
                    else {
                        btnFollow.setText("Follow");
                        if (follow != null)
                            follow.deleteInBackground();
                        currentUser.unfollow(userElement.getParseUser());
                        userElement.isFollowed = false;
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.objectList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case CLUB:
                FollowingAdapter.ViewHolderClub vh1 = (FollowingAdapter.ViewHolderClub) viewHolder;
                configureViewHolderClubCell(vh1, position);
                break;
            case USER:
                FollowingAdapter.ViewHolderUser vh2 = (FollowingAdapter.ViewHolderUser) viewHolder;
                configureViewHolderUserCell(vh2, position);
                break;
            default:
                DefaultViewHolder vh = (DefaultViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;
        }
    }

    /**
     * Method for binding object data to the Active Cell View Holder
     *
     * @param viewHolder
     * @param position
     */
    private void configureViewHolderClubCell(ViewHolderClub viewHolder, int position) {
        Club club = (Club) objectList.get(position);
        if (club != null) {
            viewHolder.bind(club);
        }
    }

    /**
     * Method for binding object data to the Future Cell View Holder
     *
     * @param viewHolder
     * @param position
     */
    private void configureViewHolderUserCell(ViewHolderUser viewHolder, int position) {
        User user = (User) objectList.get(position);
        if (user != null) {
            viewHolder.bind(user);
        }
    }

    private void configureDefaultViewHolder(DefaultViewHolder vh, int position) {
        Object o = objectList.get(position);
        if (o != null) {
            vh.bind(o);
        }
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvClubName;
        private TextView tvRoomName;
        private TextView tvParticipants;
        private LinearLayout llActiveRoom;

        public DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvClubName = itemView.findViewById(R.id.tvClubName);
        }

        public void bind(Object o) {
        }
    }


}


