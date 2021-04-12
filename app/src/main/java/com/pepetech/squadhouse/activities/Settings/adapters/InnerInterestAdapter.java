package com.pepetech.squadhouse.activities.Settings.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Interest;

import java.util.List;

/**
 * The Inner Interest Adapter inflates the cell_interest_select and populates it
 * with elements passed through the binding process carried out in the Outer Interest Adapter.
 * It also handles any User modifications (addition/deletion of Interest) to their List of Interests.
 */
public class InnerInterestAdapter extends RecyclerView.Adapter<InnerInterestAdapter.ViewHolder> {
    public static final String TAG = "InnerInterestAdapter";
    private Context context;
    private List<Interest> interests;

    public InnerInterestAdapter(Context context, List<Interest> interests) {
        this.context = context;
        this.interests = interests;
        Log.i(TAG, "interests size: " + String.valueOf(interests.size()));
    }

    @NonNull
    @Override
    public InnerInterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_interest_select, parent, false);
        return new InnerInterestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerInterestAdapter.ViewHolder holder, int position) {
        Interest interest = interests.get(position);
        // Set the group name
        holder.bind(interest);
//        holder.rlInterestItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: update view to show colors indicating selection
//                // change background color
//                // change text color
//                // TODO: add func call to update interest list
//                // add/remove from interest list
//                Toast.makeText(context, holder.tvInterest.getText() + " selected!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // view elements
        TextView tvInterest;
        RelativeLayout rlInterestItem;
        boolean isNotSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isNotSelected = true;
            tvInterest = itemView.findViewById(R.id.tvInterest);
            rlInterestItem = itemView.findViewById(R.id.rlInterestItem);
        }

        public void bind(Interest interest) {
            // Bind data of post to the view element
            tvInterest.setText(interest.getEmoji() + " " + interest.getName());
            rlInterestItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNotSelected) {
                        isNotSelected = false;
                        rlInterestItem.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.cell_interest_selected_background));
                    } else {
                        isNotSelected = true;
                        rlInterestItem.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.cell_white_background));
                    }
                    // TODO: update view to show colors indicating selection
                    // change background color
                    // change text color
                    // TODO: add func call to update interest list
                    // add/remove from interest list
                    Toast.makeText(context, tvInterest.getText() + " selected!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, tvInterest.getText() + " selected!");
                }
            });
        }
    }
}
