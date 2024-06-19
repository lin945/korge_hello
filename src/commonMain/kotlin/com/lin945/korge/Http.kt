package com.lin945.korge

import io.ktor.client.request.*
import io.ktor.util.*

expect val client: io.ktor.client.HttpClient

@OptIn(InternalAPI::class)
suspend fun fentchData(): String =
    client.request("https://api.bilibili.com/x/web-interface/zone").content.toString()
//    clinet.requestAsString(method = Http.Method.GET, url = "https://api.bilibili.com/x/web-interface/zone", config = HttpClient.RequestConfig(simulateBrowser = true)).content
