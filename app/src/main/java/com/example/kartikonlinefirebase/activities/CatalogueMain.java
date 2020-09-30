package com.example.kartikonlinefirebase.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kartikonlinefirebase.adapters.ProductAdapter;
import com.example.kartikonlinefirebase.fragments.AddCategoryDialogFragment;
import com.example.kartikonlinefirebase.fragments.EditCatalogueTypeDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.models.Catalogue;
import com.example.kartikonlinefirebase.models.Product;
import com.example.kartikonlinefirebase.utils.Config;
import com.example.kartikonlinefirebase.viewholders.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.kartikonlinefirebase.utils.Config.mStaticCatalogue;

public class CatalogueMain extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST = 11;
    private String Toolbar_CATALOGUE_TEXT = "Edit Title";
    private Toolbar mToolbar;
    private TextView textGallery;
    private TextView textCamera;
    private AlertDialog catTitleDialog;
    private RecyclerView productRecycler;
    private ProductAdapter productAdapter;
    private ViewGroup mEmptyView;
    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 2;
    //captured picture uri
    private Uri picUri;
    Uri photoUri;
    final int PIC_CROP = 8;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mFirebaseFirestoreReference;
    private DocumentReference mProductRef;
    private DatabaseReference mFirebaseDatabaseReference;
    //private EditText catalogueText;
    private Catalogue mCatalogue;
    private List<Catalogue> mCatalogueList;
    private FirebaseRecyclerAdapter<Product, ProductViewHolder> mFirebaseAdapter;
    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private ProductAdapter adapter;
    private Query mQuery;
    private CollectionReference productCollection;
    public CollectionReference catalogueCollection;

    private EditCatalogueTypeDialog editCatalogueTypeDialogFragment;
    private FragmentTransaction ft;

    Intent receivedIntent;
    String catalogueId,catalogueName,catalogueVisitors,catalogueType,catalogoueImageUrl;


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue_main);
        //Views initialisation
        textGallery = (TextView) findViewById(R.id.tv_add_gall);
        textCamera = (TextView) findViewById(R.id.tv_add_cam);
        //catalogueText = (EditText) findViewById(R.id.et_add_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        productRecycler = findViewById(R.id.rv_products);
        mEmptyView = findViewById(R.id.view_empty);

        receivedIntent = getIntent();

        if(receivedIntent != null){
            catalogueId = receivedIntent.getStringExtra("catalogueId");
            catalogueName = receivedIntent.getStringExtra("catalogueName");
            catalogueVisitors = receivedIntent.getStringExtra("catalogueVisitors");
            catalogueType = receivedIntent.getStringExtra("catalogueType");
            catalogoueImageUrl = receivedIntent.getStringExtra("catalogoueImageUrl");

        }

        Toolbar_CATALOGUE_TEXT = catalogueName;
        if(TextUtils.isEmpty(Toolbar_CATALOGUE_TEXT ) || Toolbar_CATALOGUE_TEXT == null){
            Toolbar_CATALOGUE_TEXT = "Edit title";

        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Toolbar_CATALOGUE_TEXT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Firebase instances initialisation
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mCatalogue = new Catalogue();
        mCatalogueList = new ArrayList<>();

        mFirebaseFirestoreReference = mFirestore.collection("products");
        productCollection = mFirestore.collection("products");
        catalogueCollection = mFirestore.collection("catalogues");

        SnapshotParser<Product> parser = new SnapshotParser<Product>() {
            @Override
            public Product parseSnapshot(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                if (product != null) {
                    product.setProductId(dataSnapshot.getKey());
                }
                return product;
            }
        };

        DatabaseReference prodRef = mFirebaseDatabaseReference.child("catalogues");
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(prodRef, parser)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new ProductViewHolder(inflater.inflate(R.layout.products_list_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(ProductViewHolder productViewHolder, int i, Product product) {
            }
        };


        if(TextUtils.isEmpty(catalogueId) || catalogueId == null){


        }else{
            mQuery = mFirestore.collection("products")
                    .whereEqualTo("catalogueId", catalogueId)
                    .orderBy("productName").limit(50);
        }


        /*orderBy("productName").limit(50);*/


        adapter = new ProductAdapter(mQuery){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    productRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                } else {
                    productRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        productRecycler.setAdapter(adapter);



        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, SignInActivity.class));
//            finish();
//            return;
        }
