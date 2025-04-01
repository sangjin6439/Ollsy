package kr.ollsy.global.util;

import java.util.UUID;

public class NicknameGenerator {

    public static String generateNickname() {

        String baseNickname = "Ollsy";
        String randomNickname = UUID.randomUUID().toString().substring(0, 8);

        return baseNickname + randomNickname;
    }
}