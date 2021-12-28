package util;

import config.ConfigField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class UtilFile {
    public static List<String> readAllLine(String pathFile) throws IOException {
        Path path = Paths.get(pathFile);

        return Files.readAllLines(path);
    }
    public static List<String> filterLine(List<String> listLine)
    {
        List<String> result = new ArrayList<>();
        listLine.forEach(a ->{
            if(a.charAt(0) != '#')
            {
                result.add(a);
            }
        });
        return result;
    }
    public static ConfigField readConfigurationServer(String pathFile) throws IOException {

        ConfigField config = new ConfigField();
        List<String> listLineRead = readAllLine(pathFile);
        List<String> listLineFiltered = filterLine(listLineRead);
        listLineFiltered.forEach(s -> {
            StringTokenizer st = new StringTokenizer(s,"=");
            if(s.contains("SERVER"))
            {
                st.nextToken();
                config.setIpServer(st.nextToken());
            }else if(s.contains("TCPPORT"))
            {
                st.nextToken();
                config.setTcpPort(Integer.parseInt(st.nextToken()));
            } else if(s.contains("UDPPORT"))
            {
                st.nextToken();
                config.setUdpPort(Integer.parseInt(st.nextToken()));
            }else if(s.contains("MULTICAST"))
            {
                st.nextToken();
                config.setMulticastIp(st.nextToken());
            }else if(s.contains("MCASTPORT"))
            {
                st.nextToken();
                config.setMulticastPort(Integer.parseInt(st.nextToken()));
            }else if(s.contains("REGHOST"))
            {
                st.nextToken();
                config.setRegisterHost(st.nextToken());
            }else if(s.contains("REGPORT"))
            {
                st.nextToken();
                config.setRegisterPort(Integer.parseInt(st.nextToken()));
            }else if(s.contains("TIMEOUT"))
            {
                st.nextToken();
                config.setMsTimeout(Integer.parseInt(st.nextToken()));
            }
        });
        return config;
    }
}
