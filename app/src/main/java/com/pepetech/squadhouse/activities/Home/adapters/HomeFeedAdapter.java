package com.pepetech.squadhouse.activities.Home.adapters;

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
import com.pepetech.squadhouse.models.Room;

import java.util.List;

/**
 * Deprecated adapter class refer to HomeMultiViewAdapter
 */
public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {
    public static final String TAG = "HomeFeedAdapter";

    private Context context;
    private List<Room> rooms;

    public HomeFeedAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_home_room_active, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvClubName;
        private TextView tvRoomName;
        private TextView tvParticipants;
        private LinearLayout llActiveRoom;

        //        private TextView tvDescription;
        // TODO: add swipe right on cell to reveal a button to hide the recommended active room
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
            llActiveRoom = itemView.findViewById(R.id.llActiveRoom);
        }

        public void bind(Room room) {
            // Bind data of post to the view element
            tvClubName.setText(room.getClubName());
            tvRoomName.setText(room.getTitle());
            String emojiStr = getEmojiByUnicode(0x1F4AC);

            String newText = tvParticipants.getText() + " " + getEmojiByUnicode(0x1F4AC);
            tvParticipants.setText(newText); // DEBUG with emoji in unicode format
            Log.i(TAG, newText);
            llActiveRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Room: " + tvClubName.getText() + " " + tvRoomName.getText() + " clicked!");
                    // TODO: add routing of room data here for joining the conference
//                    Toast.makeText(context, "Room: " + tvClubName.getText() + " " + tvRoomName.getText() + " clicked!",
//                            Toast.LENGTH_SHORT).show();
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


}