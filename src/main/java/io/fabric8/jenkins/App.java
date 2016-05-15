package io.fabric8.jenkins;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Updates jenkins plugin versions that are defined in Jenkins plugins.txt used in Docker images
 */
public class App {
    public static void main(String[] args) throws Exception {

        String txt = IOUtils.toString(new URL("http://ftp.icm.edu.pl/packages/jenkins/updates/current/update-center.json"), Charset.forName("UTF-8"));
        txt = txt.substring(19);
        JSONObject jenkinsUpdateJson = new JSONObject(txt);

        if (jenkinsUpdateJson == null) {
            throw new Exception("No update JSON found");
        }

        ClassLoader classLoader = App.class.getClassLoader();
        URL url = classLoader.getResource("plugins.txt");
        if (url == null) {
            throw new Exception("No plugins.txt file found in src/main/resources");
        }
        File file = new File(url.getFile());

        StringBuilder result = new StringBuilder("");
        Map<String, String> plugins = new TreeMap<String, String>();
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] split = line.split(":");

                String name = split[0];

                plugins.put(name, jenkinsUpdateJson.getJSONObject("plugins").getJSONObject(name).getString("version"));

            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator it = plugins.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + ":" + pair.getValue());
        }
        System.out.println("Found " + plugins.size() + " plugins");


    }


}
