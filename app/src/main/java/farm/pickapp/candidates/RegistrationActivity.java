package farm.pickapp.candidates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import farm.pickapp.candidates.data.network.RegistrationRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";

    private Call<Object> registerCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this, task -> {
            if (!isResumed()) return;

            String token = task.getResult().getToken();
            // Recommended way to work with unique identifiers
            // https://developer.android.com/training/articles/user-data-ids
            final String deviceId = UUID.randomUUID().toString();
            Log.d(TAG, "token:" + token);

            // Activity shouldn't interact with network directly, but it's way too simple to bother
            registerCall = FarmingApplication.getFarmingApiService().register(new RegistrationRequest(deviceId, token));
            registerCall.enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    if (response.isSuccessful()) {
                        FarmingApplication.getDeviceIdRepository().setDeviceId(deviceId);
                        FarmingApplication.getLastUpdatedRepository().setLastUpdated(new Date());
                        if (isResumed()) {
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (registerCall != null && (!registerCall.isExecuted() || !registerCall.isCanceled())) {
            registerCall.cancel();
        }
    }

    private boolean isResumed() {
        return getLifecycle().getCurrentState() == Lifecycle.State.RESUMED;
    }
}
