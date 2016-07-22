
object Solver {
  var rows: Array[Array[Cell]] = null
  var cols: Array[Array[Cell]] = null
  var blks: Array[Array[Cell]] = null
  def RCB(c: Cell) = (rows(c.row) ++ cols(c.col) ++ blks(c.blk)).distinct
  val rFin, cFin, bFin = Array.fill(9)(false)
  
  def populateSolvers(cs: Array[Array[Cell]]) {
    rows = cs
    cols = Array.ofDim[Cell](9, 9)
    blks = Array.ofDim[Cell](9, 9)
    for(x <- (0 to 8); y <- (0 to 8)) {
      cols(x)(y) = rows(y)(x)
//      blks(c.blk)((c.col + 3*c.row) % 9) = rows
      val bxy = {
        val ab = x/3 -> y/3
        (ab._1*3 + ab._2) -> ((x - ab._1*3)*3 + (y - ab._2*3))
      }
      blks(bxy._1)(bxy._2) = rows(x)(y)
    }
    //println(rows)
  }
  
  def cellRCBCheck(c: Cell): Array[(Cell,Boolean)] = {
    if(c.value != 0) {
      val completeSet = "123456789"
      rFin(c.row) = rows(c.row).sortBy(_.value).mkString.equals(completeSet)
      cFin(c.col) = cols(c.col).sortBy(_.value).mkString.equals(completeSet)
      bFin(c.blk) = blks(c.blk).sortBy(_.value).mkString.equals(completeSet)
    }
    val invalids = whereInvalid(c)
    RCB(c).filterNot(_ == c).map(f => f -> !invalids.exists(_ == f))
  }
  
  def whereInvalid(c: Cell): Array[Cell] = {
    if(c.value != 0) {
      var invalids = RCB(c).filter(_.value == c.value) 
      if(invalids.length < 2) Array.empty[Cell]
      else invalids
    }
    else Array.empty[Cell]
  }
  
  def isInvalid(c: Cell): Boolean = {
    if(c.value != 0 && RCB(c).filter(_.value == c.value).length > 1) true
    else false
  }
  
  def winCondition() = !(rFin ++ cFin ++ bFin).exists(_ == false)
}