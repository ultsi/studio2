package undo;

import video._
import scala.collection.mutable.Buffer

/**
 * AddAction implements a single undoable action where a VideoSection is added on a VideoTrack.
 */
class AddAction(val newTrack: VideoTrack, val sectionList:Buffer[VideoSection], val sectionToAdd: VideoSection) extends UndoableAction {

    // sectionList     Section list of the video track
    // sectionToAdd    Section to be added on the video track 
    // newTrack        VideoTrack of the VideoSection after addition

    private val oldTrack = sectionToAdd.getTrack // VideoTrack of the VideoSection before addition
    private val oldTime  = sectionToAdd.getStart // original beginning timecode for this section
    
    private val newTime  =                       // new beginning timecode for this section.
      if (sectionList.isEmpty)
        // If there are no other sections, the section is placed at 00:00:00.
        new TimeCode(0, 0, 0)
      else {
        // If there are other sections, the section begins one frame after
        // the last section in the sectionlist ends

        val lastSection: VideoSection = sectionList(sectionList.size - 1)
        lastSection.getEnd.nextFrame        
      } 

    private val addIndex = this.sectionList.size  // index where in the section list the section is to be added (in future).

    /**
     * Returns a brief explanation of the action. Can be used for example
     * to provide action descriptions for a GUI such as "undo adding a section".
     * 
     * @return description of this action.
     */    
    
    def getDescription = "adding a section"

    
    /**
     * This method re-does an action performed and recorded in an UndoManager. 
     */    

    def redo() = {
        this.execute()
    }

    
    /**
     * This method undoes an action performed and recorded in an UndoManager. 
     */    

    def undo() = {
        // remove the section from the section list
        this.sectionList.remove(this.addIndex)

        // restore the old starting and ending times for this section.
        this.sectionToAdd.transpose(this.oldTime)
        
        // restore the old track
        this.oldTrack match {
          case Some(track) => this.sectionToAdd.setTrack(track)
          case None        => this.sectionToAdd.clearTrack()
        }
    }

    
    /**
     * This method performs the original action.
     * It should be executed only once.
     * The redo-implementation calls this
     * this method.
     */

    def execute() = {
        
        // Set the new starting and ending times for this section.
        this.sectionToAdd.transpose(this.newTime);

        // Is the section added in the end or in the middle?
        if (this.addIndex > this.sectionList.size) {
            this.sectionList.append(this.sectionToAdd)
        } else {
            this.sectionList.insert(this.addIndex, this.sectionToAdd)
        }
        
        this.sectionToAdd.setTrack(this.newTrack)
    }

}