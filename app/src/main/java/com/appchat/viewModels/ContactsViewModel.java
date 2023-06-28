package com.appchat.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.appchat.OperationCallback;
import com.appchat.entities.Contact;
import com.appchat.repositories.ContactRepository;
import java.util.List;

public class ContactsViewModel extends ViewModel {
    private ContactRepository contactRepository;
    private LiveData<List<Contact>> contacts;

    public ContactsViewModel() {
        this.contactRepository = new ContactRepository();
        this.contacts = contactRepository.getContacts();
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }

    public void setCallback(OperationCallback callback) {
        contactRepository.setCallback(callback);
    }

    public void add(Contact contact) {
        contactRepository.add(contact);
    }

    public LiveData<Contact> get(String id) { return contactRepository.get(id);}

    public void delete(Contact contact) {
        contactRepository.delete(contact.getId());
    }

    public void reload() {
        contactRepository.reload();
    }

    public void refresh() {
        contactRepository.setContactsOfLocalDatabaseWithServerApiContacts();
    }
}