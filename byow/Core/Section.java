package byow.Core;
public class Section {
    int topLeftY, topLeftX;
    int bottomRightY, bottomRightX;

    Room room;

    public Section(int topLeftY, int topLeftX, int bottomRightY, int bottomRightX) {
        this.topLeftY = topLeftY;
        this.topLeftX = topLeftX;
        this.bottomRightY = bottomRightY;
        this.bottomRightX = bottomRightX;
        this.room = null;
    }

    public int getTopLeftX() {
        return topLeftY;
    }

    public int getTopLeftY() {
        return topLeftX;
    }

    public int getBottomRightX() {
        return bottomRightY;
    }

    public int getBottomRightY() {
        return bottomRightX;
    }

    public int getSize() {
        return (bottomRightY - topLeftY) * (bottomRightX - topLeftX);
    }
}

