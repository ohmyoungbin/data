package com.example.gst.chatproj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1001;

    private EditText editTxt; //채팅메시지 입력창
    private Button sendBtn; //채팅메시지 전송창
    private RecyclerView listView; //채팅메시지 표시하는 리스트뷰

    private String userName;
    private ChatAdapter adapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("message");

    private ArrayList<ChatData> mItems = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    private SignInButton mBtnGoogleSignIn; // 로그인 버튼
    private Button mBtnGoogleSignOut; // 로그아웃 버튼
    private TextView mTxtProfileInfo; // 사용자 정보 표시
    private ImageView mImgProfile; // 사용자 프로필 이미지 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initFirebaseDatabase();
        initFirebaseAuth();
        initValues();

    }

    private void initViews() {
        listView = (RecyclerView) findViewById(R.id.listView);
        // 기본 Text를 담을 수 있는 simple_list_item_1을 사용해서 ArrayAdapter를 만들고 listview에 설정
        adapter = new ChatAdapter(mItems);

        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setItemAnimator(new DefaultItemAnimator());


        editTxt = (EditText) findViewById(R.id.editText);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this);
        editTxt = (EditText) findViewById(R.id.editText);

        mBtnGoogleSignIn = (SignInButton) findViewById(R.id.btn_google_signin);
        mBtnGoogleSignOut = (Button) findViewById(R.id.btn_google_signout);
        mBtnGoogleSignIn.setOnClickListener(this);
        mBtnGoogleSignOut.setOnClickListener(this);

        mTxtProfileInfo = (TextView) findViewById(R.id.txt_profile_info);
        mImgProfile = (ImageView) findViewById(R.id.img_profile);
    }

    private void initValues() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null){
            userName = "Guest" + new Random().nextInt(5000);
        }else{
            userName = user.getDisplayName();
        }
    }

    private void initFirebaseDatabase(){
        databaseReference.child("message").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고

                chatData.setFirebaseKey(dataSnapshot.getKey());
                mItems.add(chatData);
                listView.smoothScrollToPosition(adapter.getItemCount());

                 //리스트 갱신
                adapter.notifyData(mItems);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = adapter.getItemCount();
                for (int i = 0; i < count; i++) {
                    if (adapter.getItem(i).getFirebaseKey().equals(firebaseKey)) {
                        adapter.remove(adapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    //로그인 인증
    private void initFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                            .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                                @Override
                                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                }
                            })
                            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                            .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateProfile();
            }
        };
    }

    private void updateProfile(){
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d("updateProfile","-------------------------------------");
        if (user == null) {
            // 비 로그인 상태 (메시지를 전송할 수 없다.)
            mBtnGoogleSignIn.setVisibility(View.VISIBLE);
            mBtnGoogleSignOut.setVisibility(View.GONE);
            mTxtProfileInfo.setVisibility(View.GONE);
            mImgProfile.setVisibility(View.GONE);
            findViewById(R.id.sendBtn).setVisibility(View.GONE);
            adapter.setmMyEmail(null);

            adapter.notifyData(mItems);
            adapter.notifyDataSetChanged();
            Log.d("비로그인..","ㅇㅇㅇ--------");
        } else {
            // 로그인 상태
            mBtnGoogleSignIn.setVisibility(View.GONE);
            mBtnGoogleSignOut.setVisibility(View.VISIBLE);
            mTxtProfileInfo.setVisibility(View.VISIBLE);
            mImgProfile.setVisibility(View.VISIBLE);
            findViewById(R.id.sendBtn).setVisibility(View.VISIBLE);

            userName = user.getDisplayName(); // 채팅에 사용 될 닉네임 설정
            String email = user.getEmail();
            StringBuilder profile = new StringBuilder();
            profile.append(userName).append("\n").append(user.getEmail());
            mTxtProfileInfo.setText(profile);
            adapter.setmMyEmail(email);
            adapter.notifyData(mItems);
            adapter.notifyDataSetChanged();

            Picasso.with(this).load(user.getPhotoUrl()).into(mImgProfile);
            Log.d("로그인","ㅇㅇㅇ//"+user.toString());
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.sendBtn:

                initValues();
                String message = editTxt.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    editTxt.setText("");
                    ChatData chatData = new ChatData();
                    chatData.setUserName(userName);
                    chatData.setMessage(message);
                    chatData.setTime(System.currentTimeMillis());

                    chatData.setUserEmail(mAuth.getCurrentUser().getEmail()); //사용자 이메일 주소
                    chatData.setUserPhotoUrl(mAuth.getCurrentUser().getPhotoUrl().toString());
                    databaseReference.child("message").push().setValue(chatData);
                }

            break;
            case R.id.btn_google_signin:
                signIn();
                break;
            case R.id.btn_google_signout:
                signOut();
                break;
        }
    }

    //로그인
    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //로그아웃
    private void signOut(){
            mAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            updateProfile();
                        }
                    });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result;
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) { //인증실패
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }else{ //인증성공
                                    updateProfile();
                                }
                            }
                        });
                Toast.makeText(MainActivity.this, "onActivityResult-----isSuccess",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "onActivityResult---실패",
                        Toast.LENGTH_SHORT).show();
                updateProfile();
            }
        }
    }
}
