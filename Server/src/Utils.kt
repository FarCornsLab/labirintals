package com.labirintals

import io.ktor.network.selector.*
import kotlinx.coroutines.Dispatchers

object Utils{
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val DefaultPort = 9002

}

object ErrorCode{
    val ErrBadRequest = 400
    val ErrConflict = 409
    val ErrGame= 405
    val NotAuthorized = 403
}