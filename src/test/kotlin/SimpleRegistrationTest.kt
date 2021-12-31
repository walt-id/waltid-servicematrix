import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.ServiceRegistry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SimpleRegistrationTest : StringSpec({
    "Register SimpleTestService with Implementation1" {
        ServiceRegistry.registerService<SimpleTestService>(SimpleTestServiceImpl1())
    }
    "SimpleTestService should now be implemented by Implementation1" {
        SimpleTestService.getService().function1() shouldBe 1
    }
    "Reregister SimpleTestService with Implementation2" {
        ServiceRegistry.registerService<SimpleTestService>(SimpleTestServiceImpl2())
    }
    "SimpleTestService should now be implemented by Implementation2" {
        SimpleTestService.getService().function1() shouldBe 2
    }
}) {
    abstract class SimpleTestService : BaseService() {
        override val implementation get() = serviceImplementation<SimpleTestService>()

        open fun function1(): Int = implementation.function1()
        open fun function2(): String = implementation.function2()

        companion object : ServiceProvider {
            override fun getService() = object : SimpleTestService() {}
        }
    }

    class SimpleTestServiceImpl1 : SimpleTestService() {
        override fun function1() = 1
        override fun function2() = "Impl 1"
    }

    class SimpleTestServiceImpl2 : SimpleTestService() {
        override fun function1() = 2
        override fun function2() = "Impl 2"
    }
}
