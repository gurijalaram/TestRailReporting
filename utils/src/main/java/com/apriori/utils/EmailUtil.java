package com.apriori.utils;

import com.sun.mail.imap.IMAPFolder;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailUtil {
    public static Message[] getEmail(String address, String userName, String password) throws MessagingException,
            IOException {
        Folder folder = null;
        Store store = null;
        String subject = null;
        Flags.Flag flag = null;


        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        store.connect(address, userName, password);
        folder = (IMAPFolder) store.getFolder("inbox"); //This works for both email account

        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }

        return folder.getMessages();

    }
}