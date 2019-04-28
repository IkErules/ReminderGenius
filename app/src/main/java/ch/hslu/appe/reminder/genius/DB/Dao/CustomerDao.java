package ch.hslu.appe.reminder.genius.DB.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Customer;

@Dao
public interface CustomerDao {
    @Insert
    void insert(Customer customer);

    @Update
    public void updateUsers(Customer... customers);

    @Delete
    public void deleteCustomers(Customer... customers);

    @Query("DELETE FROM customer_table")
    void deleteAll();

    @Query("SELECT * FROM customer_table WHERE firstName LIKE :search " +
            "OR lastName LIKE :search")
    public LiveData<List<Customer>> findCustomersWithName(String search);

    @Query("SELECT * from customer_table ORDER BY customerId ASC")
    public LiveData<List<Customer>> getAllCustomers();
}
