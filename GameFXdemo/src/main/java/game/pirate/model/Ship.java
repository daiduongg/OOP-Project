package game.pirate.model;

public class Ship {
    public static final int START_X = 230;
    public static final int START_Y = 44;

    private int currentIsland;
    private int currentPosX;
    private int currentPosY;

    public Ship() {
        currentIsland = 0;
        currentPosX = START_X;
        currentPosY = START_Y;
    }

    public int getCurrentIsland() {
        return currentIsland;
    }

    public void setCurrentIsland(int currentIsland) {
        this.currentIsland = currentIsland;
    }

    public void increaseCurrentIsland() {
        if (currentIsland < 23) {
            currentIsland++;
        }
    }

    public void decreaseCurrentIsland() {
        if (currentIsland > 0) {
            currentIsland--;
        }
    }

    public int getCurrentPosX() {
        return currentPosX;
    }

    public void setCurrentPosX(int currentPosX) {
        this.currentPosX = currentPosX;
    }

    public int getCurrentPosY() {
        return currentPosY;
    }

    public void setCurrentPosY(int currentPosY) {
        this.currentPosY = currentPosY;
    }
}
