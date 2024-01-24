package com.example.chessappgroupd.domain.friendslist;

import jakarta.persistence.Embeddable;

@Embeddable
public enum Visibility {
    PUBLIC, PRIVATE;
}
