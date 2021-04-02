package com.pepetech.squadhouse.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Interest;

//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {
    public static final String TAG = "InterestAdapter";

    private Context context;
    private List<Interest> interests;
    private LinkedHashMap<String, List<Interest>> interestsGrouped;


//    public InterestAdapter(Context context, LinkedHashMap<String, List<Interest>> interestsGrouped) {
//        this.context = context;
//        this.interestsGrouped = interestsGrouped;
//    }

    public InterestAdapter(Context context,  List<Interest> interests) {
        this.context = context;
        this.interests = interests;
    }

    @NonNull
    @Override
    public InterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_search_interest, parent, false);
        return new InterestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestAdapter.ViewHolder holder, int position) {
//        List<Interest> interest = (List<Interest>) interestsGrouped.get(position);
        Interest interest = interests.get(position);
        holder.bind(interest);
    }

    @Override
    public int getItemCount() {
//        return interestsGrouped.size();
        return interests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        private TextView tvArchetypeEmoji;
        private TextView tvArchetype;
        private TextView tvInterests;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArchetypeEmoji = itemView.findViewById(R.id.tvArchetypeEmoji);
            tvArchetype = itemView.findViewById(R.id.tvArchetype);
            tvInterests = itemView.findViewById(R.id.tvInterests);
        }

//        public void bind(List<Interest> interests) {
//            // Bind data of post to the view element
//            tvArchetypeEmoji.setText(interests.get(0).getArchetypeEmoji());
//            tvArchetype.setText(interests.get(0).getArchetype());
//            String interestNames = "";
//            for (Interest i : interests){
//                interestNames += i.getName();
//                interestNames += ", ";
//            }
//            tvInterests.setText(interestNames); // DEBUG with emoji in unicode format
////            Log.i(TAG, newText);
//        }

        public void bind(Interest interest) {
            // Bind data of post to the view element
            tvArchetype.setText(interest.getArchetype());
            tvArchetypeEmoji.setText(interest.getArchetypeEmoji());
            tvInterests.setText(interest.getName()); // DEBUG with emoji in unicode format
//            Log.i(TAG, newText);
        }
    }
}