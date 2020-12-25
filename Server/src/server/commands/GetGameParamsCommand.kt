package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.responses.ConnectionAnswer
import com.labirintals.model.responses.GameParamsAnswer
import com.labirintals.server.Server.storage
import io.ktor.utils.io.*

class GetGameParamsCommand : BaseCommand {

    override fun doCommand(): String? {
        val error: ErrorModel?
        if(storage.players.isEmpty()){
            error = ErrorModel(code = ErrorCode.ErrBadRequest, message = "Массив игроков пуст")
        }else{
            error = null
        }
        val response = GameParamsAnswer(
            startTime = storage.serverParams.timeToString(),
            stepTime = storage.serverParams.stepTime,
            players = storage.players,
            error = error
        )
        return response.toString()
    }
}