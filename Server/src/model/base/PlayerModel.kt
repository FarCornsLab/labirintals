package com.labirintals.model.base

import com.google.gson.annotations.SerializedName
import com.labirintals.model.requests.MakeStepModel
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.Server
import com.labirintals.server.labirint.Entity
import com.labirintals.server.labirint.Labirint

data class PlayerModel(
    val name: String? = null,
    val cid: String? = null,
    val oid: String? = null,
    var stepId: Int? = 0,
    var stepType: StepType? = null,
    var coords: Coords? = null,
    var borders: List<Entity>? = null
) {
    companion object{
        val END_GAME = -1
        val OBSTACLE = 0
    }

    fun toClientModel() = ClientModel(name, oid)

    fun updateStep(params: MakeStepModel): Int {
        stepId = params.stepId
        stepType = params.stepType
        changeCoords()
        val canStep = canMakeStep()
        if (canStep == stepId) {
            borders = Server.storage.labirint.getBorders(coords!!)
        }
        return canStep
    }

    private fun canMakeStep(): Int {
        if (borders!![stepType?.index!!] == Entity.exit) {
            return END_GAME
        }
        if (borders!![stepType?.index!!] == Entity.obstacle) {
            return OBSTACLE
        }
        return stepId!!
    }

    private fun changeCoords() {
        when (stepType) {
            StepType.up -> {
                coords = coords?.copy(y = coords?.y!! - 1)
            }
            StepType.right -> {
                coords = coords?.copy(x = coords?.x!! + 1)
            }
            StepType.left -> {
                coords = coords?.copy(x = coords?.x!! - 1)
            }
            StepType.down -> {
                coords = coords?.copy(y = coords?.y!! + 1)
            }
        }
    }
}

data class ClientModel(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("oid")
    val oid: String? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}

data class Coords(
    @SerializedName("x")
    val x: Int? = null,
    @SerializedName("y")
    val y: Int? = null
)