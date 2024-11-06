package byow.Core.engine.input;

/**
 * Created by hug.
 */
public interface InputSource {
    char getNextKey();

    boolean possibleNextInput();
}
