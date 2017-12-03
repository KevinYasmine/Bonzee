public class GreenToken extends AbstractToken {
    public static final String ANSI_RESET = "";
    public static final String ANSI_GREEN = "G";
    public GreenToken() {
        color = "G";
    }
    public GreenToken(AbstractToken greenToken){
        this.color = greenToken.color;
        this.posX = greenToken.posX;
        this.posY = greenToken.posY;
        this.direction = greenToken.direction;
    }

    public String toString(){
        return ANSI_GREEN + "" + ANSI_RESET;
    }
}
