package farm.pickapp.candidates.data.network;

public class PingRequest {
    public final String deviceId;
    public final Ping[] pings;

    public PingRequest(String deviceId, Ping[] pings) {
        this.deviceId = deviceId;
        this.pings = pings;
    }

    public static class Ping {
        public final String timestamp;

        public Ping(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
