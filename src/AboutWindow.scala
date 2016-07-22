
import scala.swing.Dialog

object AboutWindow {
  val title = "About"
  val message = "I made this.\n\n" + 
    "LeftClick: Lock a cell\n" +
    "MouseWheelUp: Increment a cell\n" +
    "MouseWheelDown: Decrement a cell\n" +
    "NumberKeys: Changes mouseovered cell to number\n" +
    "Hold Ctrl and swipe cells to go on a cell clearing rampage"
    
  def open() {
    Dialog.showMessage(Main.contents(0), message, title, Dialog.Message.Info, null)
  }
  
}