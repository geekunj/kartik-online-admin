package com.example.kartikonlinefirebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kartikonlinefirebase.MainActivity;
import com.example.kartikonlinefirebase.R;
import com.example.kartikonlinefirebase.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginToFireStore extends AppCompatActivity {

    EditText lEmailEt, lPwdEt;
    Button lLoginBtn;
    TextView createAccountText, loginWithPhoneText;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    String htmlTextForCreateAccount;
    String htmlTextForSignInWithPhone;

    User currentUser;

    @Override
    protected void onStart() {
        super.onStart();
        createAccountText.setEnabled(true);
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null) {
            userID = fAuth.getCurrentUser().getUid();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_to_fire_store);
        fAuth= FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        lEmailEt = (EditText) findViewById(R.id.l_email_et);
        lPwdEt = (EditText) findViewById(R.id.l_pwd_et);
        lLoginBtn = (Button) findViewById(R.id.l_login_btn);
        createAccountText = (TextView) findViewById(R.id.tv_create_account);
        loginWithPhoneText = findViewById(R.id.tv_phone_login);

        htmlTextForCreateAccount = "<u>Don't have an account ? create here</u>";
        htmlTextForSignInWithPhone = "<u>Login with Phone Number instead</u>";

        loginWithPhoneText.setText(Html.fromHtml(htmlTextForSignInWithPhone));
        createAccountText.setText(Html.fromHtml(htmlTextForCreateAccount));

//        if(fAuth.getCurrentUser() != null) {
//            userID = fAuth.getCurrentUser().getUid();
//            currentUser = new User();
//
//           final DocumentReference documentReference = fstore.collection("users").document(userID);
//            Log.d("LoginToFireStoreError", documentReference+"");
//           documentReference.addSnapshotListener(LoginToFireStore.this, new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                    Log.d("LoginToFireStoreError", e+"");
//                    //Log.d("LoginToFireStoreActivity", documentSnapshot.getString("userEmail"));
//                    currentUser.setUserEmail(documentSnapshot.getString("userEmail"));
//                    currentUser.setUserName(documentSnapshot.getString("userName"));
//                    currentUser.setUserPassword(documentSnapshot.getString("password"));
//                    currentUser.setUserId(documentSnapshot.getString("userId"));
//                    currentUser.setOnline(documentSnapshot.getBoolean("isOnline"));
//                    currentUser.setLastActive(Long.parseLong(documentSnapshot.getString("lastActive")));
//
//                }
//            });
//            Log.d("LoginToFireStoreActivity", currentUser.toString());
//            makeUserOnline(currentUser,documentReference);
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }


        lLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lEmailEt.getText().toString().trim();
                String password = lPwdEt.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    lEmailEt.setError("email is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    lPwdEt.setError("password is required");
                    return;
                } else if (password.length() < 6) {
                    lPwdEt.setError("password must be greater than 6");
                    return;
                } else {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                lLoginBtn.setClickable(false);
                                Toast.makeText(LoginToFireStore.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginToFireStore.this, "Register user first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }

            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountText.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), RegisterToFireStore.class));
            }
        });

        loginWithPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithPhoneText.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), LoginWithPhone.class));
                finish();
            }
        });
    }

//    public void makeUserOnline(User mUser, DocumentReference documentReference){
//
//
//        mUser.setOnline(true);
//        mUser.setLastActive(0);
//        documentReference.set(mUser);
//        FirebaseDatabase.getInstance().getReference("status/" + mUser.getUserId()).setValue("online");
//
//        FirebaseDatabase.getInstance().getReference("/status/" + mUser.getUserId())
//                .onDisconnect()     // Set up the disconnect hook
//                .setValue("offline");
//
//    }
//
//    public void makeUserOffline(User mUser){
//
//        DocumentReference query = FirebaseFirestore.getInstance().collection("users").document(mUser.getUserId());
//        mUser.setOnline(false);
//        mUser.setLastActive(System.currentTimeMillis());
//        query.set(mUser);
//        FirebaseDatabase.getInstance().getReference("status/" + mUser.getUserId()).setValue("offline");
//
//    }

}




