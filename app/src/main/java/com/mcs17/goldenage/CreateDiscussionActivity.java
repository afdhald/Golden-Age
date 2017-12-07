package com.mcs17.goldenage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcs17.goldenage.models.Discussion;
import com.mcs17.goldenage.models.Post;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by twindo-mac2 on 06/12/17.
 */

public class CreateDiscussionActivity extends AppCompatActivity {
    @BindView(R.id.topicDiscussion) EditText topic;
    @BindView(R.id.description) EditText description;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private SharedPreferences sharedPref;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discussion);
        ButterKnife.bind(this);

        //Get Firebase auth instance
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("posts");

    }

    @OnClick(R.id.btn_submitDiscussion)
    public void submitDiscussion(View view){
        String topicD = topic.getText().toString().trim();
        String descriptionD = description.getText().toString().trim();

        if (TextUtils.isEmpty(topicD)) {
            Toast.makeText(getApplicationContext(), "Topik tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(descriptionD)) {
            Toast.makeText(getApplicationContext(), "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //sharedPref = getPreferences(MODE_PRIVATE);
        //String userId = sharedPref.getString("firebasekey", "");
        //Toast.makeText(CreateDiscussionActivity.this, "userId : " + userId, Toast.LENGTH_SHORT).show();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        //Toast.makeText(this, "USER ID = " + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();

        if (TextUtils.isEmpty(postId)) {
            postId = mDatabase.push().getKey();
        }
        String userId = currentFirebaseUser.getUid();
        Discussion discussion = new Discussion(userId, topicD, descriptionD);
        mDatabase.child(postId).setValue(discussion, new DatabaseReference.CompletionListener() {
        //mDatabase.child("posts").setValue(discussion, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                progressBar.setVisibility(View.GONE);
                // clear edit text
                topic.setText("");
                description.setText("");
                onBackPressed();
                finish();
            }
        });
        /*
        myRef.child("Users").child(Username).child("Userid").setValue(user.getUid(), new DatabaseReference.CompletionListener() {
            void onComplete(DatabaseError error, DatabaseReference ref) {
                System.out.println("Value was set. Error = "+error);
            }
        });
        */
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
