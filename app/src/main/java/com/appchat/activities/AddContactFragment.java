package com.appchat.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.appchat.R;
import com.appchat.entities.Contact;
import com.appchat.viewModels.ContactsViewModel;

public class AddContactFragment extends Fragment {

    private ContactsViewModel contactsViewModel;

    public static AddContactFragment newInstance(ContactsViewModel contactsViewModel) {
        AddContactFragment fragment = new AddContactFragment();
        Bundle args = new Bundle();
        args.putSerializable("contactsViewModel", contactsViewModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            contactsViewModel = (ContactsViewModel) getArguments().getSerializable("contactsViewModel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);

        EditText usernameEditText = rootView.findViewById(R.id.usernameEditText);
        Button addContactBtn = rootView.findViewById(R.id.addButton);

        addContactBtn.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            addContact(username);
        });

        return rootView;
    }

    private void addContact(String username) {
        Contact contact = contactsViewModel.get(username).getValue();
        if (contact != null) {
            contactsViewModel.add(contact);
            requireActivity().finish();
        } else {
            Toast.makeText(requireContext(), "Username not found", Toast.LENGTH_SHORT).show();
        }
    }
}
