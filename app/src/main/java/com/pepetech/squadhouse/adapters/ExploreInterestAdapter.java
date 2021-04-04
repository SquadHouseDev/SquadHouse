package com.pepetech.squadhouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.InterestGroup;

import java.util.List;

public class ExploreInterestAdapter extends RecyclerView.Adapter<ExploreInterestAdapter.ViewHolder> {
    public static final String TAG = "InterestAdapter";

    private Context context;
    private List<InterestGroup> interestsGrouped;

    public ExploreInterestAdapter(Context context, List<InterestGroup> interests) {
        this.context = context;
        this.interestsGrouped =interests;
    }

    @NonNull
    @Override
    public ExploreInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_explore_interest, parent, false);
        return new ExploreInterestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreInterestAdapter.ViewHolder holder, int position) {
//        InterestGroup interest = (InterestGroup) interestsGrouped.get(position);
        InterestGroup interests = (InterestGroup) interestsGrouped.get(position);
        holder.bind(interests);
    }

    @Override
    public int getItemCount() {
        return interestsGrouped.size();
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

        public void bind(InterestGroup interests) {
            // Bind data of post to the view element
            tvArchetype.setText(interests.archetype);
            tvArchetypeEmoji.setText(interests.archetypeEmoji);
            tvInterests.setText(interests.names); // DEBUG with emoji in unicode format
        }
    }
}