package com.lularoe.erinfetz.imagecapture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Build;
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
import android.widget.LinearLayout;
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
import android.support.v4.content.FileProvider;
import android.support.constraint.ConstraintLayout;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import com.google.common.base.Strings;

public class MainActivity extends AppCompatActivity {

    //TODO Handle Mashup Mode

    private Products products;
    private Spinner productStyleSpinner;
    private Spinner productSizeSpinner;
    private Button takePictureButton;
    private String currentProductStyle=null;
    private String currentProductSize=null;
    private ImageFileInfo mCurrentFile;
    private ImageView mImageView;
    private CheckBox mashupCheckBox;
    private ConstraintLayout imageLayout;
    private InventoryImageMaker imageMaker;

    private static final String PRODUCT_STYLE_STORAGE_KEY = "productStyle";
    private static final String PRODUCT_SIZE_STORAGE_KEY = "productSize";

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

//        if(savedInstanceState!=null) {
//            setCurrentProductStyle(savedInstanceState.getString(PRODUCT_STYLE_STORAGE_KEY));
//            setCurrentProductSize(savedInstanceState.getString(PRODUCT_SIZE_STORAGE_KEY));
//        }

        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERM);
        checkPermissions(Manifest.permission.CAMERA, CAMERA_PERM);
    }

    private void checkPermissions(final String perm, final int code){
        if (ContextCompat.checkSelfPermission(this,
                perm)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    perm)) {
                showMessageOKCancel("You need to allow access to storage",
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
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(this, "Permission Granted For Real", Toast.LENGTH_LONG).show();
//                }
            } else {
                Snackbar.make(findViewById(R.id.mainLayout1), "No Permissions", Snackbar.LENGTH_SHORT).show();
            }
        }else if (tpp == CAMERA_PERM){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();

//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(this, "Permission Granted For Real", Toast.LENGTH_LONG).show();
//                }
            }else{
                Snackbar.make(findViewById(R.id.mainLayout1), "No Permissions", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }
    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PRODUCT_STYLE_STORAGE_KEY, currentProductStyle );
        outState.putString(PRODUCT_SIZE_STORAGE_KEY, currentProductSize );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentProductStyle=savedInstanceState.getString(PRODUCT_STYLE_STORAGE_KEY);
        currentProductSize = savedInstanceState.getString(PRODUCT_SIZE_STORAGE_KEY);
    }

    private ImageFileInfo setUpPhotoFile() throws IOException {

        return StorageUtils.createImageFile(this,
                Environment.DIRECTORY_PICTURES,
                getString(R.string.album_name),
                products.shortName(currentProductStyle),
                currentProductSize);
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
                ImageFileInfo photoFile = null;
                try {
                    photoFile = setUpPhotoFile();
                } catch (IOException ex) {
                    Log.e("LLRIC", ex.getMessage(), ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCurrentFile = photoFile;
                    Uri puri = mCurrentFile.getUri(this);
                    Log.v("LLRIC", puri.toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, puri);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (Throwable e) {
            Log.e("LLRIC", e.getMessage(), e);
            Snackbar.make(findViewById(R.id.mainLayout1), e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();

            takePictureButton.setClickable(true);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File photoFile = mCurrentFile.getFile();
        Uri contentUri = StorageUtils.getFileURI(this, photoFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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

            createStandardImage();
            setPic();
            galleryAddPic();
            //mCurrentPhotoPath = null;

            takePictureButton.setClickable(true);
        }
    }

    private void createStandardImage(){

        FileOutputStream outStream=null;
        try{
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inMutable=true;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentFile.getFile().getAbsolutePath(), bmOptions);

            bitmap = imageMaker.standard(mCurrentFile.getFile().getAbsolutePath(), bitmap, currentProductStyle, currentProductSize);

            File outFile = mCurrentFile.getFile();

            if(outFile.exists()){
                outFile.delete();
            }

            outStream = new FileOutputStream(outFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outStream);

        }catch(IOException e){
            Log.e("LLRIC", e.getMessage(),e);
        }finally{
            try{
                if(outStream!=null){
                    outStream.flush();
                    outStream.close();
                }
            }catch(IOException e1){

                Log.e("LLRIC", e1.getMessage(),e1);
            }
        }
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		try {
		/* Get the size of the ImageView */
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentFile.getFile().getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentFile.getFile().getAbsolutePath(), bmOptions);

		/* Associate the Bitmap to the ImageView */
            mImageView.setImageBitmap(bitmap);
            imageLayout.setVisibility(View.VISIBLE);
        }catch(Throwable e){
            Log.e("LLRIC", e.getMessage(),e);
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
                    Snackbar.make(findViewById(R.id.mainLayout1), "Previous file deleted.", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(findViewById(R.id.mainLayout1), "File unable to be deleted! " +
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
            Snackbar.make(view, String.format("%s - %s", currentProductStyle, products.shortName(currentProductStyle)), Snackbar.LENGTH_SHORT).show();
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
            Snackbar.make(view, currentProductSize, Snackbar.LENGTH_SHORT).show();
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
