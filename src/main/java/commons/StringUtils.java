package commons;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringUtils {
    public static @NotNull String format(@Nullable String str, @Nullable Object... formats) {
        if (str == null) return "";
        if (formats.length == 0) return str;

        final char BEGIN_FORMAT = '{';
        final char END_FORMAT = '}';

        StringBuilder result = new StringBuilder(str.length());
        boolean isGettingIndex = false;
        int formatIndex = -1;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // If we're getting a format Index
            if (isGettingIndex) {
                // If we've got to the end of the format Index
                if (c == END_FORMAT) {
                    // We're not getting the index anymore
                    isGettingIndex = false;
                    // If the Index is within the specified formats
                    if (formatIndex >= 0 && formatIndex < formats.length)
                        // Append the Format
                        result.append(formats[formatIndex]);
                    else {
                        // Else if it's not a valid Format we put the index back in the String
                        result
                                .append(BEGIN_FORMAT)
                                .append(formatIndex >= 0 ? formatIndex : "")
                                .append(END_FORMAT);
                    }
                    // If the current char is a digit
                } else if (Character.isDigit(c)) {
                    // Update the Index
                    if (formatIndex < 0)
                        formatIndex = 0;
                    else formatIndex *= 10;
                    formatIndex += c - '0';
                } else {
                    // If it's not a valid digit and the format didn't end put everything back
                    isGettingIndex = false;
                    result
                            .append(BEGIN_FORMAT)
                            .append(formatIndex >= 0 ? formatIndex : "")
                            .append(c);
                }
                // If we're starting to get an Index
            } else if (c == BEGIN_FORMAT) {
                formatIndex = -1;
                isGettingIndex = true;
                // Else put the char in the String
            } else result.append(c);
        }

        return result.toString();
    }
}
