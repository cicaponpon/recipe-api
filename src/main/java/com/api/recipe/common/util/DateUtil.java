package com.api.recipe.common.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateUtil {
    private DateUtil() {
    }

    public static OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }
}
