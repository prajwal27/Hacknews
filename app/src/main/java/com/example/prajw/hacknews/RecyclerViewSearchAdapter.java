package com.example.prajw.hacknews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewSearchAdapter extends RecyclerView.Adapter<RecyclerViewSearchAdapter.MyViewHolder> {
    private static final String TAG = "ListViewAdapter";
    private List<Story> stories = new ArrayList<Story>();
    private List<Story> favourites = new ArrayList<Story>();
    private Context context;
    private static final int ALPHA_ANIMATION_TIME = 200;
    final static int RECENT = 1;
    final static int BEST = 2;
    final static int TOP = 0;
    private int identifier;
    private MainActivity activity;
    private FavouriteActivity favouriteActivity;
    private ArrayList<Long> searchID;

    public RecyclerViewSearchAdapter(Context context, ArrayList<Long> searchID /*, List<Story> stories*/){
        this.context = context;
        this.searchID = searchID;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public RecyclerViewSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.search_card, parent, false);
        RecyclerViewSearchAdapter.MyViewHolder myViewHolder = new RecyclerViewSearchAdapter.MyViewHolder(view);
        return myViewHolder;

    }

    @Override                          ////////////////////final or not
    public void onBindViewHolder(@NonNull final RecyclerViewSearchAdapter.MyViewHolder holder, int position) {
        holder.id.setText(String.valueOf(searchID.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return this.searchID.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView heart;
        TextView by_vh, title_vh, url_vh, likes_vh, comments_vh, time_vh,id;

        ConstraintLayout detailed;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.submitted);
        }
    }

    void setAnimation(View itemView) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(250);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        itemView.setAnimation(animationSet);
        itemView.animate();
    }
}

