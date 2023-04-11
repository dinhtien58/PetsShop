package com.etrungpro.appshoppet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class EditInfoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100 ;
    TextView tvEmailInfo;
    EditText edtFirstNameInfo;
    EditText edtLastNameInfo;
    EditText edtPasswordInfo;
    EditText edtNewPasswordInfo;
    EditText edtConfirmPasswordInfo;
    Button btnConfirmInfo;
    ImageView imgInfo;
    String imageName;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        initUI();

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            tvEmailInfo.setText((String) documentSnapshot.get("email"));
                            edtFirstNameInfo.setText((String) documentSnapshot.get("firstName"));
                            edtLastNameInfo.setText((String) documentSnapshot.get("lastName"));
                            edtPasswordInfo.setText((String) documentSnapshot.get("hashPassword"));
                            storageRef.child((String) documentSnapshot.get("img"))
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageUri = uri;
                                            Glide.with(EditInfoActivity.this)
                                                    .load(uri)
                                                    .into(imgInfo);
                                            imageName = new File(uri.getPath()).getName();
                                        }
                                    });
                        }
                    }
                });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImageInfo();
            }
        });

        btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tvEmailInfo.getText().toString();
                String firstName = edtFirstNameInfo.getText().toString();
                String lastName = edtLastNameInfo.getText().toString();
                String newPassword = edtNewPasswordInfo.getText().toString();
                String passwordConfirm = edtConfirmPasswordInfo.getText().toString();
                if(!newPassword.equals(passwordConfirm)) {
                    edtConfirmPasswordInfo.setError("Mật khẩu không trùng nhau !!");
                    edtConfirmPasswordInfo.setFocusable(true);
                    return ;
                }

                Timestamp timestamp = Timestamp.now();
                User user = new User(email, firstName, lastName, imageName, newPassword, timestamp);
                db.collection("users")
                        .document(userId).
                        set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditInfoActivity.this, "Sửa thành công !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditInfoActivity.this, MainActivity.class));
                                finish();
                            }
                        });

                storageRef.child(imageName)
                        .putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Glide.with(EditInfoActivity.this)
                                        .load(imageUri)
                                        .into(imgInfo);
                            }
                        });

            }
        });
    }

    void initUI() {
        tvEmailInfo = findViewById(R.id.tv_email_info);
        edtFirstNameInfo = findViewById(R.id.edt_firstName_info);
        edtLastNameInfo = findViewById(R.id.edt_lastName_info);
        edtPasswordInfo = findViewById(R.id.edt_password_info);
        edtNewPasswordInfo = findViewById(R.id.edt_new_password_info);
        edtConfirmPasswordInfo = findViewById(R.id.edt_password_confirm_info);
        btnConfirmInfo = findViewById(R.id.btn_confirm_info);
        imgInfo = findViewById(R.id.img_info);
    }

    void setImageInfo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(imgInfo);
            String date = String.valueOf(Timestamp.now().toDate().getTime());
            String parseName = new File(imageUri.getPath()).getName();
            imageName =parseName + "_" + date;
        } else {
            Toast.makeText(EditInfoActivity.this, "Bạn chưa chọn ảnh nào cả", Toast.LENGTH_SHORT).show();
        }
    }

}