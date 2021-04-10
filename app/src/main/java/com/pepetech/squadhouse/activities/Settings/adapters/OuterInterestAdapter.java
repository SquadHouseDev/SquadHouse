package com.pepetech.squadhouse.activities.Settings.adapters;
//import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Interest;

import java.util.List;

public class OuterInterestAdapter extends RecyclerView.Adapter<OuterInterestAdapter.ViewHolder> {
    public static final String TAG = "OuterInterestAdapter";
    private Context context;
    private List<List<Interest>> interests;

    public OuterInterestAdapter(Context context, List<List<Interest>> interests) {
        this.context = context;
        this.interests = interests;
        Log.i(TAG, "interests size: " + String.valueOf(interests.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_inner_rv_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Interest> interestsByArchetype = interests.get(position);
        // Set the group name
        holder.bind(interestsByArchetype);
        // Init inner adapter
        InnerInterestAdapter innerInterestAdapter = new InnerInterestAdapter(context, interestsByArchetype);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        int ROWS = 0;
        Log.i(TAG, interestsByArchetype.get(0).getArchetype() + " " + String.valueOf((interestsByArchetype.size() / 5)));
        if (interestsByArchetype.size() / 3 > 3) {
            ROWS = 3;
        } else if (interestsByArchetype.size() / 3 > 2) {
            ROWS = 2;
        } else {
            ROWS = 1;
        }
        // Init inner adapter layout
        GridLayoutManager layoutManager = new GridLayoutManager(
                context, ROWS, GridLayoutManager.HORIZONTAL, false);
//        layoutManager.setB
        // Assign inner recycler layout
        holder.rvInnerInterest.setLayoutManager(layoutManager);
        // Assian inner recycler adapater
        holder.rvInnerInterest.setAdapter(innerInterestAdapter);
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // view elements
        private TextView tvInterestArchetype;
        private RecyclerView rvInnerInterest;

        //        private TextView tvDescription;
        // TODO: add swipe right on cell to reveal a button to hide the recommended active room
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterestArchetype = itemView.findViewById(R.id.tvInterestArchetype);
            rvInnerInterest = itemView.findViewById(R.id.rvInnerInterest);
//            rvInnerInterest.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2) );
            rvInnerInterest.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//            tvParticipants = itemView.findViewById(R.id.tvParticipants);
        }

        public void bind(List<Interest> interestsByArchetype) {
            // Bind data of post to the view element
            tvInterestArchetype.setText(interestsByArchetype.get(0).getArchetypeEmoji() +
                    " " + interestsByArchetype.get(0).getArchetype());
            Log.i(TAG, interestsByArchetype.get(0).getArchetype());
        }
    }


}