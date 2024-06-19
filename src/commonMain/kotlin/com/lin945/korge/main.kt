package com.lin945.korge

import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.image.qr.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.render.*
import kotlin.math.*
import kotlin.random.*

suspend fun main() = Korge(windowSize = Size(512, 800), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()
    LocalVfs.readConfig()
    sceneContainer.changeTo { MyScene() }
}

class MyScene : Scene() {
    val fonts = Size(20, 20)

    override suspend fun SContainer.sceneMain() {
        buildUI()
//        buildUI2()
    }

    private suspend fun SContainer.buildUI() {
        val bitmap = Bitmap32(views.virtualWidth, 500,true)
        val s = uiScrollable(Size(views.virtualWidth, 500)) {
//            NativeImage()
            image(bitmap){
                onClick {
//                bitmap.fill(Colors.RED)
                    val posLocal = it.currentPosLocal
                    val clickX=posLocal.x.toInt()
                    val clickY=posLocal.y.toInt()
                    println(posLocal)
                    val pixels = IntArray(2 * 2)
                    val randomColor = RGBA(
                        Random.nextInt(256),
                        Random.nextInt(256),
                        Random.nextInt(256)
                    )                    // 添加涟漪效果，改变周围像素的颜色
                    val rippleRadius = 5 // 涟漪效果的半径
                    for (dx in -rippleRadius..rippleRadius) {
                        for (dy in -rippleRadius..rippleRadius) {
                            val newX = clickX + dx
                            val newY = clickY + dy

                            if (newX in 0 until bitmap.width && newY in 0 until bitmap.height) {
                                // 根据距离中心的距离调整颜色的透明度，距离越远透明度越高
                                val distance = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                                val alpha = (1.0f - distance / rippleRadius.toFloat()).coerceIn(0.0f, 1.0f) * 255

                                val modifiedColor = RGBA(randomColor.value)
                                bitmap[newX, newY] = modifiedColor
                            }
                        }
                    }
                    bitmap.setRgba(posLocal.x.toInt(), posLocal.y.toInt(), randomColor)
                    bitmap.showImageAndWait()

//                bitmap.writePixelsUnsafe(posLocal.x.toInt(), posLocal.y.toInt(), 2, 2, pixels, offset = 0)
                }
            }
            onScroll {

            }
        }
        image(QR.msg("Hello from KorIM-QR!")){
            scale(6.0).also { it.smoothing = false }
            alignTopToBottomOf(s)
        }//.filters(DropshadowFilter(0.0, 0.0, blurRadius = 12.0, shadowColor = Colors.BLACK))
        text("vvvvvvaaaaa") {
            onClick {
                doFentch()
            }
            alignTopToBottomOf(s)
        }
    }

    private suspend fun SContainer.buildUI2() {
        val pixelSize = 10 // 像素块的大小
        val pixels = mutableMapOf<Pair<Int, Int>, RGBA>() // 存储像素颜色的映射
        val block=uiScrollable(Size(views.virtualWidth, 500))

        // 绘制像素点
        fun drawPixel(x: Int, y: Int, color: RGBA) {
            pixels[x to y] = color
        }

        // 更新画板
        fun Container.updateCanvas() {
            for ((pos, color) in pixels) {
                val (x, y) = pos
                solidRect(pixelSize, pixelSize, color).xy(pixelSize * x, pixelSize * y)
            }
        }

        // 初始化画板
        updateCanvas()

        // 添加点击和滑动事件处理
        container {
            onClick { e ->
                val currentPosLocal = e.currentPosLocal
                drawPixel((currentPosLocal.x / pixelSize).toInt(), (currentPosLocal.y / pixelSize).toInt(), Colors.WHITE)
                updateCanvas()
            }
            onMouseDrag { e ->
                val currentPosLocal = e.dx
                //drawPixel(e.x / pixelSize, e.y / pixelSize, Colors.BLACK)
                //updateCanvas()
            }
        }
    }

    private suspend fun doFentch() {
        views.gameWindow.alert(fentchData())

    }

}
