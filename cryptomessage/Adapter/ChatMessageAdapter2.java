package com.example.ket.cryptomessage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ket.cryptomessage.ChatMessageActivity;
import com.example.ket.cryptomessage.Holder.QBChatMessagesHolder;
import com.example.ket.cryptomessage.Holder.QBUsersHolder;
import com.example.ket.cryptomessage.R;
import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

/**
 * Created by KET on 8.05.2018.
 */

public class ChatMessageAdapter2 extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter2(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;

        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId())){
                view =inflater.inflate(R.layout.list_send_message,null);
                BubbleTextView bubbleTextView=(BubbleTextView)view.findViewById(R.id.message_content);
                bubbleTextView.setText(qbChatMessages.get(position).getBody());
            }
            else{

                view =inflater.inflate(R.layout.list_recv_message,null);
                BubbleTextView bubbleTextView=(BubbleTextView)view.findViewById(R.id.message_content);
                bubbleTextView.setText(qbChatMessages.get(position).getBody());
                TextView txtName=(TextView)view.findViewById(R.id.message_user);
                txtName.setText(QBUsersHolder.getInstance().getUserById(qbChatMessages.get(position).getSenderId()).getFullName());

                String sifrelenecekmesaj = bubbleTextView.getText().toString();
                char [] harfler=sifrelenecekmesaj.toCharArray();
                String sifresiz="";
                for (int i = 0; i < sifrelenecekmesaj.length(); i++)
                {
                    sifresiz += Character.toString((char)((harfler[i]-3)-harfler.length));


                    bubbleTextView.setText(sifresiz);
                    bubbleTextView.setFocusable(true);

                }



            }

        }
        return view;
    }
}
