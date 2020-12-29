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

class MakeStepCommand(args: Any?) : BaseCommand {

    companion object {
        val TAG = "make_step"
    }

    private val params: MakeStepModel = Server.gson.fromJson(args.toString(), MakeStepModel::class.java)

    override fun doCommand(socketData: SocketDataHolder): String? {
        var response: StepAnswer
        if (params.stepid == 0) {
            response = wrongStartGameResponse
        }
        if (params.stepid == -1) {
            response = wrongEndGameResponse
        }

        val player = socketData.player
        response = if (player == null) {
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

    private val wrongPlayerResponse = StepAnswer(
        error = ErrorModel(ErrorCode.NotAuthorized, message = "Игрок не найден")
    )

    private val successResponse: (Int?, StepType?) -> StepAnswer = { stepId, stepType ->
        StepAnswer(
            stepId = stepId,
            stepType = stepType
        )
    }
}