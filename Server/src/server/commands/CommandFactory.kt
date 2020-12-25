package com.labirintals.server.commands

import com.labirintals.model.base.Command

class CommandFactory private constructor() {

    companion object {
        private val simpleCommands = mapOf(
            "get_game_params" to GetGameParamsCommand(),
            "get_position" to GetPositionCommand(),
            "step" to GetStepInfoCommand()
        )

        private val commandsWithParams: (Any?) -> Map<String, BaseCommand> = { params ->
            mapOf(
                "connection" to ConnectionCommand(params),
                "make_step" to MakeStepCommand(params),
            )
        }

        val create: (String?) -> (Any?) -> BaseCommand? = { name: String? ->
            { params: Any? ->
                if (params == null) {
                    simpleCommands[name]
                } else {
                    commandsWithParams(params)[name]
                }
            }
        }
    }
}