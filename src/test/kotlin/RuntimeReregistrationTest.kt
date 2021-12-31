import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.ServiceRegistry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RuntimeReregistrationTest : StringSpec({
    "Register SimpleTestService with Implementation1" {
        ServiceRegistry.registerService<ReregistrationTestService>(ReregistrationTestServiceImpl1())

        val service = ReregistrationTestService.getService()

        service.function1() shouldBe 1

        ServiceRegistry.registerService<ReregistrationTestService>(ReregistrationTestServiceImpl2())

        service.function1() shouldBe 2
    }
}) {
    abstract class ReregistrationTestService : BaseService() {
        override val implementation get() = serviceImplementation<ReregistrationTestService>()

        open fun function1(): Int = implementation.function1()

        companion object : ServiceProvider {
            override fun getService() = object : ReregistrationTestService() {}
        }
    }

    class ReregistrationTestServiceImpl1 : ReregistrationTestService() {
        override fun function1() = 1
    }

    class ReregistrationTestServiceImpl2 : ReregistrationTestService() {
        override fun function1() = 2
    }
}
