package game.io

import java.io.BufferedReader
import java.io.IOException
import java.io.Reader
import game.Board
import game.Game

/**********************************************************************
 * 
 *   This file is returned in Exercise 3.2
 * 
 *   The idea is to read a file written manually by
 *   someone. There can be useless whitespace all around the file.  
 * 
 **********************************************************************/

object HumanWritableIO {

    def loadGame(input: Reader): Game = {

        /**
         * This is the game object this method will fill with data. The object
         * is returned when the file ends and everything is ok.
         */

        val board = new Board()
        val game  = new Game(board)
           
        /*
        You might want to keep track of different required parts of the file  
         if some of these are missing, the file is not valid and a CorruptedChessFileException should be thrown
         If you figure out something better, you don't have to keep these variables. 
        */
        var infoRead = false
        var whiteRead = false
        var blackRead = false

        
        
        // BufferedReader allows us to read line by line (readLine method)
        val lineReader = new BufferedReader(input)


        try {

            /*
             * You can use this variable for reading all the section headers.
             * if built well, you can probably do with using only val:s and can get rid of this.
             */
            var currentLine = lineReader.readLine()     // Read the file header and the save date

            val headerParts = currentLine.split(" ")

            // Process the data we just read.
            // NOTE: To test the line below you must test the class once with a
            // broken header

            if (!headerParts(0).equals("SHAKKI")) {
                throw new CorruptedChessFileException("Unknown file type")
            }

            if (!headerParts(2).equalsIgnoreCase("Tallennustiedosto")) {
                throw new CorruptedChessFileException("Unknown file type")
            }

            // The version information and the date are not used in this
            // exercise

            // *************************************************************
            //
            // EXERCISE
            //
            // ADD CODE HERE FOR READING THE
            // DATA FOLLOWING THE MAIN HEADERS
            //
            //
            // *************************************************************

            // If we reach this point the Game-object should now have the proper
            // players and
            // a fully set up chess board. Therefore we might as well return it.
            
            // If there is something missing, throw a CorruptedChessFileException

            return game
            
        } catch {
            case e: IOException =>
        

            // To test this part the stream would have to cause an
            // IOException. That's a bit complicated to test. Therefore we have
            // given you a "secret tool", class BrokenReader, which will throw
            // an IOException at a requested position in the stream.
            // Throw the exception inside any chunk, but not in the chunk
            // header.

            val chessExc = new CorruptedChessFileException("Reading the chess data failed.")

            // Append the information about the initial cause for use in
            // debugging. Otherwise the programmer cannot know the method or
            // line number causing the problem.

            chessExc.initCause(e)

            throw chessExc
        }
    }
}
