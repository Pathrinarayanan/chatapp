package com.example.chatapp.Login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {
    private ImageView btnPrev,imgUpload;
    private EditText txtEditName,txtEditAbout;
    private Button btnSave,btnSaveTag,btnCancel;
    private TextView btnTag;
    private String tagColor;

    private String profileImageUrl;
    private Uri filePath;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userid,imageUrl,name,about;
    private FirebaseDatabase mdb;
    private DatabaseReference mreference;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ColorGetter colorGetter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ConstraintLayout editprofilelayout = findViewById(R.id.editprofile);
        imgUpload = findViewById(R.id.imgUpload);
        btnPrev = findViewById(R.id.btnPrev);
        btnSave = findViewById(R.id.btnSave);
        txtEditName = findViewById(R.id.txtEditName);
        txtEditAbout = findViewById(R.id.txtEditAbout);
        btnTag = findViewById(R.id.btnTag);
        colorGetter = new ColorGetter();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userid = user.getUid();
        name = txtEditName.getText().toString();
        about = txtEditAbout.getText().toString();

        mdb = FirebaseDatabase.getInstance();
        mreference = mdb.getReference();

        StorageReference ref
                = storageReference
                .child(
                        "images/"
                                + userid);


        setValues();



        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }

        });

        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) EditProfile.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View tagSelectionView = layoutInflater.inflate(R.layout.activity_select_tag,null);

                btnCancel = tagSelectionView.findViewById(R.id.btnCancel);
                btnSaveTag = tagSelectionView.findViewById(R.id.btnSaveTag);

                PopupWindow popupWindow = new PopupWindow(tagSelectionView, ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);

                popupWindow.showAtLocation(editprofilelayout, Gravity.CENTER,0,0);

                setCheckedButton(tagSelectionView);

                btnSaveTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioGroup radioGroup = tagSelectionView.findViewById(R.id.tagselection);
                        int clicked_btn_id = radioGroup.getCheckedRadioButtonId();

                        RadioButton clicked_button = tagSelectionView.findViewById(clicked_btn_id);

                        String selected_tag = clicked_button.getText().toString();

                        btnTag.setText(selected_tag);

                        tagColor = colorGetter.getColor(selected_tag);
                        setTagColor();

                        popupWindow.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modified_name;
                String modified_about;

                modified_name = txtEditName.getText().toString();

                modified_about = txtEditAbout.getText().toString();

                uploadImage(modified_name,modified_about);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this,Profile.class));
            }
        });
    }

    public void setTagColor() {
        btnTag.setTextColor(Color.parseColor(tagColor));
        GradientDrawable myGrad = (GradientDrawable)btnTag.getBackground();
        myGrad.setStroke(convertDpToPx(2), Color.parseColor(tagColor));
    }

    private int convertDpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void setCheckedButton(View tagSelectionView) {
        RadioButton tag_radio_btn[] = new RadioButton[9];
        int[] btn_id = {R.id.tag1,R.id.tag2,R.id.tag3,R.id.tag4,R.id.tag5,R.id.tag6,R.id.tag7,R.id.tag8,R.id.tag9};

        for (int i = 0; i< btn_id.length; i++)
        {
            tag_radio_btn[i] = tagSelectionView.findViewById(btn_id[i]);
        }

        for(int i=0;i<tag_radio_btn.length;i++)
        {
            String btn_value = tag_radio_btn[i].getText().toString().toLowerCase();
            String tag_value = btnTag.getText().toString().toLowerCase();

            if (btn_value.equals(tag_value))
            {
                tag_radio_btn[i].setChecked(true);
                break;
            }
        }
    }

    private void setValues() {
        mreference.child("Users").child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(EditProfile.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                }
                else {
                    String about = task.getResult().child("about").getValue().toString();
                    String username = task.getResult().child("username").getValue().toString();
                    String tagname = task.getResult().child("tag").getValue().toString();
                    String connections = task.getResult().child("connections").getValue().toString();
                    String image = task.getResult().child("imageURL").getValue().toString();


                    txtEditName.setText(username);
                    btnTag.setText(tagname);
                    txtEditAbout.setText(about);

                    Picasso.with(EditProfile.this).load(image).resize(160,160).into(imgUpload);
                    tagColor = colorGetter.getColor(tagname);
                    setTagColor();
                }
            }
        });
    }

    private boolean isDataChanged(String modifiedUrl,String modifiedName,String modifiedAbout) {
        if(modifiedUrl.equals(imageUrl) && modifiedName.equals(name) && modifiedAbout.equals(about))
        {
            return false;
        }
        else
        {
            return  true;
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);

                Bitmap resized_image = Bitmap.createScaledBitmap(bitmap,180,180,true);
                imgUpload.setImageBitmap(resized_image);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String modifiedName,String modifiedAbout) {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Updating Profile");
            progressDialog.show();

            // Defining the child of storageReference

            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + userid);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUri = uri;
                                            imageUrl = downloadUri.toString();
                                            changeData(imageUrl,modifiedName,modifiedAbout);
                                        }
                                    });
                                    // Image uploaded successfully
                                    // Dismiss dialog

                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(EditProfile.this,
                                                    "Profile Updated !!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(EditProfile.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Updating Profile "
                                                    + (int) progress + "%");
                                }
                            });

        }
        else{
            changeData(modifiedName,modifiedAbout);
        }


    }

    private void changeData(String imageUrl,String modifiedName, String modifiedAbout) {

        mreference.child("Users").child(userid).child("imageURL").setValue(imageUrl);
        mreference.child("users").child(userid).child("username").setValue(modifiedName);
        mreference.child("Users").child(userid).child("about").setValue(modifiedAbout);
        mreference.child("Users").child(userid).child("tag").setValue(btnTag.getText().toString());

        startActivity(new Intent(EditProfile.this,Profile.class));



    }

    public void changeData(String modifiedName, String modifiedAbout)
    {
        mreference.child("Users").child(userid).child("username").setValue(modifiedName);
        mreference.child("Users").child(userid).child("about").setValue(modifiedAbout);
        mreference.child("Users").child(userid).child("tag").setValue(btnTag.getText().toString());

        Toast.makeText(EditProfile.this,"Profile Updated !!",Toast.LENGTH_LONG).show();

        startActivity(new Intent(EditProfile.this,Profile.class));

    }
}