package undo;

/**
 * This interface depicts a single undoable action.
 */
trait UndoableAction {

    /**
     * This method performs the original action.
     * It should be executed only once. In many cases this method
     * might initialize data needed both in the undo and redo processes.
     * 
     * HINT : The redo-implementation is likely to call this method.
     */
    def execute()
    
    /**
     * This method undoes an action performed and recorded in an UndoManager. 
     */    
    def undo()

    /**
     * This method redoes an action performed and recorded in an UndoManager. 
     */    
    def redo()
    
    /**
     * Returns a brief explanation of the action. Can be used for example
     * to provide action descriptions for a GUI such as "undo adding a section".
     * 
     * @return description of this action.
     */
    def getDescription: String
    
}
