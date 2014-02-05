package video;


/**
 * Useful constants
 */

object TimeCode {
  val FRAMES_PER_SECOND = 50;
  val SECONDS_PER_MINUTE = 60;
}

/**
 * This class provides a time code for a video editing system. Each video
 * consists of frames (single images). There are a given number of frames in a
 * second and of course 60 seconds in a minute.
 *
 * The main constructor creates a TimeCode instance with the given parameters.
 * TimeCode is immutable
 *
 * @param minutes
 *            minutes of this TimeCode object.
 * @param seconds
 *            seconds of this TimeCode object.
 * @param frames
 *            frames of this TimeCode object.
 */

case class TimeCode(minutes: Int, seconds: Int, frames: Int) {

  /**
   * Tests if this timecode is after or equal to the timecode given as the
   * parameter.
   *
   * @param event
   *            the timecode being compared to
   * @return true if and only if this timecode is after or equal to the
   *         parameter timecode.
   */

  def inclusiveAfter(event: TimeCode) = {
    if (this == event)
      true
    else (this.minutes > event.minutes || (this.minutes == event.minutes && (this.seconds > event.seconds || (this.seconds == event.seconds && this.frames > event.frames))))
  }

  /**
   * Tests if this timecode is before or equal to the timecode given as the
   * parameter.
   *
   * @param event
   *            the timecode being compared to
   * @return true if and only if this timecode is before or equal to the
   *         parameter timecode.
   */

  def inclusiveBefore(event: TimeCode) = {
    if (this == event)
      true
    else (this.minutes < event.minutes || (this.minutes == event.minutes && (this.seconds < event.seconds || (this.seconds == event.seconds && this.frames < event.frames))))
  }

  /**
   * Tests if this timecode is between (inclusive) the timecodes given as
   * parameters.
   *
   * @param event1
   *            the begin timecode being compared to
   * @param event2
   *            the end timecode being compared to
   * @return true if and only if this timecode is between (inclusive) the given timecodes.
   */

  def inclusiveBetween(event1: TimeCode, event2: TimeCode) = {
    inclusiveAfter(event1) && inclusiveBefore(event2)
  }

  /**
   * Returns a new TimeCode instance which is one frame later from this timecode.
   *
   * @return a new timecode object.
   */
  def previousFrame() = {
    transposeFrames(-1)
  }

  /**
   * Returns a new TimeCode instance which is one frame before from this timecode.
   *
   * @return a new timecode object.
   */
  def nextFrame() = {
    transposeFrames(1)
  }

  /**
   * Converts the minutes, seconds and frames to just frames.
   *
   * @return a frames equivalent to this timecode.
   */
  def toFrames(): Int = {
    this.frames +
      TimeCode.FRAMES_PER_SECOND * this.seconds +
      TimeCode.SECONDS_PER_MINUTE * TimeCode.FRAMES_PER_SECOND * this.minutes
  }

  /**
   * Calculates the distance in frames between two timecodes.
   *
   * @param time The timecode to which the distance is calculated.
   * @return The difference between the timecode in frames.
   */
  def calculateFrameDistance(time: TimeCode): Int = {
    time.toFrames() - this.toFrames()
  }

  /**
   * Returns a new TimeCode instance at the given offset in frames.
   *
   * @param offset offset in frames from this timecode.
   * @return the new timecode object.
   */
  def transposeFrames(offset: Int): TimeCode = {
    val totalFrames = this.toFrames() + offset

    val newFrames = totalFrames % TimeCode.FRAMES_PER_SECOND
    val secondsLeft = totalFrames / TimeCode.FRAMES_PER_SECOND

    val newSeconds = secondsLeft % TimeCode.SECONDS_PER_MINUTE
    val newMinutes = secondsLeft / TimeCode.SECONDS_PER_MINUTE

    new TimeCode(newMinutes, newSeconds, newFrames)
  }

  /**
   * Returns a String representation of this TimeCode-Object.
   *
   * @return a String in the form [MM:SS:FF] (field widths may vary).
   */
  override def toString() = {
    s"[$minutes : $seconds : $frames"
  }
}
