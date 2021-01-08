package com.labirintals.server.commands

class CommandFactory private constructor() {

    companion object {
        private val simpleCommands = mapOf(
            GetGameParamsCommand.TAG_NAME to GetGameParamsCommand(),
            GetPositionCommand.TAG_NAME to GetPositionCommand(),
            GetStepInfoCommand.TAG_NAME to GetStepInfoCommand(),
            GetGameResultCommand.TAG_NAME to GetGameResultCommand()
        )

        private val commandsWithParams: (Any?) -> Map<String, BaseCommand> = { params ->
            mapOf(
                ConnectionCommand.TAG_NAME to ConnectionCommand(params),
                MakeStepCommand.TAG_NAME to MakeStepCommand(params),
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