package com.labirintals.model.base

import com.labirintals.model.requests.ConnectionModel
import com.labirintals.model.requests.MakeStepModel
import com.labirintals.server.Server

sealed class Command private constructor() {

    class getConnection(params: Any?) : Command() {
        val params = Server.gson.fromJson(params.toString(), ConnectionModel::class.java)
    }

    class getGameParams : Command()
    class getMakeStep(params: Any?) : Command() {
        val params = Server.gson.fromJson(params.toString(), MakeStepModel::class.java)
    }

    class getStepInfo : Command()
    class getPosition : Command()

    companion object {
        val command: (String?) -> (Any?) -> Command? = { name ->
            { params ->
                mapOf(
                    "connection" to getConnection(params),
                    "get_game_params" to getGameParams(),
                    "make_step" to getMakeStep(params),
                    "get_position" to getPosition(),
                    "step" to getStepInfo()
                )[name]
            }
        }


        val commands: (String?, Any?) -> Command? = { name, params ->
            mapOf(
                "connection" to getConnection(params),
                "get_game_params" to getGameParams(),
                "make_step" to getMakeStep(params),
                "get_position" to getPosition(),
                "step" to getStepInfo()
            )[name]
        }

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