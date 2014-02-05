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
class ChunkIOTest extends FlatSpec with Matchers {

    /*
     * IMPORTANT!
     * 
     * The test method is allowed here to throw the CorruptedChessFileException.
     * 
     * The reasons for this are
     * 1) we expect the code to work
     * 2) if the code throws this exception the test will fail 
     * 
     * This is therefore desired behavior for this test. It also removes the problem
     * of untestable code in the catch section.
     */

    "ChunkIO.loadGame" should "be able to load a correctly formatted file" in {

        
        val testData = "SHAKKI1205072001" +
            "CMT54Laurin revanssipeli, hyvin huonosti on taas menossa..." +
            "PLR17M5MarkoKa4Ta6b3c3" + "PLR13V5LAURIKd3Rf1" + "END00"

        val testInput: Reader = new StringReader(testData)
        val game = ChunkIO.loadGame(testInput)
        
        assert(!game.getBlack.isEmpty,  "Loading data failed. Player missing.")
        game.getBlack.get.name shouldBe "Marko"

        
        // THIS IS A POOR TEST! You can make it better!

        // Add your own tests, check that the players are ok and that the pieces were correctly placed.   
    }

    
    /**
     * This test was designed to test the catch code in the loadGame-method.
     */
    
    
    it should "throw a CorruptedChessFileException when encountering an IOException" in {

        val testData = "SHAKKI1205072001" +
            "CMT54Laurin revanssipeli, hyvin huonosti on taas menossa..." +
            "PLR17M5MarkoKa4Ta6b3c3" + "PLR13V5LAURIKd3Rf1" + "END00"

        // Adding a brokenreader allows throwing simulated exceptions
        val testInput:Reader = new BrokenReader(new StringReader(testData), 26)

        // Note that initially your code does not read past the file header
        // so this test will fail. 26 bytes is past the header and date (16 bytes)
        intercept[CorruptedChessFileException] {
            ChunkIO.loadGame(testInput)
        }

    }
}
    
