package com.appchat.repositories;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import com.appchat.AppStateManager;
import com.appchat.OperationCallback;
import com.appchat.api.ContactApi;
import com.appchat.db.AppDB;
import com.appchat.db.dao.ContactDao;
import com.appchat.entities.Contact;
import java.util.List;

public class ContactRepository {
    private ContactApi contactApi;
    private ContactDao contactDao;
    private ContactListData contactListData;
    private  String token;

    public ContactRepository() {
        AppDB appDB = Room.databaseBuilder(AppStateManager.context, AppDB.class, AppStateManager.loggedUser)
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        this.contactDao = appDB.contactDao();
        this.contactListData = new ContactListData();
        this.contactApi = new ContactApi(this.contactDao);
        this.token = "Bearer " + AppStateManager.loggerUserToken;
    }


    /*
        This class intends to keep the contact data up-to-date by fetching the latest contacts
        from the server when there are active observers.
    * */
    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            // initialize contact's list from local database
            setValue(contactDao.getAllContacts());
        }

        @Override
        protected void onActive() {
            super.onActive();
            // load contacts from server API
            setContactsOfLocalDatabaseWithServerApiContacts();
        }
    }

    public void setContactsOfLocalDatabaseWithServerApiContacts() {
        this.contactApi.getAllContacts(this.contactListData, this.token);
    }

    public void setCallback(OperationCallback callback) {
        contactApi.setCallback(callback);
    }

    public MutableLiveData<List<Contact>> getContacts() {
        return contactListData;
    }

    public void add(String contact) {
        this.contactApi.addContact(this.contactListData, contact, this.token);
    }

    // TODO: may cause bugs....... need to be checked
    public LiveData<Contact> get(String id) {
        MutableLiveData<Contact> contactLiveData = new MutableLiveData<>();
        contactApi.getContact(contactLiveData, id, token);
        return contactLiveData;
    }

    public void delete(String id) {
        this.contactApi.deleteContact(this.contactListData, id, this.token);
    }

    public void reload() {
        new GetContactsTask(contactListData, contactDao).execute();
    }

    public class GetContactsTask extends AsyncTask<Void, Void, Void> {
        private MutableLiveData<List<Contact>> contactsListData;
        private ContactDao contactDao;

        public GetContactsTask(MutableLiveData<List<Contact>> contactsListData,
                               ContactDao contactDao) {
            this.contactDao = contactDao;
            this.contactsListData = contactsListData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
