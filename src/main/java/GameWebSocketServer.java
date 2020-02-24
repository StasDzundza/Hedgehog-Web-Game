import com.google.gson.Gson;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/gameActions")
public class GameWebSocketServer {

    private GameSessionHandler sessionHandler = new GameSessionHandler();
    private Game game;

    @OnOpen
    public void open(Session session) throws IOException {
        game = new Game();
        sessionHandler.addSession(session);
        sendConfigurationToClient(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
        game = null;
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(GameWebSocketServer.class.getName()).log(Level.SEVERE, error.getMessage(), error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        if(message == "restart"){
            game = new Game();
            try {
                sendConfigurationToClient(session);
            } catch (IOException e) {
                Logger.getLogger(GameWebSocketServer.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }else {
            Gson gson = new Gson();
            game.moveHedgehog(message);
            boolean gameOver = game.getGameStatus();
            boolean hedgehogHasApple = game.isHedgehogHasApple();
            game.takeAppleFromHedgehog();
            Map<String, String> map = new LinkedHashMap<>();
            map.put("messageType", "gameInfo");
            map.put("hedgehogXPos", Integer.toString(game.getHedgehogPosition().x));
            map.put("hedgehogYPos", Integer.toString(game.getHedgehogPosition().y));
            map.put("appleEat", Boolean.toString(hedgehogHasApple));
            map.put("gameOver", Boolean.toString(gameOver));
            String jsonAnswer = gson.toJson(map);
            try {
                session.getBasicRemote().sendText(jsonAnswer);
            } catch (IOException error) {
                Logger.getLogger(GameWebSocketServer.class.getName()).log(Level.SEVERE, error.getMessage(), error);
            }
        }
    }

    private void sendConfigurationToClient(Session session) throws IOException {
        Gson gson = new Gson();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("messageType", "build");
        map.put("hedgehogXPos", Integer.toString(game.getHedgehogPosition().x));
        map.put("hedgehogYPos", Integer.toString(game.getHedgehogPosition().y));
        map.put("fieldSize", Integer.toString(game.getFieldSize()));
        String jsonAnswer = gson.toJson(map);
        session.getBasicRemote().sendText(jsonAnswer);
        map.clear();
        ArrayList<Point> applePositions = game.getApplePositions();
        for (Point position : applePositions) {
            map.put("messageType", "applePosition");
            map.put("appleXPos", Integer.toString(position.x));
            map.put("appleYPos", Integer.toString(position.y));
            jsonAnswer = gson.toJson(map);
            session.getBasicRemote().sendText(jsonAnswer);
            map.clear();
        }
    }
}    