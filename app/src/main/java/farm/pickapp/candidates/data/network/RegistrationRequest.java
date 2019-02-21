package farm.pickapp.candidates.data.network;

public class RegistrationRequest {
    public final String deviceId;
    public final String fcmToken;

    public RegistrationRequest(String deviceId, String fcmToken) {
        this.deviceId = deviceId;
        this.fcmToken = fcmToken;
    }
}
