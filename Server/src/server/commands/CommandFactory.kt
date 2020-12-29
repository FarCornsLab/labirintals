package com.labirintals.server.commands

import com.labirintals.model.base.Command

class CommandFactory private constructor() {

    companion object {
        private val simpleCommands = mapOf(
            GetGameParamsCommand.TAG to GetGameParamsCommand(),
            GetPositionCommand.TAG to GetPositionCommand(),
            GetStepInfoCommand.TAG to GetStepInfoCommand()
        )

        private val commandsWithParams: (Any?) -> Map<String, BaseCommand> = { params ->
            mapOf(
                ConnectionCommand.TAG to ConnectionCommand(params),
                MakeStepCommand.TAG to MakeStepCommand(params),
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