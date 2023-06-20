package com.appchat.repositories;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import com.appchat.AppStateManager;
import com.appchat.api.MessageApi;
import com.appchat.db.AppDB;
import com.appchat.db.dao.ContactDao;
import com.appchat.db.dao.MessageDao;
import com.appchat.entities.Message;
import java.util.List;

public class MessageRepository {
    private MessageApi messageApi;
    private ContactDao contactDao;
    private MessageDao messageDao;
    private MessageListData messagesListData;
    private  String token;

    public MessageRepository() {
        AppDB appDB = Room.databaseBuilder(AppStateManager.context, AppDB.class, AppStateManager.loggedUser)
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        this.messageDao = appDB.messageDao();
        this.contactDao = appDB.contactDao();
        this.messageApi = new MessageApi(this.messageDao);
        this.messagesListData = new MessageListData();
        this.token = "Bearer " + AppStateManager.loggerUserToken;
    }

    /*
    This class intends to keep the messages data up-to-date by fetching the latest messages
    from the server when there are active observers.
* */
    class MessageListData extends MutableLiveData<List<Message>> {
        public MessageListData() {
            super();
            // initialize chat's messages from local database
            setValue(messageDao.getChatMessages(AppStateManager.loggedUser, AppStateManager.contactId));
        }

        @Override
        protected void onActive() {
            super.onActive();
            // load messages from server API
            setMessagesOfLocalDatabaseWithServerApiMessages();
        }
    }

    public void setMessagesOfLocalDatabaseWithServerApiMessages() {
        this.messageApi.getAllMessages(this.messagesListData, this.token , AppStateManager.contactId);
    }

    protected void setMessagesListDataWithDbMessages() {
        new Thread(() -> {
            messagesListData.postValue(messageDao
                    .getChatMessages(AppStateManager.loggedUser, AppStateManager.contactId));
        }).start();
    }

    public LiveData<List<Message>> get() {
        return messagesListData;
    }

    public void add(String message) {
        messageApi.addMessage(this.messagesListData, this.token, AppStateManager.contactId, message);
    }

    public void reload() {
        new GetMessagesTask(messagesListData, messageDao).execute();
    }

    public class GetMessagesTask extends AsyncTask<Void, Void, Void> {
        private MutableLiveData<List<Message>> messagesListData;
        private MessageDao messageDao;

        public GetMessagesTask(MutableLiveData<List<Message>> messagesListData, MessageDao messageDao) {
            this.messageDao = messageDao;
            this.messagesListData = messagesListData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}