package ch.hslu.appe.reminder.genius.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

public class SearchContact implements Parcelable {
    public String title = "";
    public String content = "";
    public String type = "";
    public String name = "";
    public String firstname = "";
    public String maidenname = "";
    public String street = "";
    public int zip = 0;
    public String city = "";
    public String canton = "";
    public String phone = "";

    public SearchContact() {}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMaidenname() {
        return maidenname;
    }

    public void setMaidenname(String maidenname) {
        this.maidenname = maidenname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setZip(String zip) {
        int parsedZip = 0;
        if (zip != "") {
            try {
                parsedZip = Integer.parseInt(zip);
            } catch (Exception ex) {
                Log.e("SearchContact", String.format("Error parsing zip into int: %s", ex.getMessage()));
            }
        }

        this.zip = parsedZip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "SearchContact{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", maidenname='" + maidenname + '\'' +
                ", street='" + street + '\'' +
                ", zip=" + zip +
                ", city='" + city + '\'' +
                ", canton='" + canton + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public static final Creator<SearchContact> CREATOR = new Creator<SearchContact>() {
        @Override
        public SearchContact createFromParcel(Parcel in) {
            return new SearchContact(in);
        }

        @Override
        public SearchContact[] newArray(int size) {
            return new SearchContact[size];
        }
    };

    public SearchContact(Parcel in) {
        title = in.readString();
        content = in.readString();
        type = in.readString();
        name = in.readString();
        firstname = in.readString();
        maidenname = in.readString();
        street = in.readString();
        zip = in.readInt();
        city = in.readString();
        canton = in.readString();
        phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(firstname);
        dest.writeString(maidenname);
        dest.writeString(street);
        dest.writeInt(zip);
        dest.writeString(city);
        dest.writeString(canton);
        dest.writeString(phone);
    }
}