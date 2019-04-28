package ch.hslu.appe.reminder.genius;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Customer;

public class ReminderGeniusViewModel extends AndroidViewModel {

    private ReminderGeniusRepository repository;

    private LiveData<List<Customer>> allCustomers;

    public ReminderGeniusViewModel (Application application) {
        super(application);
        repository = new ReminderGeniusRepository(application);
        allCustomers = repository.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() { return allCustomers; }

    public void insert(Customer customer) { repository.insert(customer); }
}
