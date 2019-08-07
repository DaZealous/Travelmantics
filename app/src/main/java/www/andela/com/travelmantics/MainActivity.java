package www.andela.com.travelmantics;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Button btnSignEmail, btnSignGmail;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    public static int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        /*btnSignEmail = findViewById(R.id.auth_sign_with_email);
        btnSignGmail = findViewById(R.id.auth_sign_with_gmail);*/

        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.EmailBuilder().build());
        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setIsSmartLockEnabled(true)
        .setAvailableProviders(providers).build(), RC_SIGN_IN);
/*

        btnSignEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        btnSignGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/


    }
/*

    private void showActivity(Intent intent) {
        startActivity(intent);
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK){
            handleSignIn(data);
        }
    }

    private void handleSignIn(Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        startActivity(new Intent(MainActivity.this, UserActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUser != null) {
            startActivity(new Intent(this, UserActivity.class));
            finish();
        }
    }
}
