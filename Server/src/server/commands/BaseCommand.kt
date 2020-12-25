package com.labirintals.server.commands

import com.labirintals.model.base.PlayerModel
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import java.io.BufferedWriter

interface BaseCommand {
    fun doCommand(socketData: SocketDataHolder): String?
}