package ch.hslu.appe.reminder.genius.DB.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "contact")
public class Contact implements Parcelable {
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

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setStreet(@NonNull String street) {
        this.street = street;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("Contact[contactId: %s, type: %s, firstName: %s, lastName: %s, maidenName: %s, phone: %s, mail: %s, street: %s, city: %s, zip: %s, canton: %s, country: %s ]",
                this.getContactId(), this.getType(), this.getFirstName(), this.getLastName(), this.getMaidenName(), this.getPhone(),
                this.getMail(), this.getStreet(), this.getCity(), this.getZip(), this.getCanton(), this.getContactId());
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    // Convert Parcel to Contact --> Deserialize
    protected Contact(Parcel in) {
        this.contactId = in.readInt();
        this.type = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.maidenName = in.readString();
        this.phone = in.readString();
        this.mail = in.readString();
        this.street = in.readString();
        this.city = in.readString();
        this.zip = in.readInt();
        this.canton = in.readString();
        this.country = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize Contact to Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getContactId());
        dest.writeString(this.getType());
        dest.writeString(this.getFirstName());
        dest.writeString(this.getLastName());
        dest.writeString(this.getMaidenName());
        dest.writeString(this.getPhone());
        dest.writeString(this.getMail());
        dest.writeString(this.getStreet());
        dest.writeString(this.getCity());
        dest.writeInt(this.getZip());
        dest.writeString(this.getCanton());
        dest.writeString(this.getCountry());
    }
}
