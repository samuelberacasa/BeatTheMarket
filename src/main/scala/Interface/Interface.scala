package Interface

import Portfolio._
import Output.Report
import org.json4s.jackson.Serialization

import java.io.{File, PrintWriter}
import scala.io.Source


object Interface {
  def main(args: Array[String]):Unit = {
    val pwriter = new PrintWriter(new File("./src/main/resources/test.txt"))
    pwriter.write("Hello World!")
    pwriter.close()

    val fileContent = Source.fromFile("./src/main/resources/test.txt")
    println(fileContent.getLines().mkString)

    val pwriter2 = new PrintWriter(new File("./src/main/resources/test.txt"))
    pwriter2.write("Hello World Sammy!")
    pwriter2.close()

    val fileContent2 = Source.fromFile("./src/main/resources/test.txt")
    println(fileContent2.getLines().mkString)
  }
}
