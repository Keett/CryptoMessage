package com.example.ket.cryptomessage.Common;

import com.example.ket.cryptomessage.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by KET on 7.05.2018.
 */

public class Common {
    public static final String DIALOG_EXTRA="Dialogs";

    public static String createChatDialogName(List<Integer> qbUsers)
    {
        List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersByIds(qbUsers);
        StringBuilder name=new StringBuilder();
        for (QBUser user: qbUsers1)
            name.append(user.getFullName()).append(" ");
        if(name.length()>30)
            name = name.replace(30,name.length()-1,"...");

        return name.toString();
    }



}
