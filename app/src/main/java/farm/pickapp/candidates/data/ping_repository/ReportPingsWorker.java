package farm.pickapp.candidates.data.ping_repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import farm.pickapp.candidates.FarmingApplication;
import farm.pickapp.candidates.data.network.PingRequest;
import farm.pickapp.candidates.data.network.PingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPingsWorker extends Worker {
    public ReportPingsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        PingRepository repository = FarmingApplication.getPingRepository();
        List<Ping> pings = repository.getUnreportedPingsSync();
        String deviceId = FarmingApplication.getDeviceIdRepository().getDeviceId();
        PingRequest request = new PingRequest(deviceId, mapPings(pings));
        FarmingApplication.getFarmingApiService().ping(request).enqueue(new Callback<PingResponse>() {
            @Override
            public void onResponse(Call<PingResponse> call, Response<PingResponse> response) {
                if (response.isSuccessful()) {
                    List<Ping> reportedPings = new ArrayList<>();
                    for (int i = 0; i < response.body().ok.length; i++) {
                        reportedPings.add(pings.get(i));
                    }
                    repository.markPingsAsReported(reportedPings);
                    // Probably it should be moved to interceptor
                    FarmingApplication.getLastUpdatedRepository().setLastUpdated(new Date());
                }
            }

            @Override
            public void onFailure(Call<PingResponse> call, Throwable t) {

            }
        });
        return Result.success();
    }

    private PingRequest.Ping[] mapPings(List<Ping> databasePings) {
        PingRequest.Ping[] toReturn = new PingRequest.Ping[databasePings.size()];
        for (int i = 0; i < databasePings.size(); i++) {
            String timestamp = databasePings.get(i).timestamp;
            toReturn[i] = new PingRequest.Ping(timestamp);
        }
        return toReturn;
    }
}
