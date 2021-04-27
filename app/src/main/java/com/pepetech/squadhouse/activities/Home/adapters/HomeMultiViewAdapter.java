package com.pepetech.squadhouse.activities.Home.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.pepetech.squadhouse.activities.Explore.ExploreActivity;
import com.pepetech.squadhouse.activities.RoomActivity;
import com.pepetech.squadhouse.models.Room;

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
public class HomeMultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "HomeMultiViewAdapter";
    private Context context;
    private List<Object> objectList;

    // View Holder Item Type Mapping
    private final int ACTIVE = 0, FUTURE = 1, SEARCH = 2;

    /**
     * @param context
     * @param objectList collection of items ordered by view usage
     */
    public HomeMultiViewAdapter(Context context, List<Object> objectList) {
        this.objectList = objectList;
        this.context = context;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof String) {
            return FUTURE;
        } else if (objectList.get(position) instanceof Room) {
            return ACTIVE;
        } else if (objectList.get(position) instanceof Integer) {
            return SEARCH;
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
            case ACTIVE:
                Log.i(TAG, String.valueOf(viewType));
                View v1 = inflater.inflate(R.layout.cell_home_room_active, parent, false);
                viewHolder = new ViewHolderActive(v1);
                break;
            case FUTURE:
                Log.i(TAG, String.valueOf(viewType));
                View v2 = inflater.inflate(R.layout.cell_home_room_future, parent, false);
                viewHolder = new ViewHolderFuture(v2);
                break;
            case SEARCH:
                Log.i(TAG, String.valueOf(viewType));
                View v3 = inflater.inflate(R.layout.cell_home_explore, parent, false);
                viewHolder = new ViewHolderExplore(v3);
                break;
            default:
                Log.i(TAG, String.valueOf(viewType));
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                //TODO: Need to Update to proper ViewHolder Object
//                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                viewHolder = new ViewHolderActive(v);
                break;
        }
        return viewHolder;
    }

    class ViewHolderActive extends RecyclerView.ViewHolder {
        // Cell Room Active
        // view elements
        private TextView tvClubName;
        private TextView tvRoomName;
        private TextView tvParticipants;
        private LinearLayout llActiveRoom;
        TextView tvDateCreated;

        // TODO: add swipe right on cell to reveal a button to hide the recommended active room
        public ViewHolderActive(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvDateCreated = itemView.findViewById(R.id.tvDateCreated);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
            llActiveRoom = itemView.findViewById(R.id.llActiveRoom);
        }

        public void bind(Room room) {
            // Bind data of post to the view element
            tvClubName.setText(room.getClubName());
            tvDateCreated.setText(room.getCreatedAt().toString());
            tvRoomName.setText(room.getTitle());
            String emojiStr = getEmojiByUnicode(0x1F4AC);
            String newText = tvParticipants.getText() + " " + getEmojiByUnicode(0x1F4AC);
            tvParticipants.setText(newText); // DEBUG with emoji in unicode format
            Log.i(TAG, newText);

            // On click for the active room and routing to new activities
            llActiveRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Room: " + tvClubName.getText() + " " + tvRoomName.getText() + " clicked!");
                    // TODO: add routing of room data here for joining the conference
//                    Toast.makeText(context, "Room: " + tvClubName.getText() + " " + tvRoomName.getText() + " clicked!",
//                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(v.getContext(), RoomActivity.class);
                    i.putExtra("Room", room);
                    v.getContext().startActivity(i);
                }
            });
        }
    }

    /**
     * @param unicode
     * @return U+x1F4AC => 0x1F4AC
     */
    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    class ViewHolderFuture extends RecyclerView.ViewHolder {
        // Cell Room Future
        // view elements

        private TextView tvClubName;
        private TextView tvFutureRoomName;

        public ViewHolderFuture(View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvFutureRoomName = itemView.findViewById(R.id.tvFutureRoomName);
        }

        public void bind(String room) {
            // Bind data of post to the view element
            tvClubName.setText("FUTURE");
            tvFutureRoomName.setText("FUTURE");
        }

    }

    class ViewHolderExplore extends RecyclerView.ViewHolder {
        // Cell Room Future
        // view elements
        private TextView tvClubName;
        private TextView tvFutureRoomName;
        private LinearLayout llExploreAction;

        public ViewHolderExplore(View itemView) {
            super(itemView);
            // TODO: configure home's search cell elements
//            tvClubName = itemView.findViewById(R.id.tvClubName);
            llExploreAction = itemView.findViewById(R.id.llExploreAction);
//            tvFutureRoomName = itemView.findViewById(R.id.tvFutureRoomName);
        }

        public void bind(int room) {
            // Bind data of post to the view element
            // TODO: show a default image
            // on the app this cell changes based upon a User's search history
            // a feature that is not important to MVP

            // When explore cell is tapped, route to Explore Activity
            llExploreAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Explore cell tapped... routing to Explore Activity");
//                    Toast.makeText(context, "Explore cell tapped... routing to Explore Activity",
//                            Toast.LENGTH_SHORT).show();
                    Activity activity = (Activity) v.getContext();
                    Intent i;
                    i = new Intent(v.getContext(), ExploreActivity.class);
                    activity.startActivity(i);
                    // arg_1: page to navigate to slides from the right
                    // arg_2: page navigating from slides to the left
                    activity.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            });
        }
    }

    /**
     * Master binding method that calls slave configuration methods. Configuration
     * utilizes a switch operating on the view holder item type.
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case ACTIVE:
                ViewHolderActive vh1 = (ViewHolderActive) viewHolder;
                configureViewHolderActiveCell(vh1, position);
                break;
            case FUTURE:
                ViewHolderFuture vh2 = (ViewHolderFuture) viewHolder;
                configureViewHolderFutureCell(vh2, position);
                break;
            case SEARCH:
                ViewHolderExplore vh3 = (ViewHolderExplore) viewHolder;
                configureViewHolderExploreCell(vh3, position);
                break;
            default:
                HomeFeedAdapter.ViewHolder vh = (HomeFeedAdapter.ViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.objectList.size();
    }

    private void configureDefaultViewHolder(HomeFeedAdapter.ViewHolder vh, int position) {
        Room room = (Room) objectList.get(position);
        if (room != null) {
            vh.bind(room);
        }
    }

    /**
     * Method for binding object data to the Active Cell View Holder
     *
     * @param viewHolderActive
     * @param position
     */
    private void configureViewHolderActiveCell(ViewHolderActive viewHolderActive, int position) {
        Room room = (Room) objectList.get(position);
        if (room != null) {
            viewHolderActive.bind(room);
        }
    }

    /**
     * Method for binding object data to the Future Cell View Holder
     *
     * @param viewHolderFuture
     * @param position
     */
    private void configureViewHolderFutureCell(ViewHolderFuture viewHolderFuture, int position) {
        String futureStr = (String) objectList.get(position);
        if (futureStr != null) {
            viewHolderFuture.bind(futureStr);
        }
    }

    /**
     * Method for binding object instance to the Explore Cell View Holder
     *
     * @param viewHolderExplore
     * @param position
     */
    private void configureViewHolderExploreCell(ViewHolderExplore viewHolderExplore, int position) {
        int room = (int) objectList.get(position);
        if (room == 1) {
            viewHolderExplore.bind(room);
        }
    }


}
