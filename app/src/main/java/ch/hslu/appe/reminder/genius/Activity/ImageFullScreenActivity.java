package ch.hslu.appe.reminder.genius.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ImageViewModel;

public class ImageFullScreenActivity extends AppCompatActivity {

    public static final String SHOW_IMAGE_FULLSCREEN = "image.to.show";

    private Image img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        Intent caller = getIntent();
        this.img = caller.getParcelableExtra(SHOW_IMAGE_FULLSCREEN);
        Log.d("ImageFullScreenActivity", "Got Image to display");

        this.showImage();

    }

    private void showImage() {
        ImageView imgView = (ImageView) findViewById(R.id.image_full_screen_imageview);
        imgView.setImageBitmap(this.img.loadImageFromStorage());
    }
}
