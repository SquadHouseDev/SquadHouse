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

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

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
    User currentUser;


    public InnerInterestAdapter(Context context, List<Interest> interests, User currentUser) {
        this.currentUser = currentUser;
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
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // view elements
        TextView tvInterest;
        RelativeLayout rlInterestItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterest = itemView.findViewById(R.id.tvInterest);
            rlInterestItem = itemView.findViewById(R.id.rlInterestItem);
        }

        public void bind(Interest interest) {
            // Bind data of post to the view element
            tvInterest.setText(interest.getEmoji() + " " + interest.getName());
            // apply similar approach to that of follower
            configureInterestSelection(interest);
        }

        private void configureInterestSelection(Interest interestElement) {
            Log.i(TAG, "Configuring interest for " + interestElement.getName() + " such that selection: " + interestElement.isSelected);
            if (interestElement.isSelected) {
                // apply background to show pre-existing selection
                rlInterestItem.setBackground(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.cell_interest_selected_background));
                handleInterestToggle(interestElement);

            } else {
                // apply background to show no existing selection
                rlInterestItem.setBackground(ContextCompat.getDrawable(this.itemView.getContext(), R.drawable.cell_white_background));
                handleInterestToggle(interestElement);
            }
        }

        private void handleInterestToggle(Interest interestElement) {
            rlInterestItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // on interest toggle and current not select --> user has toggled to add the interest to list
                    if (!interestElement.isSelected) {
                        // safety check for interest existing
                        currentUser.addInterest(interestElement);
                        // update interest state to be selected
                        interestElement.isSelected = true;
                        // update GUI to show selection
                        rlInterestItem.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.cell_interest_selected_background));
                        Log.i(TAG, interestElement.getName() + " was added to " + currentUser.getFirstName() + "'s Interest list");
                    }
                    // on interest toggle and currently selected --> user has toggled to remove the interest to list
                    else {
                        interestElement.isSelected = false;
                        currentUser.removeInterest(interestElement);
                        rlInterestItem.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.cell_white_background));
                        Log.i(TAG, interestElement.getName() + " was removed to " + currentUser.getFirstName() + "'s Interest list");
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
