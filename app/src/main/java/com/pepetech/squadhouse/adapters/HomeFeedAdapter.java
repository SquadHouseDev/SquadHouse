package com.pepetech.squadhouse.adapters;
//import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.activities.HomeActivity;
import com.pepetech.squadhouse.models.Room;

import java.util.List;

/**
 * https://guides.codepath.com/android/Heterogeneous-Layouts-inside-RecyclerView
 * TODO: add cell_search to the recycler view
 * TODO: add cell_room_future to the recycler view
 * Recycler view should start with:
 * 1. cell_search
 * 2. cell_room_future
 * 3. cell_room_active
 * 4. cell_room_active
 * .
 * .
 * N
 */
public class HomeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "RoomsAdapter";
    private final int SEARCH = 0, EVENTS = 1, ROOM = 2;
    private Context context;
    //    private List<Room> rooms;
    // The items to display in your RecyclerView
    // 1. init w/ search
    // 2. add events if there exists
    // 3. add all active rooms
    private List<Object> items;

//    public RoomsAdapter(Context context, List<Room> rooms) {
//        this.context = context;
//        this.rooms = rooms;
//    }

    public HomeFeedAdapter(List<Object> items) {
//        this.context = context;
        this.items = items;
    }

    /**
     * This method creates different RecyclerView.ViewHolder objects based on the item view type.\
     *
     * @param parent   ViewGroup container for the item
     * @param viewType type of view to be inflated
     * @return viewHolder to be inflated
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(view);

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SEARCH:
                View searchView = inflater.inflate(R.layout.cell_search, parent, false);
                viewHolder = new SearchPromptViewHolder(searchView);
                break;
            case ROOM:
                View roomView = inflater.inflate(R.layout.cell_room_active, parent, false);
                viewHolder = new RoomViewHolder(roomView);
                break;
            default:
                View v = inflater.inflate(R.layout.cell_default, parent, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return (ViewHolder) viewHolder;

    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param holder   The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SEARCH:
                SearchPromptViewHolder searchViewHolder = (SearchPromptViewHolder) holder;
                configureSearchPromptViewHolder(searchViewHolder, position);
                break;
            case ROOM:
                RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
                configureRoomViewHolder(roomViewHolder, position);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
                configureDefaultViewHolder(vh, position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvClubName;
        private TextView tvRoomName;
        private TextView tvParticipants;

//        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
//            tvItemUsername = itemView.findViewById(R.id.tvItemUsername);
//            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
//            ivItemPostImage = itemView.findViewById(R.id.ivItemPostImage);
        }

        public void bind(Room room) {
            // Bind data of post to the view element
            tvClubName.setText(room.getClubName());
            tvRoomName.setText(room.getTitle());
            String emojiStr = getEmojiByUnicode(0x1F4AC);
            String newText = tvParticipants.getText() + " " + getEmojiByUnicode(0x1F4AC);
//            tvParticipants.setText(tvParticipants.getText() + " " + getEmojiByUnicode(0x1F4AC)); // DEBUG with emoji in unicode format
            tvParticipants.setText(newText); // DEBUG with emoji in unicode format
            Log.i(TAG, newText);
//            for (int i =0; i < emojiStr.length(); i++ ){
//                Log.i(TAG, String.valueOf(emojiStr.charAt(i)));
//            }
//            tvItemUsername.setText(room.getUser().getUsername());
//            tvItemDescription.setText(room.getDescription());
//            ParseFile image = room.getImage();
//            if (image != null)
//                Glide.with(context).load(image.getUrl()).into(ivItemPostImage);
////            ivItemPostImage.
        }
    }

    class SearchPromptViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSearch;

        public SearchPromptViewHolder(View v) {
            super(v);
            tvSearch = v.findViewById(R.id.tvSearch);
        }

    }

    class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {


        public RecyclerViewSimpleTextViewHolder(View v) {
            super(v);
        }

    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRoomName, tvClubName;

        public RoomViewHolder(View v) {
            super(v);
            tvRoomName = v.findViewById(R.id.tvRoomName);
            tvClubName = v.findViewById(R.id.tvClubName);
        }

    }


    /**
     * @param unicode
     * @return U+x1F4AC => 0x1F4AC
     */
    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
//        if (items.get(position) instanceof User) {
//            return EVENTS;
//        } else
        if (items.get(position) instanceof String) {
            return SEARCH;
        } else if (items.get(position) instanceof HomeActivity.Test)
            return ROOM;
        return -1;
    }

    private void configureSearchPromptViewHolder(SearchPromptViewHolder holder, int position) {
        holder.tvSearch.setText((String) items.get(position));
    }

    private void configureRoomViewHolder(RoomViewHolder holder, int position) {
        HomeActivity.Test room = (HomeActivity.Test) items.get(position);
        holder.tvRoomName.setText(room.title);
        holder.tvClubName.setText(room.clubName);
    }

    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder holder, int position) {
    }


}
