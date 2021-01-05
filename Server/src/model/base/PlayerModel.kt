package com.labirintals.model.base

import com.google.gson.annotations.SerializedName
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.PositionModel
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
    fun toClientModel() = ClientModel(name, oid)

    fun updateStep(newStepId: Int, newStepType: StepType) {
        //if(newStepId == Server.storage.globalStep) {
            stepId = newStepId
            stepType = newStepType
            changeCoords()
            val canStep = canMakeStep()
            if (canStep == stepId) {
                borders = Server.storage.labirint.getBorders(coords!!)
            }
            Server.storage.globalStep = canStep
        //}
    }

    private fun canMakeStep(): Int {
        if (borders!![stepType?.index!!] == Entity.exit) {
            return -1
        }
        if (borders!![stepType?.index!!] == Entity.obstacle) {
            return 0
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