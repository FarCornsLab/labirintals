package com.labirintals.server.commands

import io.ktor.utils.io.*
import java.io.BufferedWriter

interface BaseCommand {
    fun doCommand(): String?
}