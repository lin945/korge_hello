import korlibs.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}

korge {
	id = "com.sample.demo"

// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets
	
	targetJvm()
	targetJs()
    //targetWasm()
	//targetDesktop()
	//targetIos()
	//targetAndroid()
    jvmMainClassName="com.lin945.korge.MainKt"
	serializationJson()
}


dependencies {
    add("commonMainApi", project(":deps"))
    add("commonMainApi", "net.mamoe.yamlkt:yamlkt:0.13.0")
    add("commonMainApi", libs.ktor.client.core)
    add("jvmMainApi",  libs.ktor.client.okhttp)
    add("jsMainApi", libs.ktor.client.js)
//    add("jsMain", "...")
    //add("commonMainApi", project(":common"))
}

