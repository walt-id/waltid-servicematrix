package id.walt.servicematrix.exceptions

class UnimplementedServiceException(service: String?) :
    Exception("walt.id ServiceMatrix: No implementation has been registered for service: \"$service\"! This is a configuration error. Information about the ServiceMatrix: https://github.com/walt-id/waltid-ssikit/blob/master/README.md")
