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

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    public static final String TAG = "ParticipantAdapter";

    private Context context;
    private List<ParseObject> users;

    public ParticipantAdapter(Context context, List<ParseObject> users) {
        Log.i(TAG, String.valueOf(users.size()));
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_room_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantAdapter.ViewHolder holder, int position) {
        ParseObject user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private ImageView ivProfile;
        private TextView tvFirstname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvFirstname = itemView.findViewById(R.id.tvFirstname);
        }

        public void bind(ParseObject user) {
            // Bind data of post to the view element
            User u = new User((ParseUser) user);
            Log.i(TAG, u.getFirstName());
            ParseFile image = u.getImage();
            if (image != null)
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(ivProfile);
            tvFirstname.setText(u.getFirstName());
        }
    }
}
