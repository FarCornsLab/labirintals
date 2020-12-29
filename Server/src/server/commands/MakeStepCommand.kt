package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.MakeStepModel
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.network.sockets.*

class MakeStepCommand(args: Any?) : BaseCommand() {

    companion object {
        val TAG = "make_step"
    }

    private val params: MakeStepModel = Server.gson.fromJson(args.toString(), MakeStepModel::class.java)

    override fun doCommand(socketData: SocketDataHolder): String? {
        if (params.stepid == 0) {
            return BaseModel(commandName = TAG, commandParams = wrongStartGameResponse).toString()
        }
        if (params.stepid == -1) {
            return BaseModel(commandName = TAG, commandParams = wrongEndGameResponse).toString()
        }

        val player = socketData.player
        val response = if (player == null) {
            wrongPlayerResponse
        } else {
            val index = Server.storage.players.indexOf(player)
            Server.storage.players[index] = player.copy(stepId = params.stepid, stepType = params.stepType)
            successResponse(params.stepid, params.stepType)
        }

        return BaseModel(commandName = TAG, commandParams = response).toString()
    }

    private val wrongStartGameResponse = StepAnswer(
        stepId = 0,
        error = ErrorModel(ErrorCode.ErrGame, message = "Игра еще не началась")
    )

    private val wrongEndGameResponse = StepAnswer(
        stepId = -1,
        error = ErrorModel(ErrorCode.ErrGame, message = "Игра уже закончилась")
    )
}