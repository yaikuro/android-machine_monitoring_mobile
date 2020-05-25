package com.app.android_machine_monitoring_mobile;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.android_machine_monitoring_mobile.shared.BaseActivity;
import com.app.android_machine_monitoring_mobile.shared.machine.Machine;
import com.app.android_machine_monitoring_mobile.shared.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.app.android_machine_monitoring_mobile.shared.Notification.CHANNEL_1_ID;

public class MainDashboard extends BaseActivity implements View.OnClickListener {
    boolean doubleBackToExitPressedOnce = false;
    private String TAG = "MainActivity";
    private NotificationManagerCompat notificationManagerCompat;
    private int RC_SIGN_IN = 1;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseMachineRef;
    private User user;
    private GoogleSignInClient mGoogleSignInclient;


    private String uid, email, fullName, nickname, mobilePhoneNumber;
    private TextView txtWelcomeUser;
    private ImageView ivUserProfilePicture;
    private MaterialCardView cvUser, cvMachineDashboard, cvProblemWaitingList;


    private ShimmerFrameLayout container;

    private void userProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
//             FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.shimmer_view_container);
        container.startShimmer(); // If auto-start is set to false

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUserRef = mDatabase.getReference("Users");
        mDatabaseMachineRef = mDatabase.getReference("Machines");
        readUserFromDatabase();
        readMachineStatusFromDatabase();


        // Notification
        notificationManagerCompat = NotificationManagerCompat.from(this);

        // Views
        txtWelcomeUser = findViewById(R.id.txtWelcomeUser);
        ivUserProfilePicture = findViewById(R.id.ivUserProfilePicture);

        // Buttons
        findViewById(R.id.cvUser).setOnClickListener(this);
        findViewById(R.id.cvMachineDashboard).setOnClickListener(this);
        findViewById(R.id.cvBreakdownList).setOnClickListener(this);
        findViewById(R.id.cvReportHistory).setOnClickListener(this);


    } // End of onCreate

    private void readUserFromDatabase() {
        // Read from the database
        mDatabaseUserRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                user = dataSnapshot.getValue(User.class);

                assert user != null;
                txtWelcomeUser.setText(getString(R.string.stringWelcome, user.getNickname()));

                Picasso.get()
                        .load(user.getUserProfilePictureUrl())
                        .fit()
                        .centerCrop()
                        .into(ivUserProfilePicture);

                container.stopShimmer();
                container.setShimmer(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                container.stopShimmer();
                container.setShimmer(null);
            }
        });
    }

    private void readMachineStatusFromDatabase() {
        mDatabaseMachineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot lineNode : dataSnapshot.getChildren()) {
                    for (DataSnapshot machineNode : lineNode.getChildren()) {
                        Machine machine = machineNode.getValue(Machine.class);
                        assert machine != null;
                        if (machine.getMachineStatus().equals("2")) {
                            sendBreakdownNotification();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainDashboard.this, "Failed to read machine status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendBreakdownNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_error_outline_black_24dp)
                .setContentTitle("Breakdown Alert!")
                .setContentText("Breakdown on one of the machines")
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    private void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account != null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "There's an error", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainDashboard.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google Sign in Failed", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    // Press back twice to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cvUser) {
            goto_UserProfile();
        } else if (id == R.id.cvMachineDashboard) {
            goto_MachineDashboard();
        } else if (id == R.id.cvBreakdownList) {
            goto_BreakdownListActivity();
        } else if (id == R.id.cvReportHistory) {
            goto_ReportHistoryActivity();
        }
    }
}
