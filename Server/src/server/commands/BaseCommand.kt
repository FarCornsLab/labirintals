package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import java.io.BufferedWriter

open class BaseCommand {
    open suspend fun doCommand(socketData: SocketDataHolder): String? { return null }

    val wrongPlayerResponse = ErrorModel(ErrorCode.NotAuthorized, message = "Игрок не найден")
}