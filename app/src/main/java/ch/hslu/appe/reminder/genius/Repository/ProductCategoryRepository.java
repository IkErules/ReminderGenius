package ch.hslu.appe.reminder.genius.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Dao.ProductCategoryDao;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;

public class ProductCategoryRepository {

    private ProductCategoryDao productCategoryDao;
    private LiveData<List<ProductCategory>> allProductCategories;

    public ProductCategoryRepository(Application application) {
        ReminderGeniusRoomDb db = ReminderGeniusRoomDb.getDatabase(application);
        this.productCategoryDao = db.productCategoryDao();
        this.allProductCategories = this.productCategoryDao.getAllProductCategories();
    }

    public LiveData<List<ProductCategory>> getAllProductCategories() {
        return this.allProductCategories;
    }

    public void insert (ProductCategory productCategory) {
        new insertAsyncTask(this.productCategoryDao).execute(productCategory);
    }

    public LiveData<ProductCategory> getProductCategoryById(int id) { return this.productCategoryDao.findProductCategoryById(id); }

    public void deleteAllProductCategories() { new deleteAllProductCategoriesAsyncTask(this.productCategoryDao).execute(); }

    private static class insertAsyncTask extends AsyncTask<ProductCategory, Void, Void> {

        private ProductCategoryDao mAsyncTaskDao;

        insertAsyncTask(ProductCategoryDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ProductCategory... params) {
            this.mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllProductCategoriesAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductCategoryDao mAsyncTaskDao;

        deleteAllProductCategoriesAsyncTask(ProductCategoryDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            this.mAsyncTaskDao.deleteAllProductCategories();
            return null;
        }
    }
}