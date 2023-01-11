package id.walt.servicematrix.exceptions

class MismappedServiceException(serviceClass: String, implementationClass: String, extra: String? = null) :
    ServiceMatrixException("Mismapped service: $implementationClass was mapped to, but  is not a service of type $serviceClass${if (extra != null) " ($extra)" else ""}")
