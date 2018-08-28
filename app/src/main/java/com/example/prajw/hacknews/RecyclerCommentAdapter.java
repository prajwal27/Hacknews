package com.example.prajw.hacknews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> comments = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();

    public RecyclerCommentAdapter(Context context){
        this.context = context;
    }

    public void add(String n, String c){
        comments.add(c);
        names.add(n);
        notifyItemInserted(comments.size() - 1);
    }

    @NonNull
    @Override
    public RecyclerCommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layout_cardview, parent, false);
        RecyclerCommentAdapter.MyViewHolder myViewHolder = new RecyclerCommentAdapter.MyViewHolder(view);
        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCommentAdapter.MyViewHolder holder, int position) {

        holder.name.setText(names.get(holder.getAdapterPosition()));
        holder.comment.setText(names.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, comment;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_commenter);
            comment = itemView.findViewById(R.id.comment_one);
        }
    }
}
