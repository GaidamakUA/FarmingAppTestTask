package farm.pickapp.candidates.data.ping_repository;

import android.app.Application;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class PingRepository {
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    private final PingDao pingDao;
    private final LiveData<List<Ping>> unreportedPings;

    public PingRepository(Application application) {
        PingDatabase database = PingDatabase.createDatabase(application);
        pingDao = database.pingDao();
        unreportedPings = pingDao.getUnreported();
    }

    public LiveData<List<Ping>> getUnreportedPings() {
        return unreportedPings;
    }

    public List<Ping> getUnreportedPingsSync() {
        return pingDao.getUnreportedSync();
    }

    public void createPing() {
        String timestamp = TIMESTAMP_FORMAT.format(new Date());
        Ping ping = new Ping(timestamp);
        new InsertAsyncTask(pingDao).execute(ping);
    }

    public void markPingsAsReported(List<Ping> pings) {
        for (Ping ping : pings) {
            ping.isReported = true;
        }
        new UpdateAsyncTask(pingDao).execute(pings.toArray(new Ping[0]));
    }

    private static class InsertAsyncTask extends AsyncTask<Ping, Void, Void> {
        private static final String DELAYED_REPORTING = "delayed_reporting";
        private final PingDao pingDao;

        public InsertAsyncTask(PingDao pingDao) {
            this.pingDao = pingDao;
        }

        @Override
        protected Void doInBackground(final Ping... pings) {
            pingDao.insert(pings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            WorkManager workManager = WorkManager.getInstance();
            workManager.cancelAllWorkByTag(DELAYED_REPORTING);
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ReportPingsWorker.class)
                    .addTag(DELAYED_REPORTING)
                    .setInitialDelay(15, TimeUnit.SECONDS)
                    .build();
            workManager.enqueue(workRequest);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Ping, Void, Void> {
        private final PingDao pingDao;

        private UpdateAsyncTask(PingDao pingDao) {
            this.pingDao = pingDao;
        }

        @Override
        protected Void doInBackground(Ping... pings) {
            pingDao.updatePings(pings);
            return null;
        }
    }
}
