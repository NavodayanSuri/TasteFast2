package com.example.surendrasingh.tastefast;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.DatabaseMetaData;

public class Add_food_Activity extends AppCompatActivity {
    ImageButton foodImage;
    private static final int GALLREQ=1;
    EditText name,decr,prize;
    DatabaseReference mref;
    Button addfood;
    FirebaseDatabase firebaseDatabas=null;
    StorageReference storageReference=null;
    Uri uri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_);
        name=(EditText)findViewById(R.id.ItemNm);
        decr=(EditText)findViewById(R.id.ItemDec);
        prize=(EditText)findViewById(R.id.ItemPrize);
        mref=FirebaseDatabase.getInstance().getReference("item");
        foodImage=(ImageButton)findViewById(R.id.imagebutton);
        addfood=(Button)findViewById(R.id.addfood);
        storageReference=FirebaseStorage.getInstance().getReference();




        addfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nm=name.getText().toString().trim();
                final String dec=decr.getText().toString().trim();
                final String pz=prize.getText().toString().trim();
                if (!TextUtils.isEmpty(nm)&&!TextUtils.isEmpty(dec)&&!TextUtils.isEmpty(pz)){
                    StorageReference filepath=storageReference.child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadurl=taskSnapshot.getDownloadUrl();
                            food item=new food(nm,dec,pz,downloadurl.toString());
                            mref.push().setValue(item);
                            Toast.makeText(Add_food_Activity.this, "Food item added Sucessfully", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("Image/*");
                startActivityForResult(galleryIntent,GALLREQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLREQ && requestCode==RESULT_OK){
            uri=data.getData();
            foodImage.setImageURI(uri);

        }
    }
}
