import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final int FieldSize = 10;
    private final int NumberOfApples = 10;
    private int currentNumberOfApples;
    private boolean gameOver;
    private boolean hedgehogHasApple;
    private Point hedgehogPosition;
    private int [][] gameField;
    private ArrayList<Point>applePositions;

    public Game(){
        gameField = new int[FieldSize][FieldSize];
        currentNumberOfApples = NumberOfApples;
        hedgehogHasApple = gameOver = false;
        hedgehogPosition = new Point();
        hedgehogPosition.x = hedgehogPosition.y = 0;
        applePositions = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < NumberOfApples; i++){
            int appleXPos;
            int appleYPos;
            do{
                appleXPos = r.nextInt(10);
                appleYPos = r.nextInt(10);
            }while (appleXPos == hedgehogPosition.x && appleYPos == hedgehogPosition.y);
            applePositions.add(new Point(appleXPos,appleYPos));
            gameField[appleYPos][appleXPos] = 1;
        }
    }

    public void moveHedgehog(String moveDirection){
        switch (moveDirection){
            case "up":
                if(hedgehogPosition.y-1<0){
                    hedgehogPosition.y = FieldSize - 1;
                }else{
                    hedgehogPosition.y -= 1;
                }
                break;
            case "down":
                if(hedgehogPosition.y+1 > FieldSize-1){
                    hedgehogPosition.y = 0;
                }else{
                    hedgehogPosition.y += 1;
                }
                break;
            case "left":
                if(hedgehogPosition.x-1<0){
                    hedgehogPosition.x = FieldSize - 1;
                }else{
                    hedgehogPosition.x -= 1;
                }
                break;
            case "right":
                if(hedgehogPosition.x+1 > FieldSize-1){
                    hedgehogPosition.x = 0;
                }else{
                    hedgehogPosition.x += 1;
                }
                break;
            default:
                break;
        }
        if(gameField[hedgehogPosition.y][hedgehogPosition.x] == 1){
            gameField[hedgehogPosition.y][hedgehogPosition.x] = 0;
            hedgehogHasApple = true;
            currentNumberOfApples--;
        }
        if(currentNumberOfApples == 0){
            gameOver = true;
        }
    }

    public int getFieldSize() {
        return FieldSize;
    }

    public void takeAppleFromHedgehog(){
        hedgehogHasApple = false;
    }

    public Point getHedgehogPosition(){
        return hedgehogPosition;
    }

    public boolean getGameStatus(){
        return gameOver;
    }

    public boolean isHedgehogHasApple() {
        return hedgehogHasApple;
    }

    public ArrayList<Point> getApplePositions(){
        return applePositions;
    }
}
