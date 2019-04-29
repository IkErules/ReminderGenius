package ch.hslu.appe.reminder.genius.DB.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "contact")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int contactId;
    @NonNull
    private String type;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String maidenName;
    private String phone;
    private String mail;
    @NonNull
    private String street;
    @NonNull
    private String city;
    @NonNull
    private int zip;
    private String canton;
    private String country;

    public Contact(String firstName,
                   String lastName,
                   String type,
                   String maidenName,
                   String phone,
                   String mail,
                   String street,
                   String city,
                   int zip,
                   String canton,
                   String country) {

        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.maidenName = maidenName;
        this.phone = phone;
        this.mail = mail;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.canton = canton;
        this.country = country;
    }

    public int getContactId(){return this.contactId;}
    public String getType() {return this.type;}
    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getMaidenName(){return this.maidenName;}
    public String getPhone(){return this.phone;}
    public String getMail(){return this.mail;}
    public String getStreet(){return this.street;}
    public String getCity(){return this.city;}
    public int getZip(){return this.zip;}
    public String getCanton(){return this.canton;}
    public String getCountry(){return this.country;}
}
