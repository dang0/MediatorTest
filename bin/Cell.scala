
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Font
import scala.swing.event.MouseWheelMoved
import scala.swing.event.MouseWheelMoved
import java.awt.Color
import scala.swing.event.MouseEntered
import scala.swing.Panel
import scala.swing.Swing
import scala.annotation.tailrec
import scala.swing.event.MouseReleased
import scala.swing.event.KeyReleased
import scala.annotation.tailrec

class Cell(val row: Int, val col: Int, val blk: Int) extends Panel {
  var value = 0
  
  var mediator: Mediator = null
  var locked = false
  var userlock = false
  var conflicts = Set.empty[Int]
  private implicit val impThis: Cell = this
  
  def status: String = {
    if(conflicts.size > 0) "conflicts"
    else if(locked) "locked"
    else if(userlock) "userlocked"
    else "valid"
  }
  
  val bgBase = new Color(230,230,230)
  background = bgBase
  minimumSize = new Dimension(35,35)
  preferredSize = minimumSize
  opaque = true
  border = Swing.LineBorder(Color.GRAY)
  
  listenTo(mouse.clicks, mouse.moves, mouse.wheel, keys)
  reactions += {
    case e: MouseEntered if e.peer.isControlDown => reset
    case _: MouseEntered => mediator.cellFocus 
    case _ if(locked) =>
    case e: MouseReleased if e.modifiers == 0 => userlock = !userlock
    case _ if(userlock) =>
    case e: MouseReleased if e.triggersPopup => println("popup")
    case e: MouseWheelMoved => if(e.rotation < 0) incVal else decVal
    case e: KeyReleased if e.peer.getKeyChar.isDigit => setVal(e.peer.getKeyChar.toString.toInt)
  }
  
  def loadVal(i: Int) {
    lock
    setVal(i)
  }
  
  def setVal(i: Int) {
    value = i
    if(value < 0) value = 10 + (value % 10)
    else if(value > 9) value = value % 10
    mediator.notify
  }
  
  @tailrec
  private def incVal() {
    value += 1
    if(mediator.checkInvalid) incVal
    else setVal(value)
  }
  
  @tailrec
  private def decVal() {
    value -= 1
    if(value < 0) value += 10
    if(mediator.checkInvalid) decVal
    else setVal(value)
  }
  
  def setMediator(m: Mediator) {
    mediator = m
  }
  
  def lock() {
    locked = true
  }
  
  def unlock() {
    locked = false
  }
  
  def reset() {
    value = 0
    locked = false
    userlock = false
    conflicts = conflicts.empty
  }
  
  implicit def valToStr(v: Int): String = v.toString()
  override def toString(): String = value
  
  def colorBg() = status match {
      case "userlocked" => background = Color.ORANGE
      case "locked" => background = Color.LIGHT_GRAY
      case "conflicts" => background = Color.RED
      case _ => background = bgBase
  }
  
  override def paintComponent(g: Graphics2D) {
    colorBg
    super.paintComponent(g)
    //g.drawRect(0, 0, size.width-1, size.height-1)
    if(row % 3 == 0) g.drawRect(1, 1, size.width, 1)
    if(col % 3 == 0) g.drawRect(1, 1, 1, size.height)
    font = new Font("Tahoma",Font.BOLD, size.height - 10)
    val strBounds = font.getStringBounds(value, g.getFontRenderContext())
    if(value != 0) g.drawString(value,
        size.getWidth().toInt / 2 - strBounds.getWidth().toInt / 2,
        size.getHeight().toInt / 2 + strBounds.getHeight().toInt / 4)
  }
}
