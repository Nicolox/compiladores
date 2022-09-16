package co.edu.uniquindio.compiladores.proyecto

class Token ( var lexema:String, var categoria:Categoria, var fila:Int, var columna:Int ) {

    override fun toString(): String {
        return "Token(Lexema='$lexema', Categoria=$categoria, Fila=$fila, Columna=$columna)"
    }
}