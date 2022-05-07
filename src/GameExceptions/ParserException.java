package GameExceptions;

public class ParserException extends Exception {

    public ParserException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return "Parse error: " + super.getMessage();
    }

}
