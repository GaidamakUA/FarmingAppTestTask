package farm.pickapp.candidates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import farm.pickapp.candidates.data.name_repository.NameRepository;
import farm.pickapp.candidates.data.ping_repository.PingRepository;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPing();
        initName();
        initLastUpdated();
    }

    private void initLastUpdated() {
        DateFormat DATE_FORMAT = android.text.format.DateFormat.getDateFormat(this);
        DateFormat TIME_FORMAT = android.text.format.DateFormat.getTimeFormat(this);
        TextView lastUpdated = findViewById(R.id.lastUpdatedLabel);
        LiveData<Date> dateLiveData = FarmingApplication.getLastUpdatedRepository().getLastUpdated();
        dateLiveData.observe(this, date -> {
            String dateText = DATE_FORMAT.format(date);
            String timeText = TIME_FORMAT.format(date);
            lastUpdated.setText(getString(R.string.date_time_pattern, dateText, timeText));
        });
    }

    private void initPing() {
        PingRepository repository = FarmingApplication.getPingRepository();

        TextView pendingSyncLabel = findViewById(R.id.pendingSyncLabel);
        // Probably main activity shouldn't know about existence of Ping object
        // and should have only number
        repository.getUnreportedPings().observe(this, pings -> {
            String text = getString(R.string.pending_sync_pattern, pings.size());
            pendingSyncLabel.setText(text);
        });
        if (FarmingApplication.getDeviceIdRepository().isDeviceIdEmpty()) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }
        findViewById(R.id.pingButton).setOnClickListener(v -> {
            repository.createPing();
        });
    }

    private void initName() {
        EditText nameEditText = findViewById(R.id.nameEditText);
        NameRepository nameRepository = FarmingApplication.getNameRepository();
        nameRepository.getName().observe(this, nameEditText::setText);
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            nameRepository.updateName(nameEditText.getText().toString());
        });
    }
}
