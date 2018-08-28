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

 class RecyclerViewPageAdapter extends RecyclerView.Adapter<RecyclerViewPageAdapter.MyViewHolder>{
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

    public RecyclerViewPageAdapter(Context context, int identifier /*, List<Story> stories*/){
        //this.stories = stories;
        if(identifier !=0) {
          favouriteActivity = (FavouriteActivity) context;
        }else{
        activity = (MainActivity)context;}
        this.context = context;

    }

    public void addStory(Story story) {
        /*if (identifier == SELECTED && student.isSelected() && students.contains(student))
            return;
        Log.d(TAG, "" + identifier);*/
        stories.add(story);
        notifyItemInserted(stories.size() - 1);
    }

    public void removeStory(Story story) {
        for (int i = 0; i < favourites.size(); i++) {
            if (favourites.get(i).getId() == story.getId()) {
                favourites.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setList(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Story> favourites) {
        this.favourites = favourites;
    }

    public List<Story> getList() {
        return stories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_cardview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override                          ////////////////////final or not
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.by_vh.setText(stories.get(position).getBy());
        holder.title_vh.setText(stories.get(position).getTitle());
        holder.likes_vh.setText(String.valueOf(stories.get(position).getScore())+" Likes");
        holder.comments_vh.setText(String.valueOf(stories.get(position).getDescendants())+" Comments");
        holder.time_vh.setText(String.valueOf(stories.get(position).getTime()));

        if(identifier ==0) {
            if (activity.favourites.contains(stories.get(holder.getAdapterPosition()).getId()))
                holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                holder.heart.setImageResource(R.drawable.border_fav_story);
        }else{
            if (favouriteActivity.favourites.contains(stories.get(holder.getAdapterPosition()).getId()))
                holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);
            else
                holder.heart.setImageResource(R.drawable.border_fav_story);
        }

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To prevent double clicks
                //v.setClickable(false);
                //holder.itemView.setClickable(false);
                if(identifier !=1){
                if (activity.favourites.contains(stories.get(holder.getAdapterPosition()).getId())) {

                    activity.favourites.remove(stories.get(holder.getAdapterPosition()).getId());
                    //favourites.add(stories.get(holder.getAdapterPosition()));
                    //stories.get(holder.getAdapterPosition()).setFavourite(0);
                    holder.heart.setImageResource(R.drawable.border_fav_story);
                    /*if(identifier==0){
                        activity.topAdapter.notifyDataSetChanged();
                    }else if(identifier == 1){
                        activity.recentAdapter.notifyDataSetChanged();
                    }else{
                        activity.bestAdapter.notifyDataSetChanged();
                    }*/

                   //   favouriteActivity.favouriteAdapter.addStory(stories.get(holder.getAdapterPosition()));
                    // **** activity.removeStory(stories.get(holder.getAdapterPosition()));
                    //  activity.mSelectedAdapter
                    //        .removeStudent(students.get(holder.getAdapterPosition()));
                    //if (identifier == ALL)
                    //  activity.mAllAdapter.notifyItemChanged(holder.getAdapterPosition());
                    //else
                    //  activity.mAllAdapter.notifyDataSetChanged();
                } else {
                    //favourites.remove(stories.get(holder.getAdapterPosition()));
                    activity.favourites.add(stories.get(holder.getAdapterPosition()).getId());
                    //stories.get(holder.getAdapterPosition()).setFavourite(1);
                    ////  *** activity..addStory(stories.get(holder.getAdapterPosition()));
                    holder.heart.setImageResource(R.drawable.ic_favorite_black_24dp);

                    /*if(identifier == 0){
                        activity.topAdapter.notifyDataSetChanged();
                    }else if(identifier == 1){
                        activity.recentAdapter.notifyDataSetChanged();
                    }else{
                        activity.bestAdapter.notifyDataSetChanged();
                    }*/
                    //favouriteActivity.favouriteAdapter.removeStory(stories.get(holder.getAdapterPosition()));
                    //  activity.mUnselectedAdapter
                    //         .removeStudent(students.get(holder.getAdapterPosition()));
                    //if (identifier == ALL)
                    //  activity.mAllAdapter.notifyItemChanged(holder.getAdapterPosition());
                    //else
                    //  activity.mAllAdapter.notifyDataSetChanged();
                }
            }}
        });

        holder.detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context,WebActivity.class).putExtra("url",stories.get(holder.getAdapterPosition()).getUrl()));
            }
        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.stories.size();
    }


     public static class MyViewHolder extends RecyclerView.ViewHolder{
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
