/*
 * To change this template, choose Tools | Templates
 * and open the template inputstream the editor.
 */
package open.dolphin.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author
 */
public class FileTool {

    /**
     *
     * @param inputstream
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream inputstream, String encoding) throws IOException {

        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputstream, encoding));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            reader.close();
        }
        return result.toString();
    }

    /**
     *
     * @param filename
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String fileToString(String filename, String encoding) throws IOException {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding));
            StringBuffer buffer = new StringBuffer();
            int ch;
            while ((ch = reader.read()) != -1) {
                buffer.append((char) ch);
            }
            return buffer.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * 
     * @param filename
     * @param data
     * @param encoding
     * @throws IOException
     */
    public static void stringToFile(String filename, String data, String encoding) throws IOException {

        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);
            writer.write(data);
        } finally {
            writer.close();
        }
    }
}
