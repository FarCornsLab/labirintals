package com.labirintals.server.commands

import com.labirintals.model.BaseModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.responses.PositionAnswer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class GetPositionCommand : BaseCommand() {
    companion object {
        const val TAG = "position"
        const val TAG_NAME = "get_position"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val player =
            Server.storage.players.find { it.oid == socketData.player?.oid && it.cid == socketData.player?.cid }
        player?.let {
            socketData.player = it
        }
        return if (player == null) {
            BaseModel(commandName = MakeStepCommand.TAG, error = wrongPlayerResponse).toString()
        } else {
            player.stepId = Server.storage.globalStep
//            if (Server.storage.globalStep == -1) {
//                player.stepId = -1
//            }
            if (player.stepId == PlayerModel.END_GAME) {
                return BaseModel(commandName = TAG, commandParams = PositionAnswer(player.stepId)).toString()
            }
            val nextStep = if (Server.storage.stepTimeTo != null) {
                Server.storage.stepTimeTo!! //+ TimeUnit.SECONDS.toMillis(LocalStorage.WAITING_START_SECONDS)
            } else {
                null
            }
            BaseModel(
                commandName = TAG,
                commandParams = PositionAnswer(
                    player.stepId, player.borders, nextStep
                )
            ).toString()
        }
    }
}