package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.utils.io.*

class GetStepInfoCommand : BaseCommand() {
    companion object {
        val TAG = "step"
    }

    override fun doCommand(socketData: SocketDataHolder): String? {
        val player = socketData.player
        val response = if (player == null) {
            wrongPlayerResponse
        } else {
            successResponse(player.stepId, player.stepType)
        }
        return BaseModel(commandName = TAG, commandParams = response).toString()
    }
}