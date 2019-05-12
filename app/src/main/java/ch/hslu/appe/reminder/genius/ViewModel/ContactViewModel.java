package ch.hslu.appe.reminder.genius.ViewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.Repository.ContactRepository;

/*
The ViewModel does provide data to the UI while surviving configuration changes.
It acts as a communication center between the Repository and the UI.

SRP: Activity draws UI, ViewModel holds the data.
 */
public class ContactViewModel extends AndroidViewModel {

    private ContactRepository repository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel(Application application) {
        super(application);
        this.repository = new ContactRepository(application);
        this.allContacts = this.repository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() { return this.allContacts; }

    public void insert(Contact contact) { this.repository.insert(contact); }

    public void delete(Contact contact) {
        repository.delete(contact);
    }

    public LiveData<Contact> getContactById(int id) { return repository.getContactById(id); }

    public Contact getSingleContactByIdSync(int id) { return repository.getSingleContactByIdSync(id); }

    public void deleteAllContacts() {
        Log.i("ContactViewModel", "Deleting all Contacts from DB!");
        this.repository.deleteAllContacts();
    }
}
