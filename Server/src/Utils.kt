package com.labirintals

import io.ktor.network.selector.*
import kotlinx.coroutines.Dispatchers

object Utils{
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val DefaultPort = 9999
}

object ErrorCode{
    val ErrBadRequest = 400
    val ErrConflict = 409
    val ErrGame= 405
    val NotAuthorized = 403
}

object ErrorNames{
    val ErrBusyName = "Name is busy"
    val ErrNoPlayers = "No players"
    val ErrNoWinners = "No winners"
    val ErrNoStep = "Walking into the wall is forbidden"
    val ErrTimeIsOver = "The time of the move is out"
    val ErrGameIsNotStarted = "The game hasn`t started yet"
    val ErrGameAlreadyEnd = "The game is already over"
    val ErrNoUserName = "Enter user name"
    val ErrNoPlayer = "Player not found"
    val ErrWrongToken = "Observer token is wrong"
}