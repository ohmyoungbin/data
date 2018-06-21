package com.example.gst.chatproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editTxt;
    private Button sendBtn;
    private RecyclerView listView;

    private String userName;
    private ChatAdapter adapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("message");

    private ArrayList<ChatData> mItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initValues();

        // 기본 Text를 담을 수 있는 simple_list_item_1을 사용해서 ArrayAdapter를 만들고 listview에 설정
        adapter = new ChatAdapter(mItems);

        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setItemAnimator(new DefaultItemAnimator());


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChatData chatData = new ChatData(userName, editTxt.getText().toString());
//                databaseReference.child("message").push().setValue(chatData);
//                editTxt.setText("");
                String message = editTxt.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    editTxt.setText("");
                    ChatData chatData = new ChatData();
                    chatData.setUserName(userName);
                    chatData.setMessage(message);
                    chatData.setTime(System.currentTimeMillis());
                    databaseReference.child("message").push().setValue(chatData);
            }
        }});

        databaseReference.child("message").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
//                adapter.add(chatData.getUserName() + ": " + chatData.getMessage());  // adapter에 추가합니다.

                //ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.setFirebaseKey(dataSnapshot.getKey());
                mItems.add(chatData);

                adapter.notifyData(mItems);
                listView.smoothScrollToPosition(adapter.getItemCount());
               // adapter.notifyDataSetChanged();

//                Toast.makeText(MainActivity.this,""+chatData.getMessage()+"//"+adapter.getItemCount(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String firebaseKey = dataSnapshot.getKey();
//                int count = adapter.getCount();
//                for (int i = 0; i < count; i++) {
//                    if (adapter.get(i).equals(firebaseKey)) {
//                        adapter.remove(adapter.getItem(i));
//                        break;
//                    }
//                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void initViews() {
        listView = (RecyclerView) findViewById(R.id.listView);
        editTxt = (EditText) findViewById(R.id.editText);
        sendBtn = (Button) findViewById(R.id.sendBtn);

        editTxt = (EditText) findViewById(R.id.editText);
        //findViewById(R.id.sendBtn).setOnClickListener(this);
    }

    private void initValues() {
        userName = "Guest" + new Random().nextInt(5000);
    }

}
