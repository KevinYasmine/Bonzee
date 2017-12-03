public class RedToken extends AbstractToken {
    public static final String ANSI_RESET = "";
    public static final String ANSI_RED = "R";
    public RedToken() {
        color = "R";
    }
    public RedToken(AbstractToken redToken){
        this.color = redToken.color;
        this.posX = redToken.posX;
        this.posY = redToken.posY;
        this.direction = redToken.direction;
    }

    public String toString(){
        return ANSI_RED + "" + ANSI_RESET;
    }
}
