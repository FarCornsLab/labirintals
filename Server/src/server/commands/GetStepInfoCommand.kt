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

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val player = socketData.player
        return BaseModel(
            commandName = TAG,
            commandParams = if (player != null) successResponse(player.stepId, player.stepType) else null,
            error = if (player == null) wrongPlayerResponse else null
        ).toString()
    }
}