package farm.pickapp.candidates.data.name_repository;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import farm.pickapp.candidates.FarmingApplication;

public class FarmingMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String deviceName = remoteMessage.getData().get("deviceName");
        FarmingApplication.getNameRepository().setName(deviceName);
    }
}
