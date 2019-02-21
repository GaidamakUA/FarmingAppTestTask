package farm.pickapp.candidates.data.network;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FarmingApiService {
    // TODO Added interceptor instead of copypasting @Part("data")
    @Multipart
    @POST("registerDevice/index.php")
    Call<Object> register(@Part("data") RegistrationRequest request);

    @Multipart
    @POST("ping/index.php")
    Call<PingResponse> ping(@Part("data") PingRequest request);

    @Multipart
    @POST("setDeviceName/index.php")
    Call<Object> changeName(@Part("data") ChangeNameRequest request);
}