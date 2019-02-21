package farm.pickapp.candidates.data.network;

public class PingResponse {
    public final boolean[] ok;

    PingResponse(boolean[] ok) {
        this.ok = ok;
    }
}
