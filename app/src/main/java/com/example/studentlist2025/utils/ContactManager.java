package com.example.studentlist2025.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.studentlist2025.models.Contact;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private static final String PREF_NAME = "contacts";
    private static final String KEY_CONTACTS = "contact_list";
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;

    public ContactManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void addContact(Contact contact) {
        List<Contact> contacts = getAllContacts();
        contacts.add(contact);
        saveContacts(contacts);
    }

    public List<Contact> getAllContacts() {
        String json = prefs.getString(KEY_CONTACTS, "[]");
        Type type = new TypeToken<List<Contact>>(){}.getType();
        List<Contact> contacts = gson.fromJson(json, type);
        return contacts != null ? contacts : new ArrayList<>();
    }

    public List<Contact> getFavoriteContacts() {
        List<Contact> allContacts = getAllContacts();
        List<Contact> favorites = new ArrayList<>();
        for (Contact contact : allContacts) {
            if (contact.isFavorite()) {
                favorites.add(contact);
            }
        }
        return favorites;
    }

    public void updateContact(Contact contact) {
        List<Contact> contacts = getAllContacts();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId().equals(contact.getId())) {
                contacts.set(i, contact);
                break;
            }
        }
        saveContacts(contacts);
    }

    public void deleteContact(String contactId) {
        List<Contact> contacts = getAllContacts();
        contacts.removeIf(contact -> contact.getId().equals(contactId));
        saveContacts(contacts);
    }

    private void saveContacts(List<Contact> contacts) {
        String json = gson.toJson(contacts);
        prefs.edit().putString(KEY_CONTACTS, json).apply();
    }

    public String[] getContactCategories() {
        return new String[]{
                "Family",
                "Friends",
                "Work",
                "Emergency",
                "Service Providers",
                "Healthcare",
                "Business",
                "Other"
        };
    }
}
