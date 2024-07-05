package com.example.assignment1.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Adapters.PlayerAdapter;
import com.example.assignment1.Interfaces.ShowMapCallback;
import com.example.assignment1.Model.Player;
import com.example.assignment1.R;
import com.example.assignment1.Utilities.MSP;

import java.util.ArrayList;

public class Fragment_List extends Fragment {

    private RecyclerView list_LST_players;
    private ShowMapCallback mapCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        list_LST_players = view.findViewById(R.id.list_LST_players);
        initViews();

        return view;
    }

    public void setMapCallback(ShowMapCallback mapCallback){
        this.mapCallback=mapCallback;
    }
    private void initViews() {
        ArrayList<Player> players= MSP.getInstance().readPlayersList(getString(R.string.players_list));
        PlayerAdapter playerAdapter = new PlayerAdapter(players);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        list_LST_players.setLayoutManager(linearLayoutManager);
        list_LST_players.setAdapter(playerAdapter);
        playerAdapter.setPlayerCallback((player, position) -> {
            if(mapCallback!= null)
                mapCallback.showLocationOnMap(player.getLat(),player.getLng());
        });
    }

}
