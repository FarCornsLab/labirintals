package com.labirintals

import io.ktor.network.selector.*
import kotlinx.coroutines.Dispatchers

object Utils{
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val DefaultPort = 9002
}