package farm.pickapp.candidates.data.network;

public class ChangeNameRequest {
    private final String deviceId;
    private final String deviceName;

    public ChangeNameRequest(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }
}
