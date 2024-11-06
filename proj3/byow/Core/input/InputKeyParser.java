//package byow.Core.input;
//
//import byow.Core.Engine;
//import byow.Core.commands.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class InputKeyParser extends AbstractInputParser {
//    public InputKeyParser(InputSource inputSource, Engine engine) {
//        super(inputSource, engine);
//    }
//
//    @Override
//    protected List<Command> prepareForSeeding() {
//        engine.displayMenuForSeed();
//        System.out.println("SEED");
//        List<Command> result = new ArrayList<>();
//        return result;
//    }
//
//    @Override
//    protected List<Command> handleNewGame(String seed) {
//        return null;
//    }
//
//    protected List<Command> handleNewGame() {
//        List<Command> result = new ArrayList<>();
//        return result;
//    }
//
//    @Override
//    protected List<Command> handleLoad() {
//        return null;
//    }
//
//    @Override
//    protected List<Command> handleMoveLeft(char nextKey) {
//        return null;
//    }
//
//    @Override
//    protected List<Command> handleMoveRight(char nextKey) {
//        return null;
//    }
//
//    @Override
//    protected List<Command> handleMoveUp(char nextKey) {
//        return null;
//    }
//
//    @Override
//    protected List<Command> handleMoveDown(char nextKey) {
//        return null;
//    }
//
//    @Override
//    protected List<Command> handleToggleFocus() {
//        return null;
//    }
//
//    @Override
//    protected List<Command> handleQuit() {
//        return null;
//    }
//
//    public String parse1() {
//        char nextKeyLowerCase = nextKeyLowerCase();
//        switch (nextKeyLowerCase) {
//            case 'n':
//                engine.displayMenuForSeed();
//                return handleSeedInput();
//            case 'l':
//                return engine.readHistory();
//            case ':':
//                handleQuitInput();
//                break;
//            case 'a':
//            case 's':
//            case 'd':
//            case 'w':
//            case 'f':
//                return Character.toString(nextKeyLowerCase);
//            default:
//                return null;
//        }
//        return null;
//    }
//
//    private void handleQuitInput() {
//        if (inputSource.possibleNextInput()) {
//            char nextInput = nextKeyLowerCase();
//            if (isQuit(nextInput)) {
//                engine.save();
//                engine.quit();
//            }
//        }
//    }
//
//    private String handleSeedInput() {
//        String seed = "";
//        while (true) {
//            char nextKey = getNextDigitOrSeedEnd();
//            if (isSeedEnd(nextKey)) {
//                break;
//            }
//            seed += nextKey;
//            engine.displayMenuForSeed(seed);
//        }
//        return "n" + seed + "s";
//    }
//    private long handleSeedInput2() {
//        String seed = "";
//        while (true) {
//            char nextKey = getNextDigitOrSeedEnd();
//            if (isSeedEnd(nextKey)) {
//                break;
//            }
//            seed += nextKey;
//            engine.displayMenuForSeed(seed);
//        }
//        return Long.parseLong(seed);
//    }
//}
