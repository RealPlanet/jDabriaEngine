package commons.util;

import commons.util.logging.EngineLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

@SuppressWarnings("unused")
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

    public static @Nullable String encode64(String str){
        try {
            return Base64.getEncoder()
                    .encodeToString(str.getBytes(StandardCharsets.UTF_8.toString()));
        } catch(UnsupportedEncodingException ex) {
            EngineLogger.logError("Could not encode64 this string: \n" + str);
            return null;
        }
    }

    public static @Nullable String decode64(String str){
        try {
            return new String(Base64.getDecoder()
                    .decode(str));
        } catch(IllegalArgumentException ex) {
            EngineLogger.logError("Could not decode64 this string: \n" + str);
            return null;
        }
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull String bytesToString(byte[] bytes){
        return new String(bytes);
    }

    public static byte @NotNull [] compressString(@NotNull String str){
        return compressBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    public static byte @NotNull [] compressBytes(byte[] data){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(os)) {
            dos.write(data);
        }
        catch (IOException e){
            EngineLogger.logError("Could not compress byte array: \n" + e);
        }
        return os.toByteArray();
    }

    public static byte @NotNull [] decompressBytes(byte[] data){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (OutputStream ios = new InflaterOutputStream(os)) {
            ios.write(data);
        }catch (IOException e){
            EngineLogger.logError("Could not decompress byte array: \n" + e);
        }

        return os.toByteArray();
    }

    public static @NotNull String getFileExtension(@NotNull File file) {
        String fileName = file.getName();
        int extIndex = fileName.lastIndexOf('.');
        if (extIndex <= 0) return "";
        return fileName.substring(extIndex + 1);
    }

    public static @NotNull String getFileName(@NotNull File file) {
        String fileName = file.getName();
        int extIndex = fileName.lastIndexOf('.');
        if (extIndex <= 0) return "";
        return fileName.substring(0, extIndex);
    }
}
