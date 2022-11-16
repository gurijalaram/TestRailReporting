package com.apriori.utils;

import com.sun.mail.imap.IMAPFolder;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailUtil {
    public static Message[] getEmail(String address, String userName, String password) throws MessagingException,
        IOException {
        Folder folder;
        Store store;
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

    /**
     * @param inlineVars - order of parameters to be passed:
     *                   (address, username, password and folder name)
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public static Message[] getEmail(String... inlineVars) throws MessagingException,
        IOException {

        Folder folder;
        Store store;
        String subject = null;
        Flags.Flag flag = null;


        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        store.connect(inlineVars[0], inlineVars[1], inlineVars[2]);
        folder = (IMAPFolder) store.getFolder(inlineVars[3]); //This works for both email account

        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }
        return folder.getMessages();
    }
}