package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.ErrorNames
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.responses.ConnectionAnswer
import com.labirintals.model.responses.GameParamsAnswer
import com.labirintals.server.Server.storage
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

class GetGameParamsCommand : BaseCommand() {
    companion object {
        const val TAG = "game_params"
        const val TAG_NAME = "get_game_params"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val error = if (storage.players.isEmpty()) {
            ErrorModel(code = ErrorCode.ErrBadRequest, message = ErrorNames.ErrNoPlayers)
        } else {
            null
        }
        val response = GameParamsAnswer(
            startTime = storage.serverParams.timeStart,
            stepTime = storage.serverParams.stepTime,
            players = storage.players.map { it.toClientModel() },
        )

        return BaseModel(
            commandName = TAG,
            commandParams = if (error == null) response else null,
            error = error
        ).toString()
    }
}