package id.walt.servicematrix

import id.walt.servicematrix.exceptions.NotValidBaseServiceException
import id.walt.servicematrix.exceptions.ServiceNotFoundException
import id.walt.servicematrix.utils.ReflectionUtils.getKClassByName
import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

/**
 * Manager to load service definitions from Java property files and subsequently
 * register these service mappings in the [ServiceRegistry].
 *
 * The primary (no-arg) constructor allows you to call [loadServiceDefinitions] yourself. Register
 * the definitions using [registerServiceDefinitions].
 * The secondary (string-arg) constructor automatically loads the service definitions and
 * registers them in the [ServiceRegistry].
 *
 * @constructor The primary (no-arg) constructor allows you to call [loadServiceDefinitions] yourself.
 */
class ServiceMatrix() {
    private val serviceList = HashMap<String, String>()

    /**
     * Uses the Java property class to read a properties file containing the service mappings.
     *
     * @param filePath The path to the service definition file as String
     */
    fun loadServiceDefinitions(filePath: String) = Properties().apply {
        load(File(filePath).reader())
        serviceList.putAll(entries.associate { it.value.toString() to it.key.toString() })
    }

    private fun createImplementationInstance(implementationClass: String): BaseService =
        getKClassByName(implementationClass).createInstance() as? BaseService
            ?: throw NotValidBaseServiceException(implementationClass)

    private fun createConfiguredImplementationInstance(
        implementationClass: String,
        configurationPath: String
    ): BaseService =
        getKClassByName(implementationClass).primaryConstructor!!.call(configurationPath) as BaseService

    /**
     * Registers all loaded service definitions in the [ServiceRegistry].
     */
    fun registerServiceDefinitions() {
        serviceList.forEach { (implementationString, serviceString) ->
            val service: KClass<out BaseService> =
                runCatching { getKClassByName(serviceString) as KClass<out BaseService> }.getOrElse {
                    throw ServiceNotFoundException(serviceString)
                }

            val implementation = when {
                implementationString.contains(':') -> {
                    implementationString.split(':').let { splittedImplementationString ->
                        val implementationClass = splittedImplementationString[0]
                        val configurationPath = splittedImplementationString[1]

                        createConfiguredImplementationInstance(implementationClass, configurationPath)
                    }
                }
                else -> createImplementationInstance(implementationString)
            }

            ServiceRegistry.registerService(implementation, service)
        }
    }

    /**
     * Calling this constructor will automatically load the service definitions from [filePath] and registers them
     *
     * @param filePath The path to the service definition file as String
     */
    constructor(filePath: String) : this() {
        loadServiceDefinitions(filePath)
        registerServiceDefinitions()
    }
}