//        else {
//            mUsername = mFirebaseUser.getDisplayName();
//            if (mFirebaseUser.getPhotoUrl() != null) {
//                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
//            }
//        }

        textGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_IMAGE);
            }
        });

        textCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Config.hasPermissions(CatalogueMain.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(CatalogueMain.this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
                } else{
                    takePicture();
                }
            }
        });



    }

    private void takePicture() {

        try {
            //use standard intent to capture an image
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
            //File imageFile = new File(imageFilePath);
            File photoFile = createImageFileWith();
            String path = photoFile.getAbsolutePath();

            photoUri = FileProvider.getUriForFile(CatalogueMain.this,
                    getString(R.string.file_provider_authority),
                    photoFile);

            Config.selectedImageUri = photoUri;
            //picUri = Uri.fromFile(imageFile); // convert path to Uri
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.setClipData(ClipData.newRawUri("", photoUri));
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast.makeText(CatalogueMain.this, errorMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Log.e("TakePicture", ex.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(CatalogueMain.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private File createImageFileWith() throws IOException {
        final String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "JPEG_" + timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                if(data != null){
                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();//evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        List<Uri> imageUriList = new ArrayList<>();
                        for(int i = 0; i < count; i++)
                            imageUriList.add(data.getClipData().getItemAt(i).getUri());
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                } else if(data.getData() != null) {
                    String imagePath = data.getData().getPath();
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
                    Uri uri = data.getData();
                    Config.selectedImageUri = uri;
                    Log.d("CatalogueMain", "Uri: " + uri.toString());
                    //TODO : add firestore logic here
                    /*Product tempProduct = Config.getmStaticProduct();
                    Logger.e("test check"+ tempProduct.getProductName())*/;

                    //DocumentReference documentReference = mFirebaseFirestoreReference.document(userID);


                                        /*StorageReference storageReference = FirebaseStorage.getInstance()
                                                .getReference(mFirebaseUser.getUid())
                                                .child(key)
                                                .child(uri.getLastPathSegment());*/

                                        StorageReference storageReference = FirebaseStorage.getInstance()
                                                .getReference(mFirebaseUser.getUid())
                                                .child("catalogues")
                                                .child(catalogueId)
                                                .child("product-images")
                                                .child(uri.getLastPathSegment());
                                        putImageInStorage(storageReference, uri, "123456789");

                    Intent intent = new Intent(this, EditProductInfoActivity.class);
                    intent.putExtra("imageUri", uri.toString());
                    intent.putExtra("catalogueName", getSupportActionBar().getTitle());
                    intent.putExtra("catalogueId", catalogueId);
                    startActivity(intent);
                    //startActivity(new Intent(this, EditProductInfoActivity.class));
                }
            }

        else if(requestCode == CAMERA_CAPTURE){

            if(resultCode == RESULT_OK) {


                    //get the Uri for the captured image
                    Uri uri = photoUri;
                    Log.d("picUri", uri.toString());
                    //carry out the crop operation
                    //performCrop();
                    Intent intent = new Intent(this, EditProductInfoActivity.class);
                    intent.putExtra("imageUri", uri.toString());
                    intent.putExtra("catalogueId", catalogueId);
                    startActivity(intent);

            }

        }
        else if(requestCode == PIC_CROP){
            //get the returned data
            Bundle extras = data.getExtras();
            //get the cropped bitmap
            Bitmap thePic = (Bitmap) extras.get("data");
        }
    }

    private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(photoUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(CatalogueMain.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(CatalogueMain.this, new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Product product = new Product();
//                                                                        task.getResult().toString()
                                                        mFirebaseDatabaseReference.child("products").child(key).setValue(task.getResult().toString());
                                                    }
                                               }
                                            });
                        } else {
                            Log.w("CatalogueMain", "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalogue_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_edit:

                /*TextInputEditText titleText = findViewById(R.id.et_catalogue_title);
                catTitleDialog = new AlertDialog.Builder(this)
                        .setTitle("Edit Title")
                        .setView(getLayoutInflater().inflate(R.layout.dialogue_edit_title, null))
                        .create();*/

                new MaterialAlertDialogBuilder(this)
                        .setTitle("Title")
                        .setView(R.layout.dialog_add_attributes)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_dialog);
                                Toolbar_CATALOGUE_TEXT = edit.getText().toString();
                                if(!TextUtils.isEmpty(Toolbar_CATALOGUE_TEXT)){
                                    getSupportActionBar().setTitle(Toolbar_CATALOGUE_TEXT);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();



                /*Window window = catTitleDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.x = 0;
                wlp.y = -100;
                window.setAttributes(wlp);

                catTitleDialog.show();*/

//                Button saveTitleButton = findViewById(R.id.btn_save);
//                Button cancelTitleButton = findViewById(R.id.btn_cancel);
//                saveTitleButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(!TextUtils.isEmpty(titleText.getText())){
//                            getSupportActionBar().setTitle(titleText.getText());
//
//                        }
//                    }
//                });
//                catTitleDialog.show();
                break;

            case R.id.item_check:
                Toast.makeText(this, "catalogue saved", Toast.LENGTH_SHORT).show();
                Catalogue mCatalogue = new Catalogue();
                mCatalogue.setCatalogueTitle(getSupportActionBar().getTitle().toString());
                catalogueCollection.whereEqualTo("catalogueTitle", catalogueName).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            int count = 0;
                            String docFoundId = "";
                            Catalogue mCatalogue = new Catalogue();
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(DocumentSnapshot document: queryDocumentSnapshots){
                                    Log.d("NIKUNJ", document.getId() + " => " + document.getData());
                                    count++;
                                    if (count == 1){
                                        docFoundId = document.getId();
                                    }
                                }

                                catalogueCollection.document(docFoundId).set(mCatalogue);

                            }


                        });


                break;

            case R.id.item_publish:
                //mStaticCatalogue.setIsPublished(true);
                break;

            case R.id.item_type:
                editCatalogueTypeDialogFragment = new EditCatalogueTypeDialog();
                ft = getSupportFragmentManager().beginTransaction();
                editCatalogueTypeDialogFragment.show(ft, "dialog");

            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "catalogue saved", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void saveCatalogue() {




    }
}




        //mStaticCatalogue.setCatalogueTitle(catalogueText.getText().toString());


