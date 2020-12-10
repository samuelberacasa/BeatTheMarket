package Output

object Tabulator {
  def format(table: Seq[Seq[Any]]) = {
    val sizes = for (row <- table.filter(_!=Nil)) yield (for (cell <- row) yield if (cell == null) 0 else cell.toString.length)
    val colSizes = for (col <- sizes.transpose) yield col.max + 1
    val rows = for (row <- table) yield formatRow(row, colSizes)
    formatRows(rowSeparator(colSizes), rows)
  }

  def formatRows(rowSeparator: String, rows: Seq[String]): String = {
    (rowSeparator :: rows.head :: rowSeparator :: rows.tail.toList ::: rowSeparator :: List()).mkString("\n")
  }

  def formatRow(row: Seq[Any], colSizes: Seq[Int]) = row match {
    case Nil => rowSeparator(colSizes)
    case _ => (for ((item, size) <- row.zip(colSizes)) yield if (size == 0) "" else ("%" + size + "s").format(item)).mkString("|", "|", "|")
  }

  def rowSeparator(colSizes: Seq[Int]) = colSizes map { "-" * _ } mkString("+", "+", "+")
}
