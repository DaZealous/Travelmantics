package www.andela.com.travelmantics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    EditText editTitle, editPrice, editResort;
    Button btnSelectImage;
    ImageView imageView;
    String price, title, resort;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog dialog;
    DatabaseReference mUserRef, mRootRef;
    FirebaseDatabase mDatabase;
    Uri uri;
    StorageReference mPhotoRef;
    String userId;
    ImageView imgContain;

    public static int IMAGE_PICKER  = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference().child("Users");
        mRootRef = mDatabase.getReference();
        mUser = mAuth.getCurrentUser();

        mPhotoRef = FirebaseStorage.getInstance().getReference();

        userId = mUser.getUid();

        imgContain = new ImageView(this);

        editTitle = findViewById(R.id.admin_title_text);
        editPrice = findViewById(R.id.admin_price_text);
        editResort = findViewById(R.id.admin_resort_text);
        imageView = findViewById(R.id.admin_image_view);
        btnSelectImage = findViewById(R.id.admin_select_image_btn);

        dialog = new ProgressDialog(this);

        dialog.setTitle("Please Wait");
        dialog.setMessage("Uploading");
        dialog.setCancelable(false);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), IMAGE_PICKER);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.admin_save_action:
                upload();
                return true;
                default:
                    return false;
        }
    }

    private void upload() {
        title = editTitle.getText().toString();
        price = editPrice.getText().toString();
        resort = editResort.getText().toString();

        if(TextUtils.isEmpty(title)){
            editTitle.requestFocus();
            editTitle.setError("Title is empty");
            return;
        }

        if(TextUtils.isEmpty(price)){
            editPrice.requestFocus();
            editPrice.setError("Price is empty");
            return;
        }


        if(TextUtils.isEmpty(resort)){
            editResort.requestFocus();
            editResort.setError("Title is empty");
            return;
        }


        if(uri == null){
            Toast.makeText(AdminActivity.this, "Upload Image First", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.show();
       final DatabaseReference key = mRootRef.child("Resorts").child(userId).push();

        final StorageReference photoRef = mPhotoRef.child("images").child(userId).child(key.getKey()+".jpg");
        final StorageReference thumbRef = mPhotoRef.child("thumb_images").child(userId).child(key.getKey()+".jpg");

        photoRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imgContain.setImageURI(uri);
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String image_url = uri.toString();
                            Bitmap bitmap = ((BitmapDrawable) imgContain.getDrawable()).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            final byte[] thumb_byte = stream.toByteArray();
                            final UploadTask uploadTask = thumbRef.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        HashMap<String, String> Resortmap = new HashMap();
                                        Resortmap.put("title", title);
                                        Resortmap.put("price", price);
                                        Resortmap.put("resort", resort);
                                        Resortmap.put("image_url", image_url);

                                        HashMap map = new HashMap();
                                        map.put("Resort/" + userId + "/" + key.getKey(), Resortmap);

                                        mRootRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                dialog.dismiss();
                                                finish();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER && resultCode == RESULT_OK){
            uri = data.getData();
            imgContain.setImageURI(uri);
            Bitmap bitmap = ((BitmapDrawable) imgContain.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            final byte[] thumb_byte = stream.toByteArray();
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(thumb_byte, 0, thumb_byte.length));
        }
    }
}
