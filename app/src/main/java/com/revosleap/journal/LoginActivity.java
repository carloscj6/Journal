package com.revosleap.journal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    private int SIGN_IN_CODE= 78;
    FirebaseAuth auth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton= findViewById(R.id.sign_in_button);

        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient= new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Connection Unavailabe", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignInButton();
            }
        });
    }
    private void setSignInButton(){

       Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
       startActivityForResult(intent,SIGN_IN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SIGN_IN_CODE){
            GoogleSignInResult signInResult= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult.isSuccess()){
                GoogleSignInAccount account= signInResult.getSignInAccount();
                UserAuthFirebase(account);
            }
        }
    }

    private void UserAuthFirebase(final GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential)

                .addOnCanceledListener(LoginActivity.this, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(LoginActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Failed, Please try again", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String name= auth.getCurrentUser().getDisplayName();
                        Toast.makeText(LoginActivity.this, "Account Connection Success "+name,
                                Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

    }
}
