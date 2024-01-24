package com.example.chessappgroupd.domain.appUser;

import org.springframework.beans.factory.annotation.Value;
//Emails
//A place for all mail templates
public class Emails {
    //official CheckMates mail address
    @Value("${spring.mail.username}")
    public static String CHECKMATES_MAIL_ADDRESS;
    //AuthCode mail
    public static final String AUTHCODE_EMAIL_SUBJECT = "♚CheckMates♕ - Two Factor Authentication";
    public static final String AUTHCODE_EMAIL_TEXT = "\n" +
            "♜♞♝♛♚♝♞♜\n" +
            "♟♟♟♟♟♟♟♟\n" +
            "\n" +
            "Hi %s!\n" +//username
            "Welcome to Checkmates.\n" +
            "Here is your authentication code:\n" +
            "\n" +
            "             > %s <\n" +
            "\n" +
            "Please note that this code expires within %s minutes.\n" +
            "Otherwise, you can simply LOGIN again and we will sent you a NEW one.\n" +
            "\n" +
            "♙♙♙♙♙♙♙♙\n" +
            "♖♘♗♕♔♗♘♖";
    //FriendRequest mail
    public static final String FRIEND_REQUEST_EMAIL_SUBJECT = "♚CheckMates♕ - NEW Friend request";
    public static final String FRIEND_REQUEST_EMAIL_TEXT = "\n" +
            "♜♞♝♛♚♝♞♜\n" +
            "♟♟♟♟♟♟♟♟\n" +
            "\n" +
            "Hi %s!\n" +//receiver username
            "'%s' wants to add you as a friend.\n" +//sender username
            "Please accept or decline the friend request.\n" +
            "\n" +
            "Please note that each user can send unlimited friend requests.\n" +
            "If you block this email address, you will no longer receive the 2 factor authentication code.\n" +
            "\n" +
            "♙♙♙♙♙♙♙♙\n" +
            "♖♘♗♕♔♗♘♖";
}