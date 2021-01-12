package com.labirintals.server.commands

import com.labirintals.model.BaseModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class GetStepInfoCommand : BaseCommand() {
    companion object {
        const val TAG = "step_answer"
        const val TAG_NAME = "step"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val player =
            Server.storage.newPlayers.find { it.oid == socketData.player?.oid && it.cid == socketData.player?.cid }
                ?: socketData.player
        return BaseModel(
            commandName = TAG,
            commandParams = StepAnswer(Server.storage.globalStep, if(Server.storage.globalStep == player?.stepId) player.stepType else null),
            error = if (player == null) wrongPlayerResponse else null
        ).toString()
    }
}