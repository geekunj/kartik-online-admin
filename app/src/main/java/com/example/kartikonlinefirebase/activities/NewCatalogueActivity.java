package com.example.kartikonlinefirebase.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.fragments.EditCatalogueTypeDialog;
import com.example.kartikonlinefirebase.models.Catalogue;
import com.example.kartikonlinefirebase.utils.BitmapTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static com.example.kartikonlinefirebase.utils.Config.mStaticCatalogue;

public class NewCatalogueActivity extends AppCompatActivity {

    private String[] CAT_TYPE = new String[] {"Our Sellers", "Featured Brands"};
    private String Toolbar_CATALOGUE_TEXT = "Edit Title";

    private Toolbar mToolbar;
    private AutoCompleteTextView catalogueTypeDropDown;
    private Switch publishSwitch;
    private ArrayAdapter<String> catalogueTypeAdapter;
    private ImageView deleteCatalogueImageButton;
    private ImageView catalogueImage;
    private TextView productCounterText;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    private Catalogue catalogue;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private StorageReference storageReference;
    private FirebaseFirestore mFirestore;
    private CollectionReference catalogueCollection;
    private Uri uri;

    String catalogueInsertedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_catalogue);

        catalogue = new Catalogue();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        catalogueCollection = mFirestore.collection("catalogues");



        mToolbar = findViewById(R.id.toolbar);
        productCounterText = findViewById(R.id.tv_product_counter);
        catalogueTypeDropDown = findViewById(R.id.catalog_type_dropdown);
        publishSwitch = findViewById(R.id.switch_publish);
        catalogueImage = findViewById(R.id.iv_catalogue_image);
        deleteCatalogueImageButton = findViewById(R.id.iv_delete_catalogue_image);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Toolbar_CATALOGUE_TEXT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        catalogueTypeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, CAT_TYPE);
        catalogueTypeDropDown.setAdapter(catalogueTypeAdapter);

        catalogueTypeDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catalogue.setCatalogueType(CAT_TYPE[position]);
            }
        });

        catalogueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        deleteCatalogueImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogueImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
            }
        });

        publishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                catalogue.setPublished(isChecked);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                if (data != null) {

                    uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this).load(bitmap).transform(new BitmapTransformer(MAX_WIDTH, MAX_HEIGHT)).into(catalogueImage);

                }
            }

        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri) {
        storageReference.putFile(uri).addOnCompleteListener(NewCatalogueActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(NewCatalogueActivity.this, new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                //callback.onImageUploaded(task.getResult().toString());
                                                //categoryViewModel.setCategoryImageUrl(task.getResult().toString());
                                                catalogue.setCatalogueImageUrl(task.getResult().toString());
                                                catalogueCollection.document(catalogueInsertedId)
                                                .set(catalogue);

                                                Toast.makeText(NewCatalogueActivity.this, "Banner Added", Toast.LENGTH_SHORT).show();

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
        getMenuInflater().inflate(R.menu.new_catalogue_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_edit:


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
                                    catalogue.setCatalogueTitle(Toolbar_CATALOGUE_TEXT);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                break;

            case R.id.item_save:

                if((!TextUtils.isEmpty(catalogue.getCatalogueTitle())
                        || catalogue.getCatalogueTitle() != null
                        || catalogue.getCatalogueTitle().equalsIgnoreCase("edit title"))
                        && (!TextUtils.isEmpty(catalogue.getCatalogueType())
                        || catalogue.getCatalogueType() != null)
                        ){

                    catalogueCollection.add(catalogue).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            catalogueInsertedId = documentReference.getId();
                            Log.d("NIKUNJ", "InsertedId =>" + catalogueInsertedId);
                            storageReference = FirebaseStorage.getInstance().getReference(
                                    mFirebaseUser.getUid())
                                    .child("catalogues")
                                    .child(catalogueInsertedId)
                                    .child("catalogue-images")
                                    .child(uri.getLastPathSegment());

                            putImageInStorage(storageReference, uri);

                        }
                    });



                }

                else{
                    Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }

                break;


        }
        return true;
    }
}
