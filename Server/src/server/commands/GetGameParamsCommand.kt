package com.labirintals.server.commands

import com.labirintals.ErrorCode
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
        val TAG = "get_game_params"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val error: ErrorModel?
        if (storage.players.isEmpty()) {
            error = ErrorModel(code = ErrorCode.ErrBadRequest, message = "Массив игроков пуст")
        } else {
            error = null
        }
        val response = GameParamsAnswer(
            startTime = storage.serverParams.timeToString(),
            stepTime = storage.serverParams.stepTime,
            players = storage.players.map { it.toClientModel() },
        )
        return BaseModel(commandName = TAG, commandParams = response, error = error).toString()
    }
}