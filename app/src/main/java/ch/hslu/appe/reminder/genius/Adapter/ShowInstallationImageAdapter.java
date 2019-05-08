package ch.hslu.appe.reminder.genius.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ch.hslu.appe.reminder.genius.Activity.ImageFullScreenActivity;
import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationImageViewModel;

public class ShowInstallationImageAdapter extends RecyclerView.Adapter<ShowInstallationImageAdapter.ShowInstallationImageViewHolder> {

    private final LayoutInflater mInflater;

    private List<Image> images; // Cached copy of images

    private Activity context;
    private InstallationImageViewModel installationImageViewModel;

    public ShowInstallationImageAdapter(Activity context, InstallationImageViewModel installationImageViewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.installationImageViewModel = installationImageViewModel;
    }

    @Override
    public ShowInstallationImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.show_installation_image_list_recycler_view_item, parent,false);
        return new ShowInstallationImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShowInstallationImageViewHolder holder, int position) {
        if (images != null) {
            Image current = images.get(position);

            holder.imageView.setImageBitmap(current.loadImageFromStorage());

            // TODO: Open an Image in FullScreen
            holder.parentLayout.setOnClickListener(view -> {
                Image image = this.images.get(position);
                Log.d("ShowInstallationImageListAdapter", "Opening Image: " + image.toString());

                Intent fullScreenViewIntent = new Intent(context, ImageFullScreenActivity.class);
                fullScreenViewIntent.putExtra(ImageFullScreenActivity.SHOW_IMAGE_FULLSCREEN, image);
                context.startActivity(fullScreenViewIntent);
            });
        } else {
            // Covers the case of data not being ready yet.
            Log.w("ShowInstallationImageListAdapter", "No Images found!");
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(this.context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Context getContext() {
        return context;
    }

    public void setImages(List<Image> images){
        this.images = images;
        notifyDataSetChanged();
    }


    // getItemCount() is called many times, and when it is first called,
    // images has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (this.images != null)
            return this.images.size();
        else return 0;
    }

    class ShowInstallationImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final RelativeLayout parentLayout;

        private ShowInstallationImageViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.show_installation_image_imgview);
            this.parentLayout = itemView.findViewById(R.id.show_installation_parent_layout);
        }
    }
}