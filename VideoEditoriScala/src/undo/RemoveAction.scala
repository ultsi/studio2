package undo;

import scala.collection.mutable.Buffer

import video.VideoSection;
import video.VideoTrack;

class RemoveAction(videoTrack: VideoTrack, sections:Buffer[VideoSection], section: VideoSection) extends UndoableAction {

    // Add fields as necessary
    // Implement what you want in the constructor

    def getDescription() = "removing a video section"

    def redo() = {        
    	???
        // Implement me!
    }

    def undo() = {
    	???
        // Implement me!
    }

    def execute() = {
    	???
        // Implement me!
    }

}
