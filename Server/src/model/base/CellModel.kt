package com.labirintals.model.base

import com.labirintals.server.labirint.Entity

data class CellModel(
    var topBorder: Entity,
    var rightBorder: Entity,
    var bottomBorder: Entity,
    var leftBorder: Entity,
    val coords: Coords
) {

}