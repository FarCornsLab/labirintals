package com.labirintals.server.commands

import com.labirintals.model.BaseModel
import com.labirintals.model.responses.PositionAnswer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class GetPositionCommand : BaseCommand() {
    companion object {
        val TAG = "get_position"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val player = socketData.player
        if (player == null) {
            return BaseModel(commandName = MakeStepCommand.TAG, error = wrongPlayerResponse).toString()
        } else {
            if(Server.storage.globalStep == -1){
                player.stepId = -1
            }
            return BaseModel(
                commandName = TAG,
                commandParams =  PositionAnswer(player.stepId, player.borders)
            ).toString()
        }
    }
}