package farm.pickapp.candidates.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

public class DeviceIdRepository {
    private static final String REGISTRATION_FILE = "REGISTRATION_FILE";
    private static final String KEY_DEVICE_ID = "DEVICE_ID";

    private final SharedPreferences sharedPreferences;

    public DeviceIdRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(REGISTRATION_FILE, Context.MODE_PRIVATE);
    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit()
                .putString(KEY_DEVICE_ID, deviceId)
                .apply();
    }

    public String getDeviceId() {
        return sharedPreferences.getString(KEY_DEVICE_ID, null);
    }

    public boolean isDeviceIdEmpty() {
        String deviceId = sharedPreferences.getString(KEY_DEVICE_ID, null);
        return deviceId == null;
    }}
