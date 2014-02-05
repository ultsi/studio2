package video;

import undo._
import scala.collection.mutable.Buffer

/**
 * This class models a video track which can contain a number of video segments
 * called VideoSections. The class contains a set of operations for modifying
 * contents of the track. The operations can be both undone and redone using the
 * undo() and redo() commands.
 * 
 * VideoTrack contains a list of VideoSections that is guaranteed not to contain
 * null-values.
*
     * Constructs a new VideoTrack with the given UndoManager.
     * 
     * @param name Name of the track created.
     * @param undoManager Undo manager for this track.
 */
class VideoTrack(val trackName: String, undoManagerParam: UndoManager) {
        
    /**
     * An list containing the video sections of this video track. The list
     * is made final to ensure that references passed to UndoableAction objects
     * will always stay valid.
     */
    val sections = Buffer[VideoSection]()

    /**
     * UndoManager of this VideoTrack.
     */
    private val undoManager = undoManagerParam

 
    /**
     * Undoes the last action. (add, remove, delete, apply)
     */
    def undo(): Boolean = {
        if (this.undoManager.canUndo) {
            this.undoManager.undo()
            true
        }
        else false
    }

    /**
     * Redoes the last action. (add, remove, delete, apply)
     */
    def redo(): Boolean = {
        if (this.undoManager.canRedo) {
            this.undoManager.redo()
            true
        }
        else false
    }

    /**
     * Describes an available undoable action.
     * 
     * @return A description of the undoable action for menus etc.
     * @see UndoManager#describeUndoableAction()
     */
    def describeUndoableAction(): String = this.undoManager.describeUndoableAction
    

    /**
     * Describes an available redoable action.
     * 
     * @return A description of the redoable action for menus etc.
     * @see UndoManager#describeRedoableAction()
     */
    def describeRedoableAction(): String = this.undoManager.describeRedoableAction
    
    
    /**
     * Performs a single undoable action where a VideoSection is added on this
     * VideoTrack. The section is added after the last section currently
     * contained in the track. If there are no such sections, the section is
     * added at 00:00:00.
     * 
     * @param section Section to add.
     */
    def addSection(section: VideoSection): Unit = {
        val action = new AddAction(this, this.sections, section)
        action.execute()

        this.undoManager.addAction(action)
    }

    /**
     * Performs a single undoable action where a VideoSection is removed from
     * this VideoTrack.
     * 
     * @param section Section to remove.
     */
    def removeSection(section: VideoSection): Unit = {

        val action =  new RemoveAction(this, this.sections, section)
        action.execute()
        
        this.undoManager.addAction(action)

    }

    /**
     * Performs a single undoable action where a selected segment of time
     * (containing fully or partially 0-N VideoSegments) is deleted from this
     * VideoTrack. Partial segments are splitted so that only the requested part
     * is deleted. The first and last frames to be deleted are both included in the deleted
     * part.
     * 
     * @param start First frame to delete.
     * @param end   Last frame to delete.
     */
    def deleteTime(start: TimeCode, end: TimeCode): Unit = {

        val action =  new DeleteTimeAction(this, this.sections, start, end)
        action.execute()
        
        this.undoManager.addAction(action)

    }

    
    /**
     * Returns an iterator for browsing through the contents of the section list.
     * 
     * @return an iterator for the section list of this VideoTrack.
     */
    def getSectionIterator(): Iterator[VideoSection] = {
        return this.sections.iterator
    }

    
    /**
     * Returns the section at the given TimeCode.
     * 
     * @param time the timecode of the moment of interest.
     * @return reference to the VideoSection object or null if a section could not be found.
     */
    def getSectionAt(time: TimeCode): Option[VideoSection] = {
        this.sections.find(current => time.inclusiveBetween(current.getStart, current.getEnd))
    }

    
    /**
     * Constructs a String representation of this video track.
     * 
     * @return a String representation of this VideoTrack.
     */
    
    override def toString() = {

        val output = new StringBuilder();
        var currentTime = new TimeCode(0, 0, 0)

        val sectionIterator = getSectionIterator()
        while (sectionIterator.hasNext) {
            val current: VideoSection = sectionIterator.next();

            if (!currentTime.equals(current.getStart)) {
                output.append("UNUSED  :\n\t" + currentTime + " - "
                        + current.getStart.previousFrame + "\n")
            }
            output.append(current.toString())
            output.append("\n")

            currentTime = current.getEnd.nextFrame
        }

        output.toString()

    }
    
    /**
     * Returns the name of this VideoTrack.
     * 
     * @return name of the track.
     */ 
    def getName: String = this.trackName   
    
}
