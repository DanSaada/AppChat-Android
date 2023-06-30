package com.appchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.R;
import com.appchat.entities.Message;

import java.util.List;

public class ChatMessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messages;
    private Context context;

    public ChatMessagesListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if (viewType == VIEW_TYPE_SENT) {
            // Inflate sent message layout
            view = inflater.inflate(R.layout.chat_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED) {
            // Inflate received message layout
            view = inflater.inflate(R.layout.chat_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }

        // Return a default ViewHolder if the viewType is unknown
        view = inflater.inflate(R.layout.chat_sent_message, parent, false);
        return new SentMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder instanceof SentMessageViewHolder) {
            SentMessageViewHolder sentHolder = (SentMessageViewHolder) holder;
            // Bind sent message data to SentMessageViewHolder
            sentHolder.sentMessageText.setText(message.getContent());
            sentHolder.messageTime.setText(message.getCreated());
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ReceivedMessageViewHolder receivedHolder = (ReceivedMessageViewHolder) holder;
            // Bind received message data to ReceivedMessageViewHolder
            receivedHolder.receivedMessageText.setText(message.getContent());
            receivedHolder.messageTime.setText(message.getCreated());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        // TODO: check that works
        if (message.isSentByLoggedUser()) {
            return VIEW_TYPE_RECEIVED;
        } else  {
            return VIEW_TYPE_SENT;
        }

    }

    @Override
    public int getItemCount() {
        if (messages == null) {
            return 0;
        }
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    // ViewHolder for sent messages
    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView sentMessageText;
        TextView messageTime;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessageText = itemView.findViewById(R.id.sentMessageText);
            messageTime = itemView.findViewById(R.id.messageTime1);
        }
    }

    // ViewHolder for received messages
    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView receivedMessageText;
        TextView messageTime;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessageText = itemView.findViewById(R.id.receivedMessageText);
            messageTime = itemView.findViewById(R.id.messageTime1);
        }
    }
}
