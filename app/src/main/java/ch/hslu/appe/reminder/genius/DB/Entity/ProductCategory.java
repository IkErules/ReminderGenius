package ch.hslu.appe.reminder.genius.DB.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.common.collect.ComparisonChain;

@Entity(tableName = "productcategory")
public class ProductCategory implements Parcelable, Comparable<ProductCategory> {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int productCategoryId;
    @NonNull
    private String categoryName;
    @NonNull
    private int defaultServiceInterval;
    private String description;

    public ProductCategory(String categoryName,
                           int defaultServiceInterval,
                           String description) {

        this.categoryName = categoryName;
        this.defaultServiceInterval = defaultServiceInterval;
        this.description = description;
    }

    public int getProductCategoryId(){return this.productCategoryId;}
    public String getCategoryName() {return this.categoryName;}
    public int getDefaultServiceInterval(){return this.defaultServiceInterval;}
    public String getDescription(){return this.description;}

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDefaultServiceInterval(@NonNull int defaultServiceInterval) {
        this.defaultServiceInterval = defaultServiceInterval;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("ProductCategory[productCategoryId: %s, categoryName: %s, defaultServiceInterval: %s, description: %s]",
                this.getProductCategoryId(), this.getCategoryName(), this.getDefaultServiceInterval(), this.getDescription());
    }

    public static final Creator<ProductCategory> CREATOR = new Creator<ProductCategory>() {
        @Override
        public ProductCategory createFromParcel(Parcel in) {
            return new ProductCategory(in);
        }

        @Override
        public ProductCategory[] newArray(int size) {
            return new ProductCategory[size];
        }
    };

    // Convert Parcel to Contact --> Deserialize
    protected ProductCategory(Parcel in) {
        this.productCategoryId = in.readInt();
        this.categoryName = in.readString();
        this.defaultServiceInterval = in.readInt();
        this.description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize Contact to Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getProductCategoryId());
        dest.writeString(this.getCategoryName());
        dest.writeInt(this.getDefaultServiceInterval());
        dest.writeString(this.getDescription());
    }

    @Override
    public int compareTo(ProductCategory o) {
        return ComparisonChain.start()
                .compare(getCategoryName(), o.getCategoryName())
                .result();
    }
}
