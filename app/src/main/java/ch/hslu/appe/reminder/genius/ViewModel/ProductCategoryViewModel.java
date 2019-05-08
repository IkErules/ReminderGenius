package ch.hslu.appe.reminder.genius.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.Repository.ProductCategoryRepository;


/*
The ViewModel does provide data to the UI while surviving configuration changes.
It acts as a communication center between the Repository and the UI.

SRP: Activity draws UI, ViewModel holds the data.
 */
public class ProductCategoryViewModel extends AndroidViewModel {

    private ProductCategoryRepository repository;
    private LiveData<List<ProductCategory>> allProductCategories;

    public ProductCategoryViewModel(Application application) {
        super(application);
        this.repository = new ProductCategoryRepository(application);
        this.allProductCategories = this.repository.getAllProductCategories();
    }

    public LiveData<List<ProductCategory>> getAllProductsCategory() { return this.allProductCategories; }

    public void insert(ProductCategory productCategory) { this.repository.insert(productCategory); }

    public LiveData<ProductCategory> getProductCategoryById(int id) { return this.repository.getProductCategoryById(id); }

    public void deleteAllProductCategories() {
        Log.i("ProductCategoryViewModel", "Deleting all ProductCategories from DB!");
        this.repository.deleteAllProductCategories();
    }
}
