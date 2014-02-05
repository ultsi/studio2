package video;

import scala.collection.mutable.{HashMap => Properties}


object VideoSection {
  
    // STANDARD PROPERTY NAMES
    val VOLUME         = "Volume";
    val GRAYSCALE      = "Grayscale";
    val DEFAULT_VOLUME = "100";

    // STANDARD PROPERTY VALUES
    val TRUE = "true";
    val FALSE= "false";

}


/**
 * This class depicts a short named segment of video which can have a number of
 * properties.
 */
class VideoSection(sectionTitleParam: String, startParam: TimeCode, endParam: TimeCode){
    import VideoSection._
    
    private var track: Option[VideoTrack] = None // The Track this section belongs to
    private var start = startParam
    private var end   = endParam
    private var sectionTitle = sectionTitleParam
    
    
    private val properties = Properties[String, String]() // Properties for this VideoSection

    setProperty(VOLUME, DEFAULT_VOLUME);

    
    /*
     * Properties are key-value pairs. Using properties instead of normal fields
     * allows addings new properties without altering the class.
     */

    // -------- Constructors --------

    /**
     * Creates a new Videosection with the given parameters.
     * 
     * @param sectionTitle
     *            title of this section.
     * @param start
     *            timecode for the first frame of the section.
     * @param end
     *            timecode for the last frame of the section.
     */

    /**
     * Internal constructor for creating sections produced by split operations.
     * 
     * @param sectionTitle
     *            title of this section.
     * @param start
     *            timecode for the first frame of the section.
     * @param end
     *            timecode for the last frame of the section.
     * @param copy
     *            a preset properties object for this section.
     */
    private def createVideoSection(sectionTitle: String, start: TimeCode, end: TimeCode, copy: Properties[String, String]) = {
        val created = new VideoSection(sectionTitle, start, end)
        created.properties.clear()
        created.properties ++ copy
        created
    }

    // -------- Accessors for the VideoSection --------

    /**
     * Sets the video track this section is currently assigned to.
     * 
     * @param track
     *            the VideoTrack this section is assigned to.
     */
    def setTrack(track: VideoTrack) = {
        this.track = Some(track)
    }
    
    def clearTrack() = {
      this.track = None
    }

    /**
     * Gets the video track this section is currently assigned to.
     * 
     * @return the VideoTrack this section is assigned to.
     */
    def getTrack = this.track

    /**
     * Returns the TimeCode of the first frame of this VideoSection.
     * 
     * @return timecode of the first frame.
     */
    def getStart: TimeCode = this.start

    /**
     * Returns the TimeCode of the last frame of this VideoSection.
     * 
     * @return timecode of the last frame.
     */
    def getEnd: TimeCode = this.end;
    

    // -------- Methods for altering properties of the VideoSection --------

    /**
     * Sets the value of a given property.
     * 
     * @param propertyName
     *            The name of the property to set.
     * @param value
     *            New value for the property.
     */
    def setProperty(propertyName: String, value: String) {
        this.properties(propertyName) = value
    }

    /**
     * Gets the value of a given property.
     * 
     * @param propertyName
     *            Name of the property to be returned.
     * @return the value of the property.
     */
    def getProperty(propertyName: String) = this.properties(propertyName)

    /**
     * Removes a given property completely.
     * 
     * @param propertyName
     *            Name of the property to remove.
     */
    def removeProperty(propertyName: String) = {
        this.properties.remove(propertyName)
    }

    // -------- Methods for altering the VideoSection --------

    /**
     * Changes the starting time of this section.
     * 
     * @param newStart
     *            New starting timecode for this section.
     */
    def transpose(newStart: TimeCode) = {
        this.end = this.end.transposeFrames(this.start.calculateFrameDistance(newStart));
        this.start = newStart;
    }

    /**
     * Splits the section into two parts at the given time. This section becomes
     * the first part of the section. The second part is given as a return
     * value.
     * 
     * NOTE: The first part replaces the original section on the video track,
     * and the last part is "taken out". If you want to have it on the track, it
     * has to be separately inserted.
     * 
     * NOTE: Splitting affects the sectionName of the split sections.
     * 
     * @param when
     *            the beginning of the second part of the split section.
     * @return the second part of the split section.
     */
    def split(when: TimeCode): VideoSection = {

        // check that the splitting point is within this section.
        if (when.inclusiveBetween(this.start, this.end)) {

            // Split sections get the text split as part of their section title
            // unless this is a re-split

            val titleEnd = if (this.sectionTitle.endsWith("(split)")) "" else " (split)"
            
            // Create the end section with the same properties as this one.
            val endSection = createVideoSection(this.sectionTitle
                    + titleEnd, when, this.end, this.properties)

            // Set this section to end one frame before the latter section
            // begins.
            this.end = when.previousFrame()
            this.sectionTitle = this.sectionTitle + titleEnd

            endSection
        } else {
            throw new IllegalArgumentException(
                    "Illegal parameter supplied by an internal method call. Request for splitting a video section out of bounds")
        }
    }

    /**
     * Joins two sections together to form a new bigger section. The sections
     * must be located back-to-back for the operation to succeed.
     * 
     * @param endPart
     *            The section after this section to be joined into this section.
     * @param name
     *            Name for the new "joined" section.
     */
    def join(endPart: VideoSection, name: String): Unit = {

        // Check that the sections are back-to-back
        if (endPart.start.previousFrame().equals(this.end)) {
            this.end = endPart.end
            this.sectionTitle = name
        } else {
            throw new IllegalArgumentException(
                    "Illegal parameter supplied by an internal method call. Request for joining two sections which are not back to back.");
        }

    }

    /**
     * Returns a String representation for this section. The representation
     * contains the name of the section, properties for this section and the
     * timecodes for the first and last frames.
     * 
     * @return a string representation of this section.
     */
    override def toString() = {
        "Section : " + this.sectionTitle + "(" + this.properties.toString() + ")\n" + "\t" + this.start + " - " + this.end
    }

    /**
     * Returns the title of this section.
     * 
     * @return the name of the section
     */
    def getName(): String = {
        this.sectionTitle;
    }

    /**
     * Tests if the two objects are equal.
     * 
     * @param the object to compare with.
     * @return true if and only if the items were equal.
     */
    override def equals(other: Any) = {
        other match {
          case v: VideoSection =>
            this.start.equals(v.start) && this.end.equals(v.end) &&
                    this.sectionTitle.equals(v.sectionTitle) &&
                    this.properties.equals(v.properties);
          case _ => false
        }
    }
    
    override def hashCode: Int = 41*this.start.toFrames + 131*this.end.toFrames + sectionTitle.hashCode()
    
}