package com.education.prolab4_3;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;

public class JsonLoader {
    public static TransportData load(String path) {
        try (Reader reader = new FileReader(path)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, TransportData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

