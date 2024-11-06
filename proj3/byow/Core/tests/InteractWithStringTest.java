package byow.Core.tests;

import byow.Core.Engine;
import byow.TileEngine.TETile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InteractWithStringTest {

    @Test
    public void testSameInputSameResult() {
        // Engine: Test same input string with no movement leads to same tile array (50/50)
        String input = "n8004217737854698935s";
        Engine engine = new Engine();
        assertEquals(engine.interactWithInputString(input), engine.interactWithInputString(input));
    }

    @Test
    public void testDifferentInputDifferentResult() {
        // Engine: Test diff input strings with no movement lead to diff tile array (50/50)
        String input = "n8004217737854698935s";
        String otherInput = "n7341909481878015308s";
        Engine engine = new Engine();
        assertNotEquals(engine.interactWithInputString(input), engine.interactWithInputString(otherInput));
    }

    @Test
    public void testSplitInputToMultipleWithSaveLoad() {
        // Game: Test splitting an input into multiple inputs with save/loads results in same array as single input (0/33.333)
        String totalInput = "n7193300625454684331saaawasdaawdwsd";
        String inputStart = "n7193300625454684331saaawasdaawd:q";
        String inputEnd = "lwsd";
        Engine engine = new Engine();
        TETile[][] expected = engine.interactWithInputString(totalInput);
        engine = new Engine();
        engine.interactWithInputString(inputStart);
        engine = new Engine();
        TETile[][] actual = engine.interactWithInputString(inputEnd);
        assertEquals(expected, actual);
    }
}
