package com.example.assignment1.Adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Interfaces.PlayerCallback;
import com.example.assignment1.Model.Player;
import com.example.assignment1.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private ArrayList<Player> players;
    private PlayerCallback playerCallback;

    public PlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPlayerCallback(PlayerCallback playerCallback) {
        this.playerCallback = playerCallback;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_player_item, parent, false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = getItem(position);

        holder.player_LBL_name.setText(player.getName());
        holder.player_LBL_score.setText(String.valueOf(player.getScore()));

    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    public Player getItem(int position) {
        return players.get(position);
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        private final ShapeableImageView player_IMG_location;
        private final MaterialTextView player_LBL_name;
        private final MaterialTextView player_LBL_score;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            player_IMG_location = itemView.findViewById(R.id.player_IMAGEVIEW_location);
            player_LBL_name = itemView.findViewById(R.id.player_LBL_name);
            player_LBL_score = itemView.findViewById(R.id.player_LBL_score);
            player_IMG_location.setOnClickListener(v -> { //Show the location of the user on the map
                if (playerCallback != null){
                    playerCallback.Show(getItem(getAdapterPosition()), getAdapterPosition());
                }
           });
        }
    }
}
