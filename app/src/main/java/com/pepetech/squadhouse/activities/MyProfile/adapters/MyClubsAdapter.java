package com.pepetech.squadhouse.activities.MyProfile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Explore.ExploreClubActivity;
import com.pepetech.squadhouse.models.Club;

import org.parceler.Parcels;

import java.util.List;

public class MyClubsAdapter extends RecyclerView.Adapter<MyClubsAdapter.ViewHolder> {
    public static final String TAG = "MyClubsAdapter";

    private Context context;
    private List<Club> clubs;

    public MyClubsAdapter(Context context, List<Club> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public MyClubsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_club_button, parent, false);
        return new MyClubsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClubsAdapter.ViewHolder holder, int position) {
        Club clubs = this.clubs.get(position);
        holder.bind(clubs);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        ImageView ivClubImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClubImage = itemView.findViewById(R.id.ivClubImage);
        }

        public void bind(Club club) {
            // Bind data of post to the view element
            if (club != null) {
                ParseFile image = null;
                image = club.getImage();
                if (image != null)
                    Glide.with(context)
                            .load(image.getUrl())
                            .circleCrop()
                            .into(ivClubImage);
                ivClubImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: add routing to club profile here
//                        Toast.makeText(context, club.getName() + " clicked!",
//                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(v.getContext(), ExploreClubActivity.class);
                        i.putExtra("club", Parcels.wrap(club));
                        v.getContext().startActivity(i);
                    }
                });
            } else {
                ivClubImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: add routing to club profile here
//                        Toast.makeText(context, "Create Club Activity clicked!",
//                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
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