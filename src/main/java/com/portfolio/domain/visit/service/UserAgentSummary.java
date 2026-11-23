package com.portfolio.domain.visit.service;

public final class UserAgentSummary {

    private UserAgentSummary() {
    }

    public static String device(String ua) {
        if (ua == null) return "알 수 없음";
        if (ua.contains("iPad") || (ua.contains("Android") && !ua.contains("Mobile"))) return "태블릿";
        if (ua.contains("Mobi") || ua.contains("iPhone") || ua.contains("Android")) return "모바일";
        return "PC";
    }

    public static String browser(String ua) {
        if (ua == null) return "알 수 없음";
        if (ua.contains("KAKAOTALK")) return "카카오톡";
        if (ua.contains("NAVER(inapp")) return "네이버 앱";
        if (ua.contains("Whale/")) return "웨일";
        if (ua.contains("SamsungBrowser/")) return "삼성 인터넷";
        if (ua.contains("Edg/") || ua.contains("EdgA/") || ua.contains("EdgiOS/")) return "Edge";
        if (ua.contains("Firefox/") || ua.contains("FxiOS/")) return "Firefox";
        if (ua.contains("CriOS/") || ua.contains("Chrome/")) return "Chrome";
        if (ua.contains("Safari/")) return "Safari";
        return "기타";
    }

    public static String os(String ua) {
        if (ua == null) return "알 수 없음";
        if (ua.contains("iPhone") || ua.contains("iPad")) return "iOS";
        if (ua.contains("Android")) return "Android";
        if (ua.contains("Windows")) return "Windows";
        if (ua.contains("Mac OS X") || ua.contains("Macintosh")) return "macOS";
        if (ua.contains("Linux")) return "Linux";
        return "기타";
    }
}
