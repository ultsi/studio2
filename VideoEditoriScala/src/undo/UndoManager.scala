package undo;
import scala.collection.mutable.Stack;

/**
 * UndoManager is a simplistic reusable component to support an undo-redo mechanism.
 * UndoableActions can be added in the manager, which gives a centered interface for
 * performing their undo and redo actions.
 */
class UndoManager {

    // Undo and redo stacks which contain the UndoableAction objects
    // When a new action is made it is put in the undo stack. When an operation
    // is undone, it is places in the redo stack.
    
    private val undoStack = new Stack[UndoableAction]()
    private val redoStack = new Stack[UndoableAction]()
      
    /**
     * Tests if an undo operation can be performed at this moment.
     * 
     * @return boolean value telling if an undo is possible to perform.
     */
    def canUndo: Boolean = !this.undoStack.isEmpty

    
    /**
     * Tests if an redo operation can be performed at this moment.
     * 
     * @return boolean value telling if a redo is possible to perform.
     */

    def canRedo: Boolean = !this.redoStack.isEmpty
    
    /**
     * Adds a new undoable action into this Undo Manager.
     * 
     * @param action the UndoableAction to be added.
     */
    def addAction( action: UndoableAction ): Unit = {
        this.redoStack.clear()        
        this.undoStack.push(action)
    }
    
    /**
     * Undoes one action from the undo stack. The undone action then is moved to
     * the undo stack.
     */
    def undo(): Unit = {
        val action: UndoableAction = this.undoStack.pop()
        action.undo()
        this.redoStack.push(action)
    }

    /**
     * Redoes one action from the redo stack. The redone action then is moved to
     * the undo stack.
     */
    def redo(): Unit = {
        val action: UndoableAction = this.redoStack.pop()
        action.redo()
        this.undoStack.push(action)
    }

    /**
     * Describes an available undoable action.
     * 
     * @return A description of the undoable action for menus etc.
     */
    def describeUndoableAction(): String = {
        if (canUndo) 
            "Undo "+this.undoStack.top.getDescription
        else "Nothing to undo."
    }

    /**
     * Describes an available redoable action.
     * 
     * @return A description of the redoable action for menus etc.
     */
    def describeRedoableAction(): String = {
        if (canRedo) 
            "Redo "+this.redoStack.top.getDescription        
        else "Nothing to redo."
    }

}
