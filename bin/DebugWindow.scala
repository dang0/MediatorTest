
import scala.swing.Dialog
import java.awt.Dimension
import scala.swing.Label
import scala.swing.Swing
import scala.swing.event.UIElementMoved
import scala.swing.GridPanel
import java.awt.Point
import scala.swing.BoxPanel
import scala.swing.Orientation
import java.awt.Color

object DebugWindow extends Dialog(Main) {
  override def owner = Main
  
  var target: Cell = new Cell(-1,-1,-1)
  resizable = false
  preferredSize = new Dimension(200,120)
  val status = new Label(){ border = Swing.EmptyBorder(4) } //status
  val conflicts = new Label(){ border = Swing.EmptyBorder(4) } //conflicts
  val value = new Label(){ border = Swing.EmptyBorder(4) } //value
  var bindLoc: (Int,Int) = null
  
  def setCell(implicit c: Cell) {
    target = c
    title = c.row +","+ c.col
    status.text_=(target.status)
    conflicts.text_=(target.conflicts.toString)
    value.text_=(target.value.toString())
  }
  
  listenTo(owner, this)
  reactions += {
    case e: UIElementMoved if(this.visible && e.source == owner) => 
      if(bindLoc == null) bindLoc = (locationOnScreen.x - owner.locationOnScreen.x, locationOnScreen.y - owner.locationOnScreen.y)
      location = new Point(owner.locationOnScreen.x + bindLoc._1, owner.locationOnScreen.y + bindLoc._2)
    case e: UIElementMoved if(peer.hasFocus() && e.source == this) => bindLoc = (locationOnScreen.x - owner.locationOnScreen.x, locationOnScreen.y - owner.locationOnScreen.y)
  }
  
  contents = new GridPanel(3,1) {
    border = Swing.EmptyBorder(5)
    contents += (
        new BoxPanel(Orientation.Horizontal) {
          border = Swing.LineBorder(Color.BLACK, 1)
          contents += status
        },
        new BoxPanel(Orientation.Horizontal) {
          border = Swing.LineBorder(Color.BLACK, 1)
          contents += conflicts
        },
        new BoxPanel(Orientation.Horizontal) {
          border = Swing.LineBorder(Color.BLACK, 1)
          contents += value
        }
    )
  }
  
  override def open() {
    setLocationRelativeTo(owner)
    bindLoc = null
    super.open
  }
  
}