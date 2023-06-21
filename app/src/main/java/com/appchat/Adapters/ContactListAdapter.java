package com.appchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appchat.R;
import com.appchat.entities.Contact;

import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder> {
    class ContactListViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactName;
        private final TextView lastMsg;
        private final TextView sentTime;
        private final TextView unreadCount;
        private final ImageView contactImage;

        private ContactListViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.h4);
            lastMsg = itemView.findViewById(R.id.message);
            sentTime = itemView.findViewById(R.id.time);
            unreadCount = itemView.findViewById(R.id.unreadCount);
            contactImage = itemView.findViewById(R.id.imgCover);
        }

    }

    private final LayoutInflater mInflater;
    private List<Contact> contacts; // Cached copy of contacts

    public ContactListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ContactListViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.chat_list_item, parent, false);
        return new ContactListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactListViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            holder.contactName.setText(current.getName());
            holder.lastMsg.setText(current.getLastMsg());
            holder.sentTime.setText(current.getSentTime());
            //TODO: set unreadCount in all contacts classes
//            holder.unreadCount.setText(current.getUnreadCount());
            holder.contactImage.setImageResource(current.getContactImage());
        }
    }

    public void setContacts(List<Contact> contacts){
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contacts != null)
            return contacts.size();
        else return 0;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
