
public abstract class AbstractToken {

    int posX, posY;                   // position of the token on the board
    String direction;
    boolean onBlackCell;
    String color;

    public AbstractToken() {}


    public boolean isOnBlackCell() {
        if (posX%2==0 && posY%2==0){
            onBlackCell = true;
        }else if (posX%2!=0 && posY%2!=0){
            onBlackCell = true;
        }else{
            onBlackCell = false;
        }
        return onBlackCell;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String getColor(){
        return color;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}