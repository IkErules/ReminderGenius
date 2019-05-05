package ch.hslu.appe.reminder.genius.DB.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "installation",
        foreignKeys = {
        @ForeignKey(entity = ProductCategory.class,
                parentColumns = "productCategoryId",
                childColumns = "productCategoryId",
                onDelete = NO_ACTION),
        @ForeignKey(entity = Contact.class,
                parentColumns = "contactId",
                childColumns = "contactId",
                onDelete = NO_ACTION)
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
    private LocalDate  installationDate;
    private LocalDate expireDate;
    private int serviceInterval;
    private String notes;
    private boolean notifyCustomerMail;
    private boolean notifyCustomerSms;
    private boolean notifyCreatorMail;
    private boolean notifyCreatorSms;

    public Installation(int productCategoryId,
                        int contactId,
                        String productDetails,
                        LocalDate installationDate,
                        LocalDate expireDate,
                        int serviceInterval,
                        String notes,
                        boolean notifyCustomerMail,
                        boolean notifyCustomerSms,
                        boolean notifyCreatorMail,
                        boolean notifyCreatorSms) {

        this.productCategoryId = productCategoryId;
        this.contactId = contactId;
        this.productDetails = productDetails;
        this.installationDate = installationDate;
        this.expireDate = expireDate;
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
    public LocalDate getInstallationDate() {
        return installationDate;
    }
    public LocalDate getExpireDate() {
        return expireDate;
    }
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

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
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

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Installation{" +
                "installationId=" + installationId +
                ", productCategoryId=" + productCategoryId +
                ", contactId=" + contactId +
                ", productDetails='" + productDetails + '\'' +
                ", installationDate=" + installationDate +
                ", expireDate=" + expireDate +
                ", serviceInterval=" + serviceInterval +
                ", notes='" + notes + '\'' +
                ", notifyCustomerMail=" + notifyCustomerMail +
                ", notifyCustomerSms=" + notifyCustomerSms +
                ", notifyCreatorMail=" + notifyCreatorMail +
                ", notifyCreatorSms=" + notifyCreatorSms +
                '}';
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
        this.installationDate = (LocalDate) in.readSerializable();
        this.expireDate = (LocalDate) in.readSerializable();
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
        dest.writeSerializable(this.getInstallationDate());
        dest.writeSerializable(this.getExpireDate());
        dest.writeInt(this.getServiceInterval());
        dest.writeString(this.getNotes());
        dest.writeByte((byte) (this.getNotifyCustomerMail() ? 1 : 0));
        dest.writeByte((byte) (this.getNotifyCustomerSms() ? 1 : 0));
        dest.writeByte((byte) (this.getNotifyCreatorMail() ? 1 : 0));
        dest.writeByte((byte) (this.getNotifyCreatorSms() ? 1 : 0));
    }

    public static class Builder {
        private int productCategoryId;
        private int contactId;
        private String productDetails;
        private LocalDate installationDate;
        private LocalDate expireDate;
        private int serviceInterval;
        private String notes;
        private boolean notifyCustomerMail;
        private boolean notifyCustomerSms;
        private boolean notifyCreatorMail;
        private boolean notifyCreatorSms;

        public Installation build() {
            return new Installation(productCategoryId, contactId, productDetails, installationDate,
                    expireDate, serviceInterval, notes, notifyCustomerMail, notifyCustomerSms,
                    notifyCreatorMail, notifyCreatorSms);
        }

        public Installation defaultInstallation() {
            return new Installation(-1, -1, "", LocalDate.now(), LocalDate.now().plusYears(2), 2,
                    "", false, false, false, false);
        }

        public Builder setProductCategoryId(int productCategoryId) {
            this.productCategoryId = productCategoryId;
            return this;
        }

        public Builder setContactId(int contactId) {
            this.contactId = contactId;
            return this;
        }

        public Builder setProductDetails(String productDetails) {
            this.productDetails = productDetails;
            return this;
        }

        public Builder setInstallationDate(LocalDate installationDate) {
            this.installationDate = installationDate;
            return this;
        }

        public Builder setExpireDate(LocalDate expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public Builder setServiceInterval(int serviceInterval) {
            this.serviceInterval = serviceInterval;
            return this;
        }

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder setNotifyCustomerMail(boolean notifyCustomerMail) {
            this.notifyCustomerMail = notifyCustomerMail;
            return this;
        }

        public Builder setNotifyCustomerSms(boolean notifyCustomerSms) {
            this.notifyCustomerSms = notifyCustomerSms;
            return this;
        }

        public Builder setNotifyCreatorMail(boolean notifyCreatorMail) {
            this.notifyCreatorMail = notifyCreatorMail;
            return this;
        }

        public Builder setNotifyCreatorSms(boolean notifyCreatorSms) {
            this.notifyCreatorSms = notifyCreatorSms;
            return this;
        }
    }
}
