package com.example.joshi.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth auth;
    Button btnRemoveUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mDatabase = FirebaseDatabase.getInstance().getReference("User Information");
        final TextView helloToUser = (TextView)findViewById(R.id.helloToUser);
        auth = FirebaseAuth.getInstance();
        btnRemoveUser=(Button)findViewById(R.id.removeUser);
        Button Logout = (Button)findViewById(R.id.goToLogoutFromMainActivityButton);

        //Logs out the user if accidentally goes to main page with out authentication.
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };

        //Retrieving and displaying hello to the user and where the user is from.
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    Users users = postSnapShot.getValue(Users.class);
                    String string = "Hello " + users.returnNameFromUsers() + ".";
                    helloToUser.setText(string);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Logout function starts
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        //logout function ends

        //Destroys the account if button clicked User List
        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                        finish();
                                        //progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        //progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                    String uid =user.getUid();
                    mDatabase.child(uid).setValue(null);
                    mDatabase = FirebaseDatabase.getInstance().getReference("User Critical Data");
                    mDatabase.child(uid).setValue(null);

                }
            }
        });
        //TextView userNameSearchedDisplay = (TextView)findViewById(R.id.UserNameSearched);

        Button searchForUsers = (Button)findViewById(R.id.searchForUsers);

        searchForUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                            Users users = postSnapShot.getValue(Users.class);

                            EditText acceptUsername = (EditText)findViewById(R.id.acceptUsername);

                            final String UsernameToBeSearched = acceptUsername.getText().toString();

                            Log.i("Names",users.returnNameFromUsers());

                            if(UsernameToBeSearched.matches(users.returnNameFromUsers())){
                                Log.i("User Exists","True");
                                Log.i("Key of the user is",dataSnapshot.getKey());//This returns User Information
                                Log.i("User ID",user.getUid());
                                Log.i("User email is",users.returnEmailFromUsers());
                            }else{
                                Log.i("User Exists","False");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        /*searchForUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("User Information");
                mDatabase.orderByChild("name").equalTo(UsernameToBeSearched).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Users users = dataSnapshot.getValue(Users.class);
                        //Log.i("Key is ",dataSnapshot.getKey());//essentially returns the ID of the data entries.
                        //Log.i("Emails are",users.returnEmailFromUsers());//returns the names
                        //if(users.returnNameFromUsers().matches(UsernameToBeSearched)){
                          //  Log.i("Name is present","True");
                        //}else{
                          //  Log.i("Name is present","False");
                        //}
                        Log.i("Names are",users.returnNameFromUsers());//returns
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                // mDatabase.child("loginpage-2b165").child("User Information").orderByChild("name").equalTo(UsernameToBeSearched).addValueEventListener(new ValueEventListener() {
                //    @Override
                //    public void onDataChange(DataSnapshot dataSnapshot) {
                //        if(dataSnapshot!=null && dataSnapshot.getChildren()!=null && dataSnapshot.getChildren().iterator().hasNext()){
                //            Log.i("User name","Exists");
                //         }else{
                //             Log.i("User name","Doesnt Exist");
                ////         }
                //     }
                //    @Override
                //        public void onCancelled(DatabaseError databaseError) {
//
                //        }
                //});
            }
        });*/
    }




    public void signOut() {
        auth.signOut();
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }
}
