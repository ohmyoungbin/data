package com.example.gst.chatproj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {
        private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());

        ArrayList<ChatData> mItems;

        public ChatAdapter(){}
        ;
        public ChatAdapter(ArrayList<ChatData> items){
            mItems = items;
        }


        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent,false);
                return new ItemViewHolder(view);
        }


        // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
                holder.mTxtUserName.setText(mItems.get(position).getUserName());
                holder.mTxtMessage.setText(mItems.get(position).getMessage());
                holder.mTxtTime.setText(mSimpleDateFormat.format(mItems.get(position).getTime()));
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

            public ItemViewHolder(View itemView) {
                super(itemView);
                mTxtUserName = (TextView) itemView.findViewById(R.id.txt_userName);
                mTxtMessage = (TextView) itemView.findViewById(R.id.txt_message);
                mTxtTime = (TextView) itemView.findViewById(R.id.txt_time);
            }
        }


    public void notifyData(ArrayList<ChatData> myList) {
       // Log.d("notifyData ", myList.size() + "");
        this.mItems = myList;
        notifyDataSetChanged();
    }
}