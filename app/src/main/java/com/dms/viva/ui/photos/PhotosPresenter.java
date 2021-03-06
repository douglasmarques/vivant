package com.dms.viva.ui.photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.dms.viva.core.PhotoPost;
import com.dms.viva.data.remote.RemoteService;
import com.dms.viva.data.repository.PhotoRepository;
import com.dms.viva.data.repository.UserRepositories;
import com.dms.viva.util.CapturePhotoUtils;
import com.dms.viva.util.EspressoIdlingResource;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link PhotosActivity}), retrieves the data and updates
 * the UI as required.
 */
public class PhotosPresenter implements PhotosContract.UserActionsListener {

    @NonNull
    private final PhotosContract.View view;

    @NonNull
    private final PhotoRepository repository;

    PhotosPresenter(@NonNull PhotosContract.View view,
                    @NonNull PhotoRepository repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void showPhotos() {
        view.showProgress(true);
        EspressoIdlingResource.increment();
        repository.getPhotos(new PhotoRepository.GetPhotosCallback() {
            @Override
            public void onResultSuccess(List<PhotoPost> photos) {
                EspressoIdlingResource.decrement();
                view.showProgress(false);
                view.showPhotos(photos);
            }

            @Override
            public void onResultFail(int error) {
                EspressoIdlingResource.decrement();
                view.showProgress(false);
                view.showErrors();
            }
        });
    }

    @Override
    public void clickPhotoDetails(PhotoPost photo) {
        view.showPhotoDetails(photo);
    }

    @Override
    public void longClickPhoto(PhotoPost photo) {
        view.showSavePhotoAlert(photo);
    }

    @Override
    public void clickLogout() {
        view.showLogoutAlert();
    }

    @Override
    public void confirmLogout(Context context) {
        UserRepositories.getInMemoryRepoInstance(context).logout();
        view.showLoginScreen();
    }

    @Override
    public void confirmSavePhoto(final Context context, final PhotoPost photo) {
        AsyncTask<PhotoPost, Void, Bitmap> task = new AsyncTask<PhotoPost, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(PhotoPost... params) {
                PhotoPost photo = params[0];
                Uri uri = Uri.parse(RemoteService.PHOTO_ENDPOINT + photo.getPhotoFileName());
                try {
                    return Picasso.with(context).load(uri).get();
                } catch (IOException e) {
                    view.showErrors();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                super.onPostExecute(image);
                CapturePhotoUtils.insertImage(
                        context.getContentResolver(),
                        image,
                        photo.getPhotoFileName(),
                        photo.getCaption());
                view.showSuccessSavePhoto();
            }
        };
        task.execute(photo);
    }
}
