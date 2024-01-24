package com.example.chessappgroupd.domain.friendslist;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "friend_request",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sender", "receiver"})
        }
)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIENDREQUEST_ID")
    private Long id;
    private String sender;

    private String receiver;
    private boolean pending;

    public FriendRequest(String sender,
                         String receiver,
                         boolean pending) {
        this.sender = sender;
        this.receiver = receiver;
        this.pending = pending;
    }

    protected FriendRequest() {

    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean pending() {return pending;}

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setStatus(boolean pending) {
        this.pending = pending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(sender, that.sender) && Objects.equals(receiver, that.receiver) && pending == that.pending;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, pending);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", sender is " + sender +
                ", receicver is =" + receiver +
                ", status:" + pending +
                '}';
    }

}
