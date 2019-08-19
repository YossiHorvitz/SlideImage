package com.user.slideimage;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageActivity extends AppCompatActivity {

    private float xCoordinate, yCoordinate;
    private double screenCenterX, screenCenterY;
    private int alpha;
    static final int DURATION = 255;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(0xff000000);
        getWindow().setNavigationBarColor(0xff000000);

        RelativeLayout view = new RelativeLayout(this);
        view.setBackgroundColor(0xff000000);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.image);
        imageView.setTransitionName("image");
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        view.addView(imageView);
        setContentView(view);

        view.getBackground().setAlpha(255);

        // calculate max hypo value and center of screen.
        final DisplayMetrics display = getResources().getDisplayMetrics();
        screenCenterX = (double) display.widthPixels / 2;
        screenCenterY = (double) (display.heightPixels - getStatusBarHeight()) / 2;
        final double maxHypo = Math.hypot(screenCenterX, screenCenterY);

        imageView.setOnTouchListener((v, event) -> {
            double hypo = calculateHypo(imageView);

            // change alpha view background
            alpha = (int) (hypo * 255) / (int) maxHypo;
            if (alpha < 255)
                view.getBackground().setAlpha(255 - alpha);

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    xCoordinate = imageView.getX() - event.getRawX();
                    yCoordinate = imageView.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    imageView.animate().x(0 /* event.getRawX() + xCoordinate */).y(event.getRawY() + yCoordinate).setDuration(0).start();
                    break;
                case MotionEvent.ACTION_UP:
                    if (alpha > 85) {
                        supportFinishAfterTransition();
                        return false;
                    } else {
                        imageView.animate()
                                .x(0)
                                .y((float) screenCenterY - (float) imageView.getHeight() / 2)
                                .setDuration(DURATION)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();

                        ObjectAnimator animator = ObjectAnimator.ofInt(view.getBackground(), "alpha", DURATION - alpha, 255);
                        animator.setDuration(DURATION).setInterpolator(new DecelerateInterpolator());
                        animator.start();
                    }
                default:
                    return false;
            }
            return true;
        });
    }

    /**
     * Calculate hypo value of {@code ImageView} position according to center
     *
     * @param imageView image
     */
    double calculateHypo(ImageView imageView) {
        double centerYPosition = imageView.getY() + (double) (imageView.getHeight() / 2);
        double centerXPosition = imageView.getX() + (double) (imageView.getWidth() / 2);
        double x = screenCenterX - centerXPosition;
        double y = screenCenterY - centerYPosition;
        return Math.hypot(x, y);
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }
}