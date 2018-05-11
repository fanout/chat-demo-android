package com.app.fanout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.fanout.model.ChatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by basim on 4/9/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatModel> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(items.get(position).userName);
        holder.message.setText(items.get(position).message);
        if (items.get(position).isSender()) {
            holder.rightImage.setVisibility(View.VISIBLE);
            holder.leftImage.setVisibility(View.INVISIBLE);
            holder.chatLayout.setBackgroundResource(R.drawable.corner_rounded);
        } else {
            holder.leftImage.setVisibility(View.VISIBLE);
            holder.rightImage.setVisibility(View.INVISIBLE);
            holder.chatLayout.setBackgroundResource(R.drawable.corner_rounded_1);
        }
    }

    public void add(ChatModel item) {
        items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(List<ChatModel> items) {
        if (items != null) {
            Collections.reverse(items);
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView userName;
        private final TextView message;
        private final View leftImage;
        private final View rightImage;
        private final View chatLayout;

        ViewHolder(View itemView) {
            super(itemView);
            chatLayout = itemView.findViewById(R.id.layout_chat);
            userName = (TextView) itemView.findViewById(R.id.username_text);
            message = (TextView) itemView.findViewById(R.id.message_txt);
            leftImage = itemView.findViewById(R.id.left_image);
            rightImage = itemView.findViewById(R.id.right_image);
        }
    }
}
