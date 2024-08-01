package flik;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlikTest {
    @Test
    public void testIsSameNumber() {
        Integer i = 500;
        Integer j = 500;
        assertEquals(i, j);
        assertTrue(i + " should be same as " + j, Flik.isSameNumber(i, j));
    }
}
