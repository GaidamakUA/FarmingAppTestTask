package farm.pickapp.candidates.data.ping_repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PingDao {
    @Insert
    public void insert(Ping ping);

    @Query("SELECT * FROM ping WHERE isReported = 0")
    public LiveData<List<Ping>> getUnreported();

    @Query("SELECT * FROM ping WHERE isReported = 0")
    public List<Ping> getUnreportedSync();

    @Update
    public void updatePings(Ping[] pings);
}
