package com.lularoe.erinfetz.imagecapture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import com.google.common.base.Strings;
import com.lularoe.erinfetz.imagecapture.storage.StorageService;
import com.lularoe.erinfetz.imagecapture.storage.StoredImageFile;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //TODO Handle Mashup Mode

    private Products products;
    private InventoryImageMaker imageMaker;
    private StorageService storage;

    private Spinner productStyleSpinner;
    private Spinner productSizeSpinner;
    private Button takePictureButton;
    private ImageView mImageView;
    private CheckBox mashupCheckBox;
    private ConstraintLayout imageLayout;
    private ConstraintLayout mainLayout;

    private String currentProductStyle=null;
    private String currentProductSize=null;
    private StoredImageFile mCurrentFile;

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

        imageMaker = new InventoryImageMaker(this);
        storage = new StorageService(this);

        FloatingActionButton deletePictureButton = (FloatingActionButton) findViewById(R.id.deletePicture);
        FloatingActionButton acceptPictureButton = (FloatingActionButton) findViewById(R.id.acceptPicture);
        mImageView = (ImageView) findViewById(R.id.imageView1);
        productStyleSpinner = (Spinner) findViewById(R.id.spinner1);
        productSizeSpinner = (Spinner) findViewById(R.id.spinner2);

        products = new Products(getResources());

        takePictureButton = (Button) findViewById(R.id.takePicture);
        setBtnListenerOrDisable(
                takePictureButton,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        mashupCheckBox = (CheckBox) findViewById(R.id.mashupCheckBox);

        productStyleSpinner.setOnItemSelectedListener(mProductStyleListener);
        productSizeSpinner.setOnItemSelectedListener(mProductSizeListener);
        deletePictureButton.setOnClickListener(mDeleteClickListener);
        acceptPictureButton.setOnClickListener(mAcceptClickListener);
        productSizeSpinner.setClickable(false);
        //takePictureButton.setClickable(false);

        imageLayout = (ConstraintLayout) findViewById(R.id.imageLayout);
        imageLayout.setVisibility(View.INVISIBLE);

        mainLayout= (ConstraintLayout) findViewById(R.id.mainLayout1);

//        if(savedInstanceState!=null) {
//            setCurrentProductStyle(savedInstanceState.getString(PRODUCT_STYLE_STORAGE_KEY));
//            setCurrentProductSize(savedInstanceState.getString(PRODUCT_SIZE_STORAGE_KEY));
//        }

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

    private void checkPermissions(final String perm, final int code, final String message){
        if (ContextCompat.checkSelfPermission(this,
                perm)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    perm)) {
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
            return;
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
       int tpp = requestCode;
        if(tpp==STORAGE_PERM) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Snackbar.make(mainLayout, "Storage Permissions Granted.", Snackbar.LENGTH_SHORT).show();

            } else {
                Snackbar.make(mainLayout, "No Permissions", Snackbar.LENGTH_SHORT).show();
            }
        }else if (tpp == CAMERA_PERM){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Snackbar.make(mainLayout, "Camera Permissions Granted.", Snackbar.LENGTH_SHORT).show();

            }else{
                Snackbar.make(mainLayout, "No Permissions", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }


    private void dispatchTakePictureIntent2() {
        try {
            mCurrentFile = null;
            mImageView.setImageBitmap(null);
            imageLayout.setVisibility(View.INVISIBLE);

            Intent takePictureIntent = new Intent(CameraActivity.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                StoredImageFile photoFile = null;
                try {
                    photoFile = storage.createImageFile(Environment.DIRECTORY_PICTURES,
                            getString(R.string.album_name),
                            products.shortName(currentProductStyle),
                            currentProductSize);
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCurrentFile = photoFile;
                    Log.v(TAG, mCurrentFile.getUri().toString());
                    takePictureIntent.putExtra(CameraActivity.INTENT_DATA_FILE, mCurrentFile.getFile().getAbsolutePath());
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }else{
                Log.e(TAG, "No Activity Found");
            }
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
            Snackbar.make(mainLayout, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

            takePictureButton.setClickable(true);
        }
    }
    private void dispatchTakePictureIntent() {
        try {
            mCurrentFile = null;
            mImageView.setImageBitmap(null);
            imageLayout.setVisibility(View.INVISIBLE);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                StoredImageFile photoFile = null;
                try {
                    photoFile = storage.createImageFile(Environment.DIRECTORY_PICTURES,
                            getString(R.string.album_name),
                            products.shortName(currentProductStyle),
                            currentProductSize);
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCurrentFile = photoFile;
                    Log.v(TAG, mCurrentFile.getUri().toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentFile.getUri());
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
            Snackbar.make(mainLayout, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

            takePictureButton.setClickable(true);
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
            //MediaScannerUtils.scanMediaIntentBroadcast(this, mCurrentFile);
            MediaScannerUtils.scanMediaBroadcast(this, mCurrentFile);
            //mCurrentPhotoPath = null;

            takePictureButton.setClickable(true);
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
                imageLayout.setVisibility(View.VISIBLE);
            }
        }catch(Throwable e){
            Log.e(TAG, e.getMessage(),e);
        }
    }

    private void acceptCurrentPicture(){
        mCurrentFile=null;
        mImageView.setImageBitmap(null);
        imageLayout.setVisibility(View.INVISIBLE);
    }

    private void deleteCurrentPicture(){
        if (mCurrentFile != null) {
            File f = mCurrentFile.getFile();
            if(f.exists()){
                if(f.delete()){
                    Snackbar.make(mainLayout, "Previous file deleted.", Snackbar.LENGTH_SHORT).show();
                    MediaScannerUtils.scanMediaBroadcast(this, mCurrentFile);
                }else{
                    Snackbar.make(mainLayout, "File unable to be deleted! " +
                            mCurrentFile.getFile().getAbsolutePath(), Snackbar.LENGTH_SHORT).show();
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

    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePictureButton.setClickable(false);
                    dispatchTakePictureIntent();
                    //dispatchTakePictureIntent2();
                }
            };

    Button.OnClickListener mDeleteClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCurrentPicture();
                }
            };


    Button.OnClickListener mAcceptClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptCurrentPicture();
                }
            };

    private void setBtnListenerOrDisable(Button btn, Button.OnClickListener onClickListener, String intentName) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setClickable(false);
        }
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
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
        if (id == R.id.action_mashup) {
            return true;
        }
        if (id == R.id.action_outfit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
