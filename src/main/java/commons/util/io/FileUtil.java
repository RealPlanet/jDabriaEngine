package commons.util.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import commons.util.logging.EngineLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static void WriteToFile(Path filePath, String contents){
        EngineLogger.log("Attempt to write file to \"" + filePath + "\"");
        String actualFilePath = filePath.normalize().toString();
        try{
            FileWriter writer = new FileWriter(actualFilePath);
            writer.write(contents);
            writer.close();
        }
        catch(IOException e){
            EngineLogger.logError("Could not write file to \"" + actualFilePath + "\": \n" + e);
        }
    }

    public static void WriteToFile(byte[] data, Path filePath){
        String actualFilePath = filePath.normalize().toString();
        try{
            OutputStream os = new FileOutputStream(actualFilePath);
            os.write(data);
            os.close();
        }
        catch(IOException e){
            EngineLogger.logError("Could not write file to \"" + actualFilePath + "\": \n" + e);
        }
    }

    /**
     * Reads file contents
     * @param filePath the file path to read from.
     * @return returns String containing the data or null
     */
    public static @Nullable String readFromFile(Path filePath){
        EngineLogger.log("Attempt to read file from \"" + filePath + "\"");

        try{
            return new String(Files.readAllBytes(filePath));
        }catch(IOException e){
            EngineLogger.logError("Could not read file from \"" + filePath +"\"" + e);
        }

        return null;
    }

    public static byte[] readBytesFromFile(Path filePath){
        EngineLogger.log("Attempt to read file from \"" + filePath + "\"");

        try{
            return Files.readAllBytes(filePath);
        }catch(IOException e){
            EngineLogger.logError("Could not read file from \"" + filePath +"\"" + e);
        }

        return null;
    }

    /**
     * Safely parses a json string with GSON without throwing.
     * @param parser The GSON parser to use for the json data.
     * @param clazz The T.class used to cast the parsed data
     * @param data The string reference pointing to the json data.
     * @param <T> The return type of the parsed objects
     * @return The object (or objects) parsed from the json or null
     */
    public static <T> T jsonSafeParse(@NotNull Gson parser, Class<T> clazz, String data){
        if(data == null || data.equals("")){
            return null;
        }

        try{
            return parser.fromJson(data, clazz);
        }catch (JsonSyntaxException e){
            EngineLogger.logWarning("Could not parse JSON, received data was: \n" + data);
            return null;
        }
    }

    /**
     * Checks if a file exists
     * @param filePath file path to check
     * @return true if file exists
     */
    public static boolean exists(String filePath){
        return Files.exists(Paths.get(filePath));
    }
}
