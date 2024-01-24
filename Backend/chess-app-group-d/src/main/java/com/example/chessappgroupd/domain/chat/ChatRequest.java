package com.example.chessappgroupd.domain.chat;

import java.util.List;

public record ChatRequest(String chatId, List<String> subs) {
}
