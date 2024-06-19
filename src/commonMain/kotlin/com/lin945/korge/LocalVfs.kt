package com.lin945.korge

import com.lin945.korge.po.*
import korlibs.io.file.*
import korlibs.io.file.std.*
import korlibs.io.file.std.LocalVfs
import net.mamoe.yamlkt.*

object LocalVfs {
    private var vfs: VfsFile = applicationVfs["data"]

    suspend fun readConfig() {
        println(vfs.absolutePathInfo.normalize())
        val file = vfs["config.yaml"]
        if (file.exists()) {
//            file.re
            val decodeFromString = Yaml.decodeFromString(KorgeConfig.serializer(), file.readString())
            println(decodeFromString)
        }
    }
}
