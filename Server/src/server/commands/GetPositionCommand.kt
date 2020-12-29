package com.labirintals.server.commands

import com.labirintals.server.managers.SocketDataHolder
import io.ktor.utils.io.*

class GetPositionCommand: BaseCommand {
    companion object{
        val TAG = "get_position"
    }

    override fun doCommand(socketData: SocketDataHolder): String? {
        TODO("Not yet implemented")
    }
}