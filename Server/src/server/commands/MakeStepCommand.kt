package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.ErrorNames
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.MakeStepModel
import com.labirintals.model.responses.StepAnswer
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class MakeStepCommand(args: Any?) : BaseCommand() {

    companion object {
        const val TAG = "step_answer"
        const val TAG_NAME = "make_step"
    }

    private val params: MakeStepModel = Server.gson.fromJson(args.toString(), MakeStepModel::class.java)

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        if (Server.storage.globalStep == 0) {
            return BaseModel(commandName = TAG, error = wrongStartGameResponse).toString()
        }
        if (Server.storage.globalStep == -1) {
            return BaseModel(commandName = TAG, error = wrongEndGameResponse).toString()
        }

        val player = socketData.player ?: return BaseModel(commandName = TAG, error = wrongPlayerResponse).toString()
        //println("GLOBAL STEP: ${Server.storage.globalStep}")
        if (params.stepId == Server.storage.globalStep) {
            val tempPlayer = player.copy()
            val resCode = tempPlayer.updateStep(params)
//            if (resCode == PlayerModel.END_GAME) {
//                //Server.storage.globalStep = PlayerModel.END_GAME
//                return BaseModel(
//                    commandName = TAG,
//                    commandParams = StepAnswer(PlayerModel.END_GAME, params.stepType)
//                ).toString()
//            }
            if (resCode == PlayerModel.OBSTACLE) {
                return BaseModel(
                    commandName = TAG,
                    error = ErrorModel(ErrorCode.ErrGame, ErrorNames.ErrNoStep)
                ).toString()
            }

            tempPlayer.stepId = resCode
            val oldTempPlayer = Server.storage.newPlayers.find { it.oid == tempPlayer.oid && it.cid == tempPlayer.cid }
            if (oldTempPlayer != null) {
                val index = Server.storage.newPlayers.indexOf(oldTempPlayer)
                Server.storage.newPlayers[index] = tempPlayer
            } else {
                Server.storage.newPlayers.add(tempPlayer)
            }
            return BaseModel(
                commandName = TAG,
                commandParams = StepAnswer(
                    Server.storage.globalStep,
                    tempPlayer.stepType
                )
            ).toString()
        } else {
            return BaseModel(commandName = TAG, error = wrongStepTime).toString()
        }
    }

    private val wrongStepTime = ErrorModel(ErrorCode.ErrGame, ErrorNames.ErrTimeIsOver)

    private val wrongStartGameResponse = ErrorModel(ErrorCode.ErrGame, message = ErrorNames.ErrGameIsNotStarted)

    private val wrongEndGameResponse = ErrorModel(ErrorCode.ErrGame, message = ErrorNames.ErrGameAlreadyEnd)
}