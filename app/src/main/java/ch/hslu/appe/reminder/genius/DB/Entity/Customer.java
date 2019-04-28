package ch.hslu.appe.reminder.genius.DB.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "customer_table")
public class Customer {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "customerId")
    private int customerId;
    @ColumnInfo(name = "firstName")
    private String firstName;
    @ColumnInfo(name = "lastName")
    private String lastName;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "mail")
    private String mail;
    @ColumnInfo(name = "street")
    private String street;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "zip")
    private int zip;

    public Customer(@NonNull String firstName,
                    @NonNull String lastName,
                    String phone,
                    String mail,
                    String street,
                    String city,
                    int zip) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.mail = mail;
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getPhone(){return this.phone;}
    public String getMail(){return this.mail;}
    public String getStreet(){return this.street;}
    public String getCity(){return this.city;}
    public int getZip(){return this.zip;}
}
