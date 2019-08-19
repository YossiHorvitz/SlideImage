package com.user.slideimage;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.material.appbar.AppBarLayout;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createView());
        getWindow().setBackgroundDrawable(new ColorDrawable(0xffffffff));
        getWindow().setStatusBarColor(0xff37474f);
        getWindow().setNavigationBarColor(0xff000000);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(MainActivity.this, imageView, "image");
            startActivity(intent, options.toBundle());
        });
    }

    /**
     * create content view
     *
     * @return CoordinatorLayout as main layout
     */
    private CoordinatorLayout createView() {
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(this);

        Toolbar toolbar = new Toolbar(this);
        toolbar.getContext().setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);
        toolbar.setBackgroundColor(0xff607d8b);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
        AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        toolbarLayoutParams.setScrollFlags(0);

        AppBarLayout appBarLayout = new AppBarLayout(this);
        appBarLayout.setBackgroundColor(0xff607d8b);
        appBarLayout.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
        appBarLayout.addView(toolbar);

        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, dpToPixels(150));
        imageView = new ImageView(this);
        imageView.setTransitionName("image");
        imageView.setImageResource(R.drawable.image);
        imageView.setLayoutParams(layoutParams);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) imageView.getLayoutParams();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        imageView.requestLayout();

        coordinatorLayout.addView(appBarLayout);
        coordinatorLayout.addView(imageView);

        return coordinatorLayout;
    }

    public int dpToPixels(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}