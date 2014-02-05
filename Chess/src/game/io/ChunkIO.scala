package game.io

import java.io.IOException
import java.io.Reader
import game.Board
import game.Game

/**********************************************************************
 * 
 *   This file is returned in Exercise 3.1
 * 
 *   The idea is to read a file written automatically by
 *   a program. This is a chunked format like PNG, TIF, MPEG, etc.
 *   explained in the exercise description
 * 
 **********************************************************************/

object ChunkIO {

    def loadGame(input: Reader): Game = {

        /**
         * This is the game object this method will fill with data. The object
         * is returned when the END chunk is reached.
         */

        val board = new Board()
        val game  = new Game(board)

        
        /*
         * Use these variables for reading all the file header, date and chunk headers.
         * 
         * HINT: check the helper methods in the end of this class, a few lines below we read the header
         *       as an example
         */

        var header = new Array[Char](8)
        var date   = new Array[Char](8)
        var chunkHeader = new Array[Char](5)

        try {

            // Read the file header and the save date
            
            Helpers.readFully(header, input);
            Helpers.readFully(date, input);

            // Process the data we just read.
            // NOTE: To test the line below you must test the class once with a broken header

            if (!header.mkString.startsWith("SHAKKI")) {
                throw new CorruptedChessFileException("Unknown file type");
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
            
            // If we reach this point the Game-object should now have the proper players and
            // a fully set up chess board. Therefore we might as well return it.
            
           game;

        } catch {
            case e:IOException =>
        

            // To test this part the stream would have to cause an
            // IOException. That's a bit complicated to test. Therefore we have
            // given you a "secret tool", class BrokenReader, which will throw
            // an IOException at a requested position in the stream.
            // Throw the exception inside any chunk, but not in the chunk header.
            
            val chessExc = new CorruptedChessFileException("Reading the chess data failed.")

            // Append the information about the initial cause for use in
            // debugging. Otherwise the programmer cannot know the method or
            // line number causing the problem.

            chessExc.initCause(e)

            throw chessExc
        }
    }
    
    object Helpers {
    // HELPER METHODS -------------------------------------------------------

    /**
     * Given a chunk header (an array of 5 chars) will return the size of this
     * chunks data.
     * 
     * @param chunkHeader
     *            a chunk header to process
     * @return the size of this chunks data
     */

    def extractChunkSize(chunkHeader: Array[Char]):Int = {
        
        // subtracting the ascii value of the character 0 from
        // a character containing a number will return the
        // number itself

        10 * (chunkHeader(3) - '0') + (chunkHeader(4) - '0')
    }

    /**
     * Given a chunk header (an array of 5 chars) will return the name of this
     * chunk as a 3-letter String.
     * 
     * @param chunkHeader
     *            a chunk header to process
     * @return the name of this chunk
     */
    def extractChunkName(chunkHeader: Array[Char]): String = {
        chunkHeader.take(3).mkString
    }

    /**
     * The read-method of the Reader class will occasionally read only part of
     * the characters that were requested. This method will repeatedly call read
     * to completely fill the given buffer. The size of the buffer tells the
     * algorithm how many bytes should be read.
     * 
     * @param result
     *            The result of the reading will be stored in this array.
     * @param input
     *            The character stream to read from
     * @throws IOException
     * @throws CorruptedChessFileException
     */
    def readFully(result: Array[Char], input: Reader) = {
        var cursor = 0

        while (cursor != result.length) {
            var numCharactersRead = input.read(result, cursor, result.length - cursor)

            // If the file end is reached before the buffer is filled
            // an exception is thrown.
            
            if (numCharactersRead == -1) {
                throw new CorruptedChessFileException("Unexpected end of file.")
            }

            cursor += numCharactersRead
        }

    }

}

}

