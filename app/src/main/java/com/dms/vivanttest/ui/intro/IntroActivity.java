package com.dms.vivanttest.ui.intro;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.view.Window;
import android.view.WindowManager;

import com.dms.vivanttest.R;
import com.dms.vivanttest.ui.login.LoginActivity;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @ColorInt int white = ContextCompat.getColor(this, R.color.white);
        @ColorInt int gray = ContextCompat.getColor(this, R.color.secondary_text_color);

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_photo_list),
                getString(R.string.intro_photo_list_desc),
                R.drawable.tutorial_1, white, gray, gray));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_photo_details),
                getString(R.string.intro_photo_details_desc),
                R.drawable.tutorial_2, white, gray, gray));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_favourite_photo),
                getString(R.string.intro_favourite_photo_desc),
                R.drawable.tutorial_3, white, gray, gray));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, LoginActivity.class));
    }
}

