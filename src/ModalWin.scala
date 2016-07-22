
import scala.swing.Dialog
import scala.swing.Label
import scala.swing.Swing
import scala.swing.BoxPanel
import scala.swing.Orientation

object ModalWin extends Dialog {
  modal = true
  resizable = false
  title = "Congrats, bruh."
  contents = new BoxPanel(Orientation.Horizontal) {
    border = Swing.EmptyBorder(5)
    contents += new Label("You are the superior dancer.")
  }
  override def open() {
    setLocationRelativeTo(Main)
    super.open
  }
}