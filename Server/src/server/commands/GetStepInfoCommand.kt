package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.utils.io.*

class GetStepInfoCommand: BaseCommand {
    override fun doCommand(socketData: SocketDataHolder): String? {
        val player = socketData.player
        return if(player == null){
            wrongPlayerResponse
        }else{
            successResponse(player.stepId, player.stepType)
        }
    }

    private val wrongPlayerResponse = StepAnswer(
        error = ErrorModel(ErrorCode.NotAuthorized, message = "Игрок не найден")
    ).toString()

    private val successResponse: (Int?, StepType?) -> String? = { stepId, stepType ->
        StepAnswer(
            stepId = stepId,
            stepType = stepType
        ).toString()
    }
}