package ch.hslu.appe.reminder.genius.DB.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "installation",
        foreignKeys = {
        @ForeignKey(entity = ProductCategory.class,
                parentColumns = "productCategoryId",
                childColumns = "productCategoryId",
                onDelete = CASCADE),
        @ForeignKey(entity = Contact.class,
                parentColumns = "contactId",
                childColumns = "contactId",
                onDelete = CASCADE)
        })
public class Installation implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int installationId;
    @NonNull
    private int productCategoryId;
    @NonNull
    private int contactId;
    private String productDetails;
    private int installDateYear;
    private int installDateMonth;
    private int installDateDay;
    private int serviceInterval;
    private String notes;
    private boolean notifyCustomerMail;
    private boolean notifyCustomerSms;
    private boolean notifyCreatorMail;
    private boolean notifyCreatorSms;

    public Installation(int productCategoryId,
                        int contactId,
                        String productDetails,
                        int installDateYear,
                        int installDateMonth,
                        int installDateDay,
                        int serviceInterval,
                        String notes,
                        boolean notifyCustomerMail,
                        boolean notifyCustomerSms,
                        boolean notifyCreatorMail,
                        boolean notifyCreatorSms) {

        this.productCategoryId = productCategoryId;
        this.contactId = contactId;
        this.productDetails = productDetails;
        this.installDateYear = installDateYear;
        this.installDateMonth = installDateMonth;
        this.installDateDay = installDateDay;
        this.serviceInterval = serviceInterval;
        this.notes = notes;
        this.notifyCustomerMail = notifyCustomerMail;
        this.notifyCustomerSms = notifyCustomerSms;
        this.notifyCreatorMail = notifyCreatorMail;
        this.notifyCreatorSms = notifyCreatorSms;
    }

    public int getInstallationId(){return this.installationId;}
    public int getProductCategoryId() {return this.productCategoryId;}
    public int getContactId(){return this.contactId;}
    public String getProductDetails(){return this.productDetails;}
    public int getInstallDateYear(){return this.installDateYear;}
    public int getInstallDateMonth(){return this.installDateMonth;}
    public int getInstallDateDay(){return this.installDateDay;}
    public int getServiceInterval(){return this.serviceInterval;}
    public String getNotes(){return this.notes;}
    public boolean getNotifyCustomerMail(){return this.notifyCustomerMail;}
    public boolean getNotifyCustomerSms(){return this.notifyCustomerSms;}
    public boolean getNotifyCreatorMail(){return this.notifyCreatorMail;}
    public boolean getNotifyCreatorSms(){return this.notifyCreatorSms;}

    public void setInstallationId(int installationId) {
        this.installationId = installationId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public void setInstallDateYear(int installDateYear) {
        this.installDateYear = installDateYear;
    }

    public void setInstallDateMonth(int installDateMonth) {
        this.installDateMonth = installDateMonth;
    }

    public void setInstallDateDay(int installDateDay) {
        this.installDateDay = installDateDay;
    }

    public void setServiceInterval(int serviceInterval) {
        this.serviceInterval = serviceInterval;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setNotifyCustomerMail(boolean notifyCustomerMail) {
        this.notifyCustomerMail = notifyCustomerMail;
    }

    public void setNotifyCustomerSms(boolean notifyCustomerSms) {
        this.notifyCustomerSms = notifyCustomerSms;
    }

    public void setNotifyCreatorMail(boolean notifyCreatorMail) {
        this.notifyCreatorMail = notifyCreatorMail;
    }

    public void setNotifyCreatorSms(boolean notifyCreatorSms) {
        this.notifyCreatorSms = notifyCreatorSms;
    }

    @Override
    public String toString() {
        return String.format("Installation[installationId: %s, productCategoryId: %s, contactId: %s, productDetails: %s, installDateYear: %s, installDateMonth: %s, installDateDay: %s, serviceInterval: %s, notes: %s, notifyCustomerMail: %s, notifyCustomerSms: %s, notifyCreatorMail: %s, notifyCreatorSms: %s]",
                this.getInstallationId(), this.getProductCategoryId(), this.getContactId(), this.getProductDetails(), this.getInstallDateYear(), this.getInstallDateMonth(),
                this.getInstallDateDay(), this.getServiceInterval(), this.getNotes(), this.getNotifyCustomerMail(), this.getNotifyCustomerSms(), this.getNotifyCreatorMail(),
                this.getNotifyCreatorSms());
    }

    public static final Creator<Installation> CREATOR = new Creator<Installation>() {
        @Override
        public Installation createFromParcel(Parcel in) {
            return new Installation(in);
        }

        @Override
        public Installation[] newArray(int size) {
            return new Installation[size];
        }
    };

    // Convert Parcel to Contact --> Deserialize
    protected Installation(Parcel in) {
        this.installationId = in.readInt();
        this.productCategoryId = in.readInt();
        this.contactId = in.readInt();
        this.productDetails = in.readString();
        this.installDateYear = in.readInt();
        this.installDateMonth = in.readInt();
        this.installDateDay = in.readInt();
        this.serviceInterval = in.readInt();
        this.notes = in.readString();
        this.notifyCustomerMail = in.readByte() != 0;
        this.notifyCustomerSms = in.readByte() != 0;
        this.notifyCreatorMail = in.readByte() != 0;
        this.notifyCreatorSms = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize Contact to Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getInstallationId());
        dest.writeInt(this.getProductCategoryId());
        dest.writeInt(this.getContactId());
        dest.writeString(this.getProductDetails());
        dest.writeInt(this.getInstallDateYear());
        dest.writeInt(this.getInstallDateMonth());
        dest.writeInt(this.getInstallDateDay());
        dest.writeInt(this.getServiceInterval());
        dest.writeString(this.getNotes());
        dest.writeByte((byte) (this.getNotifyCustomerMail() ? 1 : 0));
        dest.writeByte((byte) (this.getNotifyCustomerSms() ? 1 : 0));
        dest.writeByte((byte) (this.getNotifyCreatorMail() ? 1 : 0));
        dest.writeByte((byte) (this.getNotifyCreatorSms() ? 1 : 0));
    }
}
