import id.walt.servicematrix.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.shouldBe

class ConfigurationTest : StringSpec({
    val conf1 = tempfile(suffix = ".conf").apply {
        writeText(
            """
            env: "staging"
            
            thingsForConfiguration1: {
                name: "Implementation Nr. 1"
            }
            """.trimIndent()
        )
    }

    val conf2 = tempfile(suffix = ".conf").apply {
        writeText(
            """
            env: production
        
            someConfig2Things: {
                id: 2
            }
            """.trimIndent()
        )
    }

    val file1 = tempfile().apply {
        writeText(
            """
            ConfigurationTestService=ConfigurationTestServiceImpl1:${conf1.absolutePath}
            """.trimIndent()
        )
    }

    val file2 = tempfile().apply {
        writeText(
            """
            ConfigurationTestService=ConfigurationTestServiceImpl2:${conf2.absolutePath}
            """.trimIndent()
        )
    }

    "Register ConfigurationTestService with Implementation1" {
        ServiceRegistry.registerService<ConfigurationTestService>(ConfigurationTestServiceImpl1(conf1.absolutePath))
    }
    "ConfigurationTestService should now be implemented by Implementation1" {
        ConfigurationTestService.getService().someInfoText() shouldBe "Implementation Nr. 1"
    }
    "Reregister ConfigurationTestService with Implementation2" {
        ServiceRegistry.registerService<ConfigurationTestService>(ConfigurationTestServiceImpl2(conf2.absolutePath))
    }
    "ConfigurationTestService should now be implemented by Implementation2" {
        ConfigurationTestService.getService().someInfoText() shouldBe "This is Implementation 2"
    }

    "ServiceMatrix reading" {
        ServiceMatrix(file1.absolutePath)

        val service = ConfigurationTestService.getService()

        service.someInfoText() shouldBe "Implementation Nr. 1"

        ServiceMatrix(file2.absolutePath)

        service.someInfoText() shouldBe "This is Implementation 2"
    }

    "Error on invalid configuration access" {
        ServiceRegistry.registerService<ConfigurationTestService>(ConfigurationTestServiceImpl3())

        val service = ConfigurationTestService.getService()

        shouldThrow<IllegalStateException> {
            println(service.someInfoText())
        }
    }
})

abstract class ConfigurationTestService : BaseService() {
    override val implementation get() = serviceImplementation<ConfigurationTestService>()

    open fun someInfoText(): String = implementation.someInfoText()

    companion object : ServiceProvider {
        override fun getService() = object : ConfigurationTestService() {}
    }
}

class ConfigurationTestServiceImpl1(configurationPath: String) : ConfigurationTestService() {
    data class ThingsForConfiguration1(val name: String)
    data class Configuration1(val env: String, val thingsForConfiguration1: ThingsForConfiguration1) : ServiceConfiguration

    override val configuration: Configuration1 = fromConfiguration(configurationPath)

    override fun someInfoText(): String = configuration.thingsForConfiguration1.name
}

class ConfigurationTestServiceImpl2(configurationPath: String) : ConfigurationTestService() {
    data class SomeConfig2Things(val id: Int)
    data class Configuration2(val env: String, val someConfig2Things: SomeConfig2Things) : ServiceConfiguration

    override val configuration: Configuration2 = fromConfiguration(configurationPath)

    override fun someInfoText(): String = "This is Implementation ${configuration.someConfig2Things.id}"
}

class ConfigurationTestServiceImpl3 : ConfigurationTestService() {
    override fun someInfoText(): String = configuration.hashCode().toString()
}
