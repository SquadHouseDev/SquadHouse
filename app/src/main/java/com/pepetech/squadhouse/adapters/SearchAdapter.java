package com.pepetech.squadhouse.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    public static final String TAG = "SearchAdapter";

    private Context context;
    private List<User> allUsers;

    public SearchAdapter(Context context, List<User> users) {
        this.context = context;
        this.allUsers = users;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_found, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        User user = allUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvFoundName;
        private TextView tvDescription;
        private ImageView ivFoundProfileImage;
        private Button btnFollow;
        boolean wasFollowed;

        //        private TextView tvDescription;
        // TODO: add swipe right on cell to reveal a button to hide the recommended active room
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoundName = itemView.findViewById(R.id.tvFoundName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivFoundProfileImage = itemView.findViewById(R.id.ivFoundProfileImage);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            wasFollowed = false;
        }

        public void bind(User user) {
            // Bind data of post to the view element
            tvFoundName.setText(user.getFirstName() + " " + user.getLastName());
            tvDescription.setText(user.getBiography());
            ParseFile image = user.getImage();
            if (image != null)
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(ivFoundProfileImage);
            setupButtons();
        }

        private void setupButtons() {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Follow button clicked!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Follow button clicked!");
                    // toggle button follow
                    if (wasFollowed) {
                        wasFollowed = false;
                        btnFollow.setText("Follow");
                    }
                    else {
                        wasFollowed = true;
                        btnFollow.setText("Following");
                    }
                    // TODO: Call method for following the selected User
                }
            });
        }
    }


}
