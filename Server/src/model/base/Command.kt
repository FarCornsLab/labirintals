package com.labirintals.model.base

import com.labirintals.model.BaseModel
import com.labirintals.model.ConnectionModel
import com.labirintals.model.MakeStepModel
import com.labirintals.server.Server

sealed class Command {
    class getConnection(params: Any?) : Command(){
        val params = Server.gson.fromJson(params.toString(), ConnectionModel::class.java)
    }
    class getGameParams : Command()
    class getMakeStep(params: Any?) : Command(){
        val params = Server.gson.fromJson(params.toString(), MakeStepModel::class.java)
    }
    class getStepInfo : Command()
    class getPosition : Command()

    companion object {
        fun getCommand(cmd: String, params: Any?): Command? {
            when (cmd) {
                "connection" -> return getConnection(params)
                "get_game_params" -> return getGameParams()
                "make_step" -> return getMakeStep(params)
                "get_position" -> return getPosition()
                "step" -> return getStepInfo()
            }
            return null
        }
    }
}