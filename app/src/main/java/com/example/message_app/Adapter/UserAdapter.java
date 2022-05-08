package com.example.message_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.message_app.MessageActivity;
import com.example.message_app.R;
import com.example.message_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<String> userIdList;

    public UserAdapter(Context context, List<String> userIdList) {
        this.context = context;
        this.userIdList = userIdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseReference databaseListfriend = FirebaseDatabase.getInstance().getReference("User").child(userIdList.get(position));
        databaseListfriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                holder.username.setText(user.getUserName());
                if (user.getAvatar().equals("default")) {
                    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(context).load(user.getAvatar()).into(holder.profile_image);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId",userIdList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userIdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            profile_image = view.findViewById(R.id.profile_image);
        }
    }
}
