package com.example.shopsmart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import androidx.core.content.FileProvider;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUpload {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final String PREFS_NAME = "ImageUploadPrefs";
    private static final String IMAGE_URI_KEY = "imageUri";

    private Activity activity;
    private ImageView profileImageView;
    private String currentPhotoPath;

    public ImageUpload(Activity activity, ImageView profileImageView) {
        this.activity = activity;
        this.profileImageView = profileImageView;
        loadSavedImage();  // Load saved image when the activity is initialized
    }

    public void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.example.shopsmart.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void handleImageResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Picasso.get()
                    .load(uri)
                    .transform(new CircleTransform())  // Apply circular crop
                    .into(profileImageView);
            saveImageUri(uri.toString());  // Save the selected image URI
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                Uri uri = Uri.fromFile(imgFile);
                Picasso.get()
                        .load(uri)
                        .transform(new CircleTransform())  // Apply circular crop
                        .into(profileImageView);
                saveImageUri(uri.toString());  // Save the captured image URI
            }
        }
    }

    private void saveImageUri(String uri) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(IMAGE_URI_KEY, uri);
        editor.apply();
    }

    private void loadSavedImage() {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUri = prefs.getString(IMAGE_URI_KEY, null);
        if (savedUri != null) {
            Picasso.get()
                    .load(Uri.parse(savedUri))
                    .transform(new CircleTransform())  // Apply circular crop
                    .into(profileImageView);
        }
    }
}
