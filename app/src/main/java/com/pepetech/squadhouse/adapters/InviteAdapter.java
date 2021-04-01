package com.pepetech.squadhouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import java.util.List;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder>{

    public static final String TAG = "InviteAdapter";

    private Context context;
    private List<Object> followers;

    public InviteAdapter(Context context, List<Object> followers) {
        this.context = context;
        this.followers = followers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteAdapter.ViewHolder holder, int position) {
        Object follower = followers.get(position);
        holder.bind(follower);
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView userProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.userProfileImageInvite);
        }

        public void bind(Object user){
            // Bind data of post to the view element
            ParseFile image = ((ParseObject)user).getParseFile(User.KEY_IMAGE);
            if (image != null)
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(userProfile);
        }
    }
}
