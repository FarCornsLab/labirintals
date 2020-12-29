package com.labirintals.server.labirint

import java.io.File
import kotlin.random.Random

class Labirint private constructor(
    width: Int,
    height: Int,
    val data: Array<Array<Entity>> = Array<Array<Entity>>(height) { Array(width) { Entity.empty } }
) {
    //val data = Array<Array<Entity>>(height) { Array(width) { Entity.empty } }

    private var freePlaces = ArrayList<Pair<Int, Int>>() //x, y

    fun setFreeSpaces(array: ArrayList<Pair<Int, Int>>){
        freePlaces = array
    }

    fun getFreePlace(): Pair<Int, Int> {
        val random = Random.nextInt(0, freePlaces.lastIndex)
        val res = freePlaces[random]
        data[res.first][res.second] = Entity.player
        freePlaces.remove(res)
        return res
    }


    fun printLabirint() {
        data.forEachIndexed { index, arrayOfEntitys ->
            arrayOfEntitys.forEach {
                print(it.sign)
            }
            println()
        }
    }

    companion object {

        fun generate(width: Int, height: Int): Labirint {
            val labirint = Labirint(width, height)
            labirint.data.forEachIndexed { index, arrayOfEntitys ->
                arrayOfEntitys.forEachIndexed { indexLine, entity ->
                    if (index == 0 || index == labirint.data.lastIndex) {
                        arrayOfEntitys[indexLine] = Entity.wall
                    } else if (indexLine == 0 || indexLine == arrayOfEntitys.lastIndex) {
                        arrayOfEntitys[indexLine] = Entity.wall
                    }
                }
            }
            return labirint
        }

        fun read(fileName: String): Labirint {
            val file = File(fileName)
            if (!file.isFile) {
                return generate(10, 10)
            }
            val matrix = ArrayList<Array<Entity>>()
            val freePlaces = ArrayList<Pair<Int, Int>>() //x, y
            file.bufferedReader().lineSequence().iterator().forEachRemaining { line ->
                val arrayLine = Array<Entity>(line.length) { Entity.empty }
                line.forEachIndexed { index, symbol ->
                    arrayLine[index] = Entity.fromChar(symbol)
                    if(arrayLine[index] == Entity.empty){
                        freePlaces.add(matrix.size to index)
                    }
                }
                matrix.add(arrayLine)
            }
            val labirint = Labirint(matrix.size, matrix.first().size, data = matrix.toTypedArray())
            labirint.setFreeSpaces(freePlaces)
            return labirint
        }
    }

}

enum class Entity(val sign: Char) {
    wall('#'),
    empty(' '),
    player('P'),
    exit('*');

    companion object {
        fun fromChar(ch: Char): Entity {
            when (ch) {
                '#' -> return wall
                'P' -> return player
                '*' -> return exit
                else -> return empty
            }
        }
    }
}