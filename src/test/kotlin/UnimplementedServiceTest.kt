import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.ServiceRegistry
import id.walt.servicematrix.exceptions.UnimplementedServiceException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class UnimplementedServiceTest : StringSpec({
    "Unimplemented Service without default implementation should throw UnimplementedServiceException" {
        val exception = shouldThrow<UnimplementedServiceException> {
            val theService = UnimplementedService1.getService()
            theService.function1()
        }
        exception.message shouldContain "UnimplementedService1" // Class name
        exception.message shouldContain "no default service was defined in ServiceProvider"
    }

    "Unimplemented Service without ServiceProvider (thus no default implementation possible) should throw UnimplementedServiceException" {
        val exception = shouldThrow<UnimplementedServiceException> {
            val theService = ServiceRegistry.getService<UnimplementedService2>()
            theService.function1()
        }
        exception.message shouldContain "UnimplementedService2" // Class name
        exception.message shouldContain "no ServiceProvider was defined for the service"
    }
}) {
    abstract class UnimplementedService1 : BaseService() {
        override val implementation get() = serviceImplementation<UnimplementedService1>()

        open fun function1(): Int = implementation.function1()

        companion object : ServiceProvider {
            override fun getService() = object : UnimplementedService1() {}
        }
    }

    abstract class UnimplementedService2 : BaseService() {
        override val implementation get() = serviceImplementation<UnimplementedService2>()

        open fun function1(): Int = implementation.function1()
    }
}


