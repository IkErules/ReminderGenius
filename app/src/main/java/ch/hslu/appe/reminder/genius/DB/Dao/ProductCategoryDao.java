package ch.hslu.appe.reminder.genius.DB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;

@Dao
public interface ProductCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductCategory productCategory);

    @Insert
    void insertAll(ProductCategory... products);

    @Update
    public void updateProductCategories(ProductCategory... productCategories);

    @Delete
    public void deleteProductCategories(ProductCategory... productCategories);

    @Query("DELETE FROM productcategory")
    void deleteAllProductCategories();

    @Query("SELECT * FROM productcategory WHERE productCategoryId = :id")
    public LiveData<ProductCategory> findProductCategoryById(int id);

    @Query("SELECT * FROM productcategory WHERE productCategoryId = :id")
    public ProductCategory findSingleProductCategoryById(int id);

    @Query("SELECT * FROM productcategory WHERE categoryName LIKE :search " +
            "OR description LIKE :search")
    public LiveData<List<ProductCategory>> findProductCategoriesWithName(String search);

    @Query("SELECT * from productcategory ORDER BY productCategoryId ASC")
    public LiveData<List<ProductCategory>> getAllProductCategories();
}
