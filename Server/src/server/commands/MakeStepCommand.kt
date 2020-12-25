package com.labirintals.server.commands

import com.labirintals.model.requests.MakeStepModel
import com.labirintals.server.Server

class MakeStepCommand(args: Any?): BaseCommand {
    private val params: MakeStepModel = Server.gson.fromJson(args.toString(), MakeStepModel::class.java)

    override fun doCommand(): String? {
        TODO("Not yet implemented")
    }
}