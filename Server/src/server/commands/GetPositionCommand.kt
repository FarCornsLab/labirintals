package com.labirintals.server.commands

import com.labirintals.model.BaseModel
import com.labirintals.model.responses.PositionAnswer
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.utils.io.*

class GetPositionCommand : BaseCommand() {
    companion object {
        val TAG = "get_position"
    }

    override fun doCommand(socketData: SocketDataHolder): String? {
        val player = socketData.player
        if (player == null) {
            return BaseModel(commandName = MakeStepCommand.TAG, commandParams = wrongPlayerResponse).toString()
        } else {
            return BaseModel(
                commandName = MakeStepCommand.TAG,
                commandParams =  PositionAnswer(player.stepId, player.position)
            ).toString()
        }
    }
}