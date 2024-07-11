package battleship.model;

import org.testng.*;
import org.testng.annotations.Test;

public class PlayerTest {

    @Test
    public void inverseTest() {
        Assert.assertEquals(Player.FIRST.inverse(), Player.SECOND);
        Assert.assertEquals(Player.SECOND.inverse(), Player.FIRST);
    }

}
