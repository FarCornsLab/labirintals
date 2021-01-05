package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.MakeStepModel
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.network.sockets.*

class MakeStepCommand(args: Any?) : BaseCommand() {

    companion object {
        val TAG = "make_step"
    }

    private val params: MakeStepModel = Server.gson.fromJson(args.toString(), MakeStepModel::class.java)

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        if (params.stepId == 0) {
            return BaseModel(commandName = TAG, error = wrongStartGameResponse).toString()
        }
        if (params.stepId == -1) {
            return BaseModel(commandName = TAG, error = wrongEndGameResponse).toString()
        }

        val player = socketData.player
        val response = if (player == null) {
            null
        } else {
            val index = Server.storage.players.indexOf(player)
//            if(params.stepId!! == Server.storage.globalStep) {
//
//            }else{
//
//            }
            if(Server.storage.globalStep == -1){
                successResponse(-1, null)
            }else {
                player.updateStep(
                    params.stepId!!,
                    params.stepType!!
                )//player.copy(stepId = params.stepId, stepType = params.stepType)
                socketData.player = player
                Server.storage.players[index] = player
                successResponse(params.stepId, params.stepType)
            }
        }

        return BaseModel(
            commandName = TAG,
            commandParams = response,
            error = if (player == null) wrongPlayerResponse else null
        ).toString()
    }

    private val wrongStartGameResponse = ErrorModel(ErrorCode.ErrGame, message = "Игра еще не началась")

    private val wrongEndGameResponse = ErrorModel(ErrorCode.ErrGame, message = "Игра уже закончилась")

}