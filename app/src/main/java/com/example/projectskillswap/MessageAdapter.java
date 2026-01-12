package com.example.projectskillswap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> messageList;
    private static final int TYPE_ME = 1;
    private static final int TYPE_OTHER = 2;

    public MessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isMe() ? TYPE_ME : TYPE_OTHER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
            return new MeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
            return new OtherViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof MeViewHolder) {
            ((MeViewHolder) holder).tvMessage.setText(message.getMessage());
        } else {
            ((OtherViewHolder) holder).tvMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        MeViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message_me);
        }
    }

    static class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        OtherViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message_other);
        }
    }
}
