package com.labirintals.server.managers

import com.labirintals.model.base.PlayerModel

class SocketDataHolder(val socket: BaseSocket) {
    var player: PlayerModel? = null
}