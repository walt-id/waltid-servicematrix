import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceMatrix
import id.walt.servicematrix.ServiceProvider
import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.shouldBe

class ServiceMatrixTest : StringSpec({

    val file1 = tempfile()
    val file2 = tempfile()

    file1.writeText(
        """
        ServiceMatrixTestService=ServiceMatrixTestServiceImpl1
        """.trimIndent()
    )

    file2.writeText(
        """
        ServiceMatrixTestService=ServiceMatrixTestServiceImpl2
        """.trimIndent()
    )

    "ServiceMatrix reading" {
        ServiceMatrix(file1.absolutePath)

        val service = ServiceMatrixTestService.getService()

        service.function1() shouldBe 1

        ServiceMatrix(file2.absolutePath)

        service.function1() shouldBe 2
    }
})

abstract class ServiceMatrixTestService : BaseService() {
    override val implementation get() = serviceImplementation<ServiceMatrixTestService>()

    open fun function1(): Int = implementation.function1()

    companion object : ServiceProvider {
        override fun getService() = object : ServiceMatrixTestService() {}
    }
}

class ServiceMatrixTestServiceImpl1 : ServiceMatrixTestService() {
    override fun function1() = 1
}

class ServiceMatrixTestServiceImpl2 : ServiceMatrixTestService() {
    override fun function1() = 2
}
