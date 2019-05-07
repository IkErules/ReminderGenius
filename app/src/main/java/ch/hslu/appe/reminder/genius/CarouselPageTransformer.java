package ch.hslu.appe.reminder.genius;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class CarouselPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        float scaleFactor = 0.5f;
        float rotationFactor = 20;

        if (position < 0) {
            page.setRotationY(rotationFactor * -position);
            float scale = 1 + scaleFactor * position;
            page.setScaleX(scale);
            page.setScaleY(scale);

        } else {
            page.setRotationY(rotationFactor * -position);
            float scale = 1 - scaleFactor * position;
            page.setScaleX(scale);
            page.setScaleY(scale);
        }
    }
}