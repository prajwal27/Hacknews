package com.example.prajw.hacknews;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prajw.hacknews.FavouriteActivity;
import com.example.prajw.hacknews.MainActivity;
import com.example.prajw.hacknews.R;
import com.example.prajw.hacknews.Story;

import java.util.ArrayList;
import java.util.List;

class RecyclerViewPageAdapter extends RecyclerView.Adapter<RecyclerViewPageAdapter.MyViewHolder> {
    private List<Story> stories = new ArrayList<Story>();
    private Context context;
    private static final int ALPHA_ANIMATION_TIME = 200;
    private int identifier;
    private MainActivity activity;
    private FavActivity favActivity;

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }


    public RecyclerViewPageAdapter(Context context, int identifier /*, List<Story> stories*/) {
        if (identifier == 1) {
            favActivity = (FavActivity) context;
        } else {
            activity = (MainActivity) context;
        }
        this.identifier = identifier;

        this.context = context;
    }

    public void addStory(Story story) {
        stories.add(story);
        notifyItemInserted(stories.size() - 1);
    }

    public void removeStory(int i){
        stories.remove(i);
        notifyItemRemoved(i);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_cardview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override                          ////////////////////final or not
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.by_vh.setText(stories.get(position).getBy());
        holder.title_vh.setText(stories.get(position).getTitle());
        holder.likes_vh.setText(String.valueOf(stories.get(position).getScore()) + " Likes");
        holder.comments_vh.setText(String.valueOf(stories.get(position).getDescendants()) + " Comments");
        holder.time_vh.setText(String.valueOf(stories.get(position).getTime()));

        if (identifier == 0) {
            if (activity.favourites.contains(stories.get(holder.getAdapterPosition()).getId()))
                holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                holder.heart.setImageResource(R.drawable.border_fav_story);
        } else {
                //holder.heart.setVisibility(View.INVISIBLE);
            holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);
        }/*else {
            if (favActivity.favourites.contains(stories.get(holder.getAdapterPosition()).getId()))
                holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                holder.heart.setImageResource(R.drawable.border_fav_story);
        }*/

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To prevent double clicks
                //v.setClickable(false);
                //holder.itemView.setClickable(false);
                if (identifier == 0) {
                    if (activity.favourites.contains(stories.get(holder.getAdapterPosition()).getId())) {

                        activity.fav.remove(new GsonHelper(context).getGson().toJson(stories.get(holder.getAdapterPosition())));
                        activity.favourites.remove(stories.get(holder.getAdapterPosition()).getId());
                        holder.heart.setImageResource(R.drawable.border_fav_story);
                    } else {
                        activity.fav.add(new GsonHelper(context).getGson().toJson(stories.get(holder.getAdapterPosition())));
                        activity.favourites.add(stories.get(holder.getAdapterPosition()).getId());
                        holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }else{
                    favActivity.favID.remove(stories.get(holder.getAdapterPosition()).getId());
                    favActivity.fav.remove(new GsonHelper(context).getGson().toJson(stories.get(holder.getAdapterPosition())));
                    removeStory(holder.getAdapterPosition());
                }
            }
        });
        holder.detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, WebActivityMain.class).putExtra("url", stories.get(holder.getAdapterPosition()).getUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.stories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView heart;
        TextView by_vh, title_vh, url_vh, likes_vh, comments_vh, time_vh;
        ConstraintLayout detailed;

        public MyViewHolder(View itemView) {
            super(itemView);
            by_vh = itemView.findViewById(R.id.by);
            title_vh = itemView.findViewById(R.id.title);
            likes_vh = itemView.findViewById(R.id.noOfLikes);
            comments_vh = itemView.findViewById(R.id.noOfComments);
            time_vh = itemView.findViewById(R.id.date);
            detailed = itemView.findViewById(R.id.constrainLayout);
            heart = itemView.findViewById(R.id.likes);
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
