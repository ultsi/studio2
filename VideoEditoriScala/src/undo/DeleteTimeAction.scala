package undo;

import scala.collection.mutable.Buffer

import video.TimeCode;
import video.VideoSection;
import video.VideoTrack;

class DeleteTimeAction(videoTrack: VideoTrack, sections:Buffer[VideoSection], start: TimeCode, end: TimeCode) extends UndoableAction {

    // Add fields as necessary (there will be many!)
    

    // Implement what you want in constructor
        
    // NOTE: Think about all the things that have to be saved about the sections we are going
    // to split or delete soon. Part of the work gets done here part in execute().
        
   
    // Add helper methods as necessary
    
    
    def getDescription() = "deleting from the video track"

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
