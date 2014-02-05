package game.io.tests

import game._
import game.io._

import java.io._
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This file is provided as a base for your own tests. Do whatever you want with it!
 */



@RunWith(classOf[JUnitRunner])
class HumanIOTest extends FlatSpec with Matchers {
    
    /*
     * IMPORTANT!
     * 
     * The  test method is allowed here to throw the CorruptedChessFileException.
     * 
     * The reasons for this are
     * 1) we expect the code to work
     * 2) if the code throws this exception the test will fail 
     * 
     * This is therefore desired behavior for this test. It also removes the problem
     * of untestable code in the catch section.
     */
    
    "HumanWritableIO.loadGame" should "be able to load a correctly formatted file with heaps of useless whitespace" in {

        val testData = "SHAKKI 1.2 Tallennustiedosto\n" + "\n" +
                "#Pelin tiedot\n" + "\n" + "Tallennettu : 5.7.2001\n" +
                "Musta     : Marko\n" + "Valkoinen : Lauri\n" + "\n" + "#Kommentit\n" +
                "\n" + "Laurin revanssipeli, joskin huonosti on menossa...\n" +
                "\n" + "#Musta\n" + "\n" + "Kuningas   : a4\n" + "Torni      : a6\n" +
                "Sotilas    : b3\n" + "Kuningatar : c8\n" + "\n" + "#Valkoinen\n" +
                "\n" + "Kuningas   : d3\n" + "Ratsu      : f1"

        val testInput:Reader = new StringReader(testData)
        val game = HumanWritableIO.loadGame(testInput)

        assert(!game.getBlack.isEmpty,  "Loading data failed. Player missing.")
        game.getBlack.get.name shouldBe "Marko"
        

        // THIS IS A POOR TEST! You can make it better!
                
        // Add your own tests, check that the players are ok and that the pieces were correctly placed.   
    }

    
    /**
     * This test was designed to test the catch code in the loadGame-method.
     */

    
    it should "throw a CorruptedChessFileException when encountering an IOException" in {

        val testData = "SHAKKI 1.2 Tallennustiedosto\n" + "\n" +
        "#Pelin tiedot\n" + "\n" + "Tallennettu : 5.7.2001\n" +
        "Musta     : Marko\n" + "Valkoinen : Lauri\n" + "\n" + "#Kommentit\n" +
        "\n" + "Laurin revanssipeli, joskin huonosti on menossa...\n" +
        "\n" + "#Musta\n" + "\n" + "Kuningas   : a4\n" + "Torni      : a6\n" +
        "Sotilas    : b3\n" + "Kuningatar : c8\n" + "\n" + "#Valkoinen\n" +
        "\n" + "Kuningas   : d3\n" + "Ratsu      : f1"

        // Adding a brokenreader allows throwing simulated exceptions
        val testInput:Reader = new BrokenReader(new StringReader(testData), 50)
        
        intercept[CorruptedChessFileException] {
            HumanWritableIO.loadGame(testInput)
        }
        // If loading with the broken reader does not lead to a CorruptedChessFileException
        // there is something wrong.
    }

    
    
    it should "throw a CorruptedChessFileException if there's something wrong with the file format" in {

        // The black player is missing, everything else is there
        
        val testData = "SHAKKI 1.2 Tallennustiedosto\n" + "\n" +
        "#Pelin tiedot\n" + "\n" + "Tallennettu : 5.7.2001\n" +
        "Valkoinen : Lauri\n" + "\n" + "#Kommentit\n" +
        "\n" + "Laurin revanssipeli, joskin huonosti on menossa...\n" +
        "\n" + "#Musta\n" + "\n" + "Kuningas   : a4\n" + "Torni      : a6\n" +
        "Sotilas    : b3\n" + "Kuningatar : c8\n" + "\n" + "#Valkoinen\n" +
        "\n" + "Kuningas   : d3\n" + "Ratsu      : f1"

        val testInput:Reader = new StringReader(testData)
        
        intercept[CorruptedChessFileException] {
            HumanWritableIO.loadGame(testInput)
        }
        // If loading a broken file does not lead to a CorruptedChessFileException
        // there is something wrong.
      
    }
    
    
}
