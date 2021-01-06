package com.labirintals.server.commands

import com.labirintals.server.managers.SocketDataHolder

class GetGameResult: BaseCommand() {
    companion object{
        val TAG = "get_game_result"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        return super.doCommand(socketData)
    }
}