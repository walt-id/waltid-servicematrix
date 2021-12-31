import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.ServiceRegistry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ServiceRegistryTests : StringSpec({
    "ServiceRegistry registerService by type" {
        ServiceRegistry.registerService<ServiceRegistryTestService>(ServiceRegistryTestServiceImpl1())
        ServiceRegistry.getService<ServiceRegistryTestService>().function1() shouldBe 1
    }
    "ServiceRegistry registerService by class" {
        ServiceRegistry.registerService(ServiceRegistryTestServiceImpl2(), ServiceRegistryTestService::class)
        ServiceRegistry.getService<ServiceRegistryTestService>().function1() shouldBe 2
    }
}) {
    abstract class ServiceRegistryTestService : BaseService() {
        override val implementation get() = serviceImplementation<ServiceRegistryTestService>()

        open fun function1(): Int = implementation.function1()
        open fun function2(): String = implementation.function2()

        companion object : ServiceProvider {
            override fun getService() = object : ServiceRegistryTestService() {}
        }
    }

    class ServiceRegistryTestServiceImpl1 : ServiceRegistryTestService() {
        override fun function1() = 1
        override fun function2() = "Impl 1"
    }

    class ServiceRegistryTestServiceImpl2 : ServiceRegistryTestService() {
        override fun function1() = 2
        override fun function2() = "Impl 2"
    }
}
