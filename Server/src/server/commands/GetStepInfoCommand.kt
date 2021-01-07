package com.labirintals.server.commands

import com.labirintals.model.BaseModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class GetStepInfoCommand : BaseCommand() {
    companion object {
        const val TAG = "step"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val player = Server.storage.newPlayers.find { it.oid == socketData.player?.oid && it.cid == socketData.player?.cid } ?: socketData.player

        return BaseModel(
            commandName = TAG,
            commandParams = if (player != null) stepResponse(player.stepId, player.stepType) else null,
            error = if (player == null) wrongPlayerResponse else null
        ).toString()
    }
}