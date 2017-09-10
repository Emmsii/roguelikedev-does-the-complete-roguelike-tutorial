package com.mac.rltut.engine.web;


import com.esotericsoftware.kryo.io.Input;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.InflaterInputStream;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/08/2017 at 02:19 PM.
 */
public class HttpRequest {

    private final String USER_AGENT = "Mozilla/5.0";
    private final URL url;
    
    public HttpRequest() throws IOException {
        this.url = get();
    }
    
    private URL get() throws IOException {
        File file = new File("s");
        FileInputStream fileInputStream = null;
        InflaterInputStream inflaterInputStream = null;
        Input input = null;
        URL url = null;
        
        fileInputStream = new FileInputStream(file);
        inflaterInputStream = new InflaterInputStream(fileInputStream);
        input = new Input(inflaterInputStream);
        url = new URL(input.readString());
        input.close();
        inflaterInputStream.close();
        fileInputStream.close();
        return url;
    }
    
    
    public String getScores() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        return readResponse(connection.getInputStream());
    }
    
    public String sendScore(String id, String name, int score, int deathLevel, long timeTaken, String endTime, String version) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String params = "id=" + id + "&" +
                        "name=" + name + "&" + 
                        "score=" + score + "&" +
                        "death_level=" + deathLevel + "&" +
                        "time_taken=" + timeTaken + "&" +
                        "end_time=" + endTime + "&" +
                        "version=" + version;
        
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(params);
        outputStream.flush();
        outputStream.close();

        return readResponse(connection.getInputStream());
    }
    
    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = reader.readLine()) != null) response.append(inputLine);
        reader.close();

        return response.toString();
    }
}
