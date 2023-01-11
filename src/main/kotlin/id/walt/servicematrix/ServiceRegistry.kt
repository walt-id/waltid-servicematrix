package id.walt.servicematrix

import id.walt.servicematrix.exceptions.MismappedServiceException
import id.walt.servicematrix.exceptions.NotValidServiceProviderException
import id.walt.servicematrix.exceptions.UnimplementedServiceException
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.jvmName

/**
 * Mapping of services and their respective service-implementations
 */
object ServiceRegistry {
    /**
     * Actual mapping happens here, you can directly access the map too.
     */
    val services = HashMap<KClass<out BaseService>, BaseService>()

    /**
     * Register a implementation for a specific service
     * Example: `registerService<MyCustomService>(MyCustomImplementation())`
     */
    inline fun <reified T : BaseService> registerService(serviceImplementation: BaseService) {
        services[T::class] = serviceImplementation
    }

    /**
     * Register an implementation for a specific service
     * Example: `registerService(MyCustomImplementation(), MyCustomService::class)`
     */
    fun registerService(serviceImplementation: BaseService, serviceType: KClass<out BaseService>) {
        services[serviceType] = serviceImplementation
    }

    /**
     * Get the current service implementation for this base service from the service registry
     * Example: `getService<MyCustomService>()`
     */
    inline fun <reified Service : BaseService> getService(): Service {
        return getService(Service::class)
    }

    /**
     * Get the current service implementation for this base service from the service registry
     * Example: `getService(MyCustomService::class)`
     */
    inline fun <reified Service : BaseService> getService(serviceClass: KClass<Service>): Service {
        println("Get service: ${serviceClass.jvmName}")

        val lookedupService = services[serviceClass]
        if (lookedupService != null) {
            if (lookedupService is Service) {
                return lookedupService
            } else {
                throw MismappedServiceException(Service::class.jvmName, lookedupService::class.jvmName)
            }
        }

        val uncastedServiceClassProvider = serviceClass.companionObjectInstance ?: throw UnimplementedServiceException(serviceClass.qualifiedName, "and no ServiceProvider was defined for the service?")
        val serviceClassProvider = (uncastedServiceClassProvider as? ServiceProvider) ?: throw NotValidServiceProviderException(uncastedServiceClassProvider::class.jvmName, serviceClass::class.jvmName, true)
        val defaultImplementation = serviceClassProvider.defaultImplementation() ?: throw UnimplementedServiceException(serviceClass.qualifiedName, "and no default service was defined in ServiceProvider")

        if (defaultImplementation is Service) {
            registerService(defaultImplementation, serviceClass)

            return defaultImplementation
        } else {
            throw MismappedServiceException(Service::class.jvmName, defaultImplementation::class.jvmName, "the mismapped implementation was set as a defaultImplementation for this service")
        }
    }
}
