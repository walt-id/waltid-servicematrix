package id.walt.servicematrix

interface ServiceProvider {
    fun getService(): BaseService

    fun defaultImplementation(): BaseService? = null
}
