package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.ErrorNames
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.responses.GetGameResultResponse
import com.labirintals.model.responses.MapModel
import com.labirintals.model.responses.MapPlayer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class GetMapCommand : BaseCommand() {
    companion object {
        const val TAG = "get_map"
        const val TAG_NAME = "get_map"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        if (socketData.observerToken == Server.config.observerToken) {
            with(Server.storage) {
                val map = MapModel(
                    labirint.horizontalBorders,
                    labirint.verticalBorders,
                    Server.storage.players.filter { it.stepId != PlayerModel.END_GAME }
                        .map { MapPlayer(it.toClientModel(), it.coords) }
                )
                return BaseModel(commandName = TAG, commandParams = GetGameResultResponse(stepId = Server.storage.globalStep, map = map)).toString()
            }
        }
        return BaseModel(
            commandName = TAG,
            error = ErrorModel(ErrorCode.NotAuthorized, ErrorNames.ErrWrongToken)
        ).toString()
    }
}