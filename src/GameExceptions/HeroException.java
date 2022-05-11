package GameExceptions;

public class HeroException extends Exception {

    public HeroException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return "Hero error: " + super.getMessage();
    }
}
