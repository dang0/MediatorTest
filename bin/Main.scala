
import scala.swing.MainFrame
import scala.swing.GridPanel
import java.awt.Color
import scala.swing.Swing
import scala.swing.MenuBar
import scala.swing.Menu
import scala.swing.event.Key
import scala.swing.MenuItem
import scala.swing.Action
import java.awt.event.KeyEvent
import javax.swing.KeyStroke
import scala.swing.Separator


object Main extends MainFrame with App {
  title = "Scadoku v0.9"
  resizable = false
  centerOnScreen
  
  val testMediator = new Mediator
  val cells = Array.ofDim[Cell](9,9)
  def cellsAsStr(): String = cells.view.mkString.reverse.dropWhile(_ == '0').reverse
  for {
    x <- (0 to 8)
    y <- (0 to 8)
  } {
    val bxy = {
      val ab = x/3 -> y/3
      (ab._1*3 + ab._2) -> ((x - ab._1*3)*3 + (y - ab._2*3))
    }
    cells(x)(y) = new Cell(x,y,bxy._1)
    cells(x)(y).setMediator(testMediator)
  }
  
  testMediator.setSolvers(cells)
  
  menuBar = new MenuBar {
    contents += (
      new Menu("File") {
        mnemonic = Key.F
        contents += (
          new MenuItem(new Action("Reset") { mnemonic = KeyEvent.VK_R; accelerator = Some(KeyStroke.getKeyStroke("ctrl R")); def apply { cells.view.flatten.foreach(_.reset) } }),
          new MenuItem(new Action("Save") { mnemonic = KeyEvent.VK_S; accelerator = Some(KeyStroke.getKeyStroke("ctrl S")); def apply { FileIO.saveFile } }),
          new MenuItem(new Action("Load") { mnemonic = KeyEvent.VK_L; accelerator = Some(KeyStroke.getKeyStroke("ctrl L")); def apply { FileIO.loadFile } }),
          new Separator,
          new MenuItem(new Action("Close") { mnemonic = KeyEvent.VK_C; accelerator = Some(KeyStroke.getKeyStroke("ctrl C")); def apply { closeOperation } }))
      },
      new Menu("Help") {
        mnemonic = Key.H
        contents += (
          new MenuItem(new Action("Debug") { def apply { DebugWindow.open } }), // DEBUG
          new MenuItem(new Action("About") { def apply { AboutWindow.open } }))
      })
  }

  contents = new GridPanel(9,9) {
    border = Swing.LineBorder(Color.GRAY,5)
    (contents /: cells.view.flatten) (_ += _)
  }
  open
  override def main(s: Array[String]){
    super.main(s)
  }
}