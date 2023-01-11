package id.walt.servicematrix.exceptions

class NotValidServiceProviderException(implementationClass: String, serviceClass: String, whileLookingUpDefaultService: Boolean) :
    ServiceMatrixException(
        "\"$implementationClass\" is not a valid ServiceProvider for service \"$serviceClass\"."
        + if (whileLookingUpDefaultService) " This happened while looking up a default service implementation for \"$serviceClass\", because no service was configured in a ServiceMatrix configuration beforehand." else ""
    )
