package classes

//class Variables {

    fun main(){
        //Numeric Variables
        val age:Int = 20
        val longNumber:Long = 284042455451111111;
        val temperature:Float = 27.123f;
        val Weight:Double = 68.4;

        //Variables String
        val genero:Char = 'M'
        val name:String = "Jorge Calderón"

        //Boolean
        val isGrater:Boolean = true

        //Array
        val names = arrayOf("Jorge", "Elotes", "Piña", "Arturo")

        println("Welcome $name, to your first Kotlin project")

        println(add())
        println(product(19,20))
    }

fun add():Int{
    val x = 5
    val y = 10

    return x+y
}

fun product(x:Int, y:Int):Int{
    return x+y
}
//}