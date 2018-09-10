package com.eusecom.samshopersung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.eusecom.samshopersung.models.Product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import dagger.android.AndroidInjection;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

/**
 * Created by Shaon on 12/3/2016.
 * by http://shaoniiuc.com/android/image-upload-retrofit-library/
 */


public class SetImageActivity extends AppCompatActivity {

    @Inject
    ShopperIMvvmViewModel mViewModel;

    @Inject
    SetImageAdapter mAdapter;

    Button btnUpload, btnPickImage;
    String mediaPath;
    ImageView imgView;
    String[] mediaColumns = { MediaStore.Video.Media._ID };
    ProgressDialog progressDialog;

    private CompositeSubscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setimage_activity);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        btnUpload = (Button) findViewById(R.id.upload);
        btnPickImage = (Button) findViewById(R.id.pick_img);
        imgView = (ImageView) findViewById(R.id.preview);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxUploadFile();
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

        List<Product> prods = new ArrayList<Product>();
        Product prod = new Product();
        prod.setUid(1);
        prod.setName("name 1");
        prods.add(prod);
        mAdapter.setDataToAdapter(prods);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unBind();
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void unBind() {

        mViewModel.clearUploadImageToServe();
        mSubscription.clear();
    }

    private void bind() {

        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getUploadImageToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::imageUploaded));

    }

    private void imageUploaded(@NonNull final SetImageServerResponse serverResponse) {

        if (serverResponse != null) {
            if (serverResponse.getSuccess()) {
                Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
            }
        } else {
            assert serverResponse != null;
            Log.v("Response", serverResponse.toString());
        }

        progressDialog.dismiss();
    }

    private void rxUploadFile() {

        progressDialog.show();
        mViewModel.emitUploadImageToServer(mediaPath);
    }

}