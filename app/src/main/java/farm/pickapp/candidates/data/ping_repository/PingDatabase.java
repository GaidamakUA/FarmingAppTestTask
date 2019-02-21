package farm.pickapp.candidates.data.ping_repository;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Ping.class}, version = 1)
public abstract class PingDatabase extends RoomDatabase {
    public abstract PingDao pingDao();

    public static PingDatabase createDatabase(Application application) {
        return Room.databaseBuilder(application, PingDatabase.class, "ping").build();
    }
}
