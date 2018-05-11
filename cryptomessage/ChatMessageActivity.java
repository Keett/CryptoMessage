package com.example.ket.cryptomessage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ket.cryptomessage.Adapter.ChatMessageAdapter;
import com.example.ket.cryptomessage.Adapter.ChatMessageAdapter2;
import com.example.ket.cryptomessage.Common.Common;
import com.example.ket.cryptomessage.Holder.QBChatMessagesHolder;
import com.example.ket.cryptomessage.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class ChatMessageActivity extends AppCompatActivity {



    QBChatDialog qbChatDialog;
    ListView lstChatMessages;
    ImageButton submitButton;
    EditText edtContent;
    ChatMessageAdapter adapter;
    ChatMessageAdapter2 adapter2;
    Button buton,buton3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
       buton=(Button)findViewById(R.id.button2);
        buton3=(Button)findViewById(R.id.button3);
       initViews();
        initChatDialogs();

        retrieveMessage();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QBChatMessage chatMessage=new QBChatMessage();
                chatMessage.setBody(edtContent.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                //Put message to cache
                QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(),chatMessage);
                ArrayList<QBChatMessage> messages =QBChatMessagesHolder.getInstance().getChatMessagesByDialogId(qbChatDialog.getDialogId());
                adapter =new ChatMessageAdapter(getBaseContext(),messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //remove text from edit text
                edtContent.setText("");
                edtContent.setFocusable(true);


            }
        });
    buton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String sifrelenecekmesaj = edtContent.getText().toString();
            sifrelenecekmesaj.toLowerCase();
            char [] harfler=sifrelenecekmesaj.toCharArray();
            String sifreli="";


            for (int i = 0; i < sifrelenecekmesaj.length(); i++)
            {
                sifreli += Character.toString((char)((harfler[i]+3)+harfler.length));


                edtContent.setText(sifreli);
            edtContent.setFocusable(true);

            }
        }
    });
buton3.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        String sifrelenecekmesaj = edtContent.getText().toString();
        char [] harfler=sifrelenecekmesaj.toCharArray();
        String sifresiz="";
        for (int i = 0; i < sifrelenecekmesaj.length(); i++) {
            sifresiz += Character.toString((char) ((harfler[i] - 3) - harfler.length));
        }

            edtContent.setText(sifresiz);
            edtContent.setFocusable(true);


        QBMessageGetBuilder messageGetBuilder=new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);//get limit 500 messages

            QBRestChatService.getDialogMessages(qbChatDialog,messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    //Put messages to cache
                    QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(),qbChatMessages);
                    adapter2= new ChatMessageAdapter2(getBaseContext(),qbChatMessages);
                    lstChatMessages.setAdapter(adapter2);

                    adapter2.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });



    }
});




    }

    private void retrieveMessage() {
        QBMessageGetBuilder messageGetBuilder=new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);//get limit 500 messages
        if(qbChatDialog != null)
        {
            QBRestChatService.getDialogMessages(qbChatDialog,messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    //Put messages to cache
                    QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(),qbChatMessages);
                    adapter=new ChatMessageAdapter(getBaseContext(),qbChatMessages);
                    lstChatMessages.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }


    }

    private void initChatDialogs() {
        qbChatDialog=(QBChatDialog)getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());

        //Register listener incoming Message
        QBIncomingMessagesManager incomingMessage= QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessage.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer ınteger) {

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer ınteger) {

            }
        });
        qbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer ınteger) {
                //cache message
                QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);
                ArrayList<QBChatMessage> messages= QBChatMessagesHolder.getInstance().getChatMessagesByDialogId(qbChatMessage.getDialogId());
                adapter =new ChatMessageAdapter(getBaseContext(),messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer ınteger) {
                Log.e("ERROR",e.getMessage());
            }
        });


    }

    private void initViews() {

        lstChatMessages=(ListView)findViewById(R.id.list_of_message);
        submitButton=(ImageButton)findViewById(R.id.send_button);
        edtContent=(EditText)findViewById(R.id.edt_content);
    }




}
