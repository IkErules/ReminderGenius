package ch.hslu.appe.reminder.genius;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Dao.CustomerDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Customer;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;

public class ReminderGeniusRepository {

    private CustomerDao customerDao;
    private LiveData<List<Customer>> allCustomers;

    ReminderGeniusRepository(Application application) {
        ReminderGeniusRoomDb db = ReminderGeniusRoomDb.getDatabase(application);
        customerDao = db.customerDao();
        allCustomers = customerDao.getAllCustomers();
    }

    LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public void insert (Customer customer) {
        new insertAsyncTask(customerDao).execute(customer);
    }

    private static class insertAsyncTask extends AsyncTask<Customer, Void, Void> {

        private CustomerDao mAsyncTaskDao;

        insertAsyncTask(CustomerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Customer... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}