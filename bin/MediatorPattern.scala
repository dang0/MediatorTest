
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits._

class Mediator {

  def setSolvers(cs: Array[Array[Cell]]) {
    Solver.populateSolvers(cs)
  }

  def cellFocus(implicit c: Cell) {
    c.requestFocus
    DebugWindow.setCell
  }

  def checkInvalid(implicit c: Cell): Boolean = Solver.isInvalid(c)
  
  def notify(implicit c: Cell) {
    var loaded = false
    if(c.locked) loaded = true
    else c.lock
    future {
      Solver.cellRCBCheck(c)
    } onSuccess {
      case e: Array[(Cell,Boolean)] => 
        e.foreach{ f => 
          if(f._2) {
            c.conflicts -= f._1.hashCode
            f._1.conflicts -= c.hashCode
          }
          else {
            c.conflicts += f._1.hashCode
            f._1.conflicts += c.hashCode
          }
        }
        if(!loaded) c.unlock
        future { 
          if(c.value != 0) Solver.winCondition
        } onSuccess { 
          case true => ModalWin.open 
          case _ => 
        }
    }
  }

}