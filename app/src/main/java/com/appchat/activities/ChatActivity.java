package com.appchat.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ChatMessagesListAdapter;
import com.appchat.R;
import com.appchat.SingletonFirebase;
import com.appchat.entities.Message;
import com.appchat.entities.converters.Base64TypeConverter;
import com.appchat.viewModels.MessageViewModel;
import com.appchat.viewModels.factories.MessageViewModelFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private MessageViewModel messageViewModel;
    private String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MutableLiveData<Message> messagesFirebase = SingletonFirebase.getFirebaseMessageInstance();

        initHeader();
        initRecyclerView();
        initSendMessage();

        messagesFirebase.observe(this, messages -> {
            messageViewModel.add(messages.getContent());
        });

    }

    private void initHeader() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.chatID = extras.getString("chatID");
            String displayName = extras.getString("displayName");
            String profilePic = extras.getString("profilePic");

            CircleImageView userImg = findViewById(R.id.userImg);
            Bitmap bitmapProfilePic = Base64TypeConverter.convertBase64ToBitmap(profilePic);
            if (bitmapProfilePic != null) {
                userImg.setImageBitmap(bitmapProfilePic);
            }

            TextView userName = findViewById(R.id.userName);
            userName.setText(displayName);
        }
        else {
            this.chatID = "0";
        }
    }

    private void initRecyclerView() {
        MessageViewModelFactory factory = new MessageViewModelFactory(this.chatID);
        messageViewModel = new ViewModelProvider(this, factory).get(MessageViewModel.class);

        RecyclerView chatMessagesList = findViewById(R.id.chatRecyclerView);
        final ChatMessagesListAdapter adapter = new ChatMessagesListAdapter(this);
        chatMessagesList.setAdapter(adapter);
        chatMessagesList.setLayoutManager(new LinearLayoutManager(this));

        // TODO: check that works
        messageViewModel.get().observe(this, messages -> {
            // Update the cached copy of the words in the adapter.
            adapter.setMessages(messages);
        });

        adapter.setMessages(messageViewModel.get().getValue());
    }

    private void initSendMessage() {
        ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(v -> {
            TextView messageInput = findViewById(R.id.messageInput);
            String content = messageInput.getText().toString();
            if (!content.isEmpty() && !chatID.equals("0")) { // TODO: check that the comparison chatID != 0 doesnt cause bugs - ask dan if he creats chat with id 0
                messageViewModel.add(content);
                messageInput.setText("");
            }
        });
    }
}