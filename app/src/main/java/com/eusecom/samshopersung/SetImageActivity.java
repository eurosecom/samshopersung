package com.eusecom.samshopersung;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


public class SetImageActivity extends BaseActivity {

    @Inject
    ShopperIMvvmViewModel mViewModel;

    @Inject
    SetImageAdapter mAdapter;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    IShopperModelsFactory mModelsFactory;

    @Inject
    Picasso mPicasso;

    @Inject
    public ImageUrl mImageUrl;

    Button btnUpload, btnPickImage, btnClearCache;
    String mediaPath;
    ImageView imgView;
    LinearLayout eanlay;
    EditText eanx;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private CompositeSubscription mSubscription;

    int whatactivity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setimage_activity);

        Intent i = getIntent();
        //0=set image, 1=set EAN
        Bundle extras = i.getExtras();
        whatactivity = extras.getInt("whatactivity");

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnUpload = (Button) findViewById(R.id.upload);
        btnPickImage = (Button) findViewById(R.id.pick_img);
        btnClearCache = (Button) findViewById(R.id.clearcache);
        imgView = (ImageView) findViewById(R.id.preview);
        eanlay = (LinearLayout) findViewById(R.id.eanlay);
        eanx = (EditText) findViewById(R.id.eanx);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (whatactivity == 0) {
            eanlay.setVisibility(View.GONE);
            String imgsize = "";
            btnUpload.setText(getString(R.string.imagetoserver, imgsize));
            btnPickImage.setText(getString(R.string.imagegallery));
            fab.setVisibility(View.GONE);
        } else {
            imgView.setVisibility(View.GONE);
            btnUpload.setText(getString(R.string.eantoserver));
            btnPickImage.setText(getString(R.string.eanscan));
            btnClearCache.setVisibility(View.GONE);
            fab.setOnClickListener(v -> {

                        IntentIntegrator integrator = new IntentIntegrator(SetImageActivity.this);
                        integrator.initiateScan();


                    }
            );
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatactivity == 0) {
                    rxUploadFile();
                }else{
                    String edidok = mSharedPreferences.getString("edidok", "");
                    mViewModel.emitSaveEanToServer(eanx.getText().toString() + "/" + edidok);
                }

            }
        });

        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                deleteCache(SetImageActivity.this);
                hideProgressBar();
                Toast.makeText(SetImageActivity.this, getString(R.string.cachecleared), Toast.LENGTH_LONG).show();
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (whatactivity == 0) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 0);
                }else{
                    IntentIntegrator integrator = new IntentIntegrator(SetImageActivity.this);
                    integrator.initiateScan();
                }

            }
        });

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( whatactivity == 0 ){

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
                    Log.v("SetImageActivityLog", "mediaPath " + mediaPath);

                    imageSize(mediaPath);

                    // Set the Image in ImageView for Previewing the Media
                    Bitmap bitmapImage = BitmapFactory.decodeFile(mediaPath);
                    imgView.setImageBitmap(bitmapImage);
                    cursor.close();
                    int width = imgView.getWidth();
                    int height = imgView.getHeight();
                    Log.v("SetImageActivityLog ", "width x height " + width + "x" + height);


                } else {
                    Toast.makeText(this, getString(R.string.pickednot), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.somewrong), Toast.LENGTH_LONG).show();
            }

        }else{


            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null) {
                String re = scanResult.getContents();
                eanx.setText(re);
            }


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
        mViewModel.clearSaveEanToServer();
        mSubscription.clear();
    }

    private void bind() {

        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getUploadImageToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::imageUploaded));

        mSubscription.add(mViewModel.getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setServerProducts));

        mSubscription.add(mViewModel.getObservableSaveEanToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setSavedEan));

        String edidok = mSharedPreferences.getString("edidok", "");
        Log.d("SetImageActivityLog ", edidok);
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {
            showProgressBar();
            emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));
        }

    }

    private void setSavedEan(List<ProductKt> products) {

        //mAdapter.setDataToAdapter(products);
        hideProgressBar();
        Toast.makeText(getApplicationContext(), getString(R.string.eansaved), Toast.LENGTH_SHORT).show();
        String edidok = mSharedPreferences.getString("edidok", "");
        Log.d("SetImageActivityLog ", "saved " + products.get(0).getNat());
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {
            showProgressBar();
            emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));
        }
    }

    private void setServerProducts(List<ProductKt> products) {

        mAdapter.setDataToAdapter(products);
        hideProgressBar();
        //Log.d("SetImageActivityLog ", products.get(0).getNat());
    }

    private void emitMyQueryProductsFromSqlServer(String query) {
        showProgressBar();
        mViewModel.emitMyQueryProductsFromSqlServer(query);
    }

    private void imageUploaded(@NonNull final SetImageServerResponse serverResponse) {

        hideProgressBar();
        if (serverResponse != null) {
            if (serverResponse.getSuccess()) {
                Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));
            } else {
                Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            assert serverResponse != null;
            Log.v("Response", serverResponse.toString());
        }


    }

    private void imageSize(String mediaPath) {

        File f = null;
        String path = mediaPath;
        try {
            f = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("SetImageActivityLog File size in bytes " + f.length());
        double imgsd = f.length() / 1000;
        String imgsize = String.valueOf(imgsd) + " kB";
        btnUpload.setText(getString(R.string.imagetoserver, imgsize));
    }

    private void rxUploadFile() {

        if (mediaPath != null) {

            String edidok = mSharedPreferences.getString("edidok", "");
            ProductKt prod = mModelsFactory.getProductKt();
            prod.setCis(edidok);
            prod.setPrm1(mediaPath);

            showProgressBar();
            mViewModel.emitUploadImageToServer(prod);

        } else {
            Toast.makeText(this, getString(R.string.imagegallery), Toast.LENGTH_LONG).show();
        }
    }

    public static void deleteCache(Context context) {

        try {
            File dir = context.getCacheDir();
            Log.d("SetImageActivityLog ", dir.toString());
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                //Log.d("SetImageActivityLog ", children[i].toString());
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void compresBitmap() {

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "/eusecom/qrcode.jpg";
        File myFile = new File(baseDir + File.separator + fileName);

        FileOutputStream out = null;
        Bitmap bitmap = null;
        try {
            out = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}