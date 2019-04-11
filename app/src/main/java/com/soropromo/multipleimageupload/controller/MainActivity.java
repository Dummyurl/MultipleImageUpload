package com.soropromo.multipleimageupload.controller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soropromo.multipleimageupload.R;
import com.soropromo.multipleimageupload.adapters.SelectedImagesAdapter;
import com.soropromo.multipleimageupload.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_IMAGE_CODE = 1;

    private RecyclerView mSelectedImageRecyclerView;

    private List<Item> items;

    private SelectedImagesAdapter selectedImagesAdapter;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mStorage = FirebaseStorage.getInstance().getReference();


        mSelectedImageRecyclerView = (RecyclerView) findViewById(R.id.to_upload_recyclerview);

        items = new ArrayList<>();

        selectedImagesAdapter = new SelectedImagesAdapter(items);

        //RecyclerView
        mSelectedImageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSelectedImageRecyclerView.setHasFixedSize(true);
        mSelectedImageRecyclerView.setAdapter(selectedImagesAdapter);


    }


    public void ChooseImagesTapped(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_IMAGE_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    final Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    final String fileName = getFileName(fileUri);

                    items.add(new Item(fileUri.toString(), fileName, "uploading"));

                    selectedImagesAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Images").child(fileName);

                    final int finalI = i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            items.remove(finalI);
                            items.add(finalI,new Item(fileUri.toString(),fileName,"done"));

                            selectedImagesAdapter.notifyDataSetChanged();

                        }
                    });

                }


            } else if (data.getData() != null) {

                Toast.makeText(MainActivity.this, "For Single Image selected", Toast.LENGTH_SHORT).show();

            }

        }

    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}
