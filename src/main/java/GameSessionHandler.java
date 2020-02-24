
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;

public class GameSessionHandler {
    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
}