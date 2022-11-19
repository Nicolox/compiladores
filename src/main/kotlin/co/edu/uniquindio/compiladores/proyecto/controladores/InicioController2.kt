package co.edu.uniquindio.compiladores.proyecto.controladores

import co.edu.uniquindio.compiladores.proyecto.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.proyecto.lexico.Token
import co.edu.uniquindio.compiladores.proyecto.sintaxis.AnalizadorSintactico
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.cell.PropertyValueFactory

class InicioController2 {

    @FXML lateinit var botonAnalizar: Button
    @FXML lateinit var tokens: TableView<Token>
    @FXML lateinit var lexema: TableColumn<Token, String>
    @FXML lateinit var categoria: TableColumn<Token, String>
    @FXML lateinit var fila: TableColumn<Token, String>
    @FXML lateinit var columna: TableColumn<Token, String>

    @FXML lateinit var tableResult: TableView<Token>
    @FXML lateinit var columnLex: TableColumn<Token, String>
    @FXML lateinit var columnCategory: TableColumn<Token, String>
    @FXML lateinit var columnRow: TableColumn<Token, String>
    @FXML lateinit var columnColumn: TableColumn<Token, String>

    private var analizadorLexico: AnalizadorLexico? = null

    private val data: ObservableList<Token> = FXCollections.observableArrayList()

    @FXML lateinit var codigoFuente:TextArea
    @FXML
    fun analizarLexema(e:ActionEvent)
    {
        if(codigoFuente.text.isNotEmpty())
        {
            println("Cantidad de caracteres: ${codigoFuente.length}")
            analizadorLexico = AnalizadorLexico(codigoFuente.text)
            analizadorLexico!!.analizar()
            addDataInTable(analizadorLexico!!.listaTokens)

            val lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()

            val sintaxis= AnalizadorSintactico(lexico.listaTokens)
            print(sintaxis.esUnidadDeCompilacion())
        }

    }
    fun processWords(codigoFuente: String) {

        println("Cantidad de caracteres: ${codigoFuente.length}")
        analizadorLexico = AnalizadorLexico(codigoFuente)
        analizadorLexico!!.analizar()
        addDataInTable(analizadorLexico!!.listaTokens)
        analizadorLexico!!.listaTokens.forEach { t ->
            println(t)
        }
    }
    private fun addDataInTable(listaTokens: ArrayList<Token>) {

        if(data.isNotEmpty()) data.clear()

        lexema.cellValueFactory = PropertyValueFactory("lexema")
        categoria.cellValueFactory = PropertyValueFactory("categoria")
        fila.cellValueFactory = PropertyValueFactory("fila")
        columna.cellValueFactory = PropertyValueFactory("columna")


        listaTokens.forEach { token -> tokens.items.add(token) }
//        tokens.items = data
        println(tokens.items)
        tokens.refresh()
    }

}