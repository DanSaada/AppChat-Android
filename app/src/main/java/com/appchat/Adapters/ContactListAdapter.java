package com.appchat.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appchat.AppStateManager;
import com.appchat.R;
import com.appchat.entities.Contact;
import com.appchat.entities.converters.Base64TypeConverter;

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
            holder.contactName.setText(current.getDisplayName());
            holder.lastMsg.setText(current.getContent());
            holder.sentTime.setText(current.getCreated());
//            holder.unreadCount.setText(current.getUnreadCount()); TODO: implement unread count
            String base64Image = current.getProfilePic();
            Bitmap bitmap = Base64TypeConverter.convertBase64ToBitmap(base64Image);


            // Set the click listener for the item
            holder.itemView.setOnClickListener(view -> {

                // pass relevant data to the ChatActivity
                Context context = view.getContext();
                Intent intent = new Intent(context, com.appchat.activities.ChatActivity.class);
                intent.putExtra("chatID", current.getId());
                intent.putExtra("displayName", current.getDisplayName());
                intent.putExtra("profilePic", current.getProfilePic());
                // set the current contactId with whom the user is chatting
                AppStateManager.contactId = current.getUsername();
                context.startActivity(intent);
            });
            if (bitmap != null) {
                holder.contactImage.setImageBitmap(bitmap);
            } else {
                // Set a default image if conversion fails
                holder.contactImage.setImageResource(R.drawable.cat);
            }
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

    private Bitmap convertBase64ToBitmap(String base64String) {
        if (base64String == null) {
            return null;
        }

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


}
