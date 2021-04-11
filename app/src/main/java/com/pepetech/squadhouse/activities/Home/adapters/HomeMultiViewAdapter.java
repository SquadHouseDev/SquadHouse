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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Explore.ExploreActivity;
import com.pepetech.squadhouse.models.Room;

import java.util.List;

public class HomeMultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "HomeMultiViewAdapter";
    private Context context;
    private List<Object> rooms;

    private final int ACTIVE = 0, FUTURE = 1, SEARCH = 2;

    public HomeMultiViewAdapter(Context context, List<Object> rooms) {
        this.rooms = rooms;
        this.context = context;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (rooms.get(position) instanceof String) {
            return FUTURE;
        } else if (rooms.get(position) instanceof Room) {
            return ACTIVE;
        } else if (rooms.get(position) instanceof Integer) {
            return SEARCH;
        }
        return -1;
    }

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

        //        private TextView tvDescription;
        // TODO: add swipe right on cell to reveal a button to hide the recommended active room

        public ViewHolderActive(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
        }

        public void bind(Room room) {
            // Bind data of post to the view element
            tvClubName.setText("ACTIVE");
            tvRoomName.setText("ACTIVE");
            String emojiStr = getEmojiByUnicode(0x1F4AC);
            String newText = tvParticipants.getText() + " " + getEmojiByUnicode(0x1F4AC);
            tvParticipants.setText(newText); // DEBUG with emoji in unicode format
            Log.i(TAG, newText);
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
        return this.rooms.size();
    }

    private void configureDefaultViewHolder(HomeFeedAdapter.ViewHolder vh, int position) {
        Room room = (Room) rooms.get(position);
        if (room != null) {
            vh.bind(room);
        }
    }

    /**
     * Active
     *
     * @param vh1
     * @param position
     */
    private void configureViewHolderActiveCell(ViewHolderActive vh1, int position) {
        Room room = (Room) rooms.get(position);
        if (room != null) {
            vh1.bind(room);
        }
    }

    /**
     * Future
     *
     * @param vh2
     * @param position
     */
    private void configureViewHolderFutureCell(ViewHolderFuture vh2, int position) {
        String futureStr = (String) rooms.get(position);
        if (futureStr != null) {
            vh2.bind(futureStr);
        }
    }

    /**
     * Explore
     * @param vh3
     * @param position
     */
    private void configureViewHolderExploreCell(ViewHolderExplore vh3, int position) {
        int room = (int) rooms.get(position);
        if (room == 1){
            vh3.bind(room);
        }
    }


}


