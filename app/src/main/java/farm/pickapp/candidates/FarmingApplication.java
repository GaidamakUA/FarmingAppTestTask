package farm.pickapp.candidates;

import android.app.Application;

import com.facebook.stetho.Stetho;

import farm.pickapp.candidates.data.DeviceIdRepository;
import farm.pickapp.candidates.data.LastUpdatedRepository;
import farm.pickapp.candidates.data.name_repository.NameRepository;
import farm.pickapp.candidates.data.ping_repository.PingRepository;
import farm.pickapp.candidates.data.network.FarmingApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FarmingApplication extends Application {
    // Simple implementation of Service Locator pattern
    private static FarmingApiService farmingApiService;
    private static PingRepository pingRepository;
    private static DeviceIdRepository deviceIdRepository;
    private static NameRepository nameRepository;
    private static LastUpdatedRepository lastUpdatedRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initApiService();
        pingRepository = new PingRepository(this);
        deviceIdRepository = new DeviceIdRepository(this);
        initNameRepository();
        lastUpdatedRepository = new LastUpdatedRepository(this);
    }

    private void initNameRepository() {
        String deviceId = deviceIdRepository.getDeviceId();
        nameRepository = new NameRepository(this, farmingApiService, deviceId);
    }

    private void initApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://dev.pickapp.farm/cTest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        farmingApiService = retrofit.create(FarmingApiService.class);
    }

    public static FarmingApiService getFarmingApiService() {
        return farmingApiService;
    }

    public static PingRepository getPingRepository() {
        return pingRepository;
    }

    public static DeviceIdRepository getDeviceIdRepository() {
        return deviceIdRepository;
    }

    public static NameRepository getNameRepository() {
        return nameRepository;
    }

    public static LastUpdatedRepository getLastUpdatedRepository() {
        return lastUpdatedRepository;
    }
}
