

object Main extends App {

  val formatter: TextFormatter = new TextFormatter

  val formattedText: List[String] = formatter.format(
    """In 1991, while studying computer science at University of Helsinki, Linus Torvalds began a project that later became the Linux kernel. He wrote the program specifically for the hardware he was using and independent of an operating system because he wanted to use the functions of his new PC with an 80386 processor. Development was done on MINIX using the GNU C Compiler.""",
    charLimit = 40)

  if(formattedText.isEmpty) println("No text provided or impossible to print with given char limit") else {
    formattedText.foreach(println)
  }

}