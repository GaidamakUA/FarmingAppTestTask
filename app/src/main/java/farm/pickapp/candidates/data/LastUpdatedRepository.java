package farm.pickapp.candidates.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LastUpdatedRepository {
    private static final String KEY_LAST_UPDATED_PREFERENCE = "LAST_UPDATED_PREFERENCE";
    private final SharedPreferences preferences;
    private final MutableLiveData<Date> lastUpdatedLiveData = new MutableLiveData<>();

    public LastUpdatedRepository(Context context) {
        preferences = context.getSharedPreferences(KEY_LAST_UPDATED_PREFERENCE, Context.MODE_PRIVATE);
        long initialDate = preferences.getLong(KEY_LAST_UPDATED_PREFERENCE, 0);
        lastUpdatedLiveData.setValue(new Date(initialDate));
    }

    public void setLastUpdated(Date date) {
        preferences.edit()
                .putLong(KEY_LAST_UPDATED_PREFERENCE, date.getTime())
                .apply();
        lastUpdatedLiveData.setValue(date);
    }

    public LiveData<Date> getLastUpdated() {
        return lastUpdatedLiveData;
    }
}
