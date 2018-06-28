package com.example.gst.chatproj;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {
        private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());
        private final static int TYPE_MY_SELF = 0;
        private final static int TYPE_ANOTHER = 1;
        private String mMyEmail;

        ArrayList<ChatData> mItems;

        public ChatAdapter(){}

        public void setmMyEmail(String email){
            mMyEmail = email;
        }

        public ChatAdapter(ArrayList<ChatData> items){
            mItems = items;
        }


        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_chat_my, parent,false);
                View view = null;

                if (viewType == TYPE_MY_SELF) { // 나의 채팅내용
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_chat_my, parent,false);
                } else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent,false);
                }
                return new ItemViewHolder(view);
        }

        // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
                ChatData chatData = getItem(position);
                String email = getItem(position).getUserEmail();

                holder.mTxtMessage.setText(mItems.get(position).getMessage());
                holder.mTxtTime.setText(mSimpleDateFormat.format(mItems.get(position).getTime()));

                if (!TextUtils.isEmpty(mMyEmail) && mMyEmail.equals(email)) { // 나의 채팅내용
                    holder.mTxtMessage.setText(mItems.get(position).getMessage());
                    holder.mTxtTime.setText(mSimpleDateFormat.format(mItems.get(position).getTime()));
                } else {
                    holder.mTxtUserName.setText(mItems.get(position).getUserName());
                    holder.mTxtMessage.setText(mItems.get(position).getMessage());
                    holder.mTxtTime.setText(mSimpleDateFormat.format(mItems.get(position).getTime()));
                    Picasso.with(holder.mImgProfile.getContext()).load(chatData.getUserPhotoUrl()).into(holder.mImgProfile);
                }

                //mMyEmail = getItem(position).getUserEmail();
                Log.d("데이터확인","mMyEmail:"+mMyEmail);

        }

        @Override
        public int getItemViewType(int position) {
            String email = getItem(position).getUserEmail();

            if (!TextUtils.isEmpty(mMyEmail) && mMyEmail.equals(email)) {
                return TYPE_MY_SELF; // 나의 채팅내용
            } else {
                return TYPE_ANOTHER; // 상대방의 채팅내용
            }
        }

        // 데이터 셋의 크기를 리턴해줍니다.
        @Override
        public int getItemCount() {
                return mItems.size();
        }

        // 커스텀 뷰홀더
        // item layout 에 존재하는 위젯들을 바인딩합니다.
        class ItemViewHolder extends RecyclerView.ViewHolder{
            private TextView mTxtUserName;
            private TextView mTxtMessage;
            private TextView mTxtTime;
            private ImageView mImgProfile;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mTxtUserName = (TextView) itemView.findViewById(R.id.txt_userName);
                mTxtMessage = (TextView) itemView.findViewById(R.id.txt_message);
                mTxtTime = (TextView) itemView.findViewById(R.id.txt_time);
                mImgProfile = (ImageView)itemView.findViewById(R.id.img_profile);
            }
        }


        //어뎁터 갱신
        public void notifyData(ArrayList<ChatData> myList) {
            // Log.d("notifyData ", myList.size() + "");
            this.mItems = myList;
            notifyDataSetChanged();
        }

        public ChatData getItem(int i){
            return mItems.get(i);
        }

        public void remove(ChatData data) {
            mItems.remove(data);
        }

}