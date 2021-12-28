package config;

public class ConfigField {
    private String ipServer;
    private Integer tcpPort;
    private Integer udpPort;
    private String multicastIp;
    private Integer multicastPort;
    private String registerHost;
    private Integer registerPort;
    private Integer msTimeout;

    public ConfigField() {
    }

    public String getIpServer() {
        return ipServer;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public Integer getUdpPort() {
        return udpPort;
    }

    public String getMulticastIp() {
        return multicastIp;
    }

    public Integer getMulticastPort() {
        return multicastPort;
    }

    public String getRegisterHost() {
        return registerHost;
    }

    public Integer getRegisterPort() {
        return registerPort;
    }

    public Integer getMsTimeout() {
        return msTimeout;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void setUdpPort(Integer udpPort) {
        this.udpPort = udpPort;
    }

    public void setMulticastIp(String multicastIp) {
        this.multicastIp = multicastIp;
    }

    public void setMulticastPort(Integer multicastPort) {
        this.multicastPort = multicastPort;
    }

    public void setRegisterHost(String registerHost) {
        this.registerHost = registerHost;
    }

    public void setRegisterPort(Integer registerPort) {
        this.registerPort = registerPort;
    }

    public void setMsTimeout(Integer msTimeout) {
        this.msTimeout = msTimeout;
    }

    @Override
    public String toString() {
        return "ConfigField{" +
                "ipServer='" + ipServer + '\'' +
                ", tcpPort=" + tcpPort +
                ", udpPort=" + udpPort +
                ", multicastIp='" + multicastIp + '\'' +
                ", multicastPort=" + multicastPort +
                ", registerHost='" + registerHost + '\'' +
                ", registerPort=" + registerPort +
                ", msTimeout=" + msTimeout +
                '}';
    }
}
