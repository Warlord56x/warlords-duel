package GameExceptions;

public final class CommandException extends Exception {

    public CommandException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return "Command error: " + super.getMessage();
    }
}
