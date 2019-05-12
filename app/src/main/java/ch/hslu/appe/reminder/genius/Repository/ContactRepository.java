package ch.hslu.appe.reminder.genius.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Dao.ContactDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public Contact singleContact;

    public ContactRepository(Application application) {
        ReminderGeniusRoomDb db = ReminderGeniusRoomDb.getDatabase(application);
        this.contactDao = db.contactDao();
        this.allContacts = this.contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return this.allContacts;
    }

    public void insert (Contact contact) {
        new insertAsyncTask(this.contactDao).execute(contact);
    }

    public void deleteAllContacts() { new deleteAllContactsAsyncTask(this.contactDao).execute(); }

    public void delete(Contact contact) {
        new deleteAsyncTask(contactDao).execute(contact);
    }

    public LiveData<Contact> getContactById(int id) { return this.contactDao.findContactById(id); }

    public Contact getSingleContactByIdSync(int id) {
        return this.contactDao.findSingleContactById(id);
    }

    private static class deleteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao mAsyncTaskDao;

        deleteAsyncTask(ContactDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            this.mAsyncTaskDao.deleteContacts(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao mAsyncTaskDao;

        insertAsyncTask(ContactDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            this.mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ContactDao mAsyncTaskDao;

        deleteAllContactsAsyncTask(ContactDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            this.mAsyncTaskDao.deleteAllContacts();
            return null;
        }
    }
}