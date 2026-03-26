package com.lin945.korge.scene

import korlibs.image.color.Colors
import korlibs.korge.input.onClick
import korlibs.korge.input.onKeyDown
import korlibs.korge.scene.Scene
import korlibs.korge.view.View
import korlibs.korge.view.SContainer
import korlibs.korge.view.align.centerXOnStage
import korlibs.korge.view.solidRect
import korlibs.korge.view.text
import korlibs.time.seconds
import kotlin.random.Random

class ChromeRunnerScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val worldWidth = views.virtualWidth.toDouble()
        val worldHeight = views.virtualHeight.toDouble()
        val groundY = worldHeight - 44.0

        solidRect(worldWidth, worldHeight, Colors["#f7f7f7"])
        val scoreText = text("SCORE: 00000", textSize = 14.0, color = Colors["#222222"]).also {
            it.position(worldWidth - 120.0, 8.0)
        }

        val titleText = text("Chrome Runner", textSize = 20.0, color = Colors["#333333"]).centerXOnStage().also {
            it.y = 26.0
        }

        val hintText = text("按空格/点击跳跃", textSize = 12.0, color = Colors["#666666"]).centerXOnStage().also {
            it.y = titleText.y + 26.0
        }

        solidRect(worldWidth, 2.0, Colors["#555555"]).position(0.0, groundY)

        val player = solidRect(18.0, 28.0, Colors["#2d2d2d"]).also {
            it.x = 40.0
            it.y = groundY - it.height
        }

        data class Obstacle(val view: View, val width: Double)

        val obstacles = mutableListOf<Obstacle>()
        var spawnCooldown = 0.0
        var velocityY = 0.0
        val gravity = 1800.0
        val jumpVelocity = -520.0
        var gameSpeed = 220.0
        var score = 0
        var gameOver = false

        fun resetGame() {
            obstacles.forEach { it.view.removeFromParent() }
            obstacles.clear()
            score = 0
            gameSpeed = 220.0
            spawnCooldown = 0.0
            velocityY = 0.0
            gameOver = false
            player.y = groundY - player.height
            scoreText.text = "SCORE: 00000"
            hintText.text = "按空格/点击跳跃"
            hintText.visible = true
            hintText.centerXOnStage()
        }

        fun jump() {
            if (gameOver) {
                resetGame()
                return
            }
            val onGround = player.y >= groundY - player.height - 0.1
            if (onGround) velocityY = jumpVelocity
        }

        fun spawnObstacle() {
            val obstacleWidth = Random.nextInt(12, 30).toDouble()
            val obstacleHeight = Random.nextInt(20, 44).toDouble()
            val obstacle = solidRect(obstacleWidth, obstacleHeight, Colors["#1f1f1f"]).also {
                it.position(worldWidth + 8.0, groundY - obstacleHeight)
            }
            obstacles += Obstacle(obstacle, obstacleWidth)
        }

        addUpdater { dt ->
            val deltaSeconds = dt.seconds
            if (gameOver) return@addUpdater

            velocityY += gravity * deltaSeconds
            player.y += velocityY * deltaSeconds
            if (player.y > groundY - player.height) {
                player.y = groundY - player.height
                velocityY = 0.0
            }

            spawnCooldown -= deltaSeconds
            if (spawnCooldown <= 0.0) {
                spawnObstacle()
                spawnCooldown = Random.nextDouble(0.8, 1.5)
            }

            for (obstacle in obstacles) {
                obstacle.view.x -= gameSpeed * deltaSeconds
            }
            obstacles.removeAll { obstacle ->
                val out = obstacle.view.x + obstacle.width < -8.0
                if (out) obstacle.view.removeFromParent()
                out
            }

            val playerLeft = player.x
            val playerRight = player.x + player.width
            val playerTop = player.y
            val playerBottom = player.y + player.height
            val hit = obstacles.any { obstacle ->
                val o = obstacle.view
                val obstacleLeft = o.x
                val obstacleRight = o.x + o.width
                val obstacleTop = o.y
                val obstacleBottom = o.y + o.height
                playerLeft < obstacleRight && playerRight > obstacleLeft && playerTop < obstacleBottom && playerBottom > obstacleTop
            }

            if (hit) {
                gameOver = true
                hintText.text = "游戏结束，点击重新开始"
                hintText.visible = true
                hintText.centerXOnStage()
            } else {
                score += (60 * deltaSeconds).toInt().coerceAtLeast(1)
                scoreText.text = "SCORE: ${score.toString().padStart(5, '0')}"
                gameSpeed += 3.5 * deltaSeconds
                hintText.visible = score < 30
            }
        }

        onKeyDown {
            when (it.key.name) {
                "SPACE", "UP", "W" -> jump()
            }
        }
        onClick { jump() }
    }
}
