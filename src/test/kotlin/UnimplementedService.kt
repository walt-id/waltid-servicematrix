import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.exceptions.UnimplementedServiceException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class UnimplementedServiceTest : StringSpec({
    "Service without implementation should throw UnimplementedServiceException" {
        shouldThrow<UnimplementedServiceException> {
            val theService = UnimplementedService.getService()
            theService.function1()
        }
    }
})


abstract class UnimplementedService : BaseService() {
    override val implementation get() = serviceImplementation<UnimplementedService>()

    open fun function1(): Int = implementation.function1()

    companion object : ServiceProvider {
        override fun getService() = object : UnimplementedService() {}
    }
}
