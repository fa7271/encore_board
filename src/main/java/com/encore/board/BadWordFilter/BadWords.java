package com.encore.board.BadWordFilter;

interface BadWords {
    String[] koreaWord1 = {
            "씨발", "병신", "ㅅㅂ", "ㅂㅅ", "닥쳐", "새끼", "시발", "개시발", "개씨발", "년", "년아", "개같은", "애미", "애비","naver","daum","google","com","net",
            "네이버","다음","구글"
    };

    String[] delimiters = { " ", ",", ".", "!", "?", "@", "1","2","3","4","5","6","7","8","9","0"};

}