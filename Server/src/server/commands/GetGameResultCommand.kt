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

class GetGameResultCommand : BaseCommand() {
    companion object {
        const val TAG = "game_result"
        const val TAG_NAME = "get_game_result"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        if (Server.storage.winners.isEmpty()) {
            Server.storage.winners.addAll(Server.storage.players.filter { it.stepId == PlayerModel.END_GAME }
                .map { it.toClientModel() })
        }
        if (Server.storage.winners.isNotEmpty()) {
            with(Server.storage) {
                val map = MapModel(
                    labirint.horizontalBorders,
                    labirint.verticalBorders,
                    listOf(MapPlayer(socketData.player?.toClientModel(), labirint.spawn))
                )
                val res = GetGameResultResponse(winners, finalStep, map)
                return BaseModel(commandName = TAG, commandParams = res).toString()
            }
        }
        return BaseModel(commandName = TAG, error = ErrorModel(ErrorCode.ErrGame, ErrorNames.ErrNoWinners)).toString()
    }
}