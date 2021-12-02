package me.manaki.deepup.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

public class Utils {

    public static String format(Instant instant) {
        if (instant == null) return null;

        var now = Instant.now();

        var sub = (now.toEpochMilli() - instant.toEpochMilli()) / 1000;

        if (sub < 3600) return (sub / 60 + 1) + " phút trước";
        if (sub < 86400) return (sub / 3600 + 1) + " giờ trước";

        // 20d
        if (sub < 1728000) return (sub / 86400 + 1) + " ngày trước";

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(Date.from(instant));
    }

    public static <T> T notNullOr(T value, T def) {
        return Optional.of(value).orElse(def);
    }

}
