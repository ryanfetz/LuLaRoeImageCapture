package com.lularoe.erinfetz.imagecapture;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.provider.MediaStore;
import android.util.Log;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.common.base.Strings;
import com.lularoe.erinfetz.core.intents.IntentManager;
import com.lularoe.erinfetz.core.ContentValuesBuilder;
import com.lularoe.erinfetz.core.media.MediaStoreManager;
import com.lularoe.erinfetz.core.storage.DefaultStorageUriProvider;
import com.lularoe.erinfetz.core.storage.StorageService;
import com.lularoe.erinfetz.core.storage.dir.ExternalPublicStorageDirectoryProvider;
import com.lularoe.erinfetz.core.storage.dir.InternalStorageDirectoryProvider;
import com.lularoe.erinfetz.core.storage.files.DefaultFileInfoProvider;
import com.lularoe.erinfetz.core.storage.files.FileInfoProvider;
import com.lularoe.erinfetz.core.storage.files.MediaType;
import com.lularoe.erinfetz.core.storage.files.StoredFile;
import com.lularoe.erinfetz.core.storage.perms.DefaultUriPermissionsProvider;
import com.lularoe.erinfetz.core.storage.perms.UriPermissionsProvider;
import com.lularoe.erinfetz.core.ui.SnackbarManager;
import com.lularoe.erinfetz.imagecapture.products.ProductInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //TODO Handle Mashup Mode

    private Products products;
    private InventoryImageMaker imageMaker;
    private StorageService storage;
    private UriPermissionsProvider uriPermissionsProvider;
    private IntentManager intentManager;
    private SnackbarManager snackbarManager;
    private MediaStoreManager mediaStoreManager;

    @BindView(R.id.spinner1) Spinner productStyleSpinner;
    @BindView(R.id.spinner2) Spinner productSizeSpinner;
    @BindView(R.id.imageView1) ImageView mImageView;
    @BindView(R.id.mashupCheckBox) CheckBox mashupCheckBox;
    @BindView(R.id.imageLayout) ConstraintLayout imageLayout;
    @BindView(R.id.takePicture) Button takePictureButton;

    private String currentProductStyle=null;
    private String currentProductSize=null;
    private StoredFile mCurrentFile;

    private static final String PRODUCT_STYLE_STORAGE_KEY = "productStyle";
    private static final String PRODUCT_SIZE_STORAGE_KEY = "productSize";
    private static final String CURRENT_IMAGE_STORAGE_KEY = "currentImage";

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int STORAGE_PERM = 5423;
    static final int CAMERA_PERM = 2354;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        this.imageMaker = new InventoryImageMaker(this);
        if(StorageService.isExternalMediaMounted()) {
            this.storage = new StorageService(this, new ExternalPublicStorageDirectoryProvider(), new DefaultStorageUriProvider(this, getString(R.string.fileProvider)));
        }else{
            this.storage = new StorageService(this, new InternalStorageDirectoryProvider(this), new DefaultStorageUriProvider(this, getString(R.string.fileProvider)));
        }
        this.uriPermissionsProvider=new DefaultUriPermissionsProvider(this);
        this.intentManager = new IntentManager(this);
        this.snackbarManager = new SnackbarManager(findViewById(R.id.mainLayout1), getResources());
        this.mediaStoreManager = new MediaStoreManager(this.getContentResolver());

        this.products = new Products(getResources());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, products.styles());

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productStyleSpinner.setAdapter(dataAdapter);
        productStyleSpinner.setClickable(true);

        productStyleSpinner.setOnItemSelectedListener(mProductStyleListener);
        productSizeSpinner.setOnItemSelectedListener(mProductSizeListener);

        imageLayout.setVisibility(View.INVISIBLE);

        if(!intentManager.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE)){
            takePictureButton.setClickable(false);
        }

        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERM, getString(R.string.message_perm_storage));
        checkPermissions(Manifest.permission.CAMERA, CAMERA_PERM, getString(R.string.message_perm_camera));
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PRODUCT_STYLE_STORAGE_KEY, currentProductStyle );
        outState.putString(PRODUCT_SIZE_STORAGE_KEY, currentProductSize );
        outState.putParcelable(CURRENT_IMAGE_STORAGE_KEY, mCurrentFile);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentProductStyle=savedInstanceState.getString(PRODUCT_STYLE_STORAGE_KEY);
        currentProductSize = savedInstanceState.getString(PRODUCT_SIZE_STORAGE_KEY);
        mCurrentFile = savedInstanceState.getParcelable(CURRENT_IMAGE_STORAGE_KEY);
        setPic();
    }

    static final ButterKnife.Setter<View, Boolean> CLICKABLE = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(@NonNull View view, Boolean value, int index) {
            view.setClickable(value);
        }
    };

    private void checkPermissions(final String perm, final int code, final String message){
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, perm)) {
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] {perm},
                                        code);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {perm},
                    code);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if(requestCode==STORAGE_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                snackbarManager.show(R.string.message_storage_permission_granted);
            } else {
                snackbarManager.show(R.string.message_no_storage_permission);
                disableUI();
            }
        }else if (requestCode == CAMERA_PERM){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                snackbarManager.show(R.string.message_camera_permission_granted);

            }else{
                snackbarManager.show(R.string.message_no_camera_permission);
                disableUI();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void disableUI(){
        takePictureButton.setClickable(false);
        productSizeSpinner.setClickable(false);
        productStyleSpinner.setClickable(false);
    }

    @OnClick(R.id.takePicture)
    void dispatchTakePictureIntent() {
        try {
            ButterKnife.apply(takePictureButton, CLICKABLE, false);
            acceptCurrentPicture();

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                StoredFile photoFile = null;

                try {
                    photoFile = storage.createFile(fileInfoProvider, MediaType.IMAGE_JPEG, Environment.DIRECTORY_PICTURES,getString(R.string.album_name));

                } catch (IllegalAccessException | IOException ex){
                    Log.e(TAG, ex.getMessage(), ex);
                    snackbarManager.indefiniteShow(ex.getMessage());
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCurrentFile = photoFile;
                    uriPermissionsProvider.grantReadWritePermissions(mCurrentFile.getUri(), takePictureIntent);
                    Log.v(TAG, mCurrentFile.getUri().toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentFile.getUri());
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
            snackbarManager.indefiniteShow(e.getMessage());
            ButterKnife.apply(takePictureButton, CLICKABLE, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    handlePhoto(data);
                }
                break;
            }
        }
    }

    private void handlePhoto(Intent data) {
        if (mCurrentFile != null) {

            this.imageMaker.createStandardImage(mCurrentFile, currentProductStyle, currentProductSize);

            setPic();

            mediaStoreManager.insert(mCurrentFile);
            //mCurrentPhotoPath = null;

            ButterKnife.apply(takePictureButton, CLICKABLE, true);
        }
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		try {
            if(mCurrentFile!=null) {
		        /* Decode the JPEG file into a Bitmap */
                Bitmap bitmap = BitmapUtils.loadForView(mImageView, mCurrentFile);

		        /* Associate the Bitmap to the ImageView */
                mImageView.setImageBitmap(bitmap);
                imageLayout.setVisibility(View.VISIBLE);
            }else{

                mImageView.setImageBitmap(null);
                imageLayout.setVisibility(View.INVISIBLE);
            }
        }catch(Throwable e){
            Log.e(TAG, e.getMessage(),e);
            snackbarManager.indefiniteShow(e.getMessage());
        }
    }

    @OnClick(R.id.acceptPicture)
    void acceptCurrentPicture(){
        mCurrentFile=null;
        mImageView.setImageBitmap(null);
        imageLayout.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.deletePicture)
    void deleteCurrentPicture(){
        if (mCurrentFile != null) {
            File f = mCurrentFile.getFile();
            if(f.exists()){
                if(f.delete()){
                    snackbarManager.show(R.string.message_file_deleted);
                    this.mediaStoreManager.delete(mCurrentFile.getFile());
                }else{
                    snackbarManager.longShow(getString(R.string.message_file_not_deleted) + " " + mCurrentFile.getFile().getAbsolutePath());
                }
            }

            mCurrentFile=null;
            mImageView.setImageBitmap(null);
            imageLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void setCurrentProductStyle(String newStyle) {

        currentProductStyle = newStyle;
        currentProductSize = null;

        if (Strings.isNullOrEmpty(newStyle)) {
            productSizeSpinner.setAdapter(null);
            productSizeSpinner.setClickable(false);
            takePictureButton.setClickable(false);
        } else {

            if (products.hasSizes(currentProductStyle)) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, products.sizes(currentProductStyle));

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productSizeSpinner.setAdapter(dataAdapter);
                productSizeSpinner.setClickable(true);
            } else {
                productSizeSpinner.setAdapter(null);
                productSizeSpinner.setClickable(false);
                takePictureButton.setClickable(true);
            }
        }
    }

    private void setCurrentProductSize(String newSize) {
        currentProductSize=newSize;

        if(currentProductSize==null){
            if(currentProductStyle!=null && products.hasSizes(currentProductStyle)) {
                takePictureButton.setClickable(false);
            }
        }else {
            takePictureButton.setClickable(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {
            return true;
        }
        if (id == R.id.action_outfit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean emptySize(String size){
        if(Strings.isNullOrEmpty(size)){
            return true;
        }
        if(size.equals(getString(R.string.size_NA))){
            return true;
        }
        return false;
    }

    FileInfoProvider fileInfoProvider = new DefaultFileInfoProvider(){

        @Override
        public FileInfo provide() {

            Date date = new Date();

            ProductInfo p = products.get(currentProductStyle);

            String timeStamp = dateFormat.format(date);
            String product = p.getName().getShortName();
            String size = currentProductSize;

            String imageFileName = emptySize(size)?
                    String.format("%s_%s", product, timeStamp) :
                    String.format("%s_%s_%s", product, size, timeStamp);

            return new DefaultFileInfo(imageFileName,
                    ContentValuesBuilder.start()
                            .title(p.title(currentProductSize, date))
                            .displayName(p.displayName(currentProductSize, date))
                            .description(p.description(currentProductSize, date))
                            .other("productStyle", p.getName().getName())
                            .other("productSize", currentProductSize)
                            .other("productPrice", p.getPrice().toString())
                            .dateTaken(date));
        }
    };

    AdapterView.OnItemSelectedListener mProductStyleListener=new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

            setCurrentProductStyle(parent.getItemAtPosition(pos).toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            setCurrentProductStyle(null);
        }
    };

    AdapterView.OnItemSelectedListener mProductSizeListener=new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

            setCurrentProductSize(parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            currentProductSize=null;
            setCurrentProductSize(null);
        }
    };
//    Button.OnClickListener mTakePicOnClickListener =
//            new Button.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    takePictureButton.setClickable(false);
//                    dispatchTakePictureIntent();
//                    //dispatchTakePictureIntent2();
//                }
//            };

//    Button.OnClickListener mDeleteClickListener =
//            new Button.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteCurrentPicture();
//                }
//            };
//
//
//    Button.OnClickListener mAcceptClickListener =
//            new Button.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    acceptCurrentPicture();
//                }
//            };

}
