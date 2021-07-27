package board

import board.Direction.*
import java.lang.IllegalArgumentException

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    var cells = arrayOf<Array<Cell>>()
    init {
        for(i in 0 until width) {
            var row = arrayOf<Cell>()
            for(j in 0 until width){
                row += Cell(i+1, j+1)
            }
            cells += row
        }
        println(width)
        println(cells.flatten().toString())
    }

    override fun getCell(i: Int, j: Int): Cell {
        if(i > cells.size || j > cells[0].size || i < 1 || j < 1) throw IllegalArgumentException("Invalid cell numbers")
        else return cells[i-1][j-1]
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return try {
            getCell(i, j)
        } catch (exception: IllegalArgumentException) {
            null
        }
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        if(i < 1 || i > cells.size) throw IllegalArgumentException("i is invalid")
        if(jRange.max() == null || jRange.min() == null) throw IllegalArgumentException("column range is invalid")
        if(jRange.max()!! > cells[0].size && jRange.min()!! < 1) throw IllegalArgumentException("column range is invalid")
        var ret = arrayOf<Cell>()
        for(j in jRange){
            if(j > cells[0].size) break
            ret += cells[i-1][j-1]
        }
        return ret.toList()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        if(j < 1 || j > cells.size) throw IllegalArgumentException("j is invalid")
        if(iRange.max() == null || iRange.min() == null) throw IllegalArgumentException("row range is invalid")
        if(iRange.max()!! > cells[0].size && iRange.min()!! < 1) throw IllegalArgumentException("row range is invalid")
        var ret = arrayOf<Cell>()
        for(i in iRange){
            if(i > cells.size) break
            ret += cells[i-1][j-1]
        }
        return ret.toList()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP    -> this@SquareBoardImpl.getCellOrNull(this.i-1, this.j)
            RIGHT -> this@SquareBoardImpl.getCellOrNull(this.i, this.j+1)
            LEFT  -> this@SquareBoardImpl.getCellOrNull(this.i, this.j-1)
            DOWN  -> this@SquareBoardImpl.getCellOrNull(this.i+1, this.j)
        }
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val cellValues: MutableMap<Cell, T?> = getAllCells().associateWith { null }.toMutableMap()

    override operator fun get(cell: Cell): T? = cellValues[cell]
    override operator fun get(i: Int, j: Int): T? = cellValues[Cell(i, j)]

    override fun set(cell: Cell, value: T?) {
        cellValues += cell to value
    }

    override fun set(i: Int, j: Int, value: T?) = this.set(Cell(i, j), value)

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = cellValues.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? = cellValues.filter{ predicate.invoke(it.value) }.keys.firstOrNull()

    override fun all(predicate: (T?) -> Boolean): Boolean = cellValues.values.all(predicate)

    override fun any(predicate: (T?) -> Boolean): Boolean = cellValues.values.any(predicate)
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

fun main(args: Array<String>) {
    val gameBoard = createGameBoard<Char>(2)
    gameBoard[1, 1] = 'a'
    gameBoard[1, 2] = 'a'
    println(gameBoard.all { it == 'a' })
    gameBoard[2, 1] = 'a'
    gameBoard[2, 2] = 'a'
    println(gameBoard.all { it == 'a' })
}