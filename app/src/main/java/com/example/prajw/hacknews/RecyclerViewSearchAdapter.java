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
    //private List<Integer> comment_list = new ArrayList<Integer>();
    private Context context;
    private static final int ALPHA_ANIMATION_TIME = 200;
    final static int RECENT = 1;
    final static int BEST = 2;
    final static int TOP = 0;
    private int identifier;
    private MainActivity activity;
    private FavouriteActivity favouriteActivity;
    private ArrayList<Long> searchID;

    public RecyclerViewSearchAdapter(Context context  /*, List<Story> stories*/){
        this.context = context;
        //this.searchID = searchID;
        //notifyDataSetChanged();

    }

    public void addStory(Story story) {
        /*if (identifier == SELECTED && student.isSelected() && students.contains(student))
            return;
        Log.d(TAG, "" + identifier);*/
        stories.add(story);
        notifyItemInserted(stories.size() - 1);
    }


    @NonNull
    @Override
    public RecyclerViewSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_cardview, parent, false);
        RecyclerViewSearchAdapter.MyViewHolder myViewHolder = new RecyclerViewSearchAdapter.MyViewHolder(view);
        return myViewHolder;

    }

    @Override                          ////////////////////final or not
    public void onBindViewHolder(@NonNull final RecyclerViewSearchAdapter.MyViewHolder holder, int position) {
        //holder.id.setText(String.valueOf(searchID.get(holder.getAdapterPosition())));
        holder.by_vh.setText(stories.get(position).getBy());
        holder.title_vh.setText(stories.get(position).getTitle());
        holder.likes_vh.setText(String.valueOf(stories.get(position).getScore())+" Likes");
        holder.comments_vh.setText(String.valueOf(stories.get(position).getDescendants())+" Comments");
        holder.time_vh.setText(String.valueOf(stories.get(position).getTime()));
        holder.heart.setVisibility(View.GONE);
       /* holder.detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("url",stories.get(holder.getAdapterPosition()).getUrl());
                //intent.putExtra("comment_ids",stories.get(holder.getAdapterPosition()).getCommentList());
                v.getContext().startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return this.stories.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView heart;
        TextView by_vh, title_vh, url_vh, likes_vh, comments_vh, time_vh,id;
        ConstraintLayout detailed;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.submitted);
            by_vh = itemView.findViewById(R.id.by);
            title_vh = itemView.findViewById(R.id.title);
            likes_vh = itemView.findViewById(R.id.noOfLikes);
            comments_vh = itemView.findViewById(R.id.noOfComments);
            time_vh = itemView.findViewById(R.id.date);
            detailed = itemView.findViewById(R.id.constrainLayout);
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

