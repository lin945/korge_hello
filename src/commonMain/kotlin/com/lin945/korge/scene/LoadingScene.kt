package com.lin945.korge.scene

import korlibs.audio.sound.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.korge.scene.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.logger.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*

class LoadingScene : Scene() {

    lateinit var soud: Sound
    var fps = 0
    override suspend fun sceneAfterInit() {
        super.sceneAfterInit()
        val splash = async { sceneView.splash() }
        splash.await()
    }

    override suspend fun SContainer.sceneInit() {
        super.sceneAfterInit()

        val fpsText = text("FPS: ...") {
            size = Size(10, 10)
            alignment
        }.alignTopToTopOf(this)

        var int = 0
        addUpdater {
            fps++
        }
        addFixedUpdater(1.seconds) {
            fpsText.text = "FPS: $fps"
            fps = 0
        }

    }

    suspend fun Container.splash() {
        views.clearColor = Colors.WHITE
        var map = resourcesVfs["korge_n.png"].readBitmap().slice()
        val anim = async {
            logo(map)
        }
        resourcesVfs["music.mp3"].readMusic().playNoCancelForever()
        delay(1.seconds)
        views.clearColor = Colors.BLACK
        anim.await()
    }

    suspend fun Container.logo(graph: BmpSlice) {
        val image = image(graph) {
            alpha = 0.0

        }
        Console.log(image)
        image.tween(image::alpha[1], time = 1.seconds, easing = Easing.EASE_IN_OUT)
        delay(1.seconds)
        image.tween(image::alpha[0], time = 0.8.seconds, easing = Easing.EASE_IN_OUT)
        //removeChild(image)
    }
}
