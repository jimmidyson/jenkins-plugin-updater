package io.fabric8.jenkins;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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

        String pluginsUrl = args[0];
        if (StringUtils.isEmpty(pluginsUrl)){
            throw new Exception("No plugins.txt URL found.  Please pass URL as -D arg.");
        }

        String pluginsTxt = IOUtils.toString(new URL(pluginsUrl), Charset.forName("UTF-8"));
        if (StringUtils.isEmpty(pluginsUrl)){
            throw new Exception("No plugins.txt content found at " + pluginsUrl);
        }

        String txt = IOUtils.toString(new URL("http://ftp.icm.edu.pl/packages/jenkins/updates/current/update-center.json"), Charset.forName("UTF-8"));
        txt = txt.substring(19);
        JSONObject jenkinsUpdateJson = new JSONObject(txt);

        File file = new File(pluginsTxt);

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
