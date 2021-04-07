package com.pepetech.squadhouse.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Room;
import com.pepetech.squadhouse.models.User;

import java.util.List;

public class HeterogeneousrViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "heterogeneousAdapter";
    private Context context;
    private List<Room> rooms;

    private final int ACTIVE = 0, FUTURE = 1;

    public HeterogeneousrViewAdapter(Context context, List<Room> rooms ){
        this.rooms = rooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ACTIVE:
                View v1 = inflater.inflate(R.layout.cell_home_room_active, parent, false);
                viewHolder = new ViewHolder1(v1);
                break;
            case FUTURE:
                View v2 = inflater.inflate(R.layout.cell_home_room_future, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                    //TODO: Need to Update to proper ViewHolder Object
//                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                viewHolder = new ViewHolder1(v);
                break;
        }
        return viewHolder;
    }

     class ViewHolder1 extends RecyclerView.ViewHolder {
        // Cell Room Active
        // view elements

        private TextView tvClubName;
        private TextView tvRoomName;
        private TextView tvParticipants;

        //        private TextView tvDescription;
        // TODO: add swipe right on cell to reveal a button to hide the recommended active room

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
        }

        public void bind(Room room) {
            // Bind data of post to the view element
            tvClubName.setText(room.getClubName());
            tvRoomName.setText(room.getTitle());
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

    class ViewHolder2 extends RecyclerView.ViewHolder {
        // Cell Room Future
        // view elements
        
      private TextView tvClubName;
      private TextView tvFutureRoomName;

        public ViewHolder2(View itemView ) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvFutureRoomName = itemView.findViewById(R.id.tvFutureRoomName);

        }

        public void bind(Room room) {
            // Bind data of post to the view element
            tvClubName.setText(room.getClubName());
            tvFutureRoomName.setText(room.getTitle());
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case ACTIVE:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case FUTURE:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
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

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        Room room = (Room) rooms.get(position);
        if (room != null) {
            vh1.bind(room);
        }
    }

    private void configureViewHolder2(ViewHolder2 vh2, int position) {
        Room room = (Room) rooms.get(position);
        if (room != null) {
            vh2.bind(room);
        }
    }
}


