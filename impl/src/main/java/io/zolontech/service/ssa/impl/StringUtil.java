package io.zolontech.service.ssa.impl;

import java.util.Set;

/**
 *
 */
public class StringUtil {
    static final String commaSeparated(final Set<String> values) {
        final StringBuilder sb = new StringBuilder();
        for (final String value : values) {
            if (value == null || value.isEmpty()) {
                continue;
            }
            if (sb.length() == 0) {
                sb.append(value);
            } else {
                sb.append(",").append(value);
            }
        }
        return sb.toString();
    }


}
