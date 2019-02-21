package farm.pickapp.candidates.data.name_repository;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import farm.pickapp.candidates.data.network.ChangeNameRequest;
import farm.pickapp.candidates.data.network.FarmingApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NameRepository {
    private static final String KEY_NAME_PREFERENCE = "NAME_PREFERENCE";
    private final SharedPreferences preferences;
    private final MutableLiveData<String> nameLiveData = new MutableLiveData<>();
    private final FarmingApiService farmingApiService;
    private final String deviceId;

    public NameRepository(Context context, FarmingApiService farmingApiService, String deviceId) {
        preferences = context.getSharedPreferences(KEY_NAME_PREFERENCE, Context.MODE_PRIVATE);
        String name = preferences.getString(KEY_NAME_PREFERENCE, null);
        setName(name);

        this.deviceId = deviceId;
        this.farmingApiService = farmingApiService;
    }

    public LiveData<String> getName() {
        return nameLiveData;
    }

    public void updateName(String name) {
        setName(name);
        ChangeNameRequest request = new ChangeNameRequest(deviceId, name);
        farmingApiService.changeName(request).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // TODO Handle failure
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    void setName(String name) {
        if (Objects.equals(nameLiveData.getValue(), name)) return;

        nameLiveData.setValue(name);
        preferences.edit()
                .putString(KEY_NAME_PREFERENCE, name)
                .apply();
    }
}
