package com.bobliou.chessgame.Game;

import com.bobliou.chessgame.DB.DBOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bob Liou - 18013456
 */
public class GameEngineTest {

    private GameEngine gameEngine;
    private Board board = new Board();
    private GameData gameData = new GameData();
    private DBOperations db = new DBOperations();

    public GameEngineTest() {
        db.establishConnection();
        gameEngine = new GameEngine(board, gameData, null, db);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test the reset method of the game engine.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        assertEquals(gameEngine.reset(board, gameData), true);
    }
    
    /**
     * Test the show message method of the game engine.
     */
    @Test
    public void testShowMessage() {
        System.out.println("show message");
        assertEquals(gameEngine.showMessage("This is a test message"), true);
    }

    /**
     * Test of loadExistingGame method, of class GameEngine.
     */
    @Test
    public void testLoadExistingGame() {
        System.out.println("load existing game");
        String history = "E2 E4, E7 E5";
        assertEquals(gameEngine.loadExistingGame(history), true);
    }

}
