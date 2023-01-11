import id.walt.servicematrix.BaseService
import id.walt.servicematrix.ServiceMatrix
import id.walt.servicematrix.ServiceProvider
import id.walt.servicematrix.ServiceRegistry
import id.walt.servicematrix.exceptions.MismappedServiceException
import id.walt.servicematrix.exceptions.NotValidBaseServiceException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempfile

class InvalidInstantiationRegistrationTest : StringSpec({
    val file1 = tempfile().apply {
        writeText(
            """
            InvalidInstantiationTestService=InvalidInstantiationTestServiceImpl1
            """.trimIndent()
        )
    }

    val file2 = tempfile().apply {
        writeText(
            """
            InvalidInstantiationTestService=InvalidInstantiationTestServiceImpl2
            """.trimIndent()
        )
    }

    val file3 = tempfile().apply {
        writeText(
            """
            InvalidInstantiationTestService2=ValidInstantiationTestServiceImpl3
            """.trimIndent()
        )
    }

    "Registering InvalidInstantiationTestServiceImpl1 should throw NotValidBaseServiceException" {
        shouldThrow<NotValidBaseServiceException> {
            ServiceMatrix(file1.absolutePath)
        }
    }
    "Registering InvalidInstantiationTestServiceImpl2 should throw MismappedServiceException" {
        shouldThrow<MismappedServiceException> {
            ServiceMatrix(file2.absolutePath)
            val service = InvalidInstantiationTestService.getService()
            service.function1()
        }
    }
    "Registering InvalidInstantiationTestService2 should throw NotValidBaseServiceException" {
        shouldThrow<NotValidBaseServiceException> {
            ServiceMatrix(file3.absolutePath)
        }
    }
})

abstract class InvalidInstantiationTestService : BaseService() {
    override val implementation get() = serviceImplementation<InvalidInstantiationTestService>()

    open fun function1(): Int = implementation.function1()

    companion object : ServiceProvider {
        override fun getService() = object : InvalidInstantiationTestService() {}
    }
}

// 1
class InvalidInstantiationTestServiceImpl1

// 2
class InvalidInstantiationTestServiceImpl2 : BaseService() {
    override val implementation: BaseService
        get() = throw Error("this error will never be thrown anyways")
}

// 3
abstract class InvalidInstantiationTestService2 {
    @Suppress("CAST_NEVER_SUCCEEDS")
    val implementation
        get() = ServiceRegistry.getService<BaseService>() as InvalidInstantiationTestService2

    open fun function1(): Int = implementation.function1()

    companion object : ServiceProvider {
        override fun getService() = object : InvalidInstantiationTestService() {}
    }
}

class ValidInstantiationTestServiceImpl3 : InvalidInstantiationTestService2() {
    override fun function1() = 3
}
