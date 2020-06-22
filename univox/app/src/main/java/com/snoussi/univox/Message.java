package com.snoussi.univox;

public class Message {
    private String messageText;
    private String from;
    private long messageTime;

    public Message() {
    }

    public Message(String messageText, String from, long messageTime) {
        this.messageText = messageText;
        this.from = from;
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
