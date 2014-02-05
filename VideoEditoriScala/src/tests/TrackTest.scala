package tests;

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import undo.UndoManager
import video._
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class TrackTest extends FlatSpec with Matchers {

     /**
      * This test actually just tests that the preconditions for testing the undo behavior are ok.
      * 
      */
  
     "VideoTrack.addSection" should "add a single section with correct timecodes" in {

        val testUndo = new UndoManager();  // used in some tests
        val track    = new VideoTrack("Test Track", testUndo)

      
        // Here are the two sections prior to adding them on the track.
        // Notice that neither starts from 0:0:0
        
        val sectionA = new VideoSection("Part A",
                new TimeCode(0, 3, 0), new TimeCode(0, 5, 20))

        val sectionB = new VideoSection("Part B",
                new TimeCode(0, 2, 0), new TimeCode(0, 4, 15))

        
        track.addSection(sectionA);

        // Test first that the section really was added on the track
        //
        // Basically we create an iterator to go through the sections
        // and test that the expected sections are there and that there are no extra sections
        
        var sectionIterator = track.getSectionIterator
        
        assert( sectionIterator.hasNext, "The track should hold the newly added section.")
        assert( sectionA eq sectionIterator.next(), "The newly added section - Part A - should have been on the track.")
        assert( sectionIterator.isEmpty, "All added sections should have been iterated over already. There shouldn't be more sections on the track.")
        
        
        // If all test have passed this far, we can proceed to check if the time codes
        // where changed according to the documentation. (note that this only applies to addSection)
        
        val expectedStart = new TimeCode(0, 0, 0);
        val expectedEnd   = new TimeCode(0, 2, 20);

        assert(expectedStart == sectionA.getStart, "Start frame timecode was wrong after addSection")
        assert(expectedEnd   == sectionA.getEnd,   "End frame timecode was wrong after addSection")
        assert(track eq sectionA.getTrack.get,     "Video track was incorrect after addSection")
     }

     
    /**
     * Adds a new video section on a track and tests that the starting and
     * ending time codes are correctly recalculated. The addition is then undone and
     * the track is tested to be empty again.
     * 
     * As undoing might potentially leave the track/the action object in a
     * state where redo produces unexpected results, additional undo-redo
     * cycle with tests is performed in the end.
     */

     
     "VideoTrack.addSection" should "be undoable and redoable" in {
        val testUndo = new UndoManager();  // used in some tests
        val track    = new VideoTrack("Test Track", testUndo)
        val sectionA = new VideoSection("Part A", new TimeCode(0, 3, 0), new TimeCode(0, 5, 20))
        val sectionB = new VideoSection("Part B", new TimeCode(0, 2, 0), new TimeCode(0, 4, 15))
        track.addSection(sectionA);
       
               
        // Now we undo the add and check if the track is really empty(testing Undo)
        
        track.undo()
        
        // Get a fresh new iterator
        var sectionIterator = track.getSectionIterator        
        assert(sectionIterator.isEmpty, "After this undo the track should have been empty.")
                
        // Redo should give us again the state where the section added first is on the track
        
        track.redo()
        
        sectionIterator = track.getSectionIterator
        
        assert( sectionIterator.hasNext, "After redo, the track should hold the section added first.")
        assert( sectionA eq sectionIterator.next(), "The first section - Part A - should have been on the track.")
        assert( sectionIterator.isEmpty, "All added sections should have been iterated over already. There shouldn't be more sections on the track.")
        
        // Now we undo the redo and check if the track is once again really empty
        
        track.undo()
        
        // Get a fresh new iterator
        sectionIterator = track.getSectionIterator();        
        assert(sectionIterator.isEmpty, "After this undo the track should have been empty." )        
    }

    /**
     * Tests that a redo operation cannot be done when there is nothing to redo.
     */
    "VideoTrack" should "do nothing if we attempt to redo an action when there is nothing to redo" in {
        val testUndo = new UndoManager();  // used in some tests
        val track    = new VideoTrack("Test Track", testUndo)
      
        assert(!testUndo.canRedo, "No actions have been made so it should be impossible to redo." )
        assert(!track.redo(), "No actions have been made so it should be impossible to redo.")
    }

}
