package byow.Core.tests;

import byow.Core.Engine;
import byow.Core.commands.Command;
import byow.Core.input.InputParser;
import byow.Core.input.InputSource;
import byow.Core.input.StringInputDevice;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InputParserTest {

    @Test
    public void testNewWorld() {
        String seedAsSting = "8004217737854698935";
        InputSource inputSource = new StringInputDevice("n" + seedAsSting + "s");
        InputParser parser = new InputParser(inputSource, new Engine());
        List<Command> commands = parser.parse();
        assertEquals(commands.size(), 1);
        String expected = "NewWorldCommand " + seedAsSting;
        assertEquals(expected, commands.get(0).toString());
    }
}
