package farm.pickapp.candidates.data.ping_repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ping {
    @PrimaryKey
    Integer uid;
    @ColumnInfo
    String timestamp;
    @ColumnInfo
    boolean isReported;

    public Ping(String timestamp) {
        this.timestamp = timestamp;
        isReported = false;
    }
}
