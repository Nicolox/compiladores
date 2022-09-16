package co.edu.uniquindio.compiladores.proyecto

class AnalizadorLexico (var codigoFuente:String){

    var posicionActual = 0
    var carActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0

    fun almacenarToken(lexema:String, cat:Categoria, fila:Int, columna:Int) = listaTokens.add(Token(lexema,cat,fila,columna))
    fun hacerBT(posicionInicial:Int, filaInical:Int,columnaInicial:Int){
        posicionActual = posicionInicial
        filaActual = filaInical
        columnaActual = columnaInicial

        carActual = codigoFuente[posicionActual]
    }
    fun analizar(){

        while (carActual != finCodigo){

            if(carActual == ' '|| carActual == '\t'|| carActual == '\n'){
                obtenerSiguienteCaracter()
                continue
            }
            if (esEntero())continue
            if (esDecimal())continue
            if (esIdentificadorVariable())continue

            almacenarToken("" + carActual,Categoria.DESCONOCIDO,filaActual,columnaActual)
            obtenerSiguienteCaracter()
        }
    }
    //Automata para determinar si es un número entero
    fun esEntero():Boolean{

        if (carActual.isDigit()){
            var lexema= ""
            var filaInical = filaActual
            var columnaInicial = columnaActual
            val posicionInicial = posicionActual

            lexema+=carActual
            obtenerSiguienteCaracter()

            while (carActual.isDigit()){
                lexema+=carActual
                obtenerSiguienteCaracter()
            }
            if(carActual == '.'){
                hacerBT(posicionInicial,filaInical,columnaInicial)
                return false
            }
            almacenarToken(lexema,Categoria.ENTERO,filaInical,columnaInicial)
            return true
        }
        return false
    }

    //Automata para determinar si es un identificador de variable
    //Toma como variabletodo lo que tenga un #
    //Token(Lexema='#casa', Categoria=IDENTIFICADOR_VARIABLE, Fila=0, Columna=15), Token(Lexema='#', Categoria=IDENTIFICADOR_VARIABLE, Fila=0, Columna=20)
    fun esIdentificadorVariable():Boolean{

        if (carActual == '#'){
            var lexema= ""
            var filaInical = filaActual
            var columnaInicial = columnaActual

            lexema+=carActual
            obtenerSiguienteCaracter()

            while ((carActual.isLetter() || carActual.isDigit() || (carActual == '_'))){
                lexema+=carActual
                obtenerSiguienteCaracter()
            }
            if (carActual == '#') {
                lexema += carActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema,Categoria.IDENTIFICADOR_VARIABLE,filaInical,columnaInicial)
            return true
        }

        return false
    }

    fun esDecimal(): Boolean{


        if(carActual == '.' || carActual.isDigit()){
            var lexema= ""
            var filaInical = filaActual
            var columnaInicial = columnaActual
            if (carActual == '.'){
                lexema += carActual
                obtenerSiguienteCaracter()

                if (carActual.isDigit()){
                    lexema += carActual
                    obtenerSiguienteCaracter()
                }
            }else {
                lexema += carActual
                obtenerSiguienteCaracter()

                while (carActual.isDigit()){
                    lexema += carActual
                    obtenerSiguienteCaracter()
                }
                if (carActual == '.') {
                    lexema += carActual
                    obtenerSiguienteCaracter()
                }
            }
            while (carActual.isDigit()){
                lexema += carActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema,Categoria.DECIMAL,filaInical,columnaInicial)
            return true
        }
        return false



    }
    fun obtenerSiguienteCaracter(){
        carActual = if (posicionActual == codigoFuente.length-1){
            finCodigo
        }else {
            if (carActual == '\n'){
                filaActual++
                columnaActual = 0
            }else {
                columnaActual++
            }
            posicionActual++
            codigoFuente[posicionActual]
        }
    }

}