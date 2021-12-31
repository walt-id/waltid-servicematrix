import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.ServiceRegistry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ImplementationInheritanceTest : StringSpec({
    "Register InheritanceTestService with Implementation1" {
        ServiceRegistry.registerService<InheritanceTestService>(InheritanceTestServiceImpl1())
    }
    "InheritanceTestService should now be implemented by Implementation1" {
        InheritanceTestService.getService().function1() shouldBe 1
        InheritanceTestService.getService().function2() shouldBe "Impl 1"
    }
    "Reregister InheritanceTestService with Implementation2" {
        ServiceRegistry.registerService<InheritanceTestService>(InheritanceTestServiceImpl2())
    }
    "InheritanceTestService should now be implemented by Implementation2" {
        InheritanceTestService.getService().function1() shouldBe 1
        InheritanceTestService.getService().function2() shouldBe "Impl 2"
    }
}) {
    abstract class InheritanceTestService : BaseService() {
        override val implementation get() = serviceImplementation<InheritanceTestService>()

        open fun function1(): Int = implementation.function1()
        open fun function2(): String = implementation.function2()

        companion object : ServiceProvider {
            override fun getService() = object : InheritanceTestService() {}
        }
    }

    open class InheritanceTestServiceImpl1 : InheritanceTestService() {
        override fun function1() = 1
        override fun function2() = "Impl 1"
    }

    class InheritanceTestServiceImpl2 : InheritanceTestServiceImpl1() {
        override fun function2() = super.function2().dropLast(1) + "2"
    }
}
