package co.edu.uniquindio.compiladores.proyecto.controladores

import co.edu.uniquindio.compiladores.proyecto.lexico.AnalizadorLexico
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TextArea

class InicioController
{
    lateinit var texto:TextArea
    @FXML
    fun analizarLexema(e:ActionEvent)
    {
        if(texto.text.length>0)
        {
            val lexico = AnalizadorLexico(texto.text)
            lexico.analizar()

            print (lexico.listaTokens)
        }

    }
}