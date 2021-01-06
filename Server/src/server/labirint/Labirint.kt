package com.labirintals.server.labirint

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.CellModel
import com.labirintals.model.base.Coords
import com.labirintals.server.Server
import java.io.File
import kotlin.random.Random

data class Labirint(
    @SerializedName("horizontal_border")
    val horizontalBorders: List<List<Entity>>? = null,
    @SerializedName("vertical_border")
    val verticalBorders: List<List<Entity>>? = null,
    @SerializedName("spawn")
    val spawn: Coords? = null
) {
    fun getBorders(position: Coords) = listOf(getTop(position), getRight(position), getBottom(position), getLeft(position))

    fun getTop(position: Coords) = horizontalBorders!![position.y!!][position.x!!]

    fun getBottom(position: Coords) = horizontalBorders!![position.y!! + 1][position.x!!]

    fun getRight(position: Coords) = verticalBorders!![position.x!! + 1][position.y!!]

    fun getLeft(position: Coords) = verticalBorders!![position.x!!][position.y!!]

    companion object{
        fun read(fileName: String): Labirint{
            val file = File(fileName)
            val data = file.readText()
            return Server.gson.fromJson(data, Labirint::class.java)
        }
    }
}

//class Labirint private constructor(
//    width: Int,
//    height: Int,
//    val data: Array<Array<CellModel>> = Array<Array<CellModel>>(height) {
//        Array(width) {
//            CellModel(
//                Entity.free,
//                Entity.free,
//                Entity.free,
//                Entity.free,
//                Coords(0, 0)
//            )
//        }
//    }
//) {
//    //val data = Array<Array<Entity>>(height) { Array(width) { Entity.empty } }
//
//    private var freePlaces = ArrayList<Coords>() //x, y
//
//    fun setFreeSpaces(array: ArrayList<Coords>) {
//        freePlaces = array
//    }
//
//    fun getFreePlace(): Coords {
//        val random = Random.nextInt(0, freePlaces.lastIndex)
//        val res = freePlaces[random]
//        //data[res.x!!][res.y!!]. = Entity.player
//        freePlaces.remove(res)
//        return res
//    }
//
//    fun getPositions(playerPos: Pair<Int, Int>): List<Entity> {
//        val up = getPosition(playerPos.first != 0, playerPos.first - 1, playerPos.second)
//        val left = getPosition(playerPos.second != 0, playerPos.first, playerPos.second - 1)
//        val down = getPosition(playerPos.first != data.lastIndex, playerPos.first + 1, playerPos.second)
//        val right =
//            getPosition(playerPos.second != data[playerPos.first].lastIndex, playerPos.first, playerPos.second + 1)
//        return listOf(up, left, down, right)
//    }
//
//    private fun getPosition(expression: Boolean, row: Int, column: Int): Entity {
//        return if (expression) {
//            data[row][column]
//        } else {
//            Entity.free
//        }
//    }
//
//    fun printLabirint() {
//        data.forEachIndexed { index, arrayOfEntitys ->
//            arrayOfEntitys.forEach {
//                print(it.sign)
//            }
//            println()
//        }
//    }
//
//    companion object {
//
////        fun generate(width: Int, height: Int): Labirint {
////            val labirint = Labirint(width, height)
////            labirint.data.forEachIndexed { index, arrayOfEntitys ->
////                arrayOfEntitys.forEachIndexed { indexLine, entity ->
////                    if (index == 0 || index == labirint.data.lastIndex) {
////                        arrayOfEntitys[indexLine] = Entity.obstacle
////                    } else if (indexLine == 0 || indexLine == arrayOfEntitys.lastIndex) {
////                        arrayOfEntitys[indexLine] = Entity.obstacle
////                    }
////                }
////            }
////            return labirint
////        }
//
//        fun read(fileName: String): Labirint {
//            val file = File(fileName)
////            if (!file.isFile) {
////                return generate(10, 10)
////            }
//            val matrix = ArrayList<Array<CellModel>>()
//            val freePlaces = ArrayList<Pair<Int, Int>>() //x, y
//            var row = 0
//            var x = 0
//            var y = 0
//            var isTop = true
//
//
//            file.bufferedReader().lineSequence().iterator().forEachRemaining { line ->
//                val arrayLine = Array<CellModel>(line.length) {
//                    CellModel(
//                        topBorder = Entity.free,
//                        rightBorder = Entity.free,
//                        bottomBorder = Entity.free,
//                        leftBorder = Entity.free,
//                        Coords(it, if(matrix.isNotEmpty()) matrix.lastIndex else 0)
//                    )
//                }
//                if(row % 2 == 0) {
//                    if(isTop) {
//                        for ((indexLine, index) in (1 until line.length step 2).withIndex()) {
//                            val symbol = line[index]
//                            arrayLine[indexLine].topBorder = Entity.fromChar(symbol)
//                        }
//                    }else{
//                        for ((indexLine, index) in (1 until line.length step 2).withIndex()) {
//                            val symbol = line[index]
//                            arrayLine[indexLine].bottomBorder = Entity.fromChar(symbol)
//                        }
//                    }
//                    isTop = !isTop
//                }else{
//                    for ((indexLine, index) in (1 until line.length / 3).withIndex()){
//                        val start = line[index - 1]
//                        val end = line[index + 1]
//                        arrayLine[indexLine].apply {
//                            leftBorder = Entity.fromChar(start)
//                            rightBorder = Entity.fromChar(end)
//                        }
//                    }
//                }
//
//                row++
//                matrix.add(arrayLine)
//            }
//            val labirint = Labirint(matrix.size, matrix.first().size, data = matrix.toTypedArray())
//            labirint.setFreeSpaces(freePlaces)
//            return labirint
//        }
//    }
//
//}
//
enum class Entity(val sign: Char) {
    obstacle('|'),
    free(' '),
    player('P'),
    exit('*');

    companion object {
        fun fromChar(ch: Char): Entity {
            when (ch) {
                '|' -> return obstacle
                '-' -> return obstacle
                '*' -> return player
                ':' -> return exit
                else -> return free
            }
        }
    }
}