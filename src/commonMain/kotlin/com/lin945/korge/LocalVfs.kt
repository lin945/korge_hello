package com.lin945.korge

import korlibs.io.file.*
import korlibs.io.file.std.LocalVfs

object LocalVfs {
    private var vfs: VfsFile = LocalVfs["/data"]

    suspend fun readConfig() {
        val file = vfs["config.yaml"]
        if (file.exists()) {
//            file.re
        }
    }
}
