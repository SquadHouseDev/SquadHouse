package com.pepetech.squadhouse.adapters;
//import androidx.recyclerview.widget.RecyclerView;
import com.pepetech.squadhouse.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.models.Room;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    private Context context;
    private List<Room> rooms;

    public RoomsAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_room_active, parent, false);
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
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClubName = itemView.findViewById(R.id.tvClubName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
//            tvItemUsername = itemView.findViewById(R.id.tvItemUsername);
//            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
//            ivItemPostImage = itemView.findViewById(R.id.ivItemPostImage);
        }

        public void bind(Room room){
            // Bind data of post to the view element
            tvClubName.setText(room.getClubName());
            tvRoomName.setText(room.getTitle());
//            tvItemUsername.setText(room.getUser().getUsername());
//            tvItemDescription.setText(room.getDescription());
//            ParseFile image = room.getImage();
//            if (image != null)
//                Glide.with(context).load(image.getUrl()).into(ivItemPostImage);
////            ivItemPostImage.
        }
    }

}
