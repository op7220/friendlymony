package com.nect.friendlymony.Model.Conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class DataItem {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("sender")
    @Expose
    private int sender;
    @SerializedName("recipient")
    @Expose
    private int recipient;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("convertsations_date")
    @Expose
    private String convertsationsDate;
    @SerializedName("lastMsg")
    @Expose
    private String lastMsg;
    @SerializedName("wholename")
    @Expose
    private String wholename;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("is_send_by_me")
    @Expose
    private String isSendByMe;
    @SerializedName("unread_count")
    @Expose
    private String unreadCount;
    @SerializedName("both_unread_count")
    @Expose
    private String bothUnreadCount;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("chat_date_time_info")
    @Expose
    private String chatDateTimeInfo;
    @SerializedName("message_date_time_info")
    @Expose
    private String messageDateTimeInfo;
    @SerializedName("message_date_time")
    @Expose
    private String messageDateTime;
    @SerializedName("is_online_sender")
    @Expose
    private String isOnlineSender;
    @SerializedName("is_online_receiver")
    @Expose
    private String isOnlineReceiver;
    @SerializedName("is_active_sender")
    @Expose
    private String isActiveSender;
    @SerializedName("is_active_receiver")
    @Expose
    private String isActiveReceiver;
    @SerializedName("image_sender")
    @Expose
    private String imageSender;
    @SerializedName("image_receiver")
    @Expose
    private String imageReceiver;
    @SerializedName("is_blocked")
    @Expose
    private String isBlocked;
    @SerializedName("sender_device_token")
    @Expose
    private String sender_device_token;
    @SerializedName("sender_firebase_token")
    @Expose
    private String sender_firebase_token;
    @SerializedName("receiver_device_token")
    @Expose
    private String receiver_device_token;
    @SerializedName("receiver_firebase_token")
    @Expose
    private String receiver_firebase_token;

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getPhoto() {
        return photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getSender() {
        return sender;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setConvertsationsDate(String convertsationsDate) {
        this.convertsationsDate = convertsationsDate;
    }

    public String getConvertsationsDate() {
        return convertsationsDate;
    }

    public void setWholename(String wholename) {
        this.wholename = wholename;
    }

    public String getWholename() {
        return wholename;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getIs_online_sender() {
        return isOnlineSender;
    }

    public void setIs_online_sender(String is_online_sender) {
        this.isOnlineSender = is_online_sender;
    }

    public String getIs_online_receiver() {
        return isOnlineReceiver;
    }

    public void setIs_online_receiver(String is_online_receiver) {
        this.isOnlineReceiver = is_online_receiver;
    }

    public String getUnread_count() {
        return unreadCount;
    }

    public void setUnread_count(String unread_count) {
        this.unreadCount = unread_count;
    }

    public String getIs_active_sender() {
        return isActiveSender;
    }

    public void setIs_active_sender(String is_active_sender) {
        this.isActiveSender = is_active_sender;
    }

    public String getIs_active_receiver() {
        return isActiveReceiver;
    }

    public void setIs_active_receiver(String is_active_receiver) {
        this.isActiveReceiver = is_active_receiver;
    }

    public String getImage_sender() {
        return imageSender;
    }

    public void setImage_sender(String image_sender) {
        this.imageSender = image_sender;
    }

    public String getImage_receiver() {
        return imageReceiver;
    }

    public void setImage_receiver(String image_receiver) {
        this.imageReceiver = image_receiver;
    }

    public String getMessage_date_time_info() {
        return messageDateTimeInfo;
    }

    public void setMessage_date_time_info(String message_date_time_info) {
        this.messageDateTimeInfo = message_date_time_info;
    }

    public String getChat_date_time_info() {
        return chatDateTimeInfo;
    }

    public void setChat_date_time_info(String chat_date_time_info) {
        this.chatDateTimeInfo = chat_date_time_info;
    }

    public String getIs_blocked() {
        return isBlocked;
    }

    public void setIs_blocked(String is_blocked) {
        this.isBlocked = is_blocked;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getMessage_date_time() {
        return messageDateTime;
    }

    public void setMessage_date_time(String message_date_time) {
        this.messageDateTime = message_date_time;
    }

    public String getIs_send_by_me() {
        return isSendByMe;
    }

    public void setIs_send_by_me(String is_send_by_me) {
        this.isSendByMe = is_send_by_me;
    }

    public String getBoth_unread_count() {
        return bothUnreadCount;
    }

    public void setBoth_unread_count(String both_unread_count) {
        this.bothUnreadCount = both_unread_count;
    }

    public String getSender_device_token() {
        return sender_device_token;
    }

    public void setSender_device_token(String sender_device_token) {
        this.sender_device_token = sender_device_token;
    }

    public String getSender_firebase_token() {
        return sender_firebase_token;
    }

    public void setSender_firebase_token(String sender_firebase_token) {
        this.sender_firebase_token = sender_firebase_token;
    }

    public String getReceiver_device_token() {
        return receiver_device_token;
    }

    public void setReceiver_device_token(String receiver_device_token) {
        this.receiver_device_token = receiver_device_token;
    }

    public String getReceiver_firebase_token() {
        return receiver_firebase_token;
    }

    public void setReceiver_firebase_token(String receiver_firebase_token) {
        this.receiver_firebase_token = receiver_firebase_token;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "sender = '" + sender + '\'' +
                        ",recipient = '" + recipient + '\'' +
                        ",convertsations_date = '" + convertsationsDate + '\'' +
                        ",wholename = '" + wholename + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}