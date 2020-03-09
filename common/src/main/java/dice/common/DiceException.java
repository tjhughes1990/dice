package dice.common;

/**
 * Dice exception class.
 */
public class DiceException extends Exception {

    private static final long serialVersionUID = 482767712991432482L;

    /**
     * Constructor.
     *
     * @param errMsg
     *            the error message.
     */
    public DiceException(final String errMsg) {
        super(errMsg);
    }

    /**
     * Constructor.
     *
     * @param errMsg
     *            the error message.
     * @param cause
     *            the cause of the exception.
     */
    public DiceException(final String errMsg, final Exception cause) {
        super(errMsg, cause);
    }
}
