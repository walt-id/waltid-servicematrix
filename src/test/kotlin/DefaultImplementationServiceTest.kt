import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DefaultImplementationServiceTest : StringSpec({
    "Not explicitly implemented Service with default implementation should apply the default implementation" {
        val theService = NotExplicitlyImplementedService.getService()
        theService.function1() shouldBe 123456
    }
}) {
    abstract class NotExplicitlyImplementedService : BaseService() {
        override val implementation get() = serviceImplementation<NotExplicitlyImplementedService>()

        open fun function1(): Int = implementation.function1()

        companion object : ServiceProvider {
            override fun getService() = object : NotExplicitlyImplementedService() {}
            override fun defaultImplementation() = ServiceImplementation()
        }
    }

    class ServiceImplementation : NotExplicitlyImplementedService() {
        override fun function1() = 123456
    }
}
