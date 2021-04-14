package com.pepetech.squadhouse.activities.Following.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Home.adapters.HomeFeedAdapter;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Room;
import com.pepetech.squadhouse.models.User;

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
            return CLUB;
        } else if (objectList.get(position) instanceof User) {
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
                Log.i(TAG, String.valueOf(viewType));
                View v1 = inflater.inflate(R.layout.cell_following_club, parent, false);
                viewHolder = new ViewHolderClub(v1);
                break;
            case USER:
                Log.i(TAG, String.valueOf(viewType));
                View v2 = inflater.inflate(R.layout.cell_explore_found, parent, false);
                viewHolder = new ViewHolderUser(v2);
                break;
            default:
                Log.i(TAG, String.valueOf(viewType));
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                //TODO: Need to Update to proper ViewHolder Object
//                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                viewHolder = new ViewHolderClub(v);
                break;
        }
        return viewHolder;
    }


    class ViewHolderClub extends RecyclerView.ViewHolder {
        // Cell Room Active
        // view elements
        private TextView tvClubName;

        // TODO: add swipe right on cell to reveal a button to hide the recommended active room
        public ViewHolderClub(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
        }

        public void bind(Club club) {
            // Bind data of post to the view element

            // On click for the active room and routing to new activities
        }
    }

    class ViewHolderUser extends RecyclerView.ViewHolder {
        // Cell Room Future
        // view elements

        public ViewHolderUser(View itemView) {
            super(itemView);
        }

        public void bind(User user) {
            // Bind data of post to the view element
        }

    }

    @Override
    public int getItemCount() {
        return this.objectList.size();
    }

    private void configureDefaultViewHolder(FollowingAdapter.ViewHolder vh, int position) {
        Club club = (Club) objectList.get(position);
        if (club != null) {
            vh.bind(club);
        }
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
                FollowingAdapter.ViewHolder vh = (FollowingAdapter.ViewHolder) viewHolder;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvClubName;
        private TextView tvRoomName;
        private TextView tvParticipants;
        private LinearLayout llActiveRoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvClubName = itemView.findViewById(R.id.tvClubName);
        }

        public void bind(Club club) {
        }
    }


}

