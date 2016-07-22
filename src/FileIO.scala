
import scala.io.Source
import scala.swing.FileChooser
import java.io.File
import javax.swing.filechooser.FileFilter
import java.io.PrintWriter

object FileIO {
    
  def chooseFile(saveOrLoad: String) {
    val fc = new FileChooser(new File("."))
    if(saveOrLoad == "load") fc.title = "Load a puzzle"
    else if(saveOrLoad == "save") fc.title = "Save a puzzle"
    fc.multiSelectionEnabled = false
    fc.fileFilter = new FileFilter() { 
      def accept(file: File): Boolean = {
        if(file.getName().startsWith(".")) false
        else if(file.isDirectory() || file.getName().endsWith(".scd")) true
        else false        
      }
      def getDescription(): String = "*.scd files"
    }
    try {
    if(saveOrLoad == "load" && FileChooser.Result.Approve == fc.showOpenDialog(null)) read(fc.selectedFile)
    else if(saveOrLoad == "save" && FileChooser.Result.Approve == fc.showSaveDialog(null)) write(fc.selectedFile)
    } catch {
      case ex => println("Caught exception: %s" format ex)
    } 
  }
  
  def loadFile() {
    chooseFile("load")
  }
  
  def saveFile() {
    chooseFile("save")
  }
  
  private def read(file: File) {
    Main.cells.view.flatten.foreach(_.reset)
    try {
      if ( Source.fromFile(file).length > 81 || !Source.fromFile(file).forall(_.isDigit)) throw new java.lang.IllegalArgumentException("Invalid format")
      var x, y = 0
      for (s <- Source.fromFile(file).getLines; c <- s) {
        c match {
          case '0' =>
          case '1' => Main.cells(x)(y).loadVal(1)
          case '2' => Main.cells(x)(y).loadVal(2)
          case '3' => Main.cells(x)(y).loadVal(3)
          case '4' => Main.cells(x)(y).loadVal(4)
          case '5' => Main.cells(x)(y).loadVal(5)
          case '6' => Main.cells(x)(y).loadVal(6)
          case '7' => Main.cells(x)(y).loadVal(7)
          case '8' => Main.cells(x)(y).loadVal(8)
          case '9' => Main.cells(x)(y).loadVal(9)
          case e => throw new java.lang.IllegalArgumentException("Unknown argument: %s" format e)
        }
        if (y > 7) { x += 1; y = 0 } else y += 1
      }
    } catch {
      case ex => println("Caught exception: %s" format ex)
    }
  }
  
  private def write(fileN: File) {
    var file = fileN
    if(!file.getName().endsWith(".scd")) file = new File(file + ".scd")
    try {
      val pw = new PrintWriter(file)
      pw.write(Main.cellsAsStr)
      pw.close()
    } catch {
      case ex => println("Caught exception: %s" format ex)
    }
  }
}